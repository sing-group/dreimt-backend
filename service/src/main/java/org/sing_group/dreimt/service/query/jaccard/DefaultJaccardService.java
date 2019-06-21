/*-
 * #%L
 * DREIMT - Service
 * %%
 * Copyright (C) 2018 Daniel Glez-Peña, Miguel Reboiro-Jato, Hugo López-Fernández,
 * 			Kevin Troulé, Gonzálo Gómez-López, Fátima Al-Shahrour
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.sing_group.dreimt.service.query.jaccard;

import static java.nio.file.Files.write;
import static java.nio.file.StandardOpenOption.APPEND;
import static javax.transaction.Transactional.TxType.NEVER;
import static org.sing_group.dreimt.service.query.ServiceUtils.deleteDirectory;
import static org.sing_group.dreimt.service.query.ServiceUtils.generateCommand;
import static org.sing_group.dreimt.service.query.ServiceUtils.toGmtRow;
import static org.sing_group.dreimt.service.query.jaccard.JaccardScriptResultsParser.parseResults;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.GeneOverlapData;
import org.sing_group.dreimt.service.spi.query.jaccard.JaccardService;
import org.sing_group.dreimt.service.spi.query.jaccard.JaccardServiceConfiguration;
import org.sing_group.dreimt.service.util.DatabaseVersionSingleton;

@Default
@Transactional(NEVER)
public class DefaultJaccardService implements JaccardService {
  
  @Inject
  private DatabaseVersionSingleton databaseVersion;

  @Resource(name = "java:global/dreimt/docker/jaccard/dataDir")
  private String dataDirectory;
  
  @Resource(name = "java:global/dreimt/docker/jaccard/dataDir/deleteOnFinish")
  private boolean deleteOnFinish;
  
  @Resource(name = "java:global/dreimt/docker/jaccard/command/signature")
  private String jaccardSignatureScripCmd;
  
  @Resource(name = "java:global/dreimt/docker/jaccard/command/geneset")
  private String jaccardGenesetScripCmd;

  @Override
  public Stream<GeneOverlapData> jaccard(JaccardServiceConfiguration configuration) throws IOException {
    if (configuration.getDownGenes().isEmpty()) {
      return geneSetJaccard(configuration);
    } else {
      return upDownJaccard(configuration);
    }
  }

  private Stream<GeneOverlapData> geneSetJaccard(JaccardServiceConfiguration configuration) throws IOException {
    File dataDir = getDataDirectory(this.dataDirectory);
    File geneSetFile = new File(dataDir, "input-geneset.gmt");
    try {
      write(geneSetFile.toPath(), toGmtRow("GENESET_sig", configuration.getUpGenes()).getBytes());
      
      return jaccard(configuration, geneSetFile, jaccardGenesetScripCmd, dataDir);
    } catch (IOException e) {
      throw e;
    } finally {
      if(deleteOnFinish) {
        deleteDirectory(dataDir.toPath());
      }
    }
  }

  private Stream<GeneOverlapData> upDownJaccard(JaccardServiceConfiguration configuration) throws IOException {
    File dataDir = getDataDirectory(this.dataDirectory);
    File signatureFile = new File(dataDir, "input-signature.gmt");
    try {
      write(signatureFile.toPath(), toGmtRow("SIGNATURE_UP", configuration.getUpGenes()).getBytes());
      write(signatureFile.toPath(), toGmtRow("SIGNATURE_DN", configuration.getDownGenes()).getBytes(), APPEND);

      return jaccard(configuration, signatureFile, jaccardSignatureScripCmd, dataDir);
    } catch (IOException e) {
      throw e;
    } finally {
      if (deleteOnFinish) {
        deleteDirectory(dataDir.toPath());
      }
    }
  }

  private static File getDataDirectory(String baseDir) {
    File dataDirectory = new File(baseDir, UUID.randomUUID().toString());
    dataDirectory.mkdirs();

    return dataDirectory;
  }

  private Stream<GeneOverlapData> jaccard(
    JaccardServiceConfiguration configuration,
    File inputSignatureFile,
    String jaccardCommand,
    File dataDir
  ) throws IOException {
    final Map<String, String> jaccardCommandReplacements = new HashMap<>();
    final File resultsFile = new File(dataDir, "output.tsv");
    
    if(databaseVersion.getCurrentDatabaseVersion().isPresent()) {
      jaccardCommandReplacements.put(
        "[DATABASE_VERSION]",
        databaseVersion.getCurrentDatabaseVersion().get().getVersion()
      );
    } else {
      throw new IOException(
        "The analysis cannot be run because the database version is not defined"
      );
    }
    
    jaccardCommandReplacements.put(
      "[INPUT_FILE_NAME]",
      getEffectiveCommandFileName(inputSignatureFile)
    );
    jaccardCommandReplacements.put(
      "[OUTPUT_FILE_NAME]", 
      getEffectiveCommandFileName(resultsFile)
    );
    jaccardCommandReplacements.put("[ONLY_UNIVERSE_GENES]", Boolean.toString(configuration.isOnlyUniverseGenes()));
    
    final String[] cmap = generateCommand(jaccardCommand, jaccardCommandReplacements);

    ProcessBuilder processBuilder = new ProcessBuilder(cmap);
    processBuilder.redirectError(new File(dataDir, "error.log"));
    processBuilder.redirectOutput(new File(dataDir, "output.log"));

    Process process = processBuilder.start();
    try {
      int exitCode = process.waitFor();
      if (exitCode == 0) {
        return parseResults(resultsFile);
      } else {
        throw new IOException(
          "An error ocurred running Jaccard. Exit code: " + exitCode + ". Execution logs can be found at "
            + dataDir.getAbsolutePath()
        );
      }
    } catch (InterruptedException e) {
      throw new IOException(
        "An error ocurred running Jaccard. Execution logs can be found at \"\n" +
          dataDir.getAbsolutePath(),
        e
      );
    }
  }

  private String getEffectiveCommandFileName(File file) {
    return file.getAbsolutePath().replace(this.dataDirectory, "");
  }
}
