package com.example._prj_doan.manager.utils;

import com.example._prj_doan.entity.Brand;
import com.example._prj_doan.entity.Category;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class BrandCsvExporter extends AbstractExport{

    public void export(List<Brand> brandListList, HttpServletResponse httpServletResponse) throws IOException {

        super.setHeader(httpServletResponse ,"text/csv" , ".csv","brand");

        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(httpServletResponse.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"Brand ID", "Tên Nhà Sản Xuất", "Danh Mục"};
        String[] fieldMapping={"id","name", "listCategories"};
        csvBeanWriter.writeHeader(csvHeader);
        brandListList.forEach((category) -> {
           List<Category> categories = category.getCategories();
           StringBuilder categoriesString = new StringBuilder();
           for (Category category1 : categories) {
           categoriesString.append(category1.getName()).append(",");
           }
           category.setListCategories(categoriesString.toString().substring(0, categoriesString.toString().length() - 1));
            try {
                csvBeanWriter.write(category, fieldMapping);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        csvBeanWriter.close();

    }
}