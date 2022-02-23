package com.example._prj_doan.manager.utils;

import com.example._prj_doan.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserCsvExporter extends AbstractExport{

    public void export(List<User> userList, HttpServletResponse httpServletResponse) throws IOException {

        super.setHeader(httpServletResponse ,"text/csv" , ".csv","user");

        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(httpServletResponse.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"User ID", "Email", "Tên", "Họ tên đệm", "Vai trò","Trạng thái"};
        String[] fieldMapping={"id","email", "firstName","lastName","roles","enabled"};
        csvBeanWriter.writeHeader(csvHeader);
        userList.forEach((user) -> {
            try {
                csvBeanWriter.write(user, fieldMapping);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        csvBeanWriter.close();

    }
}
