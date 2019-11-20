package com.tagsoft.registry.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES =
            new HashSet<>(Arrays.asList("application/json;charset=UTF-8"));
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .produces(DEFAULT_PRODUCES_AND_CONSUMES)
                .consumes(DEFAULT_PRODUCES_AND_CONSUMES)
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET,
                        newArrayList(new ResponseMessageBuilder()
                                        .code(500)
                                        .message("500 message")
                                        .responseModel(new ModelRef("Error"))
                                        .build(),
                                new ResponseMessageBuilder()
                                        .code(403)
                                        .message("Forbidden!")
                                        .build()));
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Registry",
                "Test task for TAGSoft Junior Java hiring",
                "0.0.1-SNAPSHOT",
                "free",
                new Contact("Viktor Kuchuhurnyi", "https://github.com/jezhische/registry", "viktorkuchuhurnyi@gmail.com"),
                "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0", Collections.emptyList());
    }
}
