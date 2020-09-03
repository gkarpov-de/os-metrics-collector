package metrics.common.model;

import java.util.Objects;

public class Hardware {
    final private long systemLoadAverage;
    final private long totalPhysicalMemorySize;
    final private long freePhysicalMemorySize;
    final private long totalSwapSpaceSize;
    final private long freeSwapSpaceSize;

    public Hardware(long systemLoadAverage, long totalPhysicalMemorySize, long freePhysicalMemorySize, long totalSwapSpaceSize, long freeSwapSpaceSize) {
        this.systemLoadAverage = systemLoadAverage;
        this.totalPhysicalMemorySize = totalPhysicalMemorySize;
        this.freePhysicalMemorySize = freePhysicalMemorySize;
        this.totalSwapSpaceSize = totalSwapSpaceSize;
        this.freeSwapSpaceSize = freeSwapSpaceSize;
    }

    public long getSystemLoadAverage() {
        return systemLoadAverage;
    }

    public long getTotalPhysicalMemorySize() {
        return totalPhysicalMemorySize;
    }

    public long getFreePhysicalMemorySize() {
        return freePhysicalMemorySize;
    }

    public long getTotalSwapSpaceSize() {
        return totalSwapSpaceSize;
    }

    public long getFreeSwapSpaceSize() {
        return freeSwapSpaceSize;
    }

    @Override
    public String toString() {
        return "Hardware{" +
                "systemLoadAverage=" + systemLoadAverage +
                ", totalPhysicalMemorySize=" + totalPhysicalMemorySize +
                ", freePhysicalMemorySize=" + freePhysicalMemorySize +
                ", totalSwapSpaceSize=" + totalSwapSpaceSize +
                ", freeSwapSpaceSize=" + freeSwapSpaceSize +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hardware)) return false;
        Hardware hardware = (Hardware) o;
        return systemLoadAverage == hardware.systemLoadAverage &&
                totalPhysicalMemorySize == hardware.totalPhysicalMemorySize &&
                freePhysicalMemorySize == hardware.freePhysicalMemorySize &&
                totalSwapSpaceSize == hardware.totalSwapSpaceSize &&
                freeSwapSpaceSize == hardware.freeSwapSpaceSize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(systemLoadAverage, totalPhysicalMemorySize, freePhysicalMemorySize, totalSwapSpaceSize, freeSwapSpaceSize);
    }
}
