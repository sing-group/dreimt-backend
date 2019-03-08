/*-
 * #%L
 * DREIMT - Service
 * %%
 * Copyright (C) 2018 - 2019 Daniel Glez-Peña, Miguel Reboiro-Jato, Hugo López-Fernández,
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
package org.sing_group.dreimt.service.query;

import static java.util.stream.Collectors.joining;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ServiceUtils {
  public static String toGmtRow(String name, Set<String> genes) {
    return genes.stream().collect(joining("\t", name + "\tNA\t", "\n"));
  }

  public static void deleteDirectory(Path directory) throws IOException {
    if (Files.isDirectory(directory)) {
      try {
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.delete(file);

            return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Files.delete(dir);

            return FileVisitResult.CONTINUE;
          }
        });
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    } else {
      throw new IllegalArgumentException("Path is not a directory: " + directory);
    }
  }

  public static String[] generateCommand(String command, Map<String, String> replacements) {
    for (Entry<String, String> entry : replacements.entrySet()) {
      command = command.replace(entry.getKey(), entry.getValue());
    }
    return command.split("\\s+");
  }
}
