package org.alblang.nessy.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * @author jenaiz on 02/10/15.
 */
public class FileUtils {

    public static String readLargerTextFile(final String aFileName, final Charset ENCODING) throws IOException {
        final File f = new File(aFileName);
        if (f.exists()) {
            final Path path = Paths.get(aFileName);
            final StringBuilder sb = new StringBuilder();
            try (Scanner scanner =  new Scanner(path, ENCODING.name())){
                while (scanner.hasNextLine()){
                    sb.append(scanner.nextLine());
                }
            }
            return sb.toString();
        }
        return "";
    }

    public static void writeLargerTextFile(final String fileName, final String content, final Charset ENCODING) throws
            IOException {
        final Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path, ENCODING)){
            writer.write(content);
        }
    }
}
