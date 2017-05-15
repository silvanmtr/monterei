package com.report;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import com.model.Usuario;

/**
 * Created by aboullaite on 2017-02-23.
 */
public class UsuarioExcelReport extends AbstractXlsView{

    
    public void buildExcelDocument(Map<String, Object> model,
                                      Workbook workbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        // change the file name
       // response.setHeader("Content-Disposition", "attachment; filename=\"arquivo.xls\"");
        
        // Create object of FileOutputStream
        FileOutputStream fout = new FileOutputStream("C://Users//Silvan//Desktop//Excel//arquivo.xls");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        @SuppressWarnings("unchecked")
        List<Usuario> users = (List<Usuario>) model.get("users");
        
        exportExcel(workbook, fout, outputStream, users);
        
        //exportarExcelEmAbas(workbook, fout, outputStream, users);
    }

	/**
	 * @param workbook
	 * @param fout
	 * @param outputStream
	 * @param users
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	private void exportExcel(Workbook workbook, FileOutputStream fout, ByteArrayOutputStream outputStream,
			List<Usuario> users) throws IOException {
		// create excel xls sheet
        Sheet sheet = workbook.createSheet("Usuário");
        sheet.setDefaultColumnWidth(30);

        // create style for header cells
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        style.setFillForegroundColor(HSSFColor.BLUE.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        font.setBold(true);
        font.setColor(HSSFColor.WHITE.index);
        style.setFont(font);
        
        // create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Nome");
        header.getCell(0).setCellStyle(style);
        header.createCell(1).setCellValue("Email");
        header.getCell(1).setCellStyle(style);
       
        
        int rowCount = 1;

        for(Usuario user : users){
            Row userRow =  sheet.createRow(rowCount++);
            userRow.createCell(0).setCellValue(user.getNome());
            userRow.createCell(1).setCellValue(user.getEmail());
            }
        workbook.write(outputStream);
        
        outputStream.writeTo(fout);
        outputStream.close();
        fout.close();
	}
	
	/**
	 * @param workbook
	 * @param dados
	 * @param fout
	 * @param outputStream
	 * @param users
	 * @throws IOException
	 */
	private void exportarExcelEmAbas(Workbook workbook, FileOutputStream fout,
			ByteArrayOutputStream outputStream, List<Usuario> users) throws IOException {
		
		Map<String, List<Usuario>> dados = new HashMap<String, List<Usuario>>();
		List<Usuario> lista = new ArrayList<Usuario>();
        
        for(Usuario usuario : users){
    		lista.add(usuario);

        	if(!dados.containsKey(usuario.getNome())){

        		dados.put(usuario.getNome(), lista);
        	} else {
        		dados.get(usuario.getNome()).addAll(lista);

        	}
        	
        	lista = new ArrayList<Usuario>();
        }
        
        for (Map.Entry<String, List<Usuario>> mapUsuarios : dados.entrySet()) {
        	
        	// create excel xls sheet
            Sheet sheet = workbook.createSheet(mapUsuarios.getKey());
            sheet.setDefaultColumnWidth(30);

            // create style for header cells
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setFontName("Arial");
            style.setFillForegroundColor(HSSFColor.BLUE.index);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            font.setBold(true);
            font.setColor(HSSFColor.WHITE.index);
            style.setFont(font);
            
            // create header row
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Nome");
            header.getCell(0).setCellStyle(style);
            header.createCell(1).setCellValue("Email");
            header.getCell(1).setCellStyle(style);
           
            
            int rowCount = 1;

            for(Usuario user : mapUsuarios.getValue()){
                Row userRow =  sheet.createRow(rowCount++);
                userRow.createCell(0).setCellValue(user.getNome());
                userRow.createCell(1).setCellValue(user.getEmail());
            	}
            }
            workbook.write(outputStream);
            
            outputStream.writeTo(fout);
            outputStream.close();
            fout.close();
	}

}
