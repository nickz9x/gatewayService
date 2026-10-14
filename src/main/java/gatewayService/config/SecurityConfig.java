package gatewayService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        // Libera o identityService (rota Path=/api/auth/** no application.yaml).
                        // O matcher precisa do mesmo prefixo da rota, senão o login fica
                        // atrás de autenticação e dá redirect para o Keycloak em loop.
                        .pathMatchers("/api/auth/**").permitAll()
                        // Exige autenticação para qualquer outra rota (usuarios, produtos, etc.)
                        .anyExchange().authenticated()
                )
                // Gateway como OAuth2 Client: o login popula a sessão e o filtro
                // TokenRelay das rotas repassa o access token para os serviços downstream.
                // O refresh do token é feito pelo RefreshTokenReactiveOAuth2AuthorizedClientProvider,
                // que já vem na cadeia padrão do ReactiveOAuth2AuthorizedClientManager.
                .oauth2Login(Customizer.withDefaults())
                // Aceita também Bearer token direto (Postman, apps), validando o JWT.
                // Não conflita com o TokenRelay: ele só age quando o principal é
                // OAuth2AuthenticationToken, então no caminho JWT o header Authorization
                // original segue intacto para o downstream.
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}