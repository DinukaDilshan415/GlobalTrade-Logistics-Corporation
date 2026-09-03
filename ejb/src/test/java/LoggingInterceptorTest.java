import jakarta.interceptor.InvocationContext;
import me.dinuka.gtlc.ejb.interceptor.LoggingInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("LoggingInterceptor Tests")
@ExtendWith(MockitoExtension.class)
class LoggingInterceptorTest {

    @InjectMocks
    private LoggingInterceptor loggingInterceptor;

    @Mock
    private InvocationContext invocationContext;

    @Mock
    private Logger mockLogger;

    private Method testMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        testMethod = TestService.class.getMethod("testMethod");
    }

    @Test
    @DisplayName("Should log method start and success completion")
    void testLogMethodStartAndSuccess() throws Exception {
        when(invocationContext.getMethod()).thenReturn(testMethod);
        when(invocationContext.proceed()).thenReturn("test result");

        Object result = loggingInterceptor.logMethod(invocationContext);

        assertEquals("test result", result);
        verify(invocationContext).proceed();
    }

    @Test
    @DisplayName("Should return the result from the invoked method")
    void testReturnMethodResult() throws Exception {

        String expectedResult = "Method executed successfully";
        when(invocationContext.getMethod()).thenReturn(testMethod);
        when(invocationContext.proceed()).thenReturn(expectedResult);

        Object result = loggingInterceptor.logMethod(invocationContext);

        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("Should propagate exceptions thrown by the method")
    void testLogMethodPropagatesException() throws Exception {
        when(invocationContext.getMethod()).thenReturn(testMethod);
        Exception testException = new RuntimeException("Test error");
        when(invocationContext.proceed()).thenThrow(testException);

        RuntimeException thrownException = assertThrows(
                RuntimeException.class,
                () -> loggingInterceptor.logMethod(invocationContext),
                "Should throw the exception from the invoked method"
        );
        assertEquals("Test error", thrownException.getMessage());
    }

    @Test
    @DisplayName("Should log execution duration on success")
    void testLogExecutionDuration() throws Exception {
        when(invocationContext.getMethod()).thenReturn(testMethod);
        when(invocationContext.proceed()).thenReturn("result");

        loggingInterceptor.logMethod(invocationContext);

        verify(invocationContext, times(1)).proceed();
    }

    @Test
    @DisplayName("Should handle null return value")
    void testLogMethodWithNullReturn() throws Exception {
        when(invocationContext.getMethod()).thenReturn(testMethod);
        when(invocationContext.proceed()).thenReturn(null);

        Object result = loggingInterceptor.logMethod(invocationContext);

        assertNull(result);
        verify(invocationContext).proceed();
    }

    @Test
    @DisplayName("Should use correct class name in log message")
    void testLogUsesCorrectClassName() throws Exception {
        Method method = AnotherService.class.getMethod("anotherMethod");
        when(invocationContext.getMethod()).thenReturn(method);
        when(invocationContext.proceed()).thenReturn("result");

        loggingInterceptor.logMethod(invocationContext);

        verify(invocationContext).proceed();
        assertEquals("AnotherService", method.getDeclaringClass().getSimpleName());
    }

    @Test
    @DisplayName("Should handle exception and continue logging")
    void testExceptionLoggingContinues() throws Exception {
        when(invocationContext.getMethod()).thenReturn(testMethod);
        when(invocationContext.proceed()).thenThrow(new IllegalArgumentException("Invalid argument"));

        assertThrows(
                IllegalArgumentException.class,
                () -> loggingInterceptor.logMethod(invocationContext)
        );
        verify(invocationContext).proceed();
    }

    @Test
    @DisplayName("Should record start before method execution")
    void testRecordStartBeforeExecution() throws Exception {
        when(invocationContext.getMethod()).thenReturn(testMethod);
        when(invocationContext.proceed()).thenReturn("result");

        long startTime = System.currentTimeMillis();
        loggingInterceptor.logMethod(invocationContext);
        long endTime = System.currentTimeMillis();

        assertTrue(endTime >= startTime);
    }

    static class TestService {
        public void testMethod() {
        }
    }

    static class AnotherService {
        public void anotherMethod() {
        }
    }
}
