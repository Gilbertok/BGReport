package com.bograntex.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Setor {
	
	public static Integer getNrMaoDeOBraSetorCargo(Connection connection, String centroDeCusto, String cargo) throws SQLException {
		Integer nrMaoDeObra = new Integer(0); 
		String query = "SELECT COUNT(*) PESSOAS FROM EFIC_050 EFIC WHERE EFIC.CENTRO_CUSTO = ? AND EFIC.CODIGO_CARGO = ? AND EFIC.SIT_FUNCIONARIO = 1";
		PreparedStatement stmt = connection.prepareStatement(query);
		stmt.setString(1, centroDeCusto);
		stmt.setString(2, cargo);
		stmt.setMaxRows(1);
		ResultSet result = stmt.executeQuery();
		while (result.next()) {
			nrMaoDeObra = result.getInt("PESSOAS");
		}
		result.close();
		stmt.close();
		return nrMaoDeObra;
	}
	
	public static Integer getNrMaoDeOBraSetorCargoTurno(Connection connection, String centroDeCusto, String cargo, String turno) throws SQLException {
		Integer nrMaoDeObra = new Integer(0); 
		String query = "SELECT COUNT(*) PESSOAS FROM EFIC_050 EFIC WHERE EFIC.CENTRO_CUSTO = ? AND EFIC.CODIGO_CARGO = ? AND EFIC.TURNO = ? AND EFIC.SIT_FUNCIONARIO = 1";
		PreparedStatement stmt = connection.prepareStatement(query);
		stmt.setString(1, centroDeCusto);
		stmt.setString(2, cargo);
		stmt.setString(3, turno);
		stmt.setMaxRows(1);
		ResultSet result = stmt.executeQuery();
		while (result.next()) {
			nrMaoDeObra = result.getInt("PESSOAS");
		}
		result.close();
		stmt.close();
		return nrMaoDeObra;
	}

	public static Integer getNrMaoDeOBraSetorCargos(Connection conn, String centroDeCusto, String[] cargos) throws SQLException {
		Integer pessoas = new Integer(0);
		for (String cargo : cargos) {
			pessoas=pessoas+Setor.getNrMaoDeOBraSetorCargo(conn, centroDeCusto, cargo);
		}
		return pessoas;
	}


	public static Integer getNrMaoDeOBraSetorCostura(Connection connection) throws SQLException {
		String[] cargos = new String[]{"3","7"};
		Integer pessoas = new Integer(0);
		for (String cargo : cargos) {
			pessoas=pessoas+Setor.getNrMaoDeOBraSetorCargo(connection, "4110", cargo);
		}
		return pessoas;
	}

	public static Integer getNrMaoDeOBraSetorRevisao(Connection connection) throws SQLException {
		String[] cargos = new String[]{"3"};
		Integer pessoas = new Integer(0);
		for (String cargo : cargos) {
			pessoas=pessoas+Setor.getNrMaoDeOBraSetorCargo(connection, "4113", cargo);
		}
		return pessoas;
	}

	public static Integer getNrMaoDeOBraSetorDobracao(Connection connection) throws SQLException {
		String[] cargos = new String[]{"4"};
		Integer pessoas = new Integer(0);
		for (String cargo : cargos) {
			pessoas=pessoas+Setor.getNrMaoDeOBraSetorCargo(connection, "4114", cargo);
		}
		return pessoas;
	}

	public static Integer getNrMaoDeOBraSetorCorteMaquina(Connection connection) throws SQLException {
		String[] cargos = new String[]{"101"};
		Integer pessoas = new Integer(0);
		for (String cargo : cargos) {
			pessoas=pessoas+Setor.getNrMaoDeOBraSetorCargo(connection, "4115", cargo);
		}
		return pessoas;
	}

	public static Integer getNrMaoDeOBraSetorCorteAcabamento(Connection connection) throws SQLException {
		String[] cargos = new String[]{"101"};
		Integer pessoas = new Integer(0);
		for (String cargo : cargos) {
			pessoas=pessoas+Setor.getNrMaoDeOBraSetorCargo(connection, "4108", cargo);
		}
		return pessoas;
	}

}
