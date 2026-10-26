package gatewayService.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI gatewayOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ChainTracker - API Gateway")
                        .description("""
                                Ponto de entrada HTTP do ChainTracker.

                                Esta página agrega as especificações dos serviços que expõem API HTTP.
                                Use o seletor no topo da página para alternar entre elas:
                                assetMarketDataService, identity-service e portfolioService.

                                O notificationService é orientado a eventos e não expõe controllers HTTP
                                de negócio. As rotas /api/auth/register e /api/auth/login são públicas;
                                as demais exigem um JWT do Keycloak. As rotas /api/bot/** exigem um token
                                de serviço com o escopo bot.
                                """)
                        .version("1.0.0"));
    }
}
