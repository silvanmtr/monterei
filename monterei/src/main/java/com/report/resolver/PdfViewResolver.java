package com.report.resolver;

import java.util.Locale;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import com.report.UsuarioPdfReport;

/**
 * Created by aboullaite on 2017-02-25.
 */
public class PdfViewResolver implements ViewResolver {
    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        UsuarioPdfReport view = new UsuarioPdfReport();
        return view;
    }
}
