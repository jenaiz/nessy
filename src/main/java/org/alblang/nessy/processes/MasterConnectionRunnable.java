package org.alblang.nessy.processes;

import org.alblang.nessy.config.ApplicationProperties;
import org.alblang.nessy.exceptions.ServerException;
import org.alblang.nessy.models.Node;
import org.alblang.nessy.utils.NodeMapper;
import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author jesus.navarrete  (24/09/14)
 */
public class MasterConnectionRunnable implements Runnable {

    private final static Logger logger = Logger.getLogger(MasterConnectionRunnable.class);

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
                logger.error("The connection with the root is giving problems: " + e.getMessage(), e);
            }
            try {
                Thread.sleep(TEN_SECONDS);
            } catch (InterruptedException ie) { /*not important*/ }
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
        os.write(NodeMapper.toJson(node).getBytes("UTF-8"));
        os.close();

        return con.getResponseCode();
    }
}
