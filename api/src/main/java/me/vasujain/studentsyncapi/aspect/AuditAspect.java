package me.vasujain.studentsyncapi.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class AuditAspect {
    private static final Logger log = LoggerFactory.getLogger("SECURITY_AUDIT");

    /**
     * Pointcut for authentication-related methods
     */
    @Pointcut("execution(* me.vasujain.studentsyncapi.service.AuthenticationService.*(..))")
    public void authenticationMethodsPointcut() {
        // Method is empty as this is just a Pointcut
    }

    /**
     * Pointcut for user management methods
     */
    @Pointcut("execution(* me.vasujain.studentsyncapi.service.UserService.*(..))")
    public void userServiceMethodsPointcut() {
        // Method is empty as this is just a Pointcut
    }

    /**
     * Pointcut for sensitive data access methods
     */
    @Pointcut("execution(* me.vasujain.studentsyncapi.service.GradeService.*(..)) || " +
            "execution(* me.vasujain.studentsyncapi.service.EnrollmentService.*(..))")
    public void sensitiveDataAccessPointcut() {
        // Method is empty as this is just a Pointcut
    }

    /**
     * Audit successful authentication attempts
     */
    @AfterReturning(
            pointcut = "execution(* me.vasujain.studentsyncapi.service.AuthenticationService.authenticate(..))",
            returning = "result"
    )
    public void auditSuccessfulLogin(JoinPoint joinPoint, Object result) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            log.info("SECURITY: Successful authentication for user: {}",
                    args[0].toString().replaceAll("(?<=.{3}).(?=.*@)", "*"));
        }
    }

    /**
     * Audit failed authentication attempts
     */
    @AfterThrowing(
            pointcut = "execution(* me.vasujain.studentsyncapi.service.AuthenticationService.authenticate(..))",
            throwing = "exception"
    )
    public void auditFailedLogin(JoinPoint joinPoint, Throwable exception) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            log.warn("SECURITY: Failed authentication attempt for user: {}, reason: {}",
                    args[0].toString().replaceAll("(?<=.{3}).(?=.*@)", "*"),
                    exception.getMessage());
        }
    }

    /**
     * Audit user changes
     */
    @AfterReturning("userServiceMethodsPointcut() && (execution(* *.create*(..)) || execution(* *.update*(..)) || execution(* *.delete*(..)))")
    public void auditUserChanges(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication != null ? authentication.getName() : "system";

        log.info("SECURITY: User {} performed {} operation with args: {}",
                currentUser,
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    /**
     * Audit sensitive data access
     */
    @AfterReturning("sensitiveDataAccessPointcut()")
    public void auditSensitiveDataAccess(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication != null ? authentication.getName() : "anonymous";

        log.info("SECURITY: User {} accessed sensitive data via {}.{}",
                currentUser,
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
    }
}