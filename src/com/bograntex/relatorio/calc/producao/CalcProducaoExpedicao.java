package com.bograntex.relatorio.calc.producao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.bograntex.dao.Referencia;
import com.bograntex.utils.NumberUtils;

public class CalcProducaoExpedicao extends CalculoProducao {
	
	public CalcProducaoExpedicao() throws SQLException {
		super();
	}

	public static Map<String, Object> calcular(Map<String, Object> paramsRelatorio) throws SQLException {
		CalcProducaoExpedicao expedicao = new CalcProducaoExpedicao();
		
		Float pecas1Qualidade = expedicao.calculaProducaoEstagio(1, 7);
		Float pecas1QualidadeAcumulado = expedicao.calculaProducaoEstagio(2, 7);
		Float pecas2Qualidade = expedicao.calculaProducaoEstagio2Qualidade(1, 7);
		Float pecas2QualidadeAcumulado = expedicao.calculaProducaoEstagio2Qualidade(2, 7);
		Float pecasPerda = expedicao.calculaProducaoEstagioPerda(1, 7);
		Float pecasPerdaAcumulado = expedicao.calculaProducaoEstagioPerda(2, 7);
		Float minutosEntradaExpedicao = expedicao.calculaMinEntradaExpedicao(1);
		Float minutosEntradaExpedicaoAcumulado = expedicao.calculaMinEntradaExpedicao(2);
		
		Double metaPecas1Qualidade = NumberUtils.getDoubleToString(paramsRelatorio.get("metaPecas1Qualidade"));
		Double metaPecas1QualidadeAcumulado = NumberUtils.getDoubleToString(paramsRelatorio.get("metaPecas1QualidadeAcumulado"));
		
		paramsRelatorio.put("realPecas1Qualidade", NumberUtils.floatToStringInteger(pecas1Qualidade));
		paramsRelatorio.put("realPecas2Qualidade", NumberUtils.floatToStringInteger(pecas2Qualidade));
		paramsRelatorio.put("realPecasPerda", NumberUtils.floatToStringInteger(pecasPerda));
		paramsRelatorio.put("realTotalEnvioExpedicao", NumberUtils.floatToStringInteger(pecas1Qualidade+pecas2Qualidade));
		paramsRelatorio.put("realPerc2Qualidade", NumberUtils.calculaPercRealEficicenciaDecimal(pecas2Qualidade, pecas1Qualidade) +"%");
		paramsRelatorio.put("realPercPerda", NumberUtils.calculaPercRealEficicenciaDecimal(pecasPerda, pecas1Qualidade) +"%");
		paramsRelatorio.put("realPerc2QualidadeEPerda", NumberUtils.calculaPercRealEficicenciaDecimal((pecas2Qualidade+pecasPerda), pecas1Qualidade) +"%");
		paramsRelatorio.put("realMinEntradaExpedicao", NumberUtils.floatToStringDecimal(minutosEntradaExpedicao));
		paramsRelatorio.put("realPecas1QualidadeAcumulado", NumberUtils.floatToStringInteger(pecas1QualidadeAcumulado));
		paramsRelatorio.put("realPecas2QualidadeAcumulado", NumberUtils.floatToStringInteger(pecas2QualidadeAcumulado));
		paramsRelatorio.put("realPecasPerdaAcumulado", NumberUtils.floatToStringInteger(pecasPerdaAcumulado));
		paramsRelatorio.put("realTotalEnvioExpedicaoAcumulado", NumberUtils.floatToStringInteger(pecas1QualidadeAcumulado+pecas2QualidadeAcumulado));
		paramsRelatorio.put("realPerc2QualidadeAcumulado", NumberUtils.calculaPercRealEficicenciaDecimal(pecas2QualidadeAcumulado, pecas1QualidadeAcumulado) +"%");
		paramsRelatorio.put("realPercPerdaAcumulado", NumberUtils.calculaPercRealEficicenciaDecimal(pecasPerdaAcumulado, pecas1QualidadeAcumulado) +"%");
		paramsRelatorio.put("realPerc2QualidadeEPerdaAcumulado", NumberUtils.calculaPercRealEficicenciaDecimal((pecas2QualidadeAcumulado+pecasPerdaAcumulado), pecas1QualidadeAcumulado) +"%");
		paramsRelatorio.put("realMinEntradaExpedicaoAcumulado", NumberUtils.floatToStringDecimal(minutosEntradaExpedicaoAcumulado));
		
		paramsRelatorio.put("percPecas1Qualidade", NumberUtils.calculaPercReal(pecas1Qualidade, metaPecas1Qualidade) +"%");
		paramsRelatorio.put("percPecas1QualidadeAcumulado", NumberUtils.calculaPercReal(pecas1QualidadeAcumulado, metaPecas1QualidadeAcumulado) +"%");
		
		if(paramsRelatorio.get("diasRelatorio") != null) {
			Integer dias = Integer.parseInt(paramsRelatorio.get("diasRelatorio").toString());
			paramsRelatorio.put("mediaPecas1Qualidade", NumberUtils.floatToStringInteger(pecas1QualidadeAcumulado/dias));
			paramsRelatorio.put("mediaPecas2Qualidade", NumberUtils.floatToStringInteger(pecas2QualidadeAcumulado/dias));
			paramsRelatorio.put("mediaPecasPerda", NumberUtils.floatToStringInteger(pecasPerdaAcumulado/dias));
			paramsRelatorio.put("mediaTotalEnvioExpedicao", NumberUtils.floatToStringInteger((pecas1QualidadeAcumulado+pecas2QualidadeAcumulado)/dias));
			paramsRelatorio.put("mediaMinEntradaExpedicao", NumberUtils.floatToStringInteger(minutosEntradaExpedicaoAcumulado/dias));
		}
		
		return paramsRelatorio;
	}

