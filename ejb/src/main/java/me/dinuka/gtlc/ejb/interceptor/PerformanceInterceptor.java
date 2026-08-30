package me.dinuka.gtlc.ejb.interceptor;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import me.dinuka.gtlc.annotation.PerformanceMonitored;
import me.dinuka.gtlc.ejb.MonitoringFileService;

import java.time.LocalDateTime;

@Interceptor
@PerformanceMonitored
@Priority(Interceptor.Priority.APPLICATION+1)
public class PerformanceInterceptor {

    @Inject
    private MonitoringFileService monitoringFileService;

    @AroundInvoke
    public Object monitor(InvocationContext context)
            throws Exception {

        long start = System.nanoTime();

        String status = "SUCCESS";

        try {

            return context.proceed();

        } catch (Exception e) {

            status = "FAILED";
            throw e;

        } finally {

            long duration =
                    (System.nanoTime() - start)
                            / 1_000_000;

            String service =
                    context.getMethod()
                            .getDeclaringClass()
                            .getSimpleName();

            String method =
                    context.getMethod()
                            .getName();

            String log = String.format(
                    "[%s]%n" +
                            "Service  : %s%n" +
                            "Method   : %s%n" +
                            "Duration : %d ms%n" +
                            "Status   : %s%n",
                    LocalDateTime.now(),
                    service,
                    method,
                    duration,
                    status
            );

            monitoringFileService
                    .writePerformanceData(log);
        }
    }
}
