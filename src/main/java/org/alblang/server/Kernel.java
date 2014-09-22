package org.alblang.server;

import org.alblang.annotations.Component;
import org.alblang.annotations.Service;
import org.alblang.config.ApplicationProperties;
import org.alblang.exceptions.ServerException;
import org.alblang.models.Node;
import org.alblang.utils.NodeUtils;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.reflections.Reflections;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * @author jesus.navarrete  (29/08/14)
 */
public class Kernel {

    private ApplicationProperties appProperties;

    public Kernel() throws ServerException {
        appProperties = ApplicationProperties.getInstance();
    }

    public static void main(String[] args) throws ServerException, IOException, URISyntaxException {
        if (args != null && args.length > 0) {
            System.out.println("with json setup!!! ");
            InputStream stream = Kernel.class.getClassLoader().getResourceAsStream(args[0]);

            System.out.println("-");
            System.out.println(" " + args[0]);
            final Scanner s = new Scanner(stream).useDelimiter("\\A");
            String text = s.hasNext() ? s.next() : "";

            Node node = NodeUtils.toJava(text);

            System.out.println(text);
        } else {
            //nodeSetup = "src/root.json";
        }

        final Kernel k = new Kernel();
        k.start("");

    }

    public void start(final String path) throws ServerException {
        try {
            final Handler[] h = loadHandlers(path);

            final Server server = new Server(Integer.valueOf(appProperties.getValue("port")));

            final ContextHandlerCollection chc = new ContextHandlerCollection();
            chc.setHandlers(h);

            server.setHandler(chc);

            server.start();
            server.join();

            String rol = appProperties.getValue("rol");

            if ("chunker".equals(rol)) {
                String root = appProperties.getValue("node.root");
                String[] props = root.split(":");
                int retries = 3;

                while (retries > 0) {
                    int code = addToRoot(new Node(props[0], new Integer(props[1])));
                    if (code == HttpURLConnection.HTTP_OK) {
                        break;
                    } else {
                        --retries;
                    }
                }
            }
        } catch (Exception e) {
            throw new ServerException("Error instantiating the server", e);
        }
    }

    private static Handler[] loadHandlers(final String scanPath) throws InstantiationException, IllegalAccessException {
        final Reflections reflections = new Reflections(scanPath);

        final Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);

        final List<Handler> handlers = new ArrayList<>();

        for (Class<?> c : services) {
            Service service = c.getAnnotation(Service.class);

            System.out.println(c.getName());
            System.out.println("mapping=" + service.mapping() + "\n");

            final ContextHandler ctx = new ContextHandler();
            ctx.setContextPath(service.mapping());
            ctx.setHandler((Handler)c.newInstance());
            handlers.add(ctx);
        }
        return handlers.toArray(new Handler[0]);
    }

    private static void ioc(final String scanPath) {
        final Reflections reflections = new Reflections(scanPath);

        final Set<Class<?>> components = reflections.getTypesAnnotatedWith(Component.class);

        final List iocComps = new ArrayList();

        for (Class<?> c : components) {
            Component component = (Component) c.getAnnotation(Component.class);
            System.out.println(c.getName());

            iocComps.add(component);
        }
    }

    public int addToRoot(final Node node) throws Exception {
        final String url = node.getUrl() + "/root";

        final URL obj = new URL(url.trim());
        final HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setInstanceFollowRedirects(false);
        con.setRequestMethod("POST");

        con.setRequestProperty("Content-Type", "application/json; charset=utf8");

        final OutputStream os = con.getOutputStream();
        os.write(NodeUtils.toJson(node).getBytes("UTF-8"));
        os.close();

        final int code = con.getResponseCode();

        return code;

    }

}
