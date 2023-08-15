package com.zbh.tce.config;

import com.zbh.tce.common.utils.SecurityUtils;
import com.zbh.tce.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @author ZinBhoneHtut
 */
@Component
@Slf4j
public class MDCFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID = "requestId";
    private static final String USERNAME = "username";
    private static final String IP_ADDRESS = "ipAddress";
    private static final String SESSION_ID = "sessionId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            MDC.put(REQUEST_ID, UUID.randomUUID().toString());
            MDC.put(USERNAME, getUsername());
            MDC.put(IP_ADDRESS, SecurityUtils.getIp(request));
            MDC.put(SESSION_ID, request.getSession().getId());
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("MDC Error: ", e);
            e.printStackTrace();
        } finally {
            MDC.remove(REQUEST_ID);
            MDC.remove(USERNAME);
            MDC.remove(IP_ADDRESS);
            MDC.remove(SESSION_ID);
        }
    }

    private String getUsername() {
        try {
            return SecurityUtils.getCurrentUsername();
        } catch (BadRequestException ex) {
            log.warn("The username is unknown.");
            return "Anonymous";
        }
    }
}