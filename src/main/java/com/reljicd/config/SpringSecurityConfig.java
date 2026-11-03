package com.reljicd.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.sql.DataSource;

/**
 * Spring Security Configuration
 * http://docs.spring.io/spring-boot/docs/current/reference/html/howto-security.html
 * Switches off Spring Boot automatic security configuration
 *
 * @author Dusan
 */
@Configuration
public class SpringSecurityConfig {

    private final AccessDeniedHandler accessDeniedHandler;

    final DataSource dataSource;

    @Value("${spring.admin.username}")
    private String adminUsername;

    @Value("${spring.admin.password}")
    private String adminPassword;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    @Autowired
    public SpringSecurityConfig(AccessDeniedHandler accessDeniedHandler, DataSource dataSource) {
        this.accessDeniedHandler = accessDeniedHandler;
        this.dataSource = dataSource;
    }

    /**
     * HTTPSecurity configurer
     * - roles ADMIN allow to access /admin/**
     * - roles USER allow to access /user/** and /newPost/**
     * - anybody can visit /, /home, /about, /registration, /error, /blog/**, /post/**, /h2-console/**
     * - every other page needs authentication
     * - custom 403 access denied handler
     */
    @Bean
    public org.springframework.security.web.SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/home", "/registration", "/error", "/h2-console/**", "/css/**", "/webjars/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .failureUrl("/login?error")
                        .defaultSuccessUrl("/home")
                        .permitAll())
                .logout(logout -> logout.permitAll())
                .exceptionHandling(exceptions -> exceptions.accessDeniedHandler(accessDeniedHandler))
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
        return http.build();
    }


    /**
     * Authentication details
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        JdbcUserDetailsManager jdbcUsers = new JdbcUserDetailsManager(dataSource);
        jdbcUsers.setUsersByUsernameQuery(usersQuery);
        jdbcUsers.setAuthoritiesByUsernameQuery(rolesQuery);
        return username -> {
            if (adminUsername.equals(username)) {
                return User.withUsername(adminUsername)
                        .password(passwordEncoder.encode(adminPassword))
                        .roles("ADMIN")
                        .build();
            }
            return jdbcUsers.loadUserByUsername(username);
        };
    }

    /**
     * Configure and return BCrypt password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
