package org.alblang.server;

/**
 * @author jesus.navarrete  (24/09/14)
 */
public class TopologyRunnable implements Runnable {

    private static final int TEN_SECONDS = 10000;

    @Override
    public void run() {
        // check for status
        while (true) {
            final Topology t = Topology.getInstance();
            t.checkNodes();
            try {
                Thread.sleep(TEN_SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
