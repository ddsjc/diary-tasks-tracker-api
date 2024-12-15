package danila.sukhov.diary_tasks_tracker_api.api.configs;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    JWTUtils jwtProvider;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();

    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                    corsConfig.addAllowedOriginPattern("*"); // Разрешить запросы с любого источника
                    corsConfig.addAllowedMethod("*");       // Разрешить все методы (POST, GET и т.д.)
                    corsConfig.addAllowedHeader("*");       // Разрешить все заголовки
                    return corsConfig;
                }))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/project/**", "/projects/**", "task-state/**", "task/add-executor/**", "task/create/**", "/task/add-comment/**").hasAuthority("ADMIN") // Только для админов
                        .requestMatchers("/task/change/**" , "/task/add-comment/**" ).hasAuthority("EXECUTOR")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}




