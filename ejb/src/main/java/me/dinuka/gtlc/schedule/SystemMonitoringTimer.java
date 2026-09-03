package me.dinuka.gtlc.schedule;

import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.dinuka.gtlc.ejb.MonitoringFileService;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.LocalDateTime;
import com.sun.management.OperatingSystemMXBean;

@Singleton
@Startup
public class SystemMonitoringTimer {

    @PersistenceContext(unitName = "gtlcPU")
    private EntityManager em;

    @Inject
    private MonitoringFileService monitoringFileService;

    private static final long START_TIME = System.currentTimeMillis();

    @Schedule(
            second = "*/5",
            minute = "*",
            hour = "*",
            persistent = false
    )
    public void monitorSystem() {

        Runtime runtime = Runtime.getRuntime();

        long usedMemory =
                runtime.totalMemory()
                        - runtime.freeMemory();

        long maxMemory =
                runtime.maxMemory();

        double memoryUsage =
                ((double) usedMemory / maxMemory) * 100;

        // Get CPU usage
        OperatingSystemMXBean osBean =
                (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        double cpuUsage = osBean.getSystemLoadAverage() * 100;
        int availableProcessors = osBean.getAvailableProcessors();

        // Get application uptime
        String uptime = getApplicationUptime();

        // Get DB status
        String dbStatus = getDatabaseStatus();

        String data = String.format(
                """
                [%s]
                Application Status : ONLINE
                Application Uptime : %s
                JVM Memory Usage   : %.2f%%
                Used Memory        : %d MB
                Max Memory         : %d MB
                CPU Load Average   : %.2f%%
                Available CPUs     : %d
                Database Status    : %s
                """,
                LocalDateTime.now(),
                uptime,
                memoryUsage,
                usedMemory / (1024 * 1024),
                maxMemory / (1024 * 1024),
                cpuUsage,
                availableProcessors,
                dbStatus
        );

        monitoringFileService
                .writeSystemData(data);
    }

    private String getApplicationUptime() {
        long currentTime = System.currentTimeMillis();
        long uptimeMillis = currentTime - START_TIME;

        long days = uptimeMillis / (1000 * 60 * 60 * 24);
        long hours = (uptimeMillis % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (uptimeMillis % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (uptimeMillis % (1000 * 60)) / 1000;

        return String.format("%d days, %d hours, %d minutes, %d seconds",
                days, hours, minutes, seconds);
    }

    private String getDatabaseStatus() {
        try {
            // Test database connectivity by executing a simple native query
            em.createNativeQuery("SELECT 1")
                    .getSingleResult();
            return "ONLINE";
        } catch (Exception e) {
            try {
                // Fallback: try with a more generic query
                em.createQuery("SELECT COUNT(u) FROM User u")
                        .setMaxResults(1)
                        .getSingleResult();
                return "ONLINE";
            } catch (Exception ex) {
                return "OFFLINE - " + ex.getMessage();
            }
        }
    }
}
