package org.alblang.client.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author jesus.navarrete  (22/10/14)
 */
public class Status {

    private String version;
    private String rol;
    private Date updatedAt;

    public Status() {
        nodes = new ArrayList<>();
        availables = new ArrayList<>();
    }

    private List<String> nodes;

    private List<String> availables;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public List<String> getAvailables() {
        return availables;
    }

    public void setAvailables(List<String> availables) {
        this.availables = availables;
    }
}
