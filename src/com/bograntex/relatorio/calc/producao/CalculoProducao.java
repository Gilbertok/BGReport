package com.bograntex.relatorio.calc.producao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bograntex.dao.Referencia;
import com.bograntex.db.DBConnectERP;
import com.bograntex.model.ReferenciaModel;
import com.bograntex.utils.DateUtils;

public class CalculoProducao {
	
	protected DateUtils dateUtils;
	protected Connection connection;
	
	public CalculoProducao() throws SQLException {
		this.dateUtils = new DateUtils(new Date());
		this.connection = new DBConnectERP().getInstance();
	}
	
	protected Connection getConnection() {
		return this.connection;
	}
	
	protected void closeConnection() throws SQLException {
		this.connection.close();
	}
	
	protected Float calculaProducaoEstagio(Integer opcao, Integer codigoEstagio) throws SQLException {
		Float totalPecas = new Float(0);
		String query = "SELECT " + 
						"    OP.PERIODO_PRODUCAO AS PERIODO, " + 
						"    OP.ORDEM_CONFECCAO AS ORDEM, " + 
						"    OP.PROCONF_GRUPO AS REF, " + 
						"    OP.PROCONF_SUBGRUPO AS TAM, " + 
						"    SUM(IOP.QTDE_PRODUZIDA) QTD_PROD " + 
						"FROM PCPC_040 OP " + 
						"    INNER JOIN PCPC_045 IOP ON IOP.PCPC040_PERCONF = OP.PERIODO_PRODUCAO " + 
						"        AND IOP.PCPC040_ORDCONF = OP.ORDEM_CONFECCAO " + 
						"        AND IOP.PCPC040_ESTCONF = OP.CODIGO_ESTAGIO " + 
						"WHERE OP.SITUACAO_ORDEM <> 9 " + 
						"    AND OP.CODIGO_ESTAGIO = ? " + 
						"    AND IOP.DATA_PRODUCAO BETWEEN ? AND ? " + 
						"GROUP BY " + 
						"    OP.PERIODO_PRODUCAO, " + 
						"    OP.ORDEM_CONFECCAO, " + 
						"    OP.PROCONF_GRUPO, " + 
						"    OP.PROCONF_SUBGRUPO";
		PreparedStatement stmt = connection.prepareStatement(query);
		stmt.setInt(1, codigoEstagio);
		if (opcao.equals(1)) {
			stmt.setString(2, dateUtils.getDataFinalProducaoSql());
			stmt.setString(3, dateUtils.getDataFinalProducaoSql());
		} else {
			stmt.setString(2, dateUtils.getDataInicialSql());
			stmt.setString(3, dateUtils.getDataFinalProducaoSql());
		}
		ResultSet res = stmt.executeQuery();
		while (res.next()) {
			totalPecas = totalPecas + res.getFloat("QTD_PROD");
		}
		res.close();
		stmt.close();
		return totalPecas;
	}
	
	protected Float calculaProducaoEstagio2Qualidade(Integer opcao, Integer codigoEstagio) throws SQLException {
		Float totalPecas = new Float(0);
		String query = "SELECT " + 
						"    OP.PERIODO_PRODUCAO AS PERIODO, " + 
						"    OP.ORDEM_CONFECCAO AS ORDEM, " + 
						"    OP.PROCONF_GRUPO AS REF, " + 
						"    OP.PROCONF_SUBGRUPO AS TAM, " + 
						"    SUM(IOP.QTDE_PECAS_2A) QTDE_PECAS_2A " + 
						"FROM PCPC_040 OP " + 
						"    INNER JOIN PCPC_045 IOP ON IOP.PCPC040_PERCONF = OP.PERIODO_PRODUCAO " + 
						"        AND IOP.PCPC040_ORDCONF = OP.ORDEM_CONFECCAO " + 
						"        AND IOP.PCPC040_ESTCONF = OP.CODIGO_ESTAGIO " + 
						"WHERE OP.SITUACAO_ORDEM <> 9 " + 
						"    AND OP.CODIGO_ESTAGIO = ? " + 
						"    AND IOP.QTDE_PECAS_2A > 0 " + 
						"    AND IOP.DATA_PRODUCAO BETWEEN ? AND ? " + 
						"GROUP BY " + 
						"    OP.PERIODO_PRODUCAO, " + 
						"    OP.ORDEM_CONFECCAO, " + 
						"    OP.PROCONF_GRUPO, " + 
						"    OP.PROCONF_SUBGRUPO";
		PreparedStatement stmt = connection.prepareStatement(query);
		stmt.setInt(1, codigoEstagio);
		if (opcao.equals(1)) {
			stmt.setString(2, dateUtils.getDataFinalProducaoSql());
			stmt.setString(3, dateUtils.getDataFinalProducaoSql());
		} else {
			stmt.setString(2, dateUtils.getDataInicialSql());
			stmt.setString(3, dateUtils.getDataFinalProducaoSql());
		}
		ResultSet res = stmt.executeQuery();
		while (res.next()) {
			totalPecas = totalPecas + res.getFloat("QTDE_PECAS_2A");
		}
		res.close();
		stmt.close();
		return totalPecas;
	}
	
