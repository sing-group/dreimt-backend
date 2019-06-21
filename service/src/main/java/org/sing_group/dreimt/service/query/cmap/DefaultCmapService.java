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
package org.sing_group.dreimt.service.query.cmap;

import static java.nio.file.Files.write;
import static java.nio.file.StandardOpenOption.APPEND;
import static javax.transaction.Transactional.TxType.NEVER;
import static org.sing_group.dreimt.service.query.ServiceUtils.deleteDirectory;
import static org.sing_group.dreimt.service.query.ServiceUtils.generateCommand;
import static org.sing_group.dreimt.service.query.ServiceUtils.toGmtRow;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.dreimt.service.spi.query.cmap.CmapResultData;
import org.sing_group.dreimt.service.spi.query.cmap.CmapService;
import org.sing_group.dreimt.service.spi.query.cmap.CmapServiceConfiguration;
import org.sing_group.dreimt.service.util.DatabaseVersionSingleton;

@Default
@Transactional(NEVER)
public class DefaultCmapService implements CmapService {
  
  @Inject
  private DatabaseVersionSingleton databaseVersion;

  @Resource(name = "java:global/dreimt/docker/cmap/dataDir")
  private String dataDirectory;
  
  @Resource(name = "java:global/dreimt/docker/cmap/dataDir/deleteOnFinish")
  private boolean deleteOnFinish;
  
  @Resource(name = "java:global/dreimt/docker/cmap/command/signature")
  private String cmapSignatureScripCmd;
  
  @Resource(name = "java:global/dreimt/docker/cmap/command/geneset")
  private String cmapGenesetScripCmd;
  
  @Resource(name = "java:global/dreimt/docker/cmap/configuration/cores")
  private int cores;

  @Override
  public Stream<CmapResultData> cmap(
    CmapServiceConfiguration configuration, 
    Set<String> genes
  ) throws IOException {
    File dataDir = getDataDirectory(this.dataDirectory);
    File geneSetFile = new File(dataDir, "input-geneset.gmt");
    try {
      write(geneSetFile.toPath(), toGmtRow("GENESET_sig", genes).getBytes());
      
      return cmap(configuration, geneSetFile, cmapGenesetScripCmd, dataDir, this::parseGeneSetResults);
    } catch (IOException e) {
      throw e;
    } finally {
      if(deleteOnFinish) {
        deleteDirectory(dataDir.toPath());
      }
    }
  }

  @Override
  public Stream<CmapResultData> cmap(
    CmapServiceConfiguration configuration,
    Set<String> upGenes, Set<String> downGenes
  ) throws IOException {
    File dataDir = getDataDirectory(this.dataDirectory);
    File signatureFile = new File(dataDir, "input-signature.gmt");
    try {
      write(signatureFile.toPath(), toGmtRow("SIGNATURE_UP", upGenes).getBytes());
      write(signatureFile.toPath(), toGmtRow("SIGNATURE_DN", downGenes).getBytes(), APPEND);

      return cmap(configuration, signatureFile, cmapSignatureScripCmd, dataDir, this::parseSignatureResults);
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

  private Stream<CmapResultData> cmap(
    CmapServiceConfiguration configuration,
    File inputSignatureFile,
    String cmapCommand,
    File dataDir,
    Function<File, Stream<CmapResultData>> parseCmapResults
  ) throws IOException {
    final Map<String, String> cmapCommandReplacements = new HashMap<>();
    
    if(databaseVersion.getCurrentDatabaseVersion().isPresent()) {
      cmapCommandReplacements.put(
        "[DATABASE_VERSION]",
        databaseVersion.getCurrentDatabaseVersion().get().getVersion()
      );
    } else {
      throw new IOException(
        "The analysis cannot be run because the database version is not defined"
      );
    }
    
    cmapCommandReplacements.put(
      "[INPUT_FILE_NAME]",
      getEffectiveCommandFileName(inputSignatureFile)
    );
    cmapCommandReplacements.put(
      "[OUTPUT_FILE_NAME]", 
      getEffectiveCommandFileName(new File(dataDir, "output"))
    );
    cmapCommandReplacements.put("[NPERM]", Integer.toString(configuration.getNumPermutations()));
    cmapCommandReplacements.put("[CORES]", Integer.toString(cores));
    
    final String[] cmap = generateCommand(cmapCommand, cmapCommandReplacements);

    ProcessBuilder processBuilder = new ProcessBuilder(cmap);
    processBuilder.redirectError(new File(dataDir, "error.log"));
    processBuilder.redirectOutput(new File(dataDir, "output.log"));

    Process process = processBuilder.start();
    try {
      int exitCode = process.waitFor();
      if (exitCode == 0) {
        return parseCmapResults.apply(new File(dataDir, "output.tsv"));
      } else {
        throw new IOException(
          "An error ocurred running Cmap. Exit code: " + exitCode + ". Execution logs can be found at "
            + dataDir.getAbsolutePath()
        );
      }
    } catch (InterruptedException | CmapParseResultsIOException e) {
      throw new IOException(
        "An error ocurred running Cmap Execution logs can be found at \"\n" +
          dataDir.getAbsolutePath(),
        e
      );
    }
  }

  private String getEffectiveCommandFileName(File file) {
    return file.getAbsolutePath().replace(this.dataDirectory, "");
  }

  private Stream<CmapResultData> parseGeneSetResults(File resultsFile) throws CmapParseResultsIOException {
    try {
      return CmapScriptResultsParser.parseGeneSetResults(resultsFile);
    } catch (IOException e) {
      throw new CmapParseResultsIOException(e);
    }
  }

  private Stream<CmapResultData> parseSignatureResults(File resultsFile) throws CmapParseResultsIOException {
    try {
      return CmapScriptResultsParser.parseSignatureResults(resultsFile);
    } catch (IOException e) {
      throw new CmapParseResultsIOException(e);
    }
  }

  private static class CmapParseResultsIOException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CmapParseResultsIOException(IOException e) {
      super(e);
    }
  }
}
