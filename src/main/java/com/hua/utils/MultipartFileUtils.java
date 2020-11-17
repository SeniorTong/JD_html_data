package com.hua.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: SeniorTong丶
 * @Date: 2020/11/5 11:47
 * @Version: 1.0
 */
public class MultipartFileUtils {

    public static File transferToFile(MultipartFile multipartFile) {
        File file = null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();

            String[] filename = originalFilename.split("\\.");
            if(filename[0].length() < 3){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
                String format = sdf.format(new Date());
                filename[0] += filename[0] + format;
            }
            file=File.createTempFile(filename[0], filename[1]);
            multipartFile.transferTo(file);
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

}
