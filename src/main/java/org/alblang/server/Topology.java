package org.alblang.server;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jesus.navarrete  (22/09/14)
 */
public class Topology {

    public static final int CHECKING_TIME = 2000;
    final private List<String> nodes;
    final private List<String> nodesAvailable;

    public Topology() {
        nodes = new ArrayList<>();
        nodesAvailable = new ArrayList<>();

        nodes.add("http://127.0.0.1:9090");
        nodes.add("http://127.0.0.1:9092");
        nodes.add("http://127.0.0.1:9093");
    }

    public void checkNodes() {

        for (String node: nodes) {
            try {
                getRequest(node, "/status");
                System.out.println(node + " - available");
            } catch (Exception e) {
                System.out.println(node + " - not available");
            }
        }
    }

    public List<String> availableNodes() {
        return nodesAvailable;
    }

    public void getRequest(final String node, final String serviceUrl) throws Exception {
        final String url = node + serviceUrl;

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
        Topology t = new Topology();
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
