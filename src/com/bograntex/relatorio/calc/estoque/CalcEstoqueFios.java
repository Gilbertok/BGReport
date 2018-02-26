package com.bograntex.relatorio.calc.estoque;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bograntex.db.DBConnectERP;
import com.bograntex.utils.NumberUtils;

public class CalcEstoqueFios {
	
	public static Float calcular(Integer opcao) throws SQLException {
		String opcaoConsulta = new String();
		if(opcao.equals(2)) {
			opcaoConsulta = "55";
		} else {
			opcaoConsulta = "71";
		}
		
		Float total = new Float(0);
		Connection conn = new DBConnectERP().getInstance();
		String query = "SELECT " + 
						"	SUM(EST.QTDE_ESTOQUE_ATU) QTDE_ESTOQUE " + 
						"FROM INTER_VI_ESTQ_040_ESTQ_080 EST " + 
						"	INNER JOIN BASI_030 BAS ON BAS.NIVEL_ESTRUTURA = EST.CDITEM_NIVEL99 " + 
						"		AND BAS.REFERENCIA = EST.CDITEM_GRUPO " + 
						"	INNER JOIN BASI_205 BASI ON BASI.CODIGO_DEPOSITO = EST.DEPOSITO " + 
						"WHERE EST.DEPOSITO IN (716,717,718,719,780) " + 
						"	AND EST.QTDE_ESTOQUE_ATU > 0 " + 
						"   AND BASI.CONSIDERA_TMRP = 1 " + 
						"   AND EST.ARTIGO_COTAS = ?";
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, opcaoConsulta);
		ResultSet res = stmt.executeQuery();
		while (res.next()) {
			Float qtdeEstoqueAtual = res.getFloat("QTDE_ESTOQUE");
			total=total+qtdeEstoqueAtual;
		}
		res.close();
		stmt.close();
		conn.close();
		return total;
	}
	
	public static String calcularToString(Integer opcao) throws SQLException {
		return NumberUtils.floatToStringDecimal(CalcEstoqueFios.calcular(opcao));
	}

}
