/**
 * Interceptor para logging de requisições HTTP usando SLF4J.
 */
package com.mercadoclone.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Interceptor para logging automático de requisições HTTP.
 */
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);
    private static final String REQUEST_ID_KEY = "requestId";
    private static final String START_TIME_KEY = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Gera ID único para a requisição
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        long startTime = System.currentTimeMillis();

        // Adiciona ao MDC para correlação de logs
        MDC.put(REQUEST_ID_KEY, requestId);
        MDC.put(START_TIME_KEY, String.valueOf(startTime));

        // Log da requisição entrante
        logger.info("🔄 HTTP {} {} from {} | User-Agent: {}",
                request.getMethod(),
                request.getRequestURI(),
                getClientIp(request),
                request.getHeader("User-Agent"));

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        try {
            long startTime = Long.parseLong(MDC.get(START_TIME_KEY));
            long duration = System.currentTimeMillis() - startTime;

            String logMessage = "✅ HTTP {} {} completed | Status: {} | Duration: {}ms";

            if (response.getStatus() >= 400) {
                logMessage = "❌ HTTP {} {} failed | Status: {} | Duration: {}ms";
                logger.warn(logMessage,
                        request.getMethod(),
                        request.getRequestURI(),
                        response.getStatus(),
                        duration);
            } else {
                logger.info(logMessage,
                        request.getMethod(),
                        request.getRequestURI(),
                        response.getStatus(),
                        duration);
            }

            if (ex != null) {
                logger.error("💥 Exception during request processing: {}", ex.getMessage(), ex);
            }

        } finally {
            // Limpa o MDC
            MDC.clear();
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}
