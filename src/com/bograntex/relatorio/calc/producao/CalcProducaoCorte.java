package com.bograntex.relatorio.calc.producao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.bograntex.dao.Referencia;
import com.bograntex.dao.Setor;
import com.bograntex.model.ReferenciaModel;
import com.bograntex.utils.NumberUtils;

public class CalcProducaoCorte extends CalculoProducao {
	
	public CalcProducaoCorte() throws SQLException {
		super();
	}
	
	public static Map<String, Object> calcular(Map<String, Object> paramsRelatorio) throws SQLException {
		CalcProducaoCorte corte = new CalcProducaoCorte();
		
		Float pecasEnfestar = corte.calculaPecasEnfestar();
		Float pecasCortadas = corte.calculaProducaoEstagio(1,1);
		Float pecasCortadasAcumulado = corte.calculaProducaoEstagio(2,1);
		Float pecasPreparadas = corte.calculaProducaoEstagio(1,27);
		Float pecasPreparadasAcumulado = corte.calculaProducaoEstagio(2,27);
		
		Integer nrMaoDeObraMaquina = Setor.getNrMaoDeOBraSetorCorteMaquina(corte.getConnection());
		Integer nrMaoDeObraAcabamento = Setor.getNrMaoDeOBraSetorCorteAcabamento(corte.getConnection());
		Integer nrmaoDeObraCorte = nrMaoDeObraMaquina+nrMaoDeObraAcabamento;
		
		Float minCapacidadeMaquina = corte.calculaMinCapacidadeMaquina();
		Float minCapacidadeMaquinaAcumulado = new Float(0);
		Float minCapacidadeAcabamento = corte.calculaMinCapacidadeAcabamento();
		Float minCapacidadeAcabamentoAcumulado = new Float(0);
		
		Float minRealMaquina = corte.calculaMinRealMaquina(1);
		Float minRealMaquinaAcumulado = corte.calculaMinRealMaquina(2);
		Float minRealAcabamento = corte.calculaMinRealAcabamento(1);
		Float minRealAcabamentoAcumulado = corte.calculaMinRealAcabamento(2);
		
		Float minCorteCostura = corte.calculaMinCosturaEstagio(1, new String[]{"27"});
		Float minCorteCosturaAcumulado = corte.calculaMinCosturaEstagio(2, new String[]{"27"});
		
		Float minRealTotalCorte = minRealMaquina+minRealAcabamento;
		Float minRealTotalCorteAcumulado = minRealMaquinaAcumulado+minRealAcabamentoAcumulado;
		
		if(paramsRelatorio.get("diasRelatorio") != null) {
			Integer dias = Integer.parseInt(paramsRelatorio.get("diasRelatorio").toString());
			minCapacidadeMaquinaAcumulado = (minCapacidadeMaquina*dias);
			minCapacidadeAcabamentoAcumulado = (minCapacidadeAcabamento*dias);
		}
		
		Float minCapacidadeTotalCorte = corte.calculaMinCapacidade(paramsRelatorio, (minCapacidadeMaquina+minCapacidadeAcabamento), 1);
		Float minCapacidadeTotalCorteAcumulado = corte.calculaMinCapacidade(paramsRelatorio, (minCapacidadeMaquinaAcumulado+minCapacidadeAcabamentoAcumulado), 2);
		corte.closeConnection();
		
		paramsRelatorio.put("realPecasEnfestar", NumberUtils.floatToStringInteger(pecasEnfestar));
		paramsRelatorio.put("realPecasCortadas", NumberUtils.floatToStringInteger(pecasCortadas));
		paramsRelatorio.put("realPecasPrecparadas", NumberUtils.floatToStringInteger(pecasPreparadas));
		paramsRelatorio.put("realNrMaoObraDiretaCorte", nrmaoDeObraCorte);
//		paramsRelatorio.put("realNrHrExtraCorte", NumberUtils.floatToStringInteger(0f));
		paramsRelatorio.put("realMinCorteCostura", NumberUtils.floatToStringDecimal(minCorteCostura));
		paramsRelatorio.put("realMinCapacidadeTotalCorte", NumberUtils.floatToStringDecimal(minCapacidadeTotalCorte));
		paramsRelatorio.put("realMinRealizadosTotalCorte", NumberUtils.floatToStringDecimal(minRealTotalCorte));
		
		paramsRelatorio.put("realNrMaoObraDiretaMaquina", nrMaoDeObraMaquina);
		paramsRelatorio.put("realMinCapacidadeMaquina", NumberUtils.floatToStringDecimal(minCapacidadeMaquina));
		paramsRelatorio.put("realMinRealizadoMaquina", NumberUtils.floatToStringDecimal(minRealMaquina));
		
		paramsRelatorio.put("realNrMaoObraDiretaAcabamento", nrMaoDeObraAcabamento);
		paramsRelatorio.put("realMinCapacidadeAcabamento", NumberUtils.floatToStringDecimal(minCapacidadeAcabamento));
		paramsRelatorio.put("realMinRealizadoAcabamento", NumberUtils.floatToStringDecimal(minRealAcabamento));
		paramsRelatorio.put("realEficienciaCorte", NumberUtils.calculaPercRealEficicencia(minRealTotalCorte, minCapacidadeTotalCorte) +"%");
		
		paramsRelatorio.put("realPecasCortadasAcumulado", NumberUtils.floatToStringInteger(pecasCortadasAcumulado));
		paramsRelatorio.put("realPecasPrecparadasAcumulado", NumberUtils.floatToStringInteger(pecasPreparadasAcumulado));
		paramsRelatorio.put("realMinCorteCosturaAcumulado", NumberUtils.floatToStringDecimal(minCorteCosturaAcumulado));
		paramsRelatorio.put("realMinCapacidadeTotalCorteAcumulado", NumberUtils.floatToStringDecimal(minCapacidadeTotalCorteAcumulado));
		paramsRelatorio.put("realMinRealizadosTotalCorteAcumulado", NumberUtils.floatToStringDecimal(minRealTotalCorteAcumulado));
		
		paramsRelatorio.put("realMinCapacidadeMaquinaAcumulado", NumberUtils.floatToStringDecimal(minCapacidadeMaquinaAcumulado));
		paramsRelatorio.put("realMinRealizadoMaquinaAcumulado", NumberUtils.floatToStringDecimal(minRealMaquinaAcumulado));
		paramsRelatorio.put("realMinCapacidadeAcabamentoAcumulado", NumberUtils.floatToStringDecimal(minCapacidadeAcabamentoAcumulado));
		paramsRelatorio.put("realMinRealizadoAcabamentoAcumulado", NumberUtils.floatToStringDecimal(minRealAcabamentoAcumulado));
		paramsRelatorio.put("realEficienciaCorteAcumulado", NumberUtils.calculaPercRealEficicencia(minRealTotalCorteAcumulado, minCapacidadeTotalCorteAcumulado) +"%");
		
		Double metaPecasCortadas = NumberUtils.getDoubleToString(paramsRelatorio.get("metaPecasCortadas"));
		Double metaPecasCortadasAcumulado = NumberUtils.getDoubleToString(paramsRelatorio.get("metaPecasCortadasAcumulado"));
		Double metaPecasPreparadas = NumberUtils.getDoubleToString(paramsRelatorio.get("metaPecasPrecparadas"));
		Double metaPecasPreparadasAcumulado = NumberUtils.getDoubleToString(paramsRelatorio.get("metaPecasPrecparadasAcumulado"));
		Double metaMinCorteCostura = NumberUtils.getDoubleToString(paramsRelatorio.get("metaMinCorteCostura"));
		Double metaMinCorteCosturaAcumulado = NumberUtils.getDoubleToString(paramsRelatorio.get("metaMinCorteCosturaAcumulado"));
		
		paramsRelatorio.put("percPecasCortadas", NumberUtils.calculaPercReal(pecasCortadas, metaPecasCortadas) +"%");
		paramsRelatorio.put("percPecasCortadasAcumulado", NumberUtils.calculaPercReal(pecasCortadasAcumulado, metaPecasCortadasAcumulado) +"%");
		paramsRelatorio.put("percPecasPrecparadas", NumberUtils.calculaPercReal(pecasPreparadas, metaPecasPreparadas) +"%");
		paramsRelatorio.put("percPecasPrecparadasAcumulado", NumberUtils.calculaPercReal(pecasPreparadasAcumulado, metaPecasPreparadasAcumulado) +"%");
		paramsRelatorio.put("percMinCorteCostura", NumberUtils.calculaPercReal(minCorteCostura, metaMinCorteCostura) +"%");
		paramsRelatorio.put("percMinCorteCosturaAcumulado", NumberUtils.calculaPercReal(minCorteCosturaAcumulado, metaMinCorteCosturaAcumulado) +"%");
		
		if(paramsRelatorio.get("diasRelatorio") != null) {
			Integer dias = Integer.parseInt(paramsRelatorio.get("diasRelatorio").toString());
			paramsRelatorio.put("mediaPecasCortadas", NumberUtils.floatToStringInteger(pecasCortadasAcumulado/dias));
			paramsRelatorio.put("mediaPecasPrecparadas", NumberUtils.floatToStringInteger(pecasPreparadasAcumulado/dias));
			paramsRelatorio.put("mediaMinCorteCostura", NumberUtils.floatToStringInteger(minCorteCosturaAcumulado/dias));
			paramsRelatorio.put("mediaMinCapacidadeTotalCorte", NumberUtils.floatToStringInteger(minCapacidadeTotalCorteAcumulado/dias));
			paramsRelatorio.put("mediaMinRealizadosTotalCorte", NumberUtils.floatToStringInteger(minRealTotalCorteAcumulado/dias));
			paramsRelatorio.put("mediaMinCapacidadeMaquina", NumberUtils.floatToStringInteger(minCapacidadeMaquinaAcumulado/dias));
			paramsRelatorio.put("mediaMinRealizadoMaquina", NumberUtils.floatToStringInteger(minRealMaquinaAcumulado/dias));
			paramsRelatorio.put("mediaMinCapacidadeAcabamento", NumberUtils.floatToStringInteger(minCapacidadeAcabamentoAcumulado/dias));
			paramsRelatorio.put("mediaMinRealizadoAcabamento", NumberUtils.floatToStringInteger(minRealAcabamentoAcumulado/dias));
		}
		return paramsRelatorio;
	}

