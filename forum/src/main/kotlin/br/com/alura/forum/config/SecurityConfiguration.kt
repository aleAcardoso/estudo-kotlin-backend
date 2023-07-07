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
        http.
        csrf()?.disable()?.
        authorizeRequests()?.
        requestMatchers("/topicos")?.hasAuthority("LEITURA_ESCRITA")?.
        requestMatchers(HttpMethod.POST, "/login")?.permitAll()?.
        anyRequest()?.
        authenticated()?.
        and()?.
        addFilterBefore(JWTLoginFilter(authManager = configuration.authenticationManager, jwtUtil = jwtUtil), UsernamePasswordAuthenticationFilter().javaClass)?.
        addFilterBefore(JWTAuthenticationFiler(jwtUtil = jwtUtil), UsernamePasswordAuthenticationFilter().javaClass)?.
        sessionManagement()?.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        return http.build()
    }

    @Bean
    fun encoder(): PasswordEncoder? {
        return BCryptPasswordEncoder()
    }
}