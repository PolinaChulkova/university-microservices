package ru.university.universityutils.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.university.universityutils.exceptions.custom_exception.FileNotFoundException;
import ru.university.universityutils.exceptions.custom_exception.FileStorageException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Transactional
@Service
@Slf4j
public class FileService {

    public void initDirectory(Path directory) {
        try {
            if (!Files.exists(directory))
                Files.createDirectories(directory);
        } catch (IOException e) {
            log.error("Не удалось создать директорию \"{}\". Error: [{}]", directory, e);

            throw new FileStorageException(String.format("Не удалось создать директорию \"%s\". Error: [%s]", directory, e));
        }
    }

    public String uploadFile(MultipartFile file, Path directory) {
        if (file.isEmpty()) {
            throw new FileStorageException("Не удалось сохранить пустой файл " + file.getName());
        }

        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        initDirectory(directory);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, directory.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            log.error("Не удалось сохранить файл {}. Error: [{}]", filename, e.toString());

            throw new FileStorageException("Не удалось сохранить файл " + filename, e.getCause());
        }

        return filename;
    }

    public Resource loadAsResource(String filename, Path directory) {
        try {
            Resource resource = new UrlResource(directory.resolve(filename).toUri());

            if (resource.exists() || resource.isReadable())
                return resource;
            else throw new FileNotFoundException(String.format("Не удалось прочитать файл \"%s\".", filename));

        } catch (MalformedURLException e) {
            throw new FileNotFoundException(String.format("Не удалось прочитать файл \"%s\".", filename));
        }
    }

    public String deleteFile(String filename, Path directory) {
        try {
            Files.deleteIfExists(directory.resolve(filename));
        } catch (IOException e) {
            log.error("Не удалось удалить файл \"{}\".", filename);

            throw new FileStorageException(String.format("Не удалось удалить файл \"%s\".", filename));
        }

        return filename;
    }

    public void deleteDirectory(Path directory) {
        try {
            Files.deleteIfExists(directory);
        } catch (IOException e) {
            log.error("Не удалось удалить папку \"{}\".", directory);

            throw new FileStorageException(String.format("Не удалось удалить папку \"%s\".", directory));
        }
    }
}
