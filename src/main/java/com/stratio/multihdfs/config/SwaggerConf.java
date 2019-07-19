/*
 * © 2019 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big
 * Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed
 * or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered,
 * disassembled or decompiled, without express written authorization from Stratio Big Data Inc.,
 * Sucursal en España.
 */
package com.stratio.multihdfs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.ant;

/**
 * Configuration for the specification 2.0 of Swagger.
 */
@Configuration
@EnableSwagger2
public class SwaggerConf {
  @Value( "${swagger.basePath:/service/multihdfs}" )
  private String swaggerBasePath;
  /**
   * Bean of a Docket to configure a subset of the services to be documented and groups them by
   * name.
   * 
   * @return a Docket object
   */
  @Bean
  @Profile({"!PRODUCTION"})
  public Docket oauthApi() {
    return new Docket(DocumentationType.SWAGGER_2).groupName("multihdfs").apiInfo(apiInfo())
        .useDefaultResponseMessages(false).select().paths(or(ant("/config/**"))).build();
  }

  @Bean
  @Profile({"PRODUCTION"})
  public Docket oauthApiProd() {
    return new Docket(DocumentationType.SWAGGER_2)
            .groupName("multihdfs").apiInfo(apiInfo())
            .useDefaultResponseMessages(false).select().paths(or(ant("/config/**"))).build()
            .pathMapping(swaggerBasePath);
  }

  private static ApiInfo apiInfo() {
    return new ApiInfoBuilder().title("API REST for Gosec Identity DAAS").description("API REST for MultiHDFS")
        .contact(new Contact("Stratio", "", ""))
        .license("© 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.")
        .version("1.0.0").build();
  }
}
