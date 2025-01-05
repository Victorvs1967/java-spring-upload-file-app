package com.vvs.java_spring_upload_file_app.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.vvs.java_spring_upload_file_app.handler.FileUploadHandler;

@Configuration
public class FileUploadRouter {

  @Bean
  public RouterFunction<ServerResponse> routeFileUpload(FileUploadHandler handler) {
    return RouterFunctions
      .route()
      .nest(RequestPredicates.path("/api/upload"), builder -> builder
        .POST("/images", handler::handleFileUpload))
      .build();
  }

}
