package me.vasujain.studentsyncapi.aspect;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import me.vasujain.studentsyncapi.annotation.TrackPerformance;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class PerformanceAspect {
    private static final Logger log = LoggerFactory.getLogger(PerformanceAspect.class);
    private final MeterRegistry meterRegistry;
    private final ThreadLocal<Map<String, Long>> methodStartTimes = ThreadLocal.withInitial(HashMap::new);

    public PerformanceAspect(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    /**
     * Pointcut for methods annotated with @TrackPerformance
     */
    @Pointcut("@annotation(me.vasujain.studentsyncapi.annotation.TrackPerformance)")
    public void trackPerformanceAnnotationPointcut() {
        // Method is empty as this is just a Pointcut
    }

    /**
     * Pointcut for service layer methods
     */
    @Pointcut("execution(* me.vasujain.studentsyncapi.service.*.*(..))")
    public void serviceMethodsPointcut() {
        // Method is empty as this is just a Pointcut
    }

    /**
     * Pointcut for repository layer methods
     */
    @Pointcut("execution(* me.vasujain.studentsyncapi.repository.*.*(..))")
    public void repositoryMethodsPointcut() {
        // Method is empty as this is just a Pointcut
    }

    /**
     * Records start time before method execution
     */
    @Before("serviceMethodsPointcut()")
    public void beforeServiceMethod(JoinPoint joinPoint) {
        String methodId = createMethodId(joinPoint);
        methodStartTimes.get().put(methodId, System.nanoTime());
    }

    /**
     * Calculates and logs execution time after method returns successfully
     */
    @AfterReturning("serviceMethodsPointcut()")
    public void afterServiceMethodReturning(JoinPoint joinPoint) {
        String methodId = createMethodId(joinPoint);
        Long startTime = methodStartTimes.get().remove(methodId);

        if (startTime != null) {
            long executionTime = System.nanoTime() - startTime;
            String className = joinPoint.getTarget().getClass().getSimpleName();
            String methodName = joinPoint.getSignature().getName();

            // Record metrics for service method
            Timer.builder("service.execution.time")
                    .tag("class", className)
                    .tag("method", methodName)
                    .description("Service method execution time")
                    .register(meterRegistry)
                    .record(executionTime, TimeUnit.NANOSECONDS);

            log.debug("Service: {}#{} executed in {} ms",
                    className,
                    methodName,
                    TimeUnit.NANOSECONDS.toMillis(executionTime));
        }
    }

    /**
     * Measures repository method execution time using @Around advice
     */
    @Around("repositoryMethodsPointcut()")
    public Object measureRepositoryExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        Timer timer = Timer.builder("repository.execution.time")
                .tag("class", className)
                .tag("method", methodName)
                .description("Repository method execution time")
                .register(meterRegistry);

        long startTime = System.nanoTime();
        try {
            return joinPoint.proceed();
        } finally {
            long executionTime = System.nanoTime() - startTime;
            timer.record(executionTime, TimeUnit.NANOSECONDS);

            log.debug("Repository: {}#{} executed in {} ms",
                    className,
                    methodName,
                    TimeUnit.NANOSECONDS.toMillis(executionTime));
        }
    }

    /**
     * Detailed performance tracking for methods explicitly annotated with @TrackPerformance
     */
    @Around("trackPerformanceAnnotationPointcut()")
    public Object measureAnnotatedMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        TrackPerformance annotation = method.getAnnotation(TrackPerformance.class);

        String metricName = annotation.value().isEmpty() ?
                "custom.execution.time" : annotation.value();

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = method.getName();

        Timer timer = Timer.builder(metricName)
                .tag("class", className)
                .tag("method", methodName)
                .description("Custom tracked method execution time")
                .register(meterRegistry);

        long startTime = System.nanoTime();
        try {
            return joinPoint.proceed();
        } finally {
            long executionTime = System.nanoTime() - startTime;
            timer.record(executionTime, TimeUnit.NANOSECONDS);

            log.info("Performance tracked method {}#{} executed in {} ms",
                    className,
                    methodName,
                    TimeUnit.NANOSECONDS.toMillis(executionTime));
        }
    }

    /**
     * Creates a unique method identifier for the join point
     */
    private String createMethodId(JoinPoint joinPoint) {
        return joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName();
    }
}