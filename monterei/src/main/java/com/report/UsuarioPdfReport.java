package com.report;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.model.Usuario;
import com.report.view.AbstractPdfView;

/**
 * Created by aboullaite on 2017-02-25.
 */
public class UsuarioPdfReport extends AbstractPdfView {
    @Override
    public void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // change the file name
        //response.setHeader("Content-Disposition", "attachment; filename=\"arquivo.pdf\"");

        List<Usuario> users = (List<Usuario>) model.get("users");

        
        document.setPageSize(PageSize.A4.rotate());
        document.open();

 
        document.add(new Paragraph("Usuarios " + LocalDate.now()));

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100.0f);
        table.setSpacingBefore(10);

        // define font for table header row
        Font font = FontFactory.getFont(FontFactory.TIMES);
        font.setColor(BaseColor.WHITE);

        // define table header cell
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.DARK_GRAY);
        cell.setPadding(5);

        // write table header
        cell.setPhrase(new Phrase("Nome", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Email", font));
        table.addCell(cell);

        

        for(Usuario user : users){
            table.addCell(user.getNome());
            table.addCell(user.getEmail());
        }
        document.add(table);


       document.close();

    }
    
}
