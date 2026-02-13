package com.company.inventory.audit;

import com.company.inventory.model.AuditLog;
import com.company.inventory.repository.AuditLogRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;


@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {
    private final AuditLogRepository auditLogRepository;

    @AfterReturning(pointcut = "@annotation(auditable)", returning = "result")
    public void logAudit(JoinPoint joinPoint, Auditable auditable, Object result) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        
        AuditLog log = AuditLog.builder()
                .username(username)
                .action(auditable.action())
                .timestamp(LocalDateTime.now())
                .build();
        
        auditLogRepository.save(log);
    }
}