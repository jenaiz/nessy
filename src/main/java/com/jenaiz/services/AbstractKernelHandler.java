package com.jenaiz.services;

import org.alblang.config.ApplicationProperties;
import org.alblang.exceptions.ServerException;
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
    protected ApplicationProperties appProperties;

    protected AbstractKernelHandler() {
        try {
        appProperties = ApplicationProperties.getInstance();
        } catch (ServerException se) {
            logger.info(se.getMessage());
        }
    }


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



}
