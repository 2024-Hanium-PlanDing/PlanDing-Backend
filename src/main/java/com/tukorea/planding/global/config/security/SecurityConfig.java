package com.tukorea.planding.global.config.security;

import com.tukorea.planding.global.UserLogoutHandler;
import com.tukorea.planding.global.config.security.jwt.JwtAuthenticationEntryPoint;
import com.tukorea.planding.global.config.security.jwt.JwtAuthenticationFilter;
import com.tukorea.planding.global.oauth.handler.Oauth2SuccessHandler;
import com.tukorea.planding.global.oauth.service.CustomOAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomOAuth2Service customOAuth2Service;
    private final Oauth2SuccessHandler oauth2SuccessHandler;
    private final UserLogoutHandler userLogoutHandler;
    private final String[] SWAGGER = {
            "/swagger-resources/**", "/v3/api-docs/", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/swagger-ui/index.html",
    };
    @Value("${front-url}")
    private String frontUrl;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(configurationSource())) // cors 활성화
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .requestMatchers(SWAGGER).permitAll()
                        .requestMatchers("/login", "/api/v1/**", "/api/v1/ws", "/logout", "/actuator/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth2Login -> oauth2Login
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2Service))
                        .successHandler(oauth2SuccessHandler))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logOut -> logOut
                        .logoutUrl("/logout")
                        .addLogoutHandler(userLogoutHandler)
                        .permitAll());


        return http.build();
    }

    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin(frontUrl);
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.addExposedHeader("refresh-token");
        config.addExposedHeader("access-token");
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

}
