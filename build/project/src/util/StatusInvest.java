package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import config.Config;

public class StatusInvest {
	
	public static void update() {
		ProcessBuilder processBuilder = new ProcessBuilder();
	
		processBuilder.command("src/services/"+	Config.get("ambiente")+"/statusInvest");

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
				//TODO: Atualizar tela inicial com valores dos investimentos
			} else {
				// abnormal...
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
	}

}
