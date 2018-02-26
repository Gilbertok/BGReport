package com.bograntex.relatorio.calc.producao;

import java.sql.SQLException;
import java.util.Map;

import com.bograntex.dao.Setor;
import com.bograntex.utils.NumberUtils;

public class CalcProducaoDobracao extends CalculoProducao {
	
	public CalcProducaoDobracao() throws SQLException {
		super();
	}

	public static Map<String, Object> calcular(Map<String, Object> paramsRelatorio) throws SQLException {
		CalcProducaoDobracao dobracao = new CalcProducaoDobracao();
		String[] estagios = new String[]{"6","7"};
		Integer nrMaoDeObraDobracao = Setor.getNrMaoDeOBraSetorDobracao(dobracao.getConnection());
		Float pecasDobradas = dobracao.calculaProducaoEstagio(1, 6);
		Float pecasDobradasAcumulado = dobracao.calculaProducaoEstagio(2, 6);
		Float minCapacidade = dobracao.calculaMinCapacidade(paramsRelatorio, nrMaoDeObraDobracao, 1);
		Float minCapacidadeAcumulado = dobracao.calculaMinCapacidade(paramsRelatorio, nrMaoDeObraDobracao, 2);
		Float minRealizados = dobracao.calculaMinProduzidosEstagios(1, estagios);
		Float minRealizadosAcumulado = dobracao.calculaMinProduzidosEstagios(2, estagios);
		Double metaPecasDobradas = NumberUtils.getDoubleToString(paramsRelatorio.get("metaPecasRevisadas"));
		Double metaPecasDobradasAcumulado = NumberUtils.getDoubleToString(paramsRelatorio.get("metaPecasRevisadasAcumulado"));
		
		paramsRelatorio.put("realPecasDobradas", NumberUtils.floatToStringInteger(pecasDobradas));
		paramsRelatorio.put("realNrMaoObraDiretaDobracao", nrMaoDeObraDobracao);
//		paramsRelatorio.put("realHorasExtrasDobracao", 0);
		paramsRelatorio.put("realMinCapacidadeDobracao", NumberUtils.floatToStringDecimal(minCapacidade));
		paramsRelatorio.put("realMinRealizadosDobracao", NumberUtils.floatToStringDecimal(minRealizados));
		paramsRelatorio.put("realEficienciaDobracao", NumberUtils.calculaPercRealEficicencia(minRealizados, minCapacidade) +"%");
		paramsRelatorio.put("realPecasDobradasAcumulado", NumberUtils.floatToStringInteger(pecasDobradasAcumulado));
		paramsRelatorio.put("realMinCapacidadeDobracaoAcumulado", NumberUtils.floatToStringDecimal(minCapacidadeAcumulado));
		paramsRelatorio.put("realMinRealizadosDobracaoAcumulado", NumberUtils.floatToStringDecimal(minRealizadosAcumulado));
		paramsRelatorio.put("realEficienciaDobracaoAcumulado", NumberUtils.calculaPercRealEficicencia(minRealizadosAcumulado, minCapacidadeAcumulado) +"%");
		paramsRelatorio.put("percPecasDobradas", NumberUtils.calculaPercReal(pecasDobradas, metaPecasDobradas) +"%");
		paramsRelatorio.put("percPecasDobradasAcumulado", NumberUtils.calculaPercReal(pecasDobradasAcumulado, metaPecasDobradasAcumulado) +"%");
		
		if(paramsRelatorio.get("diasRelatorio") != null) {
			Integer dias = Integer.parseInt(paramsRelatorio.get("diasRelatorio").toString());
			paramsRelatorio.put("mediaPecasDobradas",  NumberUtils.floatToStringInteger(pecasDobradasAcumulado/dias));
			paramsRelatorio.put("mediaMinCapacidadeDobracao", NumberUtils.floatToStringInteger(minCapacidadeAcumulado/dias));
			paramsRelatorio.put("mediaMinRealizadosDobracao", NumberUtils.floatToStringInteger(minRealizadosAcumulado/dias));
		}
		return paramsRelatorio;
	}
	
	private Float calculaMinCapacidade(Map<String, Object> paramsRelatorio, Integer nrMaoDeObra, Integer opcao) {
		Float totalCapacidade = new Float(0);
		if(opcao.equals(1)) {
			Double minRealocados = NumberUtils.getDoubleToString(paramsRelatorio.get("minDobracaoRealocados"));
			Double horasExtras = NumberUtils.getDoubleToString(paramsRelatorio.get("realHorasExtrasDobracao"));
			totalCapacidade = (float) ((nrMaoDeObra * 528) + (horasExtras*60) + minRealocados);
		} else {
			Integer dias = Integer.parseInt(paramsRelatorio.get("diasRelatorio").toString());
			Double minRealocados = NumberUtils.getDoubleToString(paramsRelatorio.get("minDobracaoRealocadosAcumulado"));
			Double horasExtras = NumberUtils.getDoubleToString(paramsRelatorio.get("realHorasExtrasDobracaoAcumulado"));
			totalCapacidade = (float) (((nrMaoDeObra * 528)*dias) + (horasExtras*60) + minRealocados);
		}
		return totalCapacidade;
	}

}
