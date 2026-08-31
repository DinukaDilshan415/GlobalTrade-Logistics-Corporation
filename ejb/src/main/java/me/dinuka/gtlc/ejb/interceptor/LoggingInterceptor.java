package me.dinuka.gtlc.ejb.interceptor;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import me.dinuka.gtlc.annotation.Logged;
import me.dinuka.gtlc.log.ApplicationLogger;

import java.util.logging.Level;
import java.util.logging.Logger;

@Interceptor
@Logged
@Priority(Interceptor.Priority.APPLICATION + 2)
public class LoggingInterceptor {

    private static final Logger LOGGER =
            ApplicationLogger.getLogger();

    @AroundInvoke
    public Object logMethod(InvocationContext context) throws Exception {

        String className = context.getMethod()
                        .getDeclaringClass()
                        .getSimpleName();

        String methodName = context.getMethod().getName();

        long start = System.currentTimeMillis();

        LOGGER.info("START | " + className + "." + methodName);

        try {
            Object result = context.proceed();
            long duration = System.currentTimeMillis() - start;
            LOGGER.info("SUCCESS | " + className + "." + methodName + " | Duration: " + duration + " ms");
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            LOGGER.log(Level.SEVERE, "FAILED | " + className + "." + methodName + " | Duration: " + duration + " ms", e);
            throw e;
        }
    }
}