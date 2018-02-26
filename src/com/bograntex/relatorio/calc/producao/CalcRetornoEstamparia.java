package com.bograntex.relatorio.calc.producao;

import java.sql.SQLException;
import java.util.Map;

import com.bograntex.utils.NumberUtils;

public class CalcRetornoEstamparia extends CalculoProducao {
	
	public CalcRetornoEstamparia() throws SQLException {
		super();
	}
	
	public static Map<String, Object> calcular(Map<String, Object> paramsRelatorio) throws SQLException {
		CalcRetornoEstamparia retorno = new CalcRetornoEstamparia();
		Float estamparia = retorno.calculaProducaoEstagio(1,2);
		Float estampariaAcumulado = retorno.calculaProducaoEstagio(2,2);
		Float bordado = retorno.calculaProducaoEstagio(1,16);
		Float bordadoAcumulado = retorno.calculaProducaoEstagio(2,16);
		retorno.closeConnection();
		
		paramsRelatorio.put("realRetornoEstamparia", NumberUtils.floatToStringInteger(estamparia));
		paramsRelatorio.put("realRetornoEstampariaAcumulado", NumberUtils.floatToStringInteger(estampariaAcumulado));
		
		paramsRelatorio.put("realRetornoBordado", NumberUtils.floatToStringInteger(bordado));
		paramsRelatorio.put("realRetornoBordadoAcumulado", NumberUtils.floatToStringInteger(bordadoAcumulado));
		
		if(paramsRelatorio.get("diasRelatorio") != null) {
			Integer dias = Integer.parseInt(paramsRelatorio.get("diasRelatorio").toString());
			paramsRelatorio.put("mediaRetornoEstamparia", NumberUtils.floatToStringInteger(estampariaAcumulado/dias));
			paramsRelatorio.put("mediaRetornoBordado", NumberUtils.floatToStringInteger(bordadoAcumulado/dias));
		}
		return paramsRelatorio;
	}

}
