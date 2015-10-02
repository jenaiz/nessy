package org.alblang.nessy.server;

import org.alblang.nessy.config.ApplicationProperties;
import org.alblang.nessy.exceptions.ServerException;
import org.alblang.nessy.models.Node;
import org.alblang.nessy.processes.MasterConnectionRunnable;
import org.alblang.nessy.processes.TopologyRunnable;
import org.alblang.nessy.utils.NodeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

/**
 * @author jesus.navarrete  (29/08/14)
 */
public class Kernel {

    private final static Logger logger = Logger.getLogger(Kernel.class);

    private ApplicationProperties appProperties;

    public Kernel() throws ServerException {
        appProperties = ApplicationProperties.getInstance();
    }

    public static void main(String[] args) throws ServerException, IOException, URISyntaxException {
        final Node node;
        if (args != null && args.length > 0) {
            final InputStream stream = Kernel.class.getClassLoader().getResourceAsStream(args[0]);

            final Scanner s = new Scanner(stream).useDelimiter("\\A");
            final String text = s.hasNext() ? s.next() : "";
            node = NodeUtils.toJava(text);
        } else {
            node = new Node("127.0.0.1", 9090);
        }

        final Kernel k = new Kernel();

        k.start("", node);
        // TODO remove if it works in different computers and packaged
        // k.start("/Users/jenaiz/projects/nessy", node);

    }

    public void start(final String path, final Node node) throws ServerException {
        try {
            int port = node.getPort() != 0 ? node.getPort() : Integer.valueOf(appProperties.getValue("port"));
            final Server server = new Server(port);

            final WebAppContext context = new WebAppContext();

            context.setDescriptor(this.getClass().getResource("../../../../WEB-INF/web.xml").toString());
            logger.info("reading web.xml from " + this.getClass().getResource("../../../../WEB-INF/web.xml").toString());
            context.setResourceBase(this.getClass().getResource("../../../../").getPath());

            context.setContextPath("/");
            context.setParentLoaderPriority(true);

            server.setHandler(context);

            server.start();


            final String rol = StringUtils.isNotEmpty(node.getRol()) ? node.getRol() : appProperties.getValue("rol");

            if (Roles.chunker.name().equals(rol)) {
                int retries = 3;

                while (retries > 0) {
                    int code = addToRoot(node);
                    if (code == HttpURLConnection.HTTP_OK) {
                        break;
                    } else {
                        --retries;
                    }
                }
                final Thread rootChecker = new Thread(new MasterConnectionRunnable(node));
                rootChecker.start();
            } else if (Roles.root.name().equals(rol)) {
                final Thread topology = new Thread(new TopologyRunnable());
                topology.start();
            }

            server.join();

        } catch (Exception e) {
            throw new ServerException("Error instantiating the server", e);
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
