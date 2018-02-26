package com.bograntex.relatorio.calc.estoque;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bograntex.db.DBConnectERP;
import com.bograntex.utils.NumberUtils;

public class CalcEstoqueMalhaTinta {

	public static Float calcular() throws SQLException {
		Float total = new Float(0);
		Connection conn = new DBConnectERP().getInstance();
		String query = "SELECT " + 
						"    EST.CDITEM_NIVEL99, " + 
						"    EST.CDITEM_GRUPO, " + 
						"    EST.CDITEM_SUBGRUPO, " + 
						"    EST.CDITEM_ITEM, " + 
						"    EST.QTDE_ESTOQUE_ATU, " + 
						"    EST.LOTE_ACOMP, " + 
						"    BAS.TIPO_VOLUME " + 
						"FROM ESTQ_040 EST " + 
						"    INNER JOIN BASI_205 BAS ON BAS.CODIGO_DEPOSITO = EST.DEPOSITO " +
						"  	 INNER JOIN BASI_030 BS3 ON BS3.NIVEL_ESTRUTURA = EST.CDITEM_NIVEL99 " + 
						"  		AND BS3.REFERENCIA = EST.CDITEM_GRUPO " + 
						"WHERE BS3.UNIDADE_MEDIDA = 'KG' " +
						"	AND EST.DEPOSITO = 741 " + 
						"   AND EST.QTDE_ESTOQUE_ATU > 0 " + 
						"   AND BS3.CONTA_ESTOQUE = 20 " + 
						"   AND BAS.CONSIDERA_TMRP = 1 " + 
						"ORDER BY " + 
						"    EST.CDITEM_NIVEL99 ASC, " + 
						"    EST.CDITEM_GRUPO ASC, " + 
						"    EST.CDITEM_SUBGRUPO ASC, " + 
						"    EST.CDITEM_ITEM  ASC ";
		PreparedStatement stmt = conn.prepareStatement(query);
		ResultSet res = stmt.executeQuery();
		while (res.next()) {
			Float qtdeEstoqueAtual = res.getFloat("QTDE_ESTOQUE_ATU");
			total=total+qtdeEstoqueAtual;
		}
		res.close();
		stmt.close();
		conn.close();
		return total;
	}
	
	public static String calcularToString() throws SQLException {
		return NumberUtils.floatToStringDecimal(CalcEstoqueMalhaTinta.calcular());
	}
	
}
