package metrics.common.model;

import java.util.Objects;

public class Host {
    final private String name;
    final private String ipAddress;
    final private Hardware hardware;

    public Host(String name, String ipAddress, Hardware hardware) {
        this.name = name;
        this.ipAddress = ipAddress;
        this.hardware = hardware;
    }

    public String getName() {
        return name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Hardware getOsData() {
        return hardware;
    }

    @Override
    public String toString() {
        return "Host{" +
                "name='" + name + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", osData=" + hardware.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Host)) return false;
        Host host = (Host) o;
        return name.equals(host.name) &&
                ipAddress.equals(host.ipAddress) &&
                hardware.equals(host.hardware);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ipAddress, hardware);
    }
}
