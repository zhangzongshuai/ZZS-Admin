package com.zzs.zzsadmin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 *
 * @author 张宗帅
 * @date 2022-06-25 18:04:33
 */
@Configuration
@EnableSwagger2
public class Swagger2Configuration {
    @Bean
    public Docket api() {

        ParameterBuilder aParameterBuilder = new ParameterBuilder();
        aParameterBuilder.name("Authorization").description("Authorization").modelRef(new ModelRef("string")).parameterType("header").build();

        List<Parameter> aParameters = new ArrayList<>();
        aParameters.add(aParameterBuilder.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(aParameters);
    }

    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //文档标题
                .title("zzsAdmin")
                //接口概述
                .description("接口文档")
                //版本号
                .version("1.0")
                .build();
    }
}
