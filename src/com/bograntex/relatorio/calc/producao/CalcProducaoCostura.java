package com.bograntex.relatorio.calc.producao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.bograntex.dao.Referencia;
import com.bograntex.dao.Setor;
import com.bograntex.utils.NumberUtils;

public class CalcProducaoCostura extends CalculoProducao {
	
	public CalcProducaoCostura() throws SQLException {
		super();
	}
	
	public static Map<String, Object> calcular(Map<String, Object> paramsRelatorio) throws SQLException {
		CalcProducaoCostura costura = new CalcProducaoCostura();
		Float envio = costura.calculaPecasEnviadasRetornadas(1, "E");
		Float envioAcumulada = costura.calculaPecasEnviadasRetornadas(2, "E");
		Float retornado = costura.calculaPecasEnviadasRetornadas(1, "R");
		Float retornadoAcumulada = costura.calculaPecasEnviadasRetornadas(2, "R");
		Float minEnvio = costura.calculaMinutosEnviadasRetornadas(1, "E", null);
		Float minEnvioAcumulada = costura.calculaMinutosEnviadasRetornadas(2, "E", null);
		Float minRetorno = costura.calculaMinutosEnviadasRetornadas(1, "R", null);
		Float minRetornoAcumulada = costura.calculaMinutosEnviadasRetornadas(2, "R", null);
		Float minCosturaAlocado = costura.calculaMinCosturaAlocado();
		costura.closeConnection();
		
		paramsRelatorio.put("realPecasEnviadas", NumberUtils.floatToStringInteger(envio));
		paramsRelatorio.put("realPecasRetornadas", NumberUtils.floatToStringInteger(retornado));
		paramsRelatorio.put("realMinEnviados", NumberUtils.floatToStringDecimal(minEnvio));
		paramsRelatorio.put("realMinRetornados", NumberUtils.floatToStringDecimal(minRetorno));
		paramsRelatorio.put("realPecasEnviadasAcumulado", NumberUtils.floatToStringInteger(envioAcumulada));
		paramsRelatorio.put("realPecasRetornadasAcumulado", NumberUtils.floatToStringInteger(retornadoAcumulada));
		paramsRelatorio.put("realMinEnviadosAcumulado", NumberUtils.floatToStringDecimal(minEnvioAcumulada));
		paramsRelatorio.put("realMinRetornadosAcumulado", NumberUtils.floatToStringDecimal(minRetornoAcumulada));
		paramsRelatorio.put("realMinCosturaAlocado", NumberUtils.floatToStringDecimal(minCosturaAlocado));
		
		Double metaEnvio = NumberUtils.getDoubleToString(paramsRelatorio.get("metaPecasEnviadas"));
		Double metaRetorno = NumberUtils.getDoubleToString(paramsRelatorio.get("metaPecasRetornadas"));
		Double metaMinEnvio = NumberUtils.getDoubleToString(paramsRelatorio.get("metaMinEnviados"));
		Double metaMinRetorno = NumberUtils.getDoubleToString(paramsRelatorio.get("metaMinRetornados"));
		Double metaEnvioAcumulado = NumberUtils.getDoubleToString(paramsRelatorio.get("metaPecasEnviadasAcumulado"));
		Double metaRetornoAcumulado = NumberUtils.getDoubleToString(paramsRelatorio.get("metaPecasRetornadasAcumulado"));
		Double metaMinEnvioAcumulado = NumberUtils.getDoubleToString(paramsRelatorio.get("metaMinEnviadosAcumulado"));
		Double metaMinRetornoAcumulado = NumberUtils.getDoubleToString(paramsRelatorio.get("metaMinRetornadosAcumulado"));
		paramsRelatorio.put("percPecasEnviadas", NumberUtils.calculaPercReal(envio, metaEnvio) +"%");
		paramsRelatorio.put("percPecasRetornadas", NumberUtils.calculaPercReal(retornado, metaRetorno) +"%");
		paramsRelatorio.put("percPecasEnviadasAcumulado", NumberUtils.calculaPercReal(envioAcumulada, metaEnvioAcumulado) +"%");
		paramsRelatorio.put("percPecasRetornadasAcumulado", NumberUtils.calculaPercReal(retornadoAcumulada, metaRetornoAcumulado) +"%");
		paramsRelatorio.put("percMinEnviados", NumberUtils.calculaPercReal(minEnvio, metaMinEnvio) +"%");
		paramsRelatorio.put("percMinRetornados", NumberUtils.calculaPercReal(minRetorno, metaMinRetorno) +"%");
		paramsRelatorio.put("percMinEnviadosAcumulado", NumberUtils.calculaPercReal(minEnvioAcumulada, metaMinEnvioAcumulado) +"%");
		paramsRelatorio.put("percMinRetornadosAcumulado", NumberUtils.calculaPercReal(minRetornoAcumulada, metaMinRetornoAcumulado) +"%");
		
		if(paramsRelatorio.get("diasRelatorio") != null) {
			Integer dias = Integer.parseInt(paramsRelatorio.get("diasRelatorio").toString());
			paramsRelatorio.put("mediaPecasEnviadas", NumberUtils.floatToStringInteger(envioAcumulada/dias));
			paramsRelatorio.put("mediaPecasRetornadas", NumberUtils.floatToStringInteger(retornadoAcumulada/dias));
			paramsRelatorio.put("mediaMinEnviados", NumberUtils.floatToStringDecimal(minEnvioAcumulada/dias));
			paramsRelatorio.put("mediaMinRetornados", NumberUtils.floatToStringDecimal(minRetornoAcumulada/dias));
			paramsRelatorio.put("diasCosturaAlocado", NumberUtils.floatToStringDecimal(minCosturaAlocado/(minRetornoAcumulada/dias)));
		}
		return paramsRelatorio;
	}
	
	public static Map<String, Object> calcularInterno(Map<String, Object> paramsRelatorio) throws SQLException {
		CalcProducaoCostura costura = new CalcProducaoCostura();
		Float minutosProduzidos = costura.calculaMinProdInterno(1);
		Float minutosProduzidosAcumulado = costura.calculaMinProdInterno(2);
		Integer nrMaoDeObra = Setor.getNrMaoDeOBraSetorCostura(costura.getConnection());
		Float minutosCapacidade = costura.calculaMinCapacidade(paramsRelatorio, nrMaoDeObra, 1);
		Float minutosCapacidadeAcumulado = costura.calculaMinCapacidade(paramsRelatorio, nrMaoDeObra, 2);
		costura.closeConnection();
		
		paramsRelatorio.put("realMinProdInterno", NumberUtils.floatToStringDecimal(minutosProduzidos));
		paramsRelatorio.put("realMinProdInternoAcumulado", NumberUtils.floatToStringDecimal(minutosProduzidosAcumulado));
		paramsRelatorio.put("realNrMaoObraDiretoInterno", nrMaoDeObra);
		paramsRelatorio.put("realMinCapacidadeInterno", NumberUtils.floatToStringDecimal(minutosCapacidade));
		paramsRelatorio.put("realMinCapacidadeInternoAcumulado", NumberUtils.floatToStringDecimal(minutosCapacidadeAcumulado));
		paramsRelatorio.put("realEficienciaCosturaInterna", NumberUtils.calculaPercRealEficicencia(minutosProduzidos, minutosCapacidade) +"%");
		paramsRelatorio.put("realEficienciaCosturaInternaAcumulado", NumberUtils.calculaPercRealEficicencia(minutosProduzidosAcumulado, minutosCapacidadeAcumulado) +"%");
		
		if(paramsRelatorio.get("diasRelatorio") != null) {
			Integer diasUteis = Integer.parseInt(paramsRelatorio.get("diasRelatorio").toString());
			paramsRelatorio.put("mediaMinProdInterno", NumberUtils.floatToStringDecimal(minutosProduzidosAcumulado/diasUteis));
			paramsRelatorio.put("mediaMinCapacidadeInterno", NumberUtils.floatToStringDecimal((float) (minutosCapacidadeAcumulado/diasUteis)));
		}
		return paramsRelatorio;
	}
	
	private Float calculaPecasEnviadasRetornadas(Integer opcao, String envioRetorno) throws SQLException {
		Float total = new Float(0);
		String query = "SELECT " + 
						"	SUM(F.QTD_PECA) QTD_PECAS " + 
						"FROM TMP_FACAO F " + 
						"WHERE F.IND_REGISTRO = ? " + 
						"	AND F.DATA_REGISTRO BETWEEN ? AND ? "; 
		
		PreparedStatement stmt = connection.prepareStatement(query);
		stmt.setString(1, envioRetorno);
		if (opcao.equals(1)) {
			stmt.setString(2, dateUtils.getDataFinalProducaoSql());
			stmt.setString(3, dateUtils.getDataFinalProducaoSql());
		} else {
			stmt.setString(2, dateUtils.getDataInicialSql());
			stmt.setString(3, dateUtils.getDataFinalProducaoSql());
		}
		ResultSet res = stmt.executeQuery();
		while (res.next()) {
			total = res.getFloat("QTD_PECAS");
		}
		res.close();
		stmt.close();
		return total;
	}
	
	private Float calculaMinutosEnviadasRetornadas(Integer opcao, String envioRetorno, String cnpjFacao) throws SQLException {
		Float total = new Float(0);
		String where = new String();
		if(cnpjFacao != null) {
			where = " AND F.CGC9 || F.CGC4 || F.CGC2 = ? ";
		}
		StringBuilder query = new StringBuilder("SELECT " + 
						"    F.GRUPO REF, " + 
						"	 BOCA_RETORNA_GRADE(F.SUB_GRUPO) GRADE," + 
						"    SUM(F.QTD_PECA) QTD_PECA " + 
						"FROM TMP_FACAO F " + 
						"WHERE F.IND_REGISTRO = ? " + 
						"    AND F.DATA_REGISTRO BETWEEN ? AND ? " +
						where +
						"GROUP BY " + 
						"   F.GRUPO, " + 
						"	BOCA_RETORNA_GRADE(F.SUB_GRUPO)");
		
		PreparedStatement stmt = connection.prepareStatement(query.toString());
		stmt.setString(1, envioRetorno);
		if (opcao.equals(1)) {
			stmt.setString(2, dateUtils.getDataFinalProducaoSql());
			stmt.setString(3, dateUtils.getDataFinalProducaoSql());
		} else {
			stmt.setString(2, dateUtils.getDataInicialSql());
			stmt.setString(3, dateUtils.getDataFinalProducaoSql());
		}
		if(cnpjFacao != null) {
			stmt.setString(4, cnpjFacao);
		}
		
		ResultSet res = stmt.executeQuery();
		while (res.next()) {
			String referencia = res.getString("REF");
			String grade = res.getString("GRADE");
			Float qtdePecas = res.getFloat("QTD_PECA");
			
			Float tempoCostura = Referencia.getTempoCosturaGrade(connection, referencia, grade);
			total=total+(qtdePecas*tempoCostura);
		}
		res.close();
		stmt.close();
		return total;
	}
	
	private Float calculaMinCosturaAlocado() throws SQLException {
		Float total = new Float(0);
		String query = "SELECT " + 
						"	SUM(BI.QUANTIDADE * BI.MINUTOS) TEMPO_ALOCADO " + 
						"FROM BI_ALOCADO_FACCAO BI " + 
						"WHERE BI.QUANTIDADE > 0"; 
		
		PreparedStatement stmt = connection.prepareStatement(query);
		ResultSet res = stmt.executeQuery();
		while (res.next()) {
			total = res.getFloat("TEMPO_ALOCADO");
		}
		res.close();
		stmt.close();
		return total;
	}
	
	private Float calculaMinProdInterno(Integer opcao) throws SQLException {
		Float totalMinutos = new Float(0);
		String[] estagios = new String[]{"60","68"};
		totalMinutos = this.calculaMinProduzidosEstagios(opcao, estagios);
		Float minRetornoFacaoInterna = this.calculaMinutosEnviadasRetornadas(opcao, "R", "3157192132");
		totalMinutos=totalMinutos+minRetornoFacaoInterna;
		return totalMinutos;
	}
	
	private Float calculaMinCapacidade(Map<String, Object> paramsRelatorio, Integer nrMaoDeObraRevisao, Integer opcao) {
		Float totalCapacidade = new Float(0);
		if(opcao.equals(1)) {
			Float minRealocados = NumberUtils.getFloatToString(paramsRelatorio.get("minCosturaRealocados"));
			Float horasExtras = NumberUtils.getFloatToString(paramsRelatorio.get("realHorasExtrasCostura"));
			totalCapacidade = (float) ((nrMaoDeObraRevisao * 528) + (horasExtras*60) + minRealocados);
		} else {
			Integer dias = Integer.parseInt(paramsRelatorio.get("diasRelatorio").toString());
			Float minRealocados = NumberUtils.getFloatToString(paramsRelatorio.get("minCosturaRealocadosAcumulado"));
			Float horasExtras = NumberUtils.getFloatToString(paramsRelatorio.get("realHorasExtrasCosturaAcumulado"));
			totalCapacidade = (float) (((nrMaoDeObraRevisao * 528)*dias) + (horasExtras*60) + minRealocados);
		}
		return totalCapacidade;
	}

}
