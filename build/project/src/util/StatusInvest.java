package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import config.Config;

public class StatusInvest {

	public static String update() {
		String result = "";
		ProcessBuilder processBuilder = new ProcessBuilder();

		if (Config.get("so").equals("windows")) {
			processBuilder.command("resources/services/" + Config.get("ambiente") + "/statusInvest.exe");
		} else {
			processBuilder.command("resources/services/" + Config.get("ambiente") + "/statusInvest");
		}

		try {

			Process process = processBuilder.start();

			StringBuilder output = new StringBuilder();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

			int exitVal = process.waitFor();
			if (exitVal == 0) {
				System.out.println("Success!");
				System.out.println(output);
				result = output.toString();
			}

		} catch (IOException e) {
			result = e.getMessage();
		} catch (InterruptedException e) {
			result = e.getMessage();
		}

		return result;

	}

}
