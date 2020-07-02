package com.zoho.ifsc;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Cell;
import com.monitorjbl.xlsx.StreamingReader;


public class ExcelReader {


    public static void main(String[] args) throws Exception {
    	ArrayList<String> ifsc_check = new ArrayList<>();
    	FileWriter fw=new FileWriter("valid.xls");
    	FileWriter fw1=new FileWriter("invalid.xls");        
    	InputStream is = new FileInputStream(new File("68774.xlsx"));
    	Workbook reader = StreamingReader.builder()
    	        .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
    	        .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
    	        .open(is);            // InputStream or File for XLSX file (required)
    	int i=0;
    	for(Sheet sheet: reader)
    	{
	    	for (Row r : sheet) {
	    	ArrayList<String> temp = new ArrayList<>();
	    	  for (Cell c : r) {
	    		  temp.add(c.getStringCellValue());
	    	  }
	    	  String ifsc = temp.get(1);
	    	  String str = temp.get(0)+"\t"+temp.get(1)+"\t"+temp.get(3);
	          	if(ifsc.trim().length()==11 && !(ifsc_check.contains(ifsc.trim())))
	          	{
	          		fw.write(str+"\n");
	          		ifsc_check.add(ifsc.trim());
	          	}
	          	else if(ifsc.length()!=11)
	          	{
	          		fw1.write(str+"\n");
	          	}
	    	  System.out.println(++i);
	    	}
    	}   
    	is.close();
    	reader.close();
    	InputStream is2 = new FileInputStream(new File("RTGEB0815.xlsx"));
    	Workbook reader2 = StreamingReader.builder()
    	        .rowCacheSize(1000)    // number of rows to keep in memory (defaults to 10)
    	        .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
    	        .open(is2);            // InputStream or File for XLSX file (required)
    	int j=0;
    	for(Sheet sheet: reader2)
    	{
	    	for (Row r : sheet) {
	    	ArrayList<String> temp = new ArrayList<>();
	    	  for (Cell c : r) {
	    		  temp.add(c.getStringCellValue());
	    	  }
	    	  String ifsc = temp.get(1);
	    	  String str = temp.get(0)+"\t"+temp.get(1)+"\t"+temp.get(3);
	          	if(ifsc.trim().length()==11 && !(ifsc_check.contains(ifsc.trim())))
	          	{
	          		fw.write(str+"\n");
	          		ifsc_check.add(ifsc.trim());
	          	}
	          	else if(ifsc.length()!=11)
	          	{
	          		fw1.write(str+"\n");
	          	}
	    	  System.out.println(++j);
	    	}
    	}   
    	is2.close();
    	reader2.close();
    	fw.close(); 
    	fw1.close();
    }
}