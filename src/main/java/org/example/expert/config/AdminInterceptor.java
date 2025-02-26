package org.example.expert.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.expert.domain.user.enums.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AdminInterceptor.class);
    private static final String USER_ROLE = "userRole";

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler
    ) throws IOException {
        UserRole currentUserRole = UserRole.of((String)request.getAttribute(USER_ROLE));

        if(!UserRole.ADMIN.equals(currentUserRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "관리자만 가능한 작업입니다.");
            return false;
        }

        logAdminAccess(request);

        return true;
    }

    private void logAdminAccess(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        LocalDateTime requestTime = LocalDateTime.now();
        logger.info("admin 접근 요청 ({}, accepted) [{}]", requestTime, requestURI);
    }
}
