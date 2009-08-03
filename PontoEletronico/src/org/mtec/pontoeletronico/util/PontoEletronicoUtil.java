package org.mtec.pontoeletronico.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * @author Maciel Escudero Bombonato
 */
public final class PontoEletronicoUtil {
	
	/**
	 * Mascara de formatacao de data: dd/MM/yyyy
	 */
	public static final String PATTERN_DD_MM_YYYY = "dd/MM/yyyy";
	
	/**
	 * Mascara de formatacao de data: dd/MM
	 */
	public static final String PATTERN_DD_MM = "dd/MM";
	
	/**
	 * Mascara de formatacao de data: dd
	 */
	public static final String PATTERN_DD = "dd";
	
	/**
	 * Mascara de formatacao de data: ddMMyyyy
	 */
	public static final String PATTERN_DDMMYYYY = "ddMMyyyy";
	
	/**
	 * Mascara de formatacao de data: MMM/yyyy
	 */
	public static final String PATTERN_MMM_YYYY = "MMM/yyyy";
	
	/**
	 * Mascara de formatacao de data: yyyy-MM
	 */
	public static final String PATTERN_YYYY_MM = "yyyy-MM";
	
	/**
	 * Mascara de formatacao de data: MMM/yyyy
	 */
	public static final String PATTERN_MMM_YY = "MMM/yy";
	
	/**
	 * Mascara de formatacao de data: dd/MMM/yyyy
	 */
	public static final String PATTERN_DD_MMM_YYYY = "dd/MMM/yyyy";
	
	/**
	 * Mascara de formatacao de data: MMyyyy
	 */
	public static final String PATTERN_MMYYYY = "MMyyyy";
	
	/**
	 * Mascara de formatacao de data: MMDDyyyy
	 */
	public static final String PATTERN_MMDDYYYY = "MMDDyyyy";
	
	/**
	 * Mascara de formatacao de data e hora: dd/MM/yyyy HH:mm
	 */
	public static final String PATTERN_DD_MM_YYYY_HH_MM = "dd/MM/yyyy HH:mm";
	
	/**
	 * Mascara de formatacao de data e hora: dd/MM/yyyy HH:mm:ss
	 */
	public static final String PATTERN_DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss";

	/**
	 * Mascara de formatacao de data e hora: ddMMyyyy HHmmss
	 */
	public static final String PATTERN_DDMMYYYY_HHMMSS = "ddMMyyyy HHmmss";	
	
	/**
	 * Mascara de formatacao de hora: HH:mm:ss
	 */
	public static final String PATTERN_HH_MM_SS = "HH:mm:ss";
	
	/**
	 * Mascara de formatacao de hora: HHmmss
	 */
	public static final String PATTERN_HHMMSS = "HHmmss";
	
	/**
	 * Mascara de formatacao de hora: HH:mm
	 */
	public static final String PATTERN_HH_MM = "HH:mm";

	/**
	 * Recebe um objeto do tipo java.util.Date e retorna uma String no formato definido via parametro
	 * @param value - java.util.Date (Data que devera ser formatada)
	 * @param pattern - Mascara de formatacao da Data, por exemplo, dd/MM/yyyy, ou dd/MMM/yyyy, ...
	 * @return String
	 */
	public static String formatDate(java.util.Date value, String pattern) {
		if (value != null) {
			// Cria Locale informando Portugues Brasil
			Locale locale = new Locale("pt", "BR");
			// Cria Formatador de data informando padr�o
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, locale);
			// Passa o resultado final obtido para uma String
			String retorno = dateFormat.format(value);
			// Trata a String para que a primeira letra seja maiuscula (caso esteja no padr�o MMM/yyyy
			if ((retorno != null) && (retorno.length() > 0)) {
				retorno = (retorno.charAt(0) + "").toUpperCase() + retorno.substring(1);
			}
			return retorno;
		} else {
			return "";
		}
	}
	
	/**
	 * Converte uma String em um java.util.Date.
	 * @param data
	 * @param pattern
	 * @return java.util.Date
	 */
	public static java.util.Date parseStringToDate(String data, String pattern) {
		java.util.Date retorno = null;

		if (data != null && pattern != null) {
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			format.setLenient(false);
			try {
				retorno = format.parse(data);
			} catch (ParseException e) {
				return null;
			}
		}
		return retorno;
	}
	
	/**
	 * Converte uma String em um Calendar
	 * @param data
	 * @param pattern
	 * @return Calendar
	 */
	public static Calendar parseStringToCalendar(String data, String pattern) {
		Calendar calendar = GregorianCalendar.getInstance();
		if (data != null 
				&& pattern != null) {
			calendar.setTime(parseStringToDate(data, pattern));
		}
		return calendar;
	} 

}
