package com.bograntex.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.bograntex.db.DBConnectERP;

public class DateUtils {
	
	private Date dataInicial;
	private Date dataFinal;
	private Date dataFinalProducao;
	
	public DateUtils(Date data) {
		Calendar calendar = Calendar.getInstance();
		this.setDataFinal(data);
		
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		this.setDataInicial(this.setHour(calendar, 0, 1).getTime());
		if (this.compararData(this.getDataInicial(), this.getDataFinal())) {
			calendar.add(Calendar.MONTH, -1);
			this.setDataInicial(this.setHour(calendar, 0, 1).getTime());
		}
		this.setDataFinalProducao(this.getDataDiaAnteriorProducao());
	}
	
	private Calendar setHour(Calendar calendar, int hora, int minutos) {
		calendar.set(Calendar.HOUR, hora);
		calendar.set(Calendar.MINUTE, minutos);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}
	
	private Boolean compararData(Date dataInicial, Date dataFinal) {
		Calendar calInicial = Calendar.getInstance();
		calInicial.setTime(dataInicial);
		
		Calendar calFinal = Calendar.getInstance();
		calFinal.setTime(dataFinal);
		
		int diaInicial = calInicial.get(Calendar.DAY_OF_MONTH);
		int diaFinal = calFinal.get(Calendar.DAY_OF_MONTH);
		
		int mesInicial = calInicial.get(Calendar.MONTH);
		int mesFinal = calFinal.get(Calendar.MONTH);
		
		int anoInicial = calInicial.get(Calendar.YEAR);
		int anoFinal = calFinal.get(Calendar.YEAR);
		
		if(diaInicial==diaFinal && mesInicial==mesFinal && anoInicial==anoFinal) {
			return true;
		}
		return false;
	}
	
	public int getAno() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.getDataFinal());
		return calendar.get(Calendar.YEAR);
	}
	
	public int getDia() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.getDataFinalProducao());
		return calendar.get(Calendar.DATE);
	}
	
	public String getMes() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.getDataFinalProducao());
		String mesString = new String();
		switch (calendar.get(Calendar.MONTH)+1) {
		case 1:
			mesString = "Janeiro";
			break;
			
		case 2:
			mesString = "Fevereiro";
			break;
			
		case 3:
			mesString = "Março";
			break;
			
		case 4:
			mesString = "Abril";
			break;
			
		case 5:
			mesString = "Maio";
			break;
			
		case 6:
			mesString = "Junho";
			break;
			
		case 7:
			mesString = "Julho";
			break;
			
		case 8:
			mesString = "Agosto";
			break;
			
		case 9:
			mesString = "Setembro";
			break;
		
		case 10:
			mesString = "Outubro";
			break;
			
		case 11:
			mesString = "Novembro";
			break;
			
		case 12:
			mesString = "Dezembro";
			break;

		default:
			break;
		}
		return mesString;
	}
	
	public int deductDates() {
		if(this.getDataInicial() == null || this.getDataFinal() == null) {
			return 0;
		}
		int days = (int) ((this.getDataFinal().getTime() - this.getDataInicial().getTime())/(24*60*60*1000))+1;
		return (days > 0 ? days : 0);
	}
	
	public static String dateToString(Date date) {	
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt_BR"));
        String dateFormated = sdf.format(date);
        return dateFormated;
	}
	
	public String dateToSql(Date date) {	
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt_BR"));
        String dateFormated = sdf.format(date);
        return dateFormated;
	}
	
	public static String dateToArquivo(Date date) {	
		String dateFormated = new String();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		dateFormated = calendar.get(Calendar.YEAR)+"_"+(calendar.get(Calendar.MONTH)+1)+"_"+calendar.get(Calendar.DAY_OF_MONTH);
        return dateFormated;
	}

	public int getWorkingDays() {
		int dias = 0;
		try {
			Connection conn = new DBConnectERP().getInstance();
			String query = "SELECT COUNT(*) DIAS FROM BASI_260 S WHERE S.DIA_UTIL = 0 AND S.DATA_CALENDARIO >= ? AND S.DATA_CALENDARIO < ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, this.dateToSql(this.getDataInicial()));
			stmt.setString(2, this.dateToSql(this.getDataFinal()));
			ResultSet res = stmt.executeQuery();
			res.next();
			dias = res.getInt(1);
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dias;
	}
	
	public int getWorkingDaysMonth() {
		int dias = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		try {
			Connection conn = new DBConnectERP().getInstance();
			String query = "SELECT COUNT(*) DIAS FROM BASI_260 S WHERE S.DIA_UTIL = 0 AND S.DATA_CALENDARIO >= ? AND S.DATA_CALENDARIO <= ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, this.dateToSql(this.getDataInicial()));
			stmt.setString(2, this.dateToSql(calendar.getTime()));
			ResultSet res = stmt.executeQuery();
			res.next();
			dias = res.getInt(1);
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dias;
	}

	public Date getDataDiaAnteriorProducao() {
		Date data = null;
		try {
			Connection conn = new DBConnectERP().getInstance();
			String query = "SELECT MAX(S.DATA_CALENDARIO) DATA FROM BASI_260 S WHERE S.DIA_UTIL = 0 AND S.DATA_CALENDARIO >= ? AND S.DATA_CALENDARIO < ? ";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, this.dateToSql(this.getDataInicial()));
			stmt.setString(2, this.dateToSql(this.getDataFinal()));
			stmt.setMaxRows(1);
			ResultSet res = stmt.executeQuery();
			res.next();
			data = new Date(res.getDate("DATA").getTime());
			res.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}

	public Date getDataInicial() {
		return dataInicial;
	}
	
	public String getDataInicialSql() {
		return this.dateToSql(dataInicial);
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}
	
	public String getDataFinalSql() {
		return this.dateToSql(dataFinal);
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Date getDataFinalProducao() {
		return dataFinalProducao;
	}
	
	public String getDataFinalProducaoSql() {
		return this.dateToSql(dataFinalProducao);
	}

	public void setDataFinalProducao(Date dataFinalProducao) {
		this.dataFinalProducao = dataFinalProducao;
	}

}
