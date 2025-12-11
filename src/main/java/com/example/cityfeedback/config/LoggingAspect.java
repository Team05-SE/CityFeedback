package com.example.cityfeedback.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * AOP-Aspekt für Logging und Performance-Monitoring im Application Layer.
 * 
 * Cross-Cutting Concerns:
 * - Methodenaufrufe loggen (Before Advice)
 * - Exceptions zentral loggen (AfterThrowing Advice)
 * - Performance-Messung (Around Advice)
 * 
 * Dieser Aspekt wird automatisch auf alle öffentlichen Methoden in Application Services angewendet:
 * - UserService (usermanagement.application)
 * - FeedbackService (feedbackmanagement.application)
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Pointcut: Alle öffentlichen Methoden in Application Services.
     * Deckt ab:
     * - UserService (usermanagement.application)
     * - FeedbackService (feedbackmanagement.application)
     */
    @Pointcut("execution(public * com.example.cityfeedback.usermanagement.application.*Service.*(..)) || " +
              "execution(public * com.example.cityfeedback.feedbackmanagement.application.*Service.*(..))")
    public void applicationServiceMethods() {}

    /**
     * Before Advice: Loggt jeden Methodenaufruf.
     * Zeigt: Methodenname und Anzahl der Parameter.
     */
    @Before("applicationServiceMethods()")
    public void logMethodCall(JoinPoint joinPoint) {
        logger.info("➡️ Aufruf: {}({})",
                joinPoint.getSignature().toShortString(),
                joinPoint.getArgs().length
        );
    }

    /**
     * AfterThrowing Advice: Loggt alle Exceptions in Services.
     * Wichtig: Wird VOR dem GlobalExceptionHandler ausgeführt.
     */
    @AfterThrowing(pointcut = "applicationServiceMethods()", throwing = "error")
    public void logException(JoinPoint joinPoint, Throwable error) {
        logger.error("❌ Fehler in {}: {} - {}",
                joinPoint.getSignature().toShortString(),
                error.getClass().getSimpleName(),
                error.getMessage()
        );
    }

    /**
     * Around Advice: Misst die Ausführungszeit jeder Service-Methode.
     * Loggt nur Methoden, die länger als 100ms dauern (Performance-Warnung).
     */
    @Around("applicationServiceMethods()")
    public Object measureExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = pjp.proceed();
        long duration = System.currentTimeMillis() - start;

        // Nur langsame Methoden loggen (Performance-Warnung)
        if (duration > 100) {
            logger.warn("⏱️ Langsame Methode {}(): {} ms",
                    pjp.getSignature().getName(),
                    duration
            );
        } else {
            logger.debug("⏱️ Dauer {}(): {} ms",
                    pjp.getSignature().getName(),
                    duration
            );
        }

        return result;
    }
}

