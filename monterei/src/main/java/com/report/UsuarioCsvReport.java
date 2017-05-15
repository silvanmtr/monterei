package com.report;


import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.model.Usuario;
import com.report.view.AbstractCsvView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by aboullaite on 2017-02-24.
 */
public class UsuarioCsvReport extends AbstractCsvView {


    @Override
    public void buildCsvDocument(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        response.setHeader("Content-Disposition", "attachment; filename=\"arquivo.csv\"");

        List<Usuario> users = (List<Usuario>) model.get("users");
        String[] header = {"Nome","Email"};
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);

        csvWriter.writeHeader(header);

        if(users != null){
        	for(Usuario user : users){
                csvWriter.write(user, header);
            }
        }
        
        csvWriter.close();


    }
}
