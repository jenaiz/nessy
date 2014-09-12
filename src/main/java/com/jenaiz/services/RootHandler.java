package com.jenaiz.services;

import org.alblang.annotations.Service;
import org.eclipse.jetty.server.Request;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jesus.navarrete (14/02/14)
 */
@Service(mapping = "/root")
public class RootHandler extends AbstractKernelHandler {

    @Override
    public void h(String s, Request baseRequest, HttpServletRequest httpServletRequest,
                       HttpServletResponse response) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println("r o o t");
    }
}
