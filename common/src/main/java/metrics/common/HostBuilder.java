package metrics.common;

import com.sun.management.OperatingSystemMXBean;
import com.typesafe.config.Config;
import metrics.common.model.Hardware;
import metrics.common.model.Host;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public final class HostBuilder {
    private static final String REMOTE_TEST_HOST = "google.com";
    private static final int REMOTE_TEST_PORT = 80;
    private static final String HOST_NAME = "omc.hostname"; // os.metrics.collector.hostname
    private static final String HOST_IP = "omc.hostip"; // os.metrics.collector.hostip

    public static Host createHost(Config config) {

        return new Host(getHostName(config), getIpAddress(config), getHardwareData());
    }

    public static Host getUpdated(Host host) {
        return new Host(host.getName(), host.getIpAddress(), getHardwareData());
    }

    private static Hardware getHardwareData() {
        final OperatingSystemMXBean osMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        return new Hardware((long) (osMXBean.getSystemLoadAverage() * 100),
                osMXBean.getTotalPhysicalMemorySize(),
                osMXBean.getFreePhysicalMemorySize(),
                osMXBean.getTotalSwapSpaceSize(),
                osMXBean.getFreeSwapSpaceSize());

    }

    private static InetAddress getInetAddress() {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException("Could not get host name. Please, update " + HOST_NAME + " in config file", e);
        }

        String tmpHostName = inetAddress.getHostName();

        if (tmpHostName.startsWith("localhost")) {
            Socket socket = new Socket();
            try {
                socket.connect(new InetSocketAddress(REMOTE_TEST_HOST, REMOTE_TEST_PORT));
            } catch (Exception e) {
                throw new RuntimeException("Could not get host name. Please, update " + HOST_NAME + " in config file", e);
            }
            inetAddress = socket.getLocalAddress();
        }
        return inetAddress;
    }


    private static String getHostName(Config config) {
        if (config.hasPath(HOST_NAME)) {
            return config.getString(HOST_NAME);
        }

        return getInetAddress().getHostName();
    }

    private static String getIpAddress(Config config) {
        if (config.hasPath(HOST_IP)) {
            return config.getString(HOST_IP);
        }

        return getInetAddress().getHostAddress();
    }

}
