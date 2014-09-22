package org.alblang.models;

/**
 * @author jesus.navarrete  (22/09/14)
 */
public class Node {

    private String name;
    private int port;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUrl() {
        final StringBuilder sb = new StringBuilder();

        if (!name.startsWith("http")) {
            sb.append("http://");
        }
        sb.append(name).append(":").append(port);

        return sb.toString();
    }
}
