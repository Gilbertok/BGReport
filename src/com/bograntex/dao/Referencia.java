package com.bograntex.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Referencia {
	
	public static Float getTempoCosturaTamanho(Connection conn, String referencia, String tamanho) throws SQLException {
		Float tempoCostura = new Float(0); 
		String queryTempo = "SELECT " + 
							"    SUM(MAQ_SUB.MINUTOS) TEMPO " + 
							"FROM MQOP_050 MAQ_SUB " + 
							"WHERE MAQ_SUB.NIVEL_ESTRUTURA = 1 " + 
							"    AND MAQ_SUB.CODIGO_ESTAGIO = 3 " + 
							"  AND MAQ_SUB.GRUPO_ESTRUTURA = ? " + 
							"  AND (MAQ_SUB.SUBGRU_ESTRUTURA = ? " + 
							"    OR MAQ_SUB.SUBGRU_ESTRUTURA = '000')";
		PreparedStatement stmtTempoCostura = conn.prepareStatement(queryTempo);
		stmtTempoCostura.setString(1, referencia);
		stmtTempoCostura.setString(2, tamanho);
		stmtTempoCostura.setMaxRows(1);
		ResultSet resTempo = stmtTempoCostura.executeQuery();
		while (resTempo.next()) {
			Float tempo = resTempo.getFloat("TEMPO");
			tempoCostura=tempoCostura+tempo;
		}
		resTempo.close();
		stmtTempoCostura.close();
		return tempoCostura;
	}
	
	public static Float getTempoCosturaGrade(Connection conn, String referencia, String grade) throws SQLException {
		Float tempoCostura = new Float(0); 
		String queryTempo = "SELECT DISTINCT " + 
							"      T.REFERENCIA, " + 
							"      T.GRADE, " + 
							"      T.TEMPO " + 
							"    FROM TMP_TEMPOS T" +
							"    WHERE T.REFERENCIA = ?" + 
							"    	AND T.GRADE = ? ";
		PreparedStatement stmtTempoCostura = conn.prepareStatement(queryTempo);
		stmtTempoCostura.setString(1, referencia);
		stmtTempoCostura.setString(2, grade);
		stmtTempoCostura.setMaxRows(1);
		ResultSet resTempo = stmtTempoCostura.executeQuery();
		while (resTempo.next()) {
			Float tempo = resTempo.getFloat("TEMPO");
			tempoCostura=tempoCostura+tempo;
		}
		resTempo.close();
		stmtTempoCostura.close();
		return tempoCostura;
	}
	
	public static Float getTempoCosturaTamanhoInterno(Connection conn, String referencia, String tamanho) throws SQLException {
		Float tempoCostura = new Float(0); 
		String queryTempo = "SELECT " + 
							"    SUM(MAQ_SUB.MINUTOS) TEMPO " + 
							"FROM MQOP_050 MAQ_SUB " + 
							"WHERE MAQ_SUB.NIVEL_ESTRUTURA = 1 " + 
							"    AND MAQ_SUB.CODIGO_ESTAGIO IN (60,68) " + 
							"  AND MAQ_SUB.GRUPO_ESTRUTURA = ? " + 
							"  AND (MAQ_SUB.SUBGRU_ESTRUTURA = ? " + 
							"    OR MAQ_SUB.SUBGRU_ESTRUTURA = '000')";
		PreparedStatement stmtTempoCostura = conn.prepareStatement(queryTempo);
		stmtTempoCostura.setString(1, referencia);
		stmtTempoCostura.setString(2, tamanho);
		stmtTempoCostura.setMaxRows(1);
		ResultSet resTempo = stmtTempoCostura.executeQuery();
		while (resTempo.next()) {
			Float tempo = resTempo.getFloat("TEMPO");
			tempoCostura=tempoCostura+tempo;
		}
		resTempo.close();
		stmtTempoCostura.close();
		return tempoCostura;
	}

	public static Float getTempoEstagio(Connection conn, String codigoEstagio, String referencia, String tamanho) throws SQLException {
		Float tempoEstagio = new Float(0); 
		String queryTempo = "SELECT " + 
							"    SUM(MAQ_SUB.MINUTOS) TEMPO " + 
							"FROM MQOP_050 MAQ_SUB " + 
							"WHERE MAQ_SUB.NIVEL_ESTRUTURA = 1 " + 
							"    AND MAQ_SUB.CODIGO_ESTAGIO = ? " + 
							"  AND MAQ_SUB.GRUPO_ESTRUTURA = ? " + 
							"  AND (MAQ_SUB.SUBGRU_ESTRUTURA = ? " + 
							"    OR MAQ_SUB.SUBGRU_ESTRUTURA = '000')";
		PreparedStatement stmtTempoCostura = conn.prepareStatement(queryTempo);
		stmtTempoCostura.setString(1, codigoEstagio);
		stmtTempoCostura.setString(2, referencia);
		stmtTempoCostura.setString(3, tamanho);
		stmtTempoCostura.setMaxRows(1);
		ResultSet resTempo = stmtTempoCostura.executeQuery();
		while (resTempo.next()) {
			tempoEstagio=resTempo.getFloat("TEMPO");
		}
		resTempo.close();
		stmtTempoCostura.close();
		return tempoEstagio;
	}

	public static Float getTempo(Connection connection, String referencia, String tamanho) throws SQLException {
		Float tempo = new Float(0); 
		String queryTempo = "SELECT " + 
							"    SUM(MAQ_SUB.MINUTOS) TEMPO " + 
							"FROM MQOP_050 MAQ_SUB " + 
							"WHERE MAQ_SUB.NIVEL_ESTRUTURA = 1 " + 
							"  AND MAQ_SUB.GRUPO_ESTRUTURA = ? " + 
							"  AND (MAQ_SUB.SUBGRU_ESTRUTURA = ? " + 
							"    OR MAQ_SUB.SUBGRU_ESTRUTURA = '000')";
		PreparedStatement stmtTempoCostura = connection.prepareStatement(queryTempo);
		stmtTempoCostura.setString(1, referencia);
		stmtTempoCostura.setString(2, tamanho);
		stmtTempoCostura.setMaxRows(1);
		ResultSet resTempo = stmtTempoCostura.executeQuery();
		while (resTempo.next()) {
			tempo=resTempo.getFloat("TEMPO");
		}
		resTempo.close();
		stmtTempoCostura.close();
		return tempo;
	}

	public static Float getTempoEstagioCentroDeCusto(Connection connection, String referencia, String tamanho, String centroDeCusto, String codigoEstagio) throws SQLException {
		Float tempo = new Float(0); 
		String queryTempo = "SELECT " + 
							"    SUM(MAQ_SUB.MINUTOS) TEMPO " + 
							"FROM MQOP_050 MAQ_SUB " + 
							"WHERE MAQ_SUB.NIVEL_ESTRUTURA = 1 " + 
							"  AND MAQ_SUB.GRUPO_ESTRUTURA = ? " + 
							"  AND (MAQ_SUB.SUBGRU_ESTRUTURA = ? " + 
							"    OR MAQ_SUB.SUBGRU_ESTRUTURA = '000')" +
							"  AND MAQ_SUB.CENTRO_CUSTO = ? " +
							"  AND MAQ_SUB.CODIGO_ESTAGIO = ? ";
		PreparedStatement stmtTempoCostura = connection.prepareStatement(queryTempo);
		stmtTempoCostura.setString(1, referencia);
		stmtTempoCostura.setString(2, tamanho);
		stmtTempoCostura.setString(3, centroDeCusto);
		stmtTempoCostura.setString(4, codigoEstagio);
		stmtTempoCostura.setMaxRows(1);
		ResultSet resTempo = stmtTempoCostura.executeQuery();
		while (resTempo.next()) {
			tempo=resTempo.getFloat("TEMPO");
		}
		resTempo.close();
		stmtTempoCostura.close();
		return tempo;
	}

	public static Float getTempoEstagioCostura(Connection connection, String referencia, String tamanho) throws SQLException {
		Float tempo = new Float(0); 
		String queryTempo = "SELECT " + 
							"    SUM(MAQ_SUB.MINUTOS) TEMPO " + 
							"FROM MQOP_050 MAQ_SUB " + 
							"WHERE MAQ_SUB.NIVEL_ESTRUTURA = 1 " + 
							"  AND MAQ_SUB.GRUPO_ESTRUTURA = ? " + 
							"  AND (MAQ_SUB.SUBGRU_ESTRUTURA = ? " + 
							"    OR MAQ_SUB.SUBGRU_ESTRUTURA = '000')" +
							"  AND MAQ_SUB.CODIGO_ESTAGIO IN (3,60,68) ";
		PreparedStatement stmtTempoCostura = connection.prepareStatement(queryTempo);
		stmtTempoCostura.setString(1, referencia);
		stmtTempoCostura.setString(2, tamanho);
		stmtTempoCostura.setMaxRows(1);
		ResultSet resTempo = stmtTempoCostura.executeQuery();
		while (resTempo.next()) {
			tempo=resTempo.getFloat("TEMPO");
		}
		resTempo.close();
		stmtTempoCostura.close();
		return tempo;
	}

}
