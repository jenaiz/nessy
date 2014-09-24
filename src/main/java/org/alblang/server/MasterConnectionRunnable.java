package org.alblang.server;

import org.alblang.config.ApplicationProperties;
import org.alblang.exceptions.ServerException;
import org.alblang.models.Node;
import org.alblang.utils.NodeUtils;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author jesus.navarrete  (24/09/14)
 */
public class MasterConnectionRunnable implements Runnable {

    private final int TEN_SECONDS = 10000;

    private ApplicationProperties appProperties;

    private Node node;

    public MasterConnectionRunnable(final Node node) throws ServerException {
        appProperties = ApplicationProperties.getInstance();
        this.node = node;
    }
    @Override
    public void run() {
        while (true) {
            try {
                int code = addToRoot(node);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(TEN_SECONDS);
            } catch (InterruptedException ie) {}
        }
    }

    public int addToRoot(final Node node) throws Exception {
        final String url = appProperties.getValue("node.root") + "/root/";

        final URL obj = new URL(url.trim());
        final HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setInstanceFollowRedirects(false);
        con.setRequestMethod("POST");
        con.setDoOutput(true);

        con.setRequestProperty("Content-Type", "application/json; charset=utf8");

        final OutputStream os = con.getOutputStream();
        os.write(NodeUtils.toJson(node).getBytes("UTF-8"));
        os.close();

        final int code = con.getResponseCode();

        return code;

    }
}
