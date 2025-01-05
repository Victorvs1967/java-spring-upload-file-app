package com.vvs.java_spring_upload_file_app.handler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
public class FileUploadHandler {

  @Value("${file.upload-dir}")
  private String uploadDir;

  public Mono<ServerResponse> handleFileUpload(ServerRequest request) {
    
    // Create directorry if not exist
    Path uploadPath = Paths.get(uploadDir);
    if (!Files.exists(uploadPath)) {
      try {
        Files.createDirectories(uploadPath);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return request.body(BodyExtractors.toMultipartData())
      .flatMap(parts -> {
        FilePart filePart = (FilePart) parts.get("file").get(0);
        return filePart.transferTo(new File(uploadDir + filePart.filename()))
          .then(ServerResponse.ok()
          .bodyValue("File upload successfully: " + filePart.filename()));
      });
  }
}
