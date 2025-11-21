package br.com.sea3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer; // Importação necessária para o .cors()
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration; // Importação para CORS
import org.springframework.web.cors.CorsConfigurationSource; // Importação para CORS
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // Importação para CORS

import java.util.Arrays; // Importação para usar Arrays.asList

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // --- CONFIGURAÇÃO DE USUÁRIOS EM MEMÓRIA ---
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails admin = User.withUsername("admin")
                .password("{noop}123qwe!@#")
                .roles("ADMIN")
                .build();

        UserDetails user = User.withUsername("user")
                .password("{noop}123qwe123")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    // --- CONFIGURAÇÃO DA CADEIA DE FILTROS DE SEGURANÇA E CORS ---
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults()) // <--- ATIVA O FILTRO DE CORS
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .antMatchers(HttpMethod.POST, "/api/clientes").hasRole("ADMIN")
                        .antMatchers(HttpMethod.GET, "/api/clientes/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                )
                .httpBasic();

        return http.build();
    }

    // --- CONFIGURAÇÃO GLOBAL DE CORS (Para permitir requisições do React 5173) ---
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite a origem do seu frontend React (Vite)
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));

        // Permite os métodos usados para as operações CRUD
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));

        // Permite o uso de credenciais (necessário para o Basic Auth)
        configuration.setAllowCredentials(true);

        // Permite todos os cabeçalhos
        configuration.setAllowedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica esta configuração a todas as rotas
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
