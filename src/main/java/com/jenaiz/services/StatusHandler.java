package com.jenaiz.services;

import org.alblang.annotations.Service;
import org.alblang.config.ApplicationProperties;
import org.alblang.exceptions.ServerException;
import org.eclipse.jetty.server.Request;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created by jesus.navarrete on 14/02/14.
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
        response.getWriter().println("Server version : " + v);
        response.getWriter().println("Status : running");
        response.getWriter().println();
        response.getWriter().println(new Date());
    }
}
