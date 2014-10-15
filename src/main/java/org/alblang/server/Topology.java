package org.alblang.server;

import org.alblang.models.Node;
import org.apache.log4j.Logger;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author jesus.navarrete  (22/09/14)
 */
public class Topology {

    public static final int CHECKING_TIME = 2000;

    final private List<Node> nodes;
    final private List<Node> nodesAvailable;

    private static Topology instance;

    private Logger logger = Logger.getLogger(Topology.class);

    private Topology() {
        nodes = Collections.synchronizedList(new ArrayList<Node>());
        nodesAvailable = Collections.synchronizedList(new ArrayList<Node>());;
    }

    public static Topology getInstance() {
        if (instance == null) {
            instance = new Topology();
        }
        return instance;
    }

    public void checkNodes() {

        for (Node node: nodes) {
            try {
                getRequest(node, "/status/");
                logger.info(node.url() + " - available");
            } catch (Exception e) {
                logger.error(node.url() + " - not available [" + e.getMessage() + "]");
                if (nodesAvailable.contains(node)) {
                    nodesAvailable.remove(node);
                    logger.error("node not available, removing: " + node.url());
                }
            }
        }
    }

    public List<Node> availableNodes() {
        return nodesAvailable;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void addNode(final Node node) {
        synchronized (this) {
            if (!nodes.contains(node)) {
                logger.info("addNode ... " + node.url());
                nodes.add(node);
            }
        }
    }

    public void getRequest(final Node node, final String serviceUrl) throws Exception {
        final String url = node.url() + serviceUrl;

        final URL obj = new URL(url.trim());
        final HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setInstanceFollowRedirects(false);
        con.setRequestMethod("GET");

        final int code = con.getResponseCode();

        if (code == HttpURLConnection.HTTP_OK) {
            logger.info("adding available node " + node.url());
            if (!nodesAvailable.contains(node)) nodesAvailable.add(node);
        } else {
            logger.error("answer from connection: " + code);
            if (nodesAvailable.contains(node)) {
                nodesAvailable.remove(node);
                logger.warn("node not available, removing: " + node.url());
            }
        }
    }
}
