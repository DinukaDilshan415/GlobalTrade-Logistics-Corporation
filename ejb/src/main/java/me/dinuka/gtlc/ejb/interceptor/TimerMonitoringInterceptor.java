package me.dinuka.gtlc.ejb.interceptor;

import jakarta.interceptor.AroundTimeout;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import me.dinuka.gtlc.annotation.MonitorTimeout;
import me.dinuka.gtlc.log.ApplicationLogger;

import java.util.logging.Level;
import java.util.logging.Logger;

@Interceptor
@MonitorTimeout
public class TimerMonitoringInterceptor {

    private static final Logger LOGGER = ApplicationLogger.getLogger();

    @AroundTimeout
    public Object monitorTimer(InvocationContext context) throws Exception {
        String methodName = context.getMethod().getName();
        long startTime = System.currentTimeMillis();
        LOGGER.info("TIMER STARTED | Method: " + methodName);

        try {
            Object result = context.proceed();

            long executionTime = System.currentTimeMillis() - startTime;

            LOGGER.info("TIMER COMPLETED | Method: " + methodName + " | Execution Time: " + executionTime + " ms");

            return result;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            LOGGER.log(Level.SEVERE, "TIMER FAILED | Method: " + methodName + " | Execution Time: " + executionTime + " ms", e);
            throw e;
        }
    }
}
