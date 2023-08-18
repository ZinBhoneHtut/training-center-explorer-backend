package com.zbh.tce.config;

import com.zbh.tce.common.constant.UrlConstant;
import com.zbh.tce.security.jwt.AuthEntryPointJwt;
import com.zbh.tce.security.jwt.JwtTokenFilter;
import com.zbh.tce.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.zbh.tce.entity.enums.RoleEnum.*;

/**
 * @author ZinBhoneHtut
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthEntryPointJwt unauthorizedHandler;
    private final JwtTokenFilter jwtTokenFilter;
    private final MDCFilter mdcFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth ->
               auth
                   .antMatchers("/api/*/admin/**").hasRole(ADMIN.name())
                   .antMatchers("/api/*/moderator/**").hasAnyRole(ADMIN.name(), MODERATOR.name())
                   .antMatchers("/api/*/user/**").hasRole(USER.name())
                   .anyRequest().authenticated())
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(mdcFilter, JwtTokenFilter.class);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer ignoreResources() {
        return (webSecurity -> webSecurity.ignoring().antMatchers(UrlConstant.API_V1_AUTH+"/**"));
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}