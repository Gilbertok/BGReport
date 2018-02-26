package com.bograntex.relatorio.calc.producao;

import java.sql.SQLException;
import java.util.Map;

import com.bograntex.dao.Setor;
import com.bograntex.utils.NumberUtils;

public class CalcProducaoRevisao extends CalculoProducao {
	
	public CalcProducaoRevisao() throws SQLException {
		super();
	}
	
	public static Map<String, Object> calcular(Map<String, Object> paramsRelatorio) throws SQLException {
		CalcProducaoRevisao revisao = new CalcProducaoRevisao();
		String[] estagios = new String[]{"5","62","69"};
		
		Integer nrMaoDeObraRevisao = Setor.getNrMaoDeOBraSetorRevisao(revisao.getConnection());
		Float pecasRevisadas = revisao.calculaProducaoEstagio(1, 5);
		Float pecasRevisadasAcumulado = revisao.calculaProducaoEstagio(2, 5);
		Float minCapacidade = revisao.calculaMinCapacidade(paramsRelatorio, nrMaoDeObraRevisao, 1);
		Float minCapacidadeAcumulado = revisao.calculaMinCapacidade(paramsRelatorio, nrMaoDeObraRevisao, 2);
		Float minRealizados = revisao.calculaMinProduzidosEstagios(1, estagios);
		Float minRealizadosAcumulado = revisao.calculaMinProduzidosEstagios(2, estagios);
		
		Double metaPecasRevisadas = NumberUtils.getDoubleToString(paramsRelatorio.get("metaPecasRevisadas"));
		Double metaPecasRevisadasAcumulado = NumberUtils.getDoubleToString(paramsRelatorio.get("metaPecasRevisadasAcumulado"));
		
		paramsRelatorio.put("realPecasRevisadas", NumberUtils.floatToStringInteger(pecasRevisadas));
		paramsRelatorio.put("realNrMaoObraDiretaRevisao", nrMaoDeObraRevisao);
//		paramsRelatorio.put("realHorasExtrasRevisao", 0);
		paramsRelatorio.put("realMinCapacidadeRevisao", NumberUtils.floatToStringDecimal(minCapacidade));
		paramsRelatorio.put("realMinRealizadosRevisao", NumberUtils.floatToStringDecimal(minRealizados));
		paramsRelatorio.put("realEficienciaRevisao", NumberUtils.calculaPercRealEficicencia(minRealizados, minCapacidade) +"%");
		paramsRelatorio.put("realPecasRevisadasAcumulado", NumberUtils.floatToStringInteger(pecasRevisadasAcumulado));
		paramsRelatorio.put("realMinCapacidadeRevisaoAcumulado", NumberUtils.floatToStringDecimal(minCapacidadeAcumulado));
		paramsRelatorio.put("realMinRealizadosRevisaoAcumulado", NumberUtils.floatToStringDecimal(minRealizadosAcumulado));
		paramsRelatorio.put("realEficienciaRevisaoAcumulado", NumberUtils.calculaPercRealEficicencia(minRealizadosAcumulado, minCapacidadeAcumulado) +"%");
		
		paramsRelatorio.put("percPecasRevisadas", NumberUtils.calculaPercReal(pecasRevisadas, metaPecasRevisadas) +"%");
		paramsRelatorio.put("percPecasRevisadasAcumulado", NumberUtils.calculaPercReal(pecasRevisadasAcumulado, metaPecasRevisadasAcumulado) +"%");
		if(paramsRelatorio.get("diasRelatorio") != null) {
			Integer dias = Integer.parseInt(paramsRelatorio.get("diasRelatorio").toString());
			paramsRelatorio.put("mediaPecasRevisadas", NumberUtils.floatToStringInteger(pecasRevisadasAcumulado/dias));
			paramsRelatorio.put("mediaMinCapacidadeRevisao", NumberUtils.floatToStringInteger(minCapacidadeAcumulado/dias));
			paramsRelatorio.put("mediaMinRealizadosRevisao", NumberUtils.floatToStringInteger(minRealizadosAcumulado/dias));
		}
		return paramsRelatorio;
	}

	private Float calculaMinCapacidade(Map<String, Object> paramsRelatorio, Integer nrMaoDeObraRevisao, Integer opcao) {
		Float totalCapacidade = new Float(0);
		if(opcao.equals(1)) {
			Double minRealocados = NumberUtils.getDoubleToString(paramsRelatorio.get("minRevisaoRealocados"));
			Double horasExtras = NumberUtils.getDoubleToString(paramsRelatorio.get("realHorasExtrasRevisao"));
			totalCapacidade = (float) ((nrMaoDeObraRevisao * 528) + (horasExtras*60) + minRealocados);
		} else {
			Integer dias = Integer.parseInt(paramsRelatorio.get("diasRelatorio").toString());
			Double minRealocados = NumberUtils.getDoubleToString(paramsRelatorio.get("minRevisaoRealocadosAcumulado"));
			Double horasExtras = NumberUtils.getDoubleToString(paramsRelatorio.get("realHorasExtrasRevisaoAcumulado"));
			totalCapacidade = (float) (((nrMaoDeObraRevisao * 528)*dias) + (horasExtras*60) + minRealocados);
		}
		return totalCapacidade;
	}


}
