package com.report.resolver;

import java.util.Locale;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import com.report.UsuarioExcelReport;


/**
 * Created by aboullaite on 2017-02-24.
 */
public class ExcelViewResolver implements ViewResolver {
    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        UsuarioExcelReport view = new UsuarioExcelReport();
        return view;
    }
}
