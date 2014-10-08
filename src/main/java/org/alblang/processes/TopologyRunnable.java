package org.alblang.processes;

import org.alblang.config.ApplicationProperties;
import org.alblang.exceptions.ServerException;
import org.alblang.models.Node;
import org.alblang.server.Topology;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * @author jesus.navarrete  (24/09/14)
 */
public class TopologyRunnable implements Runnable {

    private static final int TEN_SECONDS = 10000;

    private static final String OUTPUT_FILE = "nessy-nodes.json";

    private static ApplicationProperties applicationProperties;

    private Logger logger = Logger.getLogger(TopologyRunnable.class.getName());

    public TopologyRunnable() throws ServerException {
        applicationProperties = ApplicationProperties.getInstance();
    }

    @Override
    public void run() {

        // TODO first time check from file
        while (true) {
            final Topology t = Topology.getInstance();
            t.checkNodes();

            persistNodes(t);

            try {
                Thread.sleep(TEN_SECONDS);
            } catch (InterruptedException e) {
                logger.error("error waiting", e);
            }
        }
    }

    private void persistNodes(Topology t) {
        final List<Node> nodes = t.availableNodes();
        try {
            final String output = toString(nodes);

            // TODO don't save continously, only when there is a change!
            final String fileName = applicationProperties.getValue("temp.folder") + OUTPUT_FILE;
            final PrintWriter out = new PrintWriter(fileName);
            out.write(output);
            out.close();

        } catch (IOException e) {
            logger.error("error reading the application.properties file", e);
        }
    }


    public String toString(final List<Node> nodes) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final StringWriter sw = new StringWriter();
        final PrintWriter p = new PrintWriter(sw);

        mapper.writeValue(p, nodes);

        return sw.toString();
    }


    public static String toJson(final Node node) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final StringWriter sw = new StringWriter();
        final PrintWriter p = new PrintWriter(sw);

        mapper.writeValue(p, node);

        return sw.toString();
    }

}
