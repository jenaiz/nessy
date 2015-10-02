package org.alblang.nessy.processes;

import org.alblang.nessy.config.ApplicationProperties;
import org.alblang.nessy.exceptions.ServerException;
import org.alblang.nessy.models.Node;
import org.alblang.nessy.server.Topology;
import org.alblang.nessy.utils.FileUtils;
import org.alblang.nessy.utils.NodeMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
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
            final String previousInfo = FileUtils.readLargerTextFile(fileName, ENCODING);
            if (StringUtils.isNotEmpty(previousInfo)) {
                final List<Node> previousNodes = NodeMapper.fromString(previousInfo);

                for (Node node : previousNodes) {
                    t.addNode(node);
                }
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

    private void persistNodes(final Topology t) {
        final List<Node> nodes = t.getNodes();
        try {
            final String output = NodeMapper.toString(nodes);

            final String fileName = applicationProperties.getValue("temp.folder") + OUTPUT_FILE;

            FileUtils.writeLargerTextFile(fileName, output, ENCODING);

        } catch (IOException e) {
            logger.error("error saving the application.properties file", e);
        }
    }


}
