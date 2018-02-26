package com.bograntex.relatorio.calc.estoque;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bograntex.db.DBConnectERP;
import com.bograntex.utils.NumberUtils;

public class CalcEstoqueTinturaria {
	
	public static Float calcular(Integer opcao) throws SQLException {
		String opcaoConsulta = new String();
		if(opcao.equals(2)) {
			opcaoConsulta = "MT";
		} else {
			opcaoConsulta = "KG";
		}
		
		Float totalTinturaria = new Float(0);
		Connection conn = new DBConnectERP().getInstance();
		String query = "SELECT " + 
						"   IORD.NUMERO_ORDEM, " + 
						"   IORD.SEQUENCIA, " + 
						"   SUM(IORD.QTDE_ARECEBER) QTDE_ARECEBER " + 
						"FROM OBRF_081 IORD " + 
						"	INNER JOIN OBRF_080 ORD ON ORD.NUMERO_ORDEM = IORD.NUMERO_ORDEM " +
						"	INNER JOIN BASI_030 BS3 ON BS3.NIVEL_ESTRUTURA = IORD.PRODORD_NIVEL99 " + 
						"   	AND BS3.REFERENCIA = IORD.PRODORD_GRUPO " +
						"WHERE IORD.PRODORD_NIVEL99 = 2 " +
						"   AND ORD.COD_CANC_ORDEM = 0 " +
						"   AND ORD.SITUACAO_ORDEM BETWEEN 1 AND 3 " + 
						"	AND BS3.UNIDADE_MEDIDA = ? " +
						"GROUP BY " + 
						"   IORD.NUMERO_ORDEM, " + 
						"   IORD.SEQUENCIA";
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, opcaoConsulta);
		ResultSet res = stmt.executeQuery();
		while (res.next()) {
			String nrOrdem = res.getString("NUMERO_ORDEM");
			String sequencia = res.getString("SEQUENCIA");
			Float qtdeReceber = res.getFloat("QTDE_ARECEBER");
			Float qtdeEntrada = new Float(0); 
			String queryEntrada = "SELECT DISTINCT " + 
									"	ENT.SEQ_ENTRADA," +
									"   ENT.QTDE_ENT ENTRADA " + 
									"FROM OBRF_086 ENT " + 
									"    INNER JOIN OBRF_088 SIN ON SIN.TIPO = ENT.TIPO_ENTRADA " + 
									"WHERE SIN.SINAL = '+' " + 
									"    AND ENT.ORD086_NUMORD = ? " + 
									"    AND ENT.ORD086_SEQORD = ? ";
			PreparedStatement stmtEntrada = conn.prepareStatement(queryEntrada);
			stmtEntrada.setString(1, nrOrdem);
			stmtEntrada.setString(2, sequencia);
			ResultSet resEntrada = stmtEntrada.executeQuery();
			while (resEntrada.next()) {
				Float quantidade = resEntrada.getFloat("ENTRADA");
				qtdeEntrada=qtdeEntrada+quantidade;
			}
			resEntrada.close();
			stmtEntrada.close();
			totalTinturaria = totalTinturaria + (qtdeReceber - qtdeEntrada);
		}
		res.close();
		stmt.close();
		conn.close();
		return totalTinturaria;
	}
	
	public static String calcularToString(Integer opcao) throws SQLException {
		return NumberUtils.floatToStringDecimal(CalcEstoqueTinturaria.calcular(opcao));
	}

}
