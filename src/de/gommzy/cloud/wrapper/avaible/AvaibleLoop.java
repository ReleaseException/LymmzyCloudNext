package de.gommzy.cloud.wrapper.avaible;

import com.sun.management.OperatingSystemMXBean;
import de.gommzy.cloud.wrapper.socket.Client;

import java.lang.management.ManagementFactory;

public class AvaibleLoop {
    public AvaibleLoop(Client client) {
        while (true) {
            OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

            double freeRam = osBean.getFreePhysicalMemorySize();
            double totalRam = osBean.getTotalPhysicalMemorySize();
            double ram = 100-((freeRam * 100) / totalRam);

            double cpu = osBean.getSystemCpuLoad() * 100;


            if (ram > cpu) {
                client.write("setscore " + Math.round(ram));
            } else {
                client.write("setscore " + Math.round(cpu));
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}
        }
    }
}
