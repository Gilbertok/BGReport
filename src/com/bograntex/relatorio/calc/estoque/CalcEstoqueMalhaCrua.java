package com.bograntex.relatorio.calc.estoque;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bograntex.db.DBConnectERP;
import com.bograntex.utils.NumberUtils;

public class CalcEstoqueMalhaCrua {
	
	public static Float calcular() throws SQLException {
		Float total = new Float(0);
		Connection conn = new DBConnectERP().getInstance();
		String query = "SELECT " + 
						"    SUM(EST.QTDE_ESTOQUE_ATU) AS QTDE_ESTOQUE " + 
						"FROM ESTQ_040 EST " + 
						"    INNER JOIN BASI_030 BAS ON BAS.NIVEL_ESTRUTURA = EST.CDITEM_NIVEL99 " + 
						"        AND BAS.REFERENCIA = EST.CDITEM_GRUPO " + 
						"    INNER JOIN BASI_205 BASI ON BASI.CODIGO_DEPOSITO = EST.DEPOSITO " + 
						"WHERE EST.DEPOSITO IN (750,751) " + 
						"    AND EST.QTDE_ESTOQUE_ATU > 0 " + 
						"    AND BASI.CONSIDERA_TMRP = 1";
		PreparedStatement stmt = conn.prepareStatement(query);
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
	
	public static String calcularToString() throws SQLException {
		return NumberUtils.floatToStringDecimal(CalcEstoqueMalhaCrua.calcular());
	}

}
