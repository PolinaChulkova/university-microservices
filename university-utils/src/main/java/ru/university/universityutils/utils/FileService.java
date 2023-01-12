package ru.university.universityutils.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.university.universityutils.exceptions.custom_exception.FileStorageException;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Objects;

@Transactional
@Service
@Slf4j
public class FileService {
    @Value("${spring.servlet.multipart.location}")
    private String location;

    @PostConstruct
    public void init() throws IOException {
        try {
            Files.createDirectories(Path.of(location));
        } catch (IOException e) {
            log.error("Не удалось создать директорию для файлов. Error: " + Arrays.toString(e.getStackTrace()));
            throw new FileStorageException("Не удалось создать директорию для файлов. Error: " + e.getCause());
        }
    }

    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileStorageException("Не удалось сохранить пустой файл " + file.getName());
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, Path.of(location).resolve(filename), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            log.error(String.format("Не удалось сохранить файл {}. Error: {}", filename, Arrays.toString(e.getStackTrace())));

            throw new FileStorageException("Не удалось сохранить файл " + filename, e.getCause());
        }

        return filename;
    }
}
