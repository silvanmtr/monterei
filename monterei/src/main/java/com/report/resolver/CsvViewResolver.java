package com.report.resolver;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import com.report.UsuarioCsvReport;

import java.util.Locale;

/**
 * Created by aboullaite on 2017-02-24.
 */
public class CsvViewResolver implements ViewResolver {
    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        UsuarioCsvReport view = new UsuarioCsvReport();
        return view;
    }
}
