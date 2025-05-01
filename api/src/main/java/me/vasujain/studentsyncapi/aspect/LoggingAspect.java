package me.vasujain.studentsyncapi.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    /**
     * Pointcut for all Spring components (Repository, Service, Controller)
     */
    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut
    }

    /**
     * Pointcut for application packages
     */
    @Pointcut("within(me.vasujain.studentsyncapi..*)" +
            " && !within(me.vasujain.studentsyncapi.config..*)" +
            " && !within(me.vasujain.studentsyncapi.aspect..*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut
    }

    /**
     * Pointcut for all controller methods
     */
    @Pointcut("execution(* me.vasujain.studentsyncapi.controller.*.*(..))")
    public void controllerMethodsPointcut() {
        // Method is empty as this is just a Pointcut
    }

    /**
     * Gets the appropriate logger for the given join point
     */
    private Logger logger(JoinPoint joinPoint) {
        return LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
    }

    /**
     * Advice that logs when a method is entered
     *
     * @param joinPoint join point for advice
     */
    @Before("applicationPackagePointcut() && springBeanPointcut()")
    public void logBefore(JoinPoint joinPoint) {
        Logger log = logger(joinPoint);
        log.debug("Enter: {}() with argument[s] = {}",
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    /**
     * Advice that logs when a method is successfully completed
     *
     * @param joinPoint join point for advice
     * @param result result of the method call
     */
    @AfterReturning(pointcut = "applicationPackagePointcut() && springBeanPointcut()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        Logger log = logger(joinPoint);
        log.debug("Exit: {}() with result = {}",
                joinPoint.getSignature().getName(),
                result);
    }

    /**
     * Advice that logs when a method execution completes (regardless of outcome)
     *
     * @param joinPoint join point for advice
     */
    @After("controllerMethodsPointcut()")
    public void logAfter(JoinPoint joinPoint) {
        Logger log = logger(joinPoint);
        log.info("Completed execution of: {}.{}()",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
    }

    /**
     * Advice that logs methods throwing exceptions
     *
     * @param joinPoint join point for advice
     * @param e exception
     */
    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        Logger log = logger(joinPoint);
        log.error(
                "Exception in {}() with cause = '{}' and exception = '{}'",
                joinPoint.getSignature().getName(),
                e.getCause() != null ? e.getCause() : "NULL",
                e.getMessage(),
                e
        );
    }

    /**
     * Advice that logs API controller execution times
     *
     * @param joinPoint join point for advice
     * @return result
     * @throws Throwable throws any exception
     */
    @Around("controllerMethodsPointcut()")
    public Object logApiDuration(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = logger(joinPoint);
        long start = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;

            log.info("API {} executed in {} ms",
                    joinPoint.getSignature().getName(),
                    executionTime);

            return result;
        } catch (Exception e) {
            log.error("API {} failed after {} ms",
                    joinPoint.getSignature().getName(),
                    System.currentTimeMillis() - start);
            throw e;
        }
    }
}