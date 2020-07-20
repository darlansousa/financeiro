package util;

import java.text.NumberFormat;
import java.util.Locale;

public class Uteis {

	public static String convertToCurrency(Double valor) {

		Locale localeBR = new Locale("pt", "BR");
		NumberFormat dinheiroBR = NumberFormat.getCurrencyInstance(localeBR);

		return dinheiroBR.format(valor);
	}

	public static String getRandonColor() {
		String letters = "0123456789ABCDEF";
		String color = "#";

		for (int i = 0; i < 6; i++) {
			color += letters.charAt((int) Math.floor(Math.random() * 16));
		}
		return color;
	}

	public static String convertToPercent(Double valor) {
		return valor.intValue() + "%";
	}
	
	public static boolean isNumeric(String string) {
        return string.matches("-?\\d+(\\.\\d+)?");
	}

}
