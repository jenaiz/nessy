package com.jenaiz.services;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author jesus.navarrete (14/02/14)
 */
public abstract class AbstractKernelHandler extends AbstractHandler {

    public static final String DEFAULT_TYPE = "application/json;charset=utf-8";

    private Logger logger = Logger.getLogger(AbstractKernelHandler.class.getName());

    @Override
    public void handle(String s, Request baseRequest, HttpServletRequest httpRequest,
                       HttpServletResponse response) throws IOException, ServletException {

        logger.info("extra url - " + s);

        logger.info("Content Type IN : " + httpRequest.getContentType());

        response.setContentType(DEFAULT_TYPE);

        h(s, baseRequest, httpRequest, response);

        logger.info("response - " + response.getStatus());
    }

    public abstract void h(String s, Request baseRequest, HttpServletRequest httpServletRequest,
                      HttpServletResponse response) throws IOException, ServletException;


    /*public FakeResponse jsonToJava(final String json) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final FakeResponse fakeResponse = mapper.readValue(json, FakeResponse.class);

        return fakeResponse;
    }

    public String javaToJson(final FakeResponse fakeResponse) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final StringWriter sw = new StringWriter();
        final PrintWriter p = new PrintWriter(sw);

        mapper.writeValue(p, fakeResponse);

        return sw.toString();
    }*/
}