	private Float calculaMinEntradaExpedicao(Integer opcao) throws SQLException {
		Float totalMinutos = new Float(0);
		String query = "SELECT " + 
						"    OP.PERIODO_PRODUCAO AS PERIODO, " + 
						"    OP.ORDEM_CONFECCAO AS ORDEM, " + 
						"    OP.PROCONF_GRUPO AS REF, " + 
						"    OP.PROCONF_SUBGRUPO AS TAM, " + 
						"    BOCA_RETORNA_GRADE(OP.PROCONF_SUBGRUPO) AS GRADE, " + 
						"    SUM(IOP.QTDE_PRODUZIDA) QTD_PROD " + 
						"FROM PCPC_040 OP " + 
						"    INNER JOIN PCPC_045 IOP ON IOP.PCPC040_PERCONF = OP.PERIODO_PRODUCAO " + 
						"        AND IOP.PCPC040_ORDCONF = OP.ORDEM_CONFECCAO " + 
						"        AND IOP.PCPC040_ESTCONF = OP.CODIGO_ESTAGIO " + 
						"WHERE OP.SITUACAO_ORDEM <> 9 " + 		
						"    AND OP.CODIGO_ESTAGIO = 7 " + 
						"    AND IOP.DATA_PRODUCAO BETWEEN ? AND ? " + 
						"GROUP BY " + 
						"    OP.PERIODO_PRODUCAO, " + 
						"    OP.ORDEM_CONFECCAO, " + 
						"    OP.PROCONF_GRUPO, " + 
						"    OP.PROCONF_SUBGRUPO";
		PreparedStatement stmt = connection.prepareStatement(query);
		if (opcao.equals(1)) {
			stmt.setString(1, dateUtils.getDataFinalProducaoSql());
			stmt.setString(2, dateUtils.getDataFinalProducaoSql());
		} else {
			stmt.setString(1, dateUtils.getDataInicialSql());
			stmt.setString(2, dateUtils.getDataFinalProducaoSql());
		}
		ResultSet res = stmt.executeQuery();
		while (res.next()) {
			String referencia = res.getString("REF");
			String tamanho = res.getString("TAM");
			Float qtdeProduzida = res.getFloat("QTD_PROD");
			Float tempoEstagio = Referencia.getTempo(connection, referencia, tamanho);
			totalMinutos=totalMinutos+(qtdeProduzida*tempoEstagio);
		}
		res.close();
		stmt.close();
		return totalMinutos;
	}

}
