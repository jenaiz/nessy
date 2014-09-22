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

    public String getUrl() {
        final StringBuilder sb = new StringBuilder();

        if (!hostName.startsWith("http")) {
            sb.append("http://");
        }
        sb.append(hostName).append(":").append(port);

        return sb.toString();
    }
}
