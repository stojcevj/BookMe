package com.example.bookme.web;

import com.example.bookme.config.FileSaveConstants;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/images")
public class ImageController {
    @GetMapping("/{id}/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable Long id, @PathVariable String imageName) throws IOException {
        Path imagePath = Paths.get(FileSaveConstants.uploadDir + "/" + id +"/", imageName);
        Resource resource = new UrlResource(imagePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            String contentType = "application/octet-stream";
            if (imageName.endsWith(".jpg") || imageName.endsWith(".jpeg")) {
                contentType = "image/jpeg";
            } else if (imageName.endsWith(".png")) {
                contentType = "image/png";
            } else if (imageName.endsWith(".gif")) {
                contentType = "image/gif";
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentLength(resource.contentLength());
            headers.setContentType(MediaType.parseMediaType(contentType));

            return ResponseEntity.ok().headers(headers).body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
