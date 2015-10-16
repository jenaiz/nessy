package org.alblang.nessy.processes;

import org.alblang.nessy.config.ApplicationProperties;
import org.alblang.nessy.exceptions.ServerException;
import org.alblang.nessy.models.Node;
import org.alblang.nessy.utils.NodeOperations;
import org.apache.log4j.Logger;

/**
 * @author jesus.navarrete  (24/09/14)
 */
public class MasterConnectionRunnable implements Runnable {

    private final static Logger logger = Logger.getLogger(MasterConnectionRunnable.class);

    private static int TEN_SECONDS;

    private Node node;

    public MasterConnectionRunnable(final Node node) throws ServerException {
        TEN_SECONDS = ApplicationProperties.getInstance().getInt("node.master.check.time");
        this.node = node;
    }
    @Override
    public void run() {
        try {
            final NodeOperations operation = new NodeOperations();

            while (true) {
                try {
                    operation.addToRoot(node);
                } catch (Exception e) {
                    logger.error("The connection with the root is giving problems: " + e.getMessage(), e);
                }
                try {
                    Thread.sleep(TEN_SECONDS);
                } catch (InterruptedException ie) { /*not important*/ }
            }
        } catch (ServerException e) {
            e.printStackTrace();
        }
    }

}
