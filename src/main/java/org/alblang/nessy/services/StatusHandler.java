package org.alblang.nessy.services;

import org.alblang.nessy.config.ApplicationProperties;
import org.alblang.nessy.exceptions.ServerException;
import org.alblang.nessy.models.Node;
import org.alblang.nessy.client.models.Status;
import org.alblang.nessy.server.Topology;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.util.Date;

/**
 * @author jesus.navarrete (14/02/14)
 */
@Path("/status")
public class StatusHandler {

    private Logger logger = Logger.getLogger(StatusHandler.class.getName());

    @GET
    @Produces("application/json")
    public Status status()
            throws IOException, ServletException {

        String v = null;
        try {
            v = ApplicationProperties.getInstance().getValue("server.version");
        } catch (ServerException e) {
            logger.error("reading version from app properties", e);
        }

        String rol = "root"; //appProperties.getValue("rol");

        final Status status = new Status();

        status.setVersion(v);
        status.setRol(rol);
        status.setUpdatedAt(new Date());

        final Topology topo = Topology.getInstance();

        printNodeInfo(status, topo);

        return status;
    }

    private void printNodeInfo(Status sb, Topology topology) throws IOException {
        for (Node n : topology.availableNodes()) {
            sb.getAvailables().add(n.url());
        }

        for (Node n : topology.getNodes()) {
            sb.getNodes().add(n.url());
        }
    }
}
