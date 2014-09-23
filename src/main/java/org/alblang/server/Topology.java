package org.alblang.server;

import org.alblang.models.Node;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jesus.navarrete  (22/09/14)
 */
public class Topology {

    public static final int CHECKING_TIME = 2000;

    final private List<Node> nodes;
    final private List<Node> nodesAvailable;

    private static Topology instance;

    private Topology() {
        nodes = new ArrayList<>();
        nodesAvailable = new ArrayList<>();
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
                getRequest(node, "/status");
                System.out.println(node + " - available");
            } catch (Exception e) {
                System.out.println(node + " - not available");
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
        nodes.add(node);
    }

    public void getRequest(final Node node, final String serviceUrl) throws Exception {
        final String url = node.url() + serviceUrl;

        final URL obj = new URL(url.trim());
        final HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setInstanceFollowRedirects(false);
        con.setRequestMethod("GET");

        final int code = con.getResponseCode();

        if (code == HttpURLConnection.HTTP_OK) {
            nodesAvailable.add(node);
        } else {
            if (nodesAvailable.contains(node))
                nodesAvailable.remove(node);
        }
    }

    public static void main(String[] args) {
        Topology t = Topology.getInstance();
        while (true) {
            t.checkNodes();
            System.out.println("next try in 2 sg.");
            try {
                Thread.sleep(CHECKING_TIME);
            } catch (InterruptedException e) {
            }
        }
    }
}
