package com.bograntex.relatorio.calc.estoque;

import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;

import com.bograntex.utils.NumberUtils;

public class CalculaEstoqueTotal {
	
	public static Float calcular(Map<String, Object> paramsRelatorio) throws SQLException {
		Float totalEstoque = new Float(0);
		for (Entry<String, Object> pair : paramsRelatorio.entrySet()) {
			if (pair.getKey().contains("estoque")) {
				Float total = new Float(0);
//				String valorString = pair.getValue().toString().replace(".", "").replace(",", ".");
//				total = Float.parseFloat(valorString);
				
				total = NumberUtils.getFloatToString(pair.getValue());
				totalEstoque = totalEstoque + total;
			}
		}
		return totalEstoque;
	}
	
	public static String calcularToString(Map<String, Object> paramsRelatorio) throws SQLException {
		return NumberUtils.floatToStringDecimal(CalculaEstoqueTotal.calcular(paramsRelatorio));
	}

}
