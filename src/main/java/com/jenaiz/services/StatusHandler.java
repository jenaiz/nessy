package com.jenaiz.services;

import org.alblang.annotations.Service;
import org.alblang.config.ApplicationProperties;
import org.alblang.exceptions.ServerException;
import org.alblang.models.Node;
import org.alblang.server.Topology;
import org.eclipse.jetty.server.Request;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @author jesus.navarrete (14/02/14)
 */
@Service(mapping = "/status")
public class StatusHandler extends AbstractKernelHandler {

    @Override
    public void h(String target,Request baseRequest,HttpServletRequest request,HttpServletResponse response)
            throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        String v = null;
        try {
            v = ApplicationProperties.getInstance().getValue("server.version");
        } catch (ServerException e) {
            e.printStackTrace();
        }

        String rol = appProperties.getValue("rol");

        response.getWriter().println("Server version : " + v);
        response.getWriter().println("Server Rol : " + rol);
        response.getWriter().println("Status : running");
        response.getWriter().println();
        response.getWriter().println(new Date());
        Topology topo = Topology.getInstance();

        response.getWriter().println("\nAvailable nodes:");

        printNodeInfo(response, topo);
    }

    private void printNodeInfo(HttpServletResponse response, Topology topo) throws IOException {
        for (Node n : topo.availableNodes()) {
            response.getWriter().println(n.getUrl());
        }

        response.getWriter().println("\nAll nodes:");

        for (Node n : topo.getNodes()) {
            response.getWriter().println(n.getUrl());
        }
    }
}
