package com.example._prj_doan.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author TheNN
 */
public class FileUploadUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtil.class);

    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {

        Path path = Paths.get(uploadDir); //lay duong dan
        String nameFile = fileName.replace(" ", "");
        if(!Files.exists(path)){
            Files.createDirectories(path); //tao thu muc duong dan
        }

        try(InputStream inputStream = multipartFile.getInputStream()){
            Path filePath = path.resolve(nameFile);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
        catch(IOException exception) {
            throw new IOException("Không thể lưu file: "+ nameFile, exception);
        }
    }

    public static void cleanDir(String dir){
        Path dirPath = Paths.get(dir);

        try{
            Files.list(dirPath).forEach(file ->{
                if(!Files.isDirectory(file)){
                    try{
                        Files.delete(file);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }

                }
            });
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
