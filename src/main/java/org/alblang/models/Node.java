package org.alblang.models;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author jesus.navarrete  (22/09/14)
 */
public class Node {

    private String rol;

    @JsonProperty("host_name")
    private String hostName;
    private int port;

    public Node() {
    }

    public Node(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String url() {
        final StringBuilder sb = new StringBuilder();

        if (!hostName.startsWith("http")) {
            sb.append("http://");
        }
        sb.append(hostName).append(":").append(port);

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (port != node.port) return false;
        if (hostName != null ? !hostName.equals(node.hostName) : node.hostName != null) return false;
        if (rol != null ? !rol.equals(node.rol) : node.rol != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = rol != null ? rol.hashCode() : 0;
        result = 31 * result + (hostName != null ? hostName.hashCode() : 0);
        result = 31 * result + port;
        return result;
    }
}
