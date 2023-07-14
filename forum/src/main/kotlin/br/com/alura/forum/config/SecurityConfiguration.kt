package br.com.alura.forum.config

import br.com.alura.forum.security.JWTAuthenticationFiler
import br.com.alura.forum.security.JWTLoginFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val configuration: AuthenticationConfiguration,
    private val jwtUtil: JWTUtil
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .headers { it.frameOptions {frame ->  frame.disable() } }
            .csrf { it.disable() }
            .authorizeHttpRequests {authorize ->
                authorize.requestMatchers("/topicos")?.hasAuthority("LEITURA_ESCRITA")?.
                requestMatchers(HttpMethod.POST, "/login")?.permitAll()?.
                requestMatchers(HttpMethod.GET, "/swagger-ui/*")?.permitAll()?.
                requestMatchers(HttpMethod.GET, "/v3/api-docs/**")?.permitAll()?.
                anyRequest()?.authenticated()
            }
            .addFilterBefore(JWTLoginFilter(authManager = configuration.authenticationManager, jwtUtil = jwtUtil), UsernamePasswordAuthenticationFilter().javaClass)
            .addFilterBefore(JWTAuthenticationFiler(jwtUtil = jwtUtil), UsernamePasswordAuthenticationFilter().javaClass)
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .build()
    }

    @Bean
    fun encoder(): PasswordEncoder? {
        return BCryptPasswordEncoder()
    }
}