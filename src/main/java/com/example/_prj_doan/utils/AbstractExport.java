package com.example._prj_doan.utils;

import com.example._prj_doan.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AbstractExport {

    public void setHeader( HttpServletResponse httpServletResponse, String contentType,
                           String extension) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String timestamp = dateFormat.format(new Date());
        String fileName = "users_" + timestamp+extension;

        httpServletResponse.setContentType(contentType);
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename="+fileName;
        httpServletResponse.setHeader(headerKey,headerValue);
    }
}
