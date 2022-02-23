package com.example._prj_doan.manager.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AbstractExport {

    public void setHeader( HttpServletResponse httpServletResponse, String contentType,
                           String extension, String header) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String timestamp = dateFormat.format(new Date());
        String fileName = header + timestamp+extension;

        httpServletResponse.setContentType(contentType);
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename="+fileName;
        httpServletResponse.setHeader(headerKey,headerValue);
    }
}