	private Float calculaMinRealMaquina(Integer opcao) throws SQLException {
		String[] estagios = new String[]{"1","65"};
		Float minutosRealizados = new Float(0);
		for (String estagio : estagios) {
			List<ReferenciaModel> referencias = this.getReferenciasProduzidasStagio(opcao, estagio);
			for (ReferenciaModel referencia : referencias) {
				Float tempoEstagio = Referencia.getTempoEstagioCentroDeCusto(connection, referencia.getReferencia(), referencia.getTamanho(), "4115", estagio);
				minutosRealizados = minutosRealizados+(referencia.getQuantidade()*tempoEstagio);
			}
		}
		return minutosRealizados;
	}
	
	private Float calculaMinRealAcabamento(Integer opcao) throws SQLException {
		String[] estagios = new String[]{"14","17","22","23","24","26","27","28","29","30"};
		Float minutosRealizados = new Float(0);
		for (String estagio : estagios) {
			List<ReferenciaModel> referencias = this.getReferenciasProduzidasStagio(opcao, estagio);
			for (ReferenciaModel referencia : referencias) {
				Float tempoEstagio = Referencia.getTempoEstagioCentroDeCusto(connection, referencia.getReferencia(), referencia.getTamanho(), "4108", estagio);
				minutosRealizados = minutosRealizados+(referencia.getQuantidade()*tempoEstagio);
			}
		}
		return minutosRealizados;
	}

