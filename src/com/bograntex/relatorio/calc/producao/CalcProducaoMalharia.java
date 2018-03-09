package com.bograntex.relatorio.calc.producao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.bograntex.db.DBConnectERP;
import com.bograntex.utils.NumberUtils;

public class CalcProducaoMalharia extends CalculoProducao {
	
	public CalcProducaoMalharia() throws SQLException {
		super();
	}
	
	public static Map<String, Object> calcular(Map<String, Object> paramsRelatorio) throws SQLException {
		CalcProducaoMalharia malharia = new CalcProducaoMalharia();
		Float producao = malharia.calculaProducao(1);
		Float producaoAcumulada = malharia.calculaProducao(2);
		Float envio = malharia.calculaEnvioBeneficiamento(1);
		Float envioAcumulado = malharia.calculaEnvioBeneficiamento(2);
		Float retorno = malharia.calculaRetornoBeneficiamento(1);
		Float retornoAcumulado = malharia.calculaRetornoBeneficiamento(2);
		Float montagem = malharia.calculaMontagemSMA(1);
		Float montagemAcumulado = malharia.calculaMontagemSMA(2);
		Float minCostura = malharia.calculaMinCosturaEstagio(1, new String[]{"10"});
		Float minCosturaAcumulado = malharia.calculaMinCosturaEstagio(2, new String[]{"10"});
		
		paramsRelatorio.put("realProducaoMalharia", NumberUtils.floatToStringDecimal(producao));
		paramsRelatorio.put("realEnvioBeneficiamento", NumberUtils.floatToStringDecimal(envio));
		paramsRelatorio.put("realRetornoBeneficiamento", NumberUtils.floatToStringDecimal(retorno));
		paramsRelatorio.put("realMontagemSMA", NumberUtils.floatToStringInteger(montagem));
		paramsRelatorio.put("realMinSMACostura", NumberUtils.floatToStringDecimal(minCostura));
		
		paramsRelatorio.put("realProducaoMalhariaAcumulado", NumberUtils.floatToStringDecimal(producaoAcumulada));
		paramsRelatorio.put("realEnvioBeneficiamentoAcumulado", NumberUtils.floatToStringDecimal(envioAcumulado));
		paramsRelatorio.put("realRetornoBeneficiamentoAcumulado", NumberUtils.floatToStringDecimal(retornoAcumulado));
		paramsRelatorio.put("realMontagemSMAAcumulado", NumberUtils.floatToStringInteger(montagemAcumulado));
		paramsRelatorio.put("realMinSMACosturaAcumulado", NumberUtils.floatToStringDecimal(minCosturaAcumulado));
		
		Double metaMontagem = NumberUtils.getDoubleToString(paramsRelatorio.get("metaMontagemSMA"));
		Double metaMontagemAcumulado = NumberUtils.getDoubleToString(paramsRelatorio.get("metaMontagemSMAAcumulado"));
		Double metaMinCostura = NumberUtils.getDoubleToString(paramsRelatorio.get("metaMinSMACostura"));
		Double metaMinCosturaAcumulado = NumberUtils.getDoubleToString(paramsRelatorio.get("metaMinSMACosturaAcumulado"));
		paramsRelatorio.put("percMontagemSMA", NumberUtils.calculaPercReal(montagem, metaMontagem) +"%");
		paramsRelatorio.put("percMontagemSMAAcumulado", NumberUtils.calculaPercReal(montagemAcumulado, metaMontagemAcumulado) +"%");
		paramsRelatorio.put("percMinSMACostura", NumberUtils.calculaPercReal(minCostura, metaMinCostura) +"%");
		paramsRelatorio.put("percMinSMACosturaAcumulado", NumberUtils.calculaPercReal(minCosturaAcumulado, metaMinCosturaAcumulado) +"%");
		
		if(paramsRelatorio.get("diasRelatorio") != null) {
			Integer dias = Integer.parseInt(paramsRelatorio.get("diasRelatorio").toString());
			paramsRelatorio.put("mediaProducaoMalharia", NumberUtils.floatToStringDecimal(producaoAcumulada/dias));
			paramsRelatorio.put("mediaEnvioBeneficiamento", NumberUtils.floatToStringDecimal(envioAcumulado/dias));
			paramsRelatorio.put("mediaRetornoBeneficiamento", NumberUtils.floatToStringDecimal(retornoAcumulado/dias));
			paramsRelatorio.put("mediaMontagemSMA", NumberUtils.floatToStringInteger(montagemAcumulado/dias));
			paramsRelatorio.put("mediaMinSMACostura", NumberUtils.floatToStringDecimal(minCosturaAcumulado/dias));
		}
		return paramsRelatorio;
	}

