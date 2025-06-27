package com.continiousdisappointment.chat.inteceptor;

import com.continiousdisappointment.chat.domain.User;
import com.continiousdisappointment.chat.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Log4j2
@Component
@RequiredArgsConstructor
public class UserInterceptor implements HandlerInterceptor {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final UserService userService;

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (checkUnauthorizedAllowedPath(request.getRequestURI())) {
           return true;
        }

        String token = request.getHeader(AUTHORIZATION_HEADER);
        if (token == null) {
            log.warn("No token found in Authorization header");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        try {
            User user = userService.getUserInfo(token);
            request.setAttribute("user", user);
            log.info("User: {} made {} request with path {}", user.id(), request.getMethod(), request.getRequestURI());
            return true;
        } catch (Exception e) {
            log.warn("Error while getting user info", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }

    private boolean checkUnauthorizedAllowedPath(String path) {
        return path.startsWith("/swagger") || path.startsWith("/v3/api-docs");
    }
}
