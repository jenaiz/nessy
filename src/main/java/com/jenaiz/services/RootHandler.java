package com.jenaiz.services;

import org.alblang.annotations.Service;
import org.alblang.models.Node;
import org.alblang.server.Topology;
import org.alblang.utils.NodeUtils;
import org.eclipse.jetty.server.Request;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author jesus.navarrete (14/02/14)
 */
@Service(mapping = "/root")
public class RootHandler extends AbstractKernelHandler {

    @Override
    public void h(String s, Request baseRequest, HttpServletRequest httpServletRequest,
                       HttpServletResponse response) throws IOException, ServletException {

        final BufferedReader r = baseRequest.getReader();
        if (baseRequest.getContentLength() > 0) {
            StringBuilder sb = new StringBuilder();
            for (String line; (line = r.readLine()) != null;) {
                sb.append(line).append("\n");
            }

            final Node node = NodeUtils.toJava(sb.toString());
            System.out.println(node.getUrl() + ":" + node.getPort());

            // TODO check if the node exist before!
            // TODO validate node connection !
            Topology.getInstance().addNode(node);
        }

        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println("r o o t");

    }

}