	protected Float calculaProducaoEstagioPerda(Integer opcao, Integer codigoEstagio) throws SQLException {
		Float totalPecas = new Float(0);
		String query = "SELECT " + 
						"    OP.PERIODO_PRODUCAO AS PERIODO, " + 
						"    OP.ORDEM_CONFECCAO AS ORDEM, " + 
						"    OP.PROCONF_GRUPO AS REF, " + 
						"    OP.PROCONF_SUBGRUPO AS TAM, " + 
						"    SUM(IOP.QTDE_PERDAS) QTDE_PERDA " + 
						"FROM PCPC_040 OP " + 
						"    INNER JOIN PCPC_045 IOP ON IOP.PCPC040_PERCONF = OP.PERIODO_PRODUCAO " + 
						"        AND IOP.PCPC040_ORDCONF = OP.ORDEM_CONFECCAO " + 
						"        AND IOP.PCPC040_ESTCONF = OP.CODIGO_ESTAGIO " + 
						"WHERE OP.SITUACAO_ORDEM <> 9 " + 
						"    AND OP.CODIGO_ESTAGIO = ? " + 
						"    AND IOP.QTDE_PERDAS > 0 " + 
						"    AND IOP.DATA_PRODUCAO BETWEEN ? AND ? " + 
						"GROUP BY " + 
						"    OP.PERIODO_PRODUCAO, " + 
						"    OP.ORDEM_CONFECCAO, " + 
						"    OP.PROCONF_GRUPO, " + 
						"    OP.PROCONF_SUBGRUPO";
		PreparedStatement stmt = connection.prepareStatement(query);
		stmt.setInt(1, codigoEstagio);
		if (opcao.equals(1)) {
			stmt.setString(2, dateUtils.getDataFinalProducaoSql());
			stmt.setString(3, dateUtils.getDataFinalProducaoSql());
		} else {
			stmt.setString(2, dateUtils.getDataInicialSql());
			stmt.setString(3, dateUtils.getDataFinalProducaoSql());
		}
		ResultSet res = stmt.executeQuery();
		while (res.next()) {
			totalPecas = totalPecas + res.getFloat("QTDE_PERDA");
		}
		res.close();
		stmt.close();
		return totalPecas;
	}
	
	protected Float calculaProducaoEstagios(Integer opcao, int[] estagios) throws SQLException {
		Float totalPecasEstagios = new Float(0);
		for (int estagio : estagios) {
			Float totalPecas = new Float(0);
			totalPecas = this.calculaProducaoEstagio(opcao, estagio);
			totalPecasEstagios=totalPecasEstagios+totalPecas;
		}
		return totalPecasEstagios;
	}
	
	protected Float calculaMinProduzidosEstagios(int opcao, String[] estagios) throws SQLException {
		Float totalMinEstagios = new Float(0);
		for (String estagio : estagios) {
			List<ReferenciaModel> referencias = this.getReferenciasProduzidasStagio(opcao, estagio);
			for (ReferenciaModel referencia : referencias) {
				Float tempoEstagio = Referencia.getTempoEstagio(connection, estagio, referencia.getReferencia(), referencia.getTamanho());
				totalMinEstagios = totalMinEstagios+(referencia.getQuantidade()*tempoEstagio);
			}
		}
		return totalMinEstagios;
	}
	
	protected Float calculaMinCosturaEstagio(Integer opcao, String[] estagios) throws SQLException {
		Float minutosRealizados = new Float(0);
		for (String estagio : estagios) {
			List<ReferenciaModel> referencias = this.getReferenciasProduzidasStagio(opcao, estagio);
			for (ReferenciaModel referencia : referencias) {
				Float tempoEstagio = Referencia.getTempoEstagioCostura(connection, referencia.getReferencia(), referencia.getTamanho());
				minutosRealizados = minutosRealizados+(referencia.getQuantidade()*tempoEstagio);
			}
		}
		return minutosRealizados;
	}
	
	protected List<ReferenciaModel> getReferenciasProduzidasStagio(Integer opcao, String codigoEstagio) throws SQLException {
		List<ReferenciaModel> referencias = new ArrayList<ReferenciaModel>();
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
						"    AND OP.CODIGO_ESTAGIO = ? " + 
						"    AND IOP.DATA_PRODUCAO BETWEEN ? AND ? " + 
						"GROUP BY " + 
						"    OP.PERIODO_PRODUCAO, " + 
						"    OP.ORDEM_CONFECCAO, " + 
						"    OP.PROCONF_GRUPO, " + 
						"    OP.PROCONF_SUBGRUPO";
		PreparedStatement stmt = connection.prepareStatement(query);
		stmt.setString(1, codigoEstagio);
		if (opcao.equals(1)) {
			stmt.setString(2, dateUtils.getDataFinalProducaoSql());
			stmt.setString(3, dateUtils.getDataFinalProducaoSql());
		} else {
			stmt.setString(2, dateUtils.getDataInicialSql());
			stmt.setString(3, dateUtils.getDataFinalProducaoSql());
		}
		ResultSet res = stmt.executeQuery();
		while (res.next()) {
			String refer = res.getString("REF");
			String tamanho = res.getString("TAM");
			String grade = res.getString("GRADE");
			Integer qtdeProduzida = res.getInt("QTD_PROD");
			ReferenciaModel referencia = new ReferenciaModel(refer, tamanho, grade, qtdeProduzida);
			referencias.add(referencia);
		}
		res.close();
		stmt.close();
		return referencias;
	}

}
