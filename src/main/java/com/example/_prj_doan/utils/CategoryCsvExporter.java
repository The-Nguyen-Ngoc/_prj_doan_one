package com.example._prj_doan.utils;

import com.example._prj_doan.entity.Category;
import com.example._prj_doan.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CategoryCsvExporter extends AbstractExport{

    public void export(List<Category> categoryListList, HttpServletResponse httpServletResponse) throws IOException {

        super.setHeader(httpServletResponse ,"text/csv" , ".csv","category");

        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(httpServletResponse.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"Category ID", "Alias","Tên Danh Mục", "Trạng thái"};
        String[] fieldMapping={"id","alias", "name","enabled"};
        csvBeanWriter.writeHeader(csvHeader);
        categoryListList.forEach((category) -> {
            try {
                csvBeanWriter.write(category, fieldMapping);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        csvBeanWriter.close();

    }
}
