package com.example._prj_doan.testRepo;

import com.example._prj_doan.AmazonS3Util;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AmazonUploadTests {

    @Test
    public void testUpload() throws FileNotFoundException {
       String folderName = "test-upload";
       String fileName = "abc.png";
       String filePath = "D:\\test\\"+ fileName;
        InputStream inputStream = new FileInputStream(filePath);

        AmazonS3Util.uploadFile(folderName, fileName, inputStream);
    }

    @Test
    public void delete(){
        String fileName = "test-upload/abc.png";
        AmazonS3Util.deleteFile(fileName);
    }
}
