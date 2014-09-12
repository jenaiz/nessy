package org.alblang.server;

import org.alblang.annotations.Component;
import org.alblang.annotations.Service;
import org.alblang.config.ApplicationProperties;
import org.alblang.exceptions.ServerException;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author jesus.navarrete  (29/08/14)
 */
public class Kernel {

    private ApplicationProperties appProperties;

    public Kernel() throws ServerException {
        appProperties = ApplicationProperties.getInstance();
    }

    public static void main(String[] args) throws ServerException {
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
        } catch (Exception e) {
            throw new ServerException("Error instantiating the server", e);
        }
    }

    private static Handler[] loadHandlers(final String scanPath) throws InstantiationException, IllegalAccessException {
        final Reflections reflections = new Reflections(scanPath);

        final Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);

        final List<Handler> handlers = new ArrayList<Handler>();

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
}