	private Float calculaProducao(Integer opcao) throws SQLException {
		Float total = new Float(0);
		Connection conn = new DBConnectERP().getInstance();
		String query = "SELECT " + 
						"    SUM(TEC.QTDE_QUILOS_ACAB) QTDE_QUILOS " + 
						"FROM PCPT_020 TEC " + 
						"WHERE  TEC.AREA_PRODUCAO = 4 " + 
						"    AND TEC.ROLO_ESTOQUE > 0 " + 
						"    AND TEC.PANOACAB_NIVEL99 IN ('4') " +
						"    AND TEC.CODIGO_DEPOSITO = 750 " +
						"    AND TEC.DATA_PROD_TECEL BETWEEN ? AND ? "; 
		
		PreparedStatement stmt = conn.prepareStatement(query);
		if (opcao.equals(1)) {
			stmt.setString(1, dateUtils.getDataFinalProducaoSql());
			stmt.setString(2, dateUtils.getDataFinalProducaoSql());
		} else {
			stmt.setString(1, dateUtils.getDataInicialSql());
			stmt.setString(2, dateUtils.getDataFinalProducaoSql());
		}
		ResultSet res = stmt.executeQuery();
		while (res.next()) {
			total = res.getFloat("QTDE_QUILOS");
		}
		res.close();
		stmt.close();
		conn.close();
		return total;
	}
	
	private Float calculaEnvioBeneficiamento(Integer opcao) throws SQLException {
		Float total = new Float(0);
		Connection conn = new DBConnectERP().getInstance();
		String query = "SELECT " + 
						"    SUM(FAT.PESO_LIQUIDO) PESO_ENVIO " + 
						"FROM FATU_050 FAT " + 
						"    INNER JOIN SUPR_010 FORN ON FORN.FORNECEDOR9 = FAT.CGC_9" + 
						"        AND FORN.FORNECEDOR4 = FAT.CGC_4 " + 
						"        AND FORN.FORNECEDOR2 = FAT.CGC_2 " + 
						"WHERE FAT.CODIGO_EMPRESA = 14 " + 
						"    AND FAT.SITUACAO_NFISC = 1 " + 
						"    AND (FAT.ESPECIE_VOLUME = 'ROLO' OR FAT.ESPECIE_VOLUME = 'ROLOS') " + 
						"    AND FAT.DATA_EMISSAO BETWEEN ? AND ? "; 
		PreparedStatement stmt = conn.prepareStatement(query);
		if (opcao.equals(1)) {
			stmt.setString(1, dateUtils.getDataFinalProducaoSql());
			stmt.setString(2, dateUtils.getDataFinalProducaoSql());
		} else {
			stmt.setString(1, dateUtils.getDataInicialSql());
			stmt.setString(2, dateUtils.getDataFinalProducaoSql());
		}
		ResultSet res = stmt.executeQuery();
		while (res.next()) {
			total = res.getFloat("PESO_ENVIO");
		}
		res.close();
		stmt.close();
		conn.close();
		return total;
	}
	
//	private Float calculaRetornoBeneficiamentoNota(Integer opcao) throws SQLException {
//		Float total = new Float(0);
//		Connection conn = new DBConnectERP().getInstance();
//		String query = "SELECT " + 
//				"    SUM(OBR.PESO_LIQUIDO) PESO_RETORNO " + 
//				"FROM OBRF_010 OBR " + 
//				"    INNER JOIN SUPR_010 FORN ON FORN.FORNECEDOR9 = OBR.CGC_CLI_FOR_9 " + 
//				"        AND FORN.FORNECEDOR4 = OBR.CGC_CLI_FOR_4 " + 
//				"        AND FORN.FORNECEDOR2 = OBR.CGC_CLI_FOR_2 " + 
//				"WHERE FORN.TIPO_FORNECEDOR = 38 " + 
//				"    AND OBR.DATA_EMISSAO BETWEEN ? AND ? "; 
//		PreparedStatement stmt = conn.prepareStatement(query);
//		if (opcao.equals(1)) {
//			stmt.setString(1, dateUtils.getDataFinalProducaoSql());
//			stmt.setString(2, dateUtils.getDataFinalProducaoSql());
//		} else {
//			stmt.setString(1, dateUtils.getDataInicialSql());
//			stmt.setString(2, dateUtils.getDataFinalProducaoSql());
//		}
//		ResultSet res = stmt.executeQuery();
//		while (res.next()) {
//			total = res.getFloat("PESO_RETORNO");
//		}
//		res.close();
//		stmt.close();
//		conn.close();
//		return total;
//	}
	
