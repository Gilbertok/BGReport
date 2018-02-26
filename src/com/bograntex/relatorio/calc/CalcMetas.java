package com.bograntex.relatorio.calc;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.bograntex.utils.NumberUtils;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;


public class CalcMetas {
	
	private File arquivo;
	
	public CalcMetas() {
		this.arquivo = new File("resources/metas.xls");
	}
	
	public File getArquivo() {
		return this.arquivo;
	}

	public static Map<String, Object> calcular(Map<String, Object> paramsRelatorio) throws BiffException, IOException {
		CalcMetas metas = new CalcMetas();
		Workbook planilha = Workbook.getWorkbook(metas.getArquivo());
		Sheet sheetMetas = planilha.getSheet(0);
		int linhasMetas = sheetMetas.getRows();
		
		for(int i = 0; i < linhasMetas; i++) {
			String valorCampo = sheetMetas.getCell(1,i).getContents().toString();
			if(!valorCampo.equals("") && paramsRelatorio.get("diasRelatorio") != null) {
				String campo = sheetMetas.getCell(0,i).getContents().toString();
				Float valor = Float.parseFloat(valorCampo.replaceAll(",", "."));
				
				Integer dias = Integer.parseInt(paramsRelatorio.get("diasRelatorio").toString());
				String campoTotal = campo.concat("Acumulado");
				Float valorTotal = valor*dias;
				
				if(campo.contains("Min")) {
					paramsRelatorio.put(campo, NumberUtils.floatToStringDecimal(valor));
					paramsRelatorio.put(campoTotal, NumberUtils.floatToStringDecimal(valorTotal));
				} else {
					paramsRelatorio.put(campo, NumberUtils.floatToStringInteger(valor));
					paramsRelatorio.put(campoTotal, NumberUtils.floatToStringInteger(valorTotal));
				}
				
			}
        }
		
		Sheet sheetHoras = planilha.getSheet(1);
		int linhasHoras = sheetHoras.getRows();
		
		for(int i = 0; i < linhasHoras; i++) {
			String valorCampo = sheetHoras.getCell(1,i).getContents().toString();
			if(!valorCampo.equals("") && paramsRelatorio.get("diasRelatorio") != null) {
				String campo = sheetHoras.getCell(0,i).getContents().toString();
				Float valor = Float.parseFloat(valorCampo.replaceAll(",", "."));
				
				paramsRelatorio.put(campo, NumberUtils.floatToStringDecimal(valor));
			}
        }
		
		planilha.close();
		return paramsRelatorio;
	}
	

}
