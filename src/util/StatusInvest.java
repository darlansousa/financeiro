package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import config.Config;

public class StatusInvest {

	public static String update() {
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
				return output.toString();
			}else {
				return output.toString();
			}

		} catch (IOException e) {
			return e.getMessage();
		} catch (InterruptedException e) {
			return e.getMessage();
		}

	}

}
