package com.example.app.infrastructure.audit;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

/**
 * AOP Aspect for automatic audit logging
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {
    
    private final AuditService auditService;
    
    /**
     * Log successful method execution
     */
    @AfterReturning(pointcut = "@annotation(auditable)", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Auditable auditable, Object result) {
        try {
            HttpServletRequest request = getCurrentRequest();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            UUID userId = null;
            String username = "anonymous";
            
            if (authentication != null && authentication.isAuthenticated() && 
                !authentication.getPrincipal().equals("anonymousUser")) {
                username = authentication.getName();
                // You can extract userId from authentication if needed
            }
            
            String entityId = extractEntityId(result);
            
            auditService.logAction(
                    userId,
                    username,
                    auditable.action(),
                    auditable.entityName(),
                    entityId,
                    request != null ? request.getMethod() : null,
                    request != null ? request.getRequestURI() : null,
                    request != null ? getClientIp(request) : null,
                    request != null ? request.getHeader("User-Agent") : null,
                    true,
                    null
            );
        } catch (Exception e) {
            log.error("Error in audit logging", e);
        }
    }
    
    /**
     * Log failed method execution
     */
    @AfterThrowing(pointcut = "@annotation(auditable)", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Auditable auditable, Exception exception) {
        try {
            HttpServletRequest request = getCurrentRequest();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            UUID userId = null;
            String username = "anonymous";
            
            if (authentication != null && authentication.isAuthenticated() && 
                !authentication.getPrincipal().equals("anonymousUser")) {
                username = authentication.getName();
            }
            
            auditService.logAction(
                    userId,
                    username,
                    auditable.action(),
                    auditable.entityName(),
                    null,
                    request != null ? request.getMethod() : null,
                    request != null ? request.getRequestURI() : null,
                    request != null ? getClientIp(request) : null,
                    request != null ? request.getHeader("User-Agent") : null,
                    false,
                    exception.getMessage()
            );
        } catch (Exception e) {
            log.error("Error in audit logging", e);
        }
    }
    
    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }
    
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    private String extractEntityId(Object result) {
        if (result == null) {
            return null;
        }
        
        // Try to extract ID from result object
        try {
            if (result instanceof UUID) {
                return result.toString();
            }
            // Add more extraction logic as needed
        } catch (Exception e) {
            log.debug("Could not extract entity ID from result", e);
        }
        
        return null;
    }
}
