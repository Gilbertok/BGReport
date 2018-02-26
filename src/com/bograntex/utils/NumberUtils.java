package com.bograntex.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NumberUtils {
	
	public static String doubleToStringDecimal(Double valor) {
		NumberFormat nf = new DecimalFormat("#,##0.00");
		nf.setMaximumFractionDigits(2);
		String numero = nf.format(valor);
		return numero;
	}
	
	public static String floatToStringInteger(Float valor) {
		NumberFormat nf = new DecimalFormat("#,##0.00");
		nf.setMaximumFractionDigits(0);
		String numero = nf.format(valor);
		return numero;
	}
	
	public static String floatToStringDecimal(Float valor) {
		NumberFormat nf = new DecimalFormat("#,##0.00");
		nf.setMaximumFractionDigits(2);
		String numero = nf.format(valor);
		return numero;
	}

	public static Float getFloatToString(Object value) {
		Float valor = new Float(0);
		if (value != null) {
			String valorString = value.toString().replace(".", "").replace(",", ".");
			return Float.parseFloat(valorString);
		}
		return valor;
	}
	
	public static Double getDoubleToString(Object value) {
		Double valor = new Double(0);
		if (value != null) {
			String valorString = value.toString().replace(".", "").replace(",", ".");
			return Double.parseDouble(valorString);
		}
		return valor;
	}

	public static String floatToPercent(Float calculaPercReal) {
		return (int) Math.ceil(calculaPercReal) +"%";
	}
	
	public static Integer calculaPercReal(Float realizado, Double meta) {
		return (int) Math.round(((realizado/meta) -1)*100);
	}
	
	public static Integer calculaPercReal(Float realizado, Float meta) {
		return (int) Math.round(((realizado/new Double(meta)) -1)*100);
	}
	
	public static Integer calculaPercRealEficicencia(Float realizado, Float meta) {
		return (int) Math.round(((realizado/new Double(meta)))*100);
	}
	
	public static Float calculaPercRealEficicenciaDecimal(Float realizado, Float meta) {
		NumberFormat nf = new DecimalFormat("#,##0.00");
		nf.setMaximumFractionDigits(2);
		String valor = nf.format((((realizado/new Float(meta)))*100));
		return Float.parseFloat(valor.replaceAll(",", "."));
	}

	public static Integer parseMinutos(Float horas) {
		Integer tempo = new Integer(0);
		int i = horas.toString().indexOf(".");
		String horasString = horas.toString().substring(0, i);
		String minutosString = horas.toString().substring(i+1);
		Integer horasFloat = new Integer(0);
		Integer minutosFloat = new Integer(0);
		if(!horasString.isEmpty() || horasString != null) {
			horasFloat = Integer.parseInt(horasString)*60;
		}
		if(minutosString.isEmpty() || minutosString != null) {
			minutosFloat = Integer.parseInt(minutosString);
		}
		tempo = horasFloat+minutosFloat;
		return tempo;
	}

}
