package com.automotive.aspect;

import com.automotive.annotation.LogIgnore;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

@Aspect
@Component
@Slf4j
@Order(1)
public class LoggingAspect {
    
    @Pointcut("within(com.automotive.controller..*)")
    public void controllerMethods () {
    }
    
    @Around("controllerMethods()")
    public Object logRequestResponse (
            ProceedingJoinPoint joinPoint
    ) throws Throwable {
       
        String method = joinPoint.getSignature().toShortString();
        String user = getCurrentUser();
        
        logRequest(method, user, joinPoint.getArgs());
        
        Object result;
        try {
            result = joinPoint.proceed();
        }
        catch (Throwable ex) {
            log.error("Exception --> {} | user: {} | exception: {}", method, user, ex.getMessage());
            throw ex;
        }
        
        log.info("Response <-- {} | user: {} | result: {}", method, user, sanitize(result));
        return result;
    }
    
    private void logRequest (String method, String user, Object[] args) {
       
        String sanitizedArgs = (args == null || args.length == 0)
                ? "[]"
                : Arrays.toString(Arrays.stream(args).map(this::sanitize).toArray());
        
        log.info("Request  --> {} | user: {} | args: {}", method, user, sanitizedArgs);
    }
    
    private String getCurrentUser () {
       
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (
                auth != null &&
                auth.isAuthenticated() &&
                !"anonymousUser".equals(auth.getPrincipal())
        ) {
            return auth.getName();
        }
        
        return "anonymous";
    }
    
    private Object sanitize (Object obj) {
       
        if (obj == null) {
            return "null";
        }
        
        if (isSimpleType(obj)) {
            return obj;
        }
        
        if (obj instanceof Collection<?> collection) {
            return collection.stream().map(this::sanitize).toList();
        }
        
        Class<?> type = obj.getClass();
        
        if (type.getName().startsWith("java.") || type.isPrimitive()) {
            return obj.toString();
        }
        
        return buildFilteredMap(obj, type);
    }
    
    private boolean isSimpleType (Object obj) {
        return obj instanceof String
               || obj instanceof Number
               || obj instanceof Boolean
               || obj.getClass().isEnum();
    }
    
    private Map<String, Object> buildFilteredMap (Object obj, Class<?> type) {
       
        Map<String, Object> map = new LinkedHashMap<>();
        Set<String> ignoredFields = getIgnoredFieldNames(type);
        
        for (Field field : getAllFields(type)) {
            String name = field.getName();
            
            if (field.isAnnotationPresent(LogIgnore.class) || ignoredFields.contains(name)) {
                continue;
            }
            
            field.setAccessible(true);
           
            try {
                map.put(name, sanitize(field.get(obj)));
            }
            catch (IllegalAccessException e) {
                map.put(name, "ACCESS_DENIED");
            }
        }
       
        return map;
    }
    
    private Set<String> getIgnoredFieldNames (Class<?> type) {
       
        if (!type.isRecord()) {
            return Collections.emptySet();
        }
        
        Set<String> ignored = new HashSet<>();
        
        for (var component : type.getRecordComponents()) {
            if (component.isAnnotationPresent(LogIgnore.class)) {
                ignored.add(component.getName());
            }
        }
        
        return ignored;
    }
    
    private List<Field> getAllFields (Class<?> type) {
        
        List<Field> fields = new ArrayList<>(List.of(type.getDeclaredFields()));
        
        Class<?> superClass = type.getSuperclass();
        
        while (superClass != null && !superClass.equals(Object.class)) {
            fields.addAll(List.of(superClass.getDeclaredFields()));
            superClass = superClass.getSuperclass();
        }
        
        return fields;
    }
}