	private Float calculaMinCapacidadeMaquina() throws SQLException {
		Float capacidadeMaquina = new Float(0);
		Integer primeiroTurno = Setor.getNrMaoDeOBraSetorCargoTurno(this.getConnection(), "4115", "101", "1");
		Integer segundoTurno = Setor.getNrMaoDeOBraSetorCargoTurno(this.getConnection(), "4115", "101", "2");
		capacidadeMaquina = new Float((primeiroTurno*528)+(segundoTurno*516));
		return capacidadeMaquina;
	}
	
	private Float calculaMinCapacidadeAcabamento() throws SQLException {
		Float capacidadeAcabamento = new Float(0);
		Integer primeiroTurno = Setor.getNrMaoDeOBraSetorCargoTurno(this.getConnection(), "4108", "101", "1");
		Integer segundoTurno = Setor.getNrMaoDeOBraSetorCargoTurno(this.getConnection(), "4108", "101", "2");
		Integer turnoNormal = Setor.getNrMaoDeOBraSetorCargoTurno(this.getConnection(), "4108", "101", "4");
		capacidadeAcabamento = new Float((primeiroTurno*528)+(segundoTurno*516)+(turnoNormal*528));
		return capacidadeAcabamento;
	}

	private Float calculaPecasEnfestar() throws SQLException {
		Float total = new Float(0);
		String query = "SELECT " + 
						"    SUM(A.QTDE_PECAS_PROG) PROG, " + 
						"    SUM(A.QTDE_EM_PRODUCAO_PACOTE) EM_PROD " + 
						"FROM PCPC_040 A " + 
						"    INNER JOIN PCPC_020 B ON B.ORDEM_PRODUCAO = A.ORDEM_PRODUCAO" + 
						"    INNER JOIN BASI_030 C ON C.NIVEL_ESTRUTURA = A.PROCONF_NIVEL99 " + 
						"        AND C.REFERENCIA = A.PROCONF_GRUPO " + 
						"WHERE A.CODIGO_ESTAGIO = 1 " + 
						"    AND A.QTDE_EM_PRODUCAO_PACOTE > 0 " + 
						"    AND B.COD_CANCELAMENTO = 0" + 
						"    AND EXISTS(SELECT 1 " + 
						"                  FROM PCPC_010 " + 
						"                  WHERE PCPC_010.PERIODO_PRODUCAO = B.PERIODO_PRODUCAO " + 
						"                  AND PCPC_010.AREA_PERIODO = 1 " + 
						"                  AND PCPC_010.SITUACAO_PERIODO < 3)"; 
		
		PreparedStatement stmt = connection.prepareStatement(query);
		ResultSet res = stmt.executeQuery();
		while (res.next()) {
			total = res.getFloat("EM_PROD");
		}
		res.close();
		stmt.close();
		return total;
	}
	
	private Float calculaMinCapacidade(Map<String, Object> paramsRelatorio, Float minCapacidade, Integer opcao) {
		Float totalCapacidade = new Float(0);
		if(opcao.equals(1)) {
			Double minRealocados = NumberUtils.getDoubleToString(paramsRelatorio.get("minCorteRealocados"));
			Double horasExtras = NumberUtils.getDoubleToString(paramsRelatorio.get("realHorasExtrasCorte"));
			totalCapacidade = (float) (minCapacidade + (horasExtras*60) + minRealocados);
		} else {
			Double minRealocados = NumberUtils.getDoubleToString(paramsRelatorio.get("minCorteRealocadosAcumulado"));
			Double horasExtras = NumberUtils.getDoubleToString(paramsRelatorio.get("realHorasExtrasCorteAcumulado"));
			totalCapacidade = (float) (minCapacidade + (horasExtras*60) + minRealocados);
		}
		return totalCapacidade;
	}

}
