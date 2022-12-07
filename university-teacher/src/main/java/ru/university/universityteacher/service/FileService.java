package ru.university.universityteacher.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
@Slf4j
public class FileService {

    @Value("${spring.servlet.multipart.location}")
    private String location;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Path.of(location));
        } catch (IOException e) {
            log.error("Не удалось сохранить файл. Error: " + e.getLocalizedMessage());
        }
    }

    public String uploadFile(MultipartFile file) {
        if(file.isEmpty()) {
            throw new RuntimeException("Не удалось сохранить пустой файл");
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try(InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, Path.of(location).resolve(filename), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            log.error("Не удалось сохранить файл " + filename + ". Error: " + e.getLocalizedMessage());
        }

        return filename;
    }
}
