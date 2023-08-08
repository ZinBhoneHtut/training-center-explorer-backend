package com.zbh.tce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 
 * @author ZinBhoneHtut
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final String[] PUBLIC_ACCESS = {"/", "/css/*", "/js/*", "/api/user/**", "/api/v1/admin/**", "/h2-console"};

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		    .csrf().ignoringAntMatchers("/h2-console/**").disable();

		http.authorizeRequests()
			.antMatchers(PUBLIC_ACCESS).permitAll()
			.and()
			.httpBasic();
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
