package com.x6.arcade.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <h1>Swagger 配置类</h1>
 * 原生: /swagger-ui.html
 * 美化: /doc.html
 * */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * <h2>Swagger 实例 Bean 是 Docket, 所以通过配置 Docket 实例来配置 Swagger</h2>
     * */
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                // 展示在 Swagger 页面上的自定义工程描述信息
                .apiInfo(apiInfo())
                // 选择展示哪些接口
                .select()
                // 只有 com.x6.arcade 包内的才去展示
                .apis(RequestHandlerSelectors.basePackage("com.x6.arcade"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * <h2>Swagger 的描述信息</h2>
     * */
    public ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("arcade-x6-api")
                .description("arcade-x6-api")
                .contact(new Contact("amy.xue", "www.arcade.com", "amy.xue@163.com"))
                .version("1.0")
                .build();

    }



}
