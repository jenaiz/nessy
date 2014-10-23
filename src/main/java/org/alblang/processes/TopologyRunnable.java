package org.alblang.processes;

import org.alblang.config.ApplicationProperties;
import org.alblang.exceptions.ServerException;
import org.alblang.models.Node;
import org.alblang.server.Topology;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * @author jesus.navarrete  (24/09/14)
 */
public class TopologyRunnable implements Runnable {

    private static final int TEN_SECONDS = 10000;
    public static final int THIRTY_SECONDS = 30000;

    private static final String OUTPUT_FILE = "nessy-nodes.json";
    private final static Charset ENCODING = StandardCharsets.UTF_8;

    private static ApplicationProperties applicationProperties;

    private Logger logger = Logger.getLogger(TopologyRunnable.class);

    private static long lastUpdate;

    public TopologyRunnable() throws ServerException {
        applicationProperties = ApplicationProperties.getInstance();
    }

    @Override
    public void run() {

        final String fileName = applicationProperties.getValue("temp.folder") + OUTPUT_FILE;

        lastUpdate = System.currentTimeMillis();
        final Topology t = Topology.getInstance();

        try {
            final String previousInfo = readLargerTextFile(fileName);
            final List<Node> previousNodes = fromString(previousInfo);

            for (Node node : previousNodes) {
                t.addNode(node);
            }

        } catch (IOException e) {
            logger.error("Error reading the file", e);
        }

        while (true) {
            t.checkNodes();

            if ((System.currentTimeMillis() - lastUpdate) > THIRTY_SECONDS) {
                persistNodes(t);
            }

            try {
                Thread.sleep(TEN_SECONDS);
            } catch (InterruptedException e) {
                logger.error("error waiting", e);
            }
        }
    }

    private void persistNodes(Topology t) {
        final List<Node> nodes = t.getNodes();
        try {
            final String output = toString(nodes);

            final String fileName = applicationProperties.getValue("temp.folder") + OUTPUT_FILE;

            writeLargerTextFile(fileName, output);

        } catch (IOException e) {
            logger.error("error saving the application.properties file", e);
        }
    }


    public String toString(final List<Node> nodes) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final StringWriter sw = new StringWriter();
        final PrintWriter p = new PrintWriter(sw);

        mapper.writeValue(p, nodes);

        return sw.toString();
    }

    public List<Node> fromString(final String input) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final TypeFactory factory = mapper.getTypeFactory();
        return mapper.readValue(input, factory.constructCollectionType(List.class, Node.class));
    }


    public static String toJson(final Node node) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final StringWriter sw = new StringWriter();
        final PrintWriter p = new PrintWriter(sw);

        mapper.writeValue(p, node);

        return sw.toString();
    }

    private String readLargerTextFile(String aFileName) throws IOException {
        final Path path = Paths.get(aFileName);
        final StringBuilder sb = new StringBuilder();
        try (Scanner scanner =  new Scanner(path, ENCODING.name())){
            while (scanner.hasNextLine()){
                sb.append(scanner.nextLine());
            }
        }
        return sb.toString();
    }

    private void writeLargerTextFile(final String fileName, final String content) throws IOException {
        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path, ENCODING)){
            writer.write(content);
        }
    }
}
