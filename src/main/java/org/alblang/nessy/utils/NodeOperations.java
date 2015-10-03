package org.alblang.nessy.utils;

import org.alblang.nessy.config.ApplicationProperties;
import org.alblang.nessy.exceptions.ServerException;
import org.alblang.nessy.models.Node;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author jenaiz on 03/10/15.
 */
public class NodeOperations {

    private ApplicationProperties appProperties;

    public NodeOperations() throws ServerException {
        appProperties = ApplicationProperties.getInstance();
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
