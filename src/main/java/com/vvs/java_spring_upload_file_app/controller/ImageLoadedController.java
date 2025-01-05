package com.vvs.java_spring_upload_file_app.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

// @RestController
@RequestMapping("/api/uploads")
public class ImageLoadedController {

  @Value("${file.upload-dir}")
  private String uploadDir;

  @PostMapping("/images")
  public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
    try {
      // Save the file to the directory
      String contentType = file.getContentType();
      if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
        throw new IllegalArgumentException("Only JPEG or PNG images are allowed");
      }
      String filePath = saveImage(file);
      return ResponseEntity.ok("Image uploaded successfully: " + filePath);
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image");
    }
  }

  @GetMapping("/images/{filename}")
  public ResponseEntity<Resource> getImage(@PathVariable String filename) {
    try {
      Path filePath = Paths.get(uploadDir).resolve(filename);
      Resource resource = new UrlResource(filePath.toUri());
      
      if (resource.exists()) {
        return ResponseEntity.ok()
          .contentType(MediaType.IMAGE_JPEG)
          .body(resource);
      } else {
        return ResponseEntity.notFound().build();
      }
    } catch (MalformedURLException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  private String saveImage(MultipartFile file) throws IOException {

    Path uploadPath = Paths.get(uploadDir);
    if (!Files.exists(uploadPath)) {
      Files.createDirectories(uploadPath);
    }

    String fileName = file.getOriginalFilename();
    Path filePath = uploadPath.resolve(fileName);
    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

    return filePath.toString();
  }
}