	private Float calculaRetornoBeneficiamento(Integer opcao) throws SQLException {
		Float total = new Float(0);
		Connection conn = new DBConnectERP().getInstance();
		String query = "SELECT " + 
						"	OB.PERIODO_PRODUCAO, " + 
						"	OB.ORDEM_SERVICO, " + 
						"   OB.SEQ_ORDEM_SERVICO, " + 
						"   SUM(ITEM.QTDE_ENT) PESO_RETORNO " + 
						"FROM PCPB_010020 OB " + 
						"	INNER JOIN OBRF_080_OBRF_081 OBRF ON OBRF.NUMERO_ORDEM = OB.ORDEM_SERVICO " + 
						"   	AND OBRF.SEQUENCIA = OB.SEQ_ORDEM_SERVICO " + 
						"   INNER JOIN OBRF_086 ITEM ON ITEM.ORD086_NUMORD = OBRF.NUMERO_ORDEM " + 
						"   	AND ITEM.ORD086_SEQORD = OBRF.SEQUENCIA " + 
						"WHERE OB.ORDEM_SERVICO > 0 " + 
						"	AND OB.COD_CANCELAMENTO = 0 " + 
						"   AND OBRF.CODIGO_SERVICO = 6 " + 
						"   AND OBRF.COD_CANC_ORDEM = 0 " + 
						"   AND OBRF.SITUACAO_ORDEM > 1 " + 
						"   AND ITEM.DATA_ENT BETWEEN ? AND ? " + 
						"GROUP BY " + 
						"   OB.PERIODO_PRODUCAO, " + 
						"   OB.ORDEM_SERVICO, " + 
						"	OB.SEQ_ORDEM_SERVICO"; 
		PreparedStatement stmt = conn.prepareStatement(query);
		if (opcao.equals(1)) {
			stmt.setString(1, dateUtils.getDataFinalProducaoSql());
			stmt.setString(2, dateUtils.getDataFinalProducaoSql());
		} else {
			stmt.setString(1, dateUtils.getDataInicialSql());
			stmt.setString(2, dateUtils.getDataFinalProducaoSql());
		}
		ResultSet res = stmt.executeQuery();
		while (res.next()) {
			total = total + res.getFloat("PESO_RETORNO");
		}
		res.close();
		stmt.close();
		conn.close();
		return total;
	}
	
	private Float calculaMontagemSMA(Integer opcao) throws SQLException {
		Float totalPecas = new Float(0);
		Connection conn = new DBConnectERP().getInstance();
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
						"    AND OP.CODIGO_ESTAGIO = 10 " + 
						"    AND IOP.DATA_PRODUCAO BETWEEN ? AND ? " + 
						"GROUP BY " + 
						"    OP.PERIODO_PRODUCAO, " + 
						"    OP.ORDEM_CONFECCAO, " + 
						"    OP.PROCONF_GRUPO, " + 
						"    OP.PROCONF_SUBGRUPO";
		PreparedStatement stmt = conn.prepareStatement(query);
		if (opcao.equals(1)) {
			stmt.setString(1, dateUtils.getDataFinalProducaoSql());
			stmt.setString(2, dateUtils.getDataFinalProducaoSql());
		} else {
			stmt.setString(1, dateUtils.getDataInicialSql());
			stmt.setString(2, dateUtils.getDataFinalProducaoSql());
		}
		ResultSet res = stmt.executeQuery();
		while (res.next()) {
			totalPecas = totalPecas + res.getFloat("QTD_PROD");
		}
		res.close();
		stmt.close();
		conn.close();
		return totalPecas;
	}
	
}
