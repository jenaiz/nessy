package com.jenaiz.services;

import org.alblang.models.Node;
import org.alblang.server.Topology;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * @author jesus.navarrete (14/02/14)
 */
@Path("/root")
public class RootHandler {

    private Logger logger = Logger.getLogger(RootHandler.class.getName());

    public RootHandler() {
    }

    @POST
    @Consumes("application/json")
    public Response createProduct(Node node) {

        logger.info(node.url() + ":" + node.getPort());

        // TODO check if the node exist before!
        // TODO validate node connection !
        Topology.getInstance().addNode(node);

        return Response.status(200).entity("root ok!root ok!root ok!root ok!root ok!root ok!").build();
    }
}
