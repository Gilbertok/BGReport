package com.bograntex.testes;

import java.util.HashMap;
import java.util.Map;

import com.bograntex.relatorio.calc.producao.CalcProducaoMalharia;

public class SigiTeste {
	
	public static void main(String[] args){
		
		Map<String, Object> paramsRelatorio = new HashMap<String, Object>();
		paramsRelatorio.put("diasRelatorio", "14");
		try {
			System.out.println(CalcProducaoMalharia.calcular(paramsRelatorio));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
