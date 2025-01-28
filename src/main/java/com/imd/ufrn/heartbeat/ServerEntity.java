package com.imd.ufrn.heartbeat;

import java.net.InetAddress;
import java.util.Objects;

public class ServerEntity {

    private String associatedRoute;
    private InetAddress address;
    private Integer port;
    private Boolean isAlive;

    public ServerEntity() {
    }

    public ServerEntity(String associatedRoute, InetAddress address, Integer port, Boolean isAlive) {
        this.associatedRoute = associatedRoute;
        this.address = address;
        this.port = port;
        this.isAlive = isAlive;
    }

    public String getAssociatedRoute() {
        return associatedRoute;
    }

    public void setAssociatedRoute(String associatedRoute) {
        this.associatedRoute = associatedRoute;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Boolean getAlive() {
        return isAlive;
    }

    public void setAlive(Boolean alive) {
        isAlive = alive;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ServerEntity entity = (ServerEntity) o;
        return Objects.equals(associatedRoute, entity.associatedRoute) && Objects.equals(address, entity.address) && Objects.equals(port, entity.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(associatedRoute, address, port);
    }
}
