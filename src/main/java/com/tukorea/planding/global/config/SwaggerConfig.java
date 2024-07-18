package com.tukorea.planding.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
@Slf4j
@Configuration
public class SwaggerConfig implements WebMvcConfigurer {
    @Bean
    public OpenAPI openAPI(){
        Server server=new Server();
        server.setUrl("http://3.39.198.176:8080");
        server.setDescription("스웨거 설정 Local서버");

        Info info=new Info()
                .title("Planding API")
                .version("1.0.0")
                .description("Planding API 명세서");

        String jwt="JWT";
        SecurityRequirement securityRequirement=new SecurityRequirement().addList(jwt);
        Components components=new Components().addSecuritySchemes(jwt,new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT"));

        return new OpenAPI()
                .components(components)
                .addSecurityItem(securityRequirement)
                .servers(List.of(server))
                .info(info);

    }

}
