package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import model.InvestimentoVariavel;
import service.InvestimentoVariavelService;

public class StatusInvest {
	
	 
	private static final String USER_AGENT = "Mozilla/5.0";
	
	public static String update(InvestimentoVariavelService service) {
		String resposta = "";
		for(InvestimentoVariavel iv : service.getAll()) {
			try {
				
				String urlString = "https://statusinvest.com.br/category/tickerprice?ticker="+iv.getAtivo()+"&type=-1";
				
				URL url = new URL(urlString);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");
				con.setRequestProperty("User-Agent", USER_AGENT);
				con.setRequestProperty("Content-Type", "application/json");
				
				int responseCode = con.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) { // success
					BufferedReader in = new BufferedReader(new InputStreamReader(
							con.getInputStream()));
					String inputLine;
					StringBuffer response = new StringBuffer();

					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
					in.close();
					JSONObject jsonObject = new JSONObject(response.toString());
					
					JSONArray array = jsonObject.getJSONArray("prices");
					JSONObject json = new JSONObject(array.get(array.length() -1).toString());
					Double preco = json.getDouble("price");
					String data = json.getString("date");
					iv.setDataAtualizacao(data);
					iv.setValorUnidadeAtual(preco);
					resposta = resposta+"\n"+"ATIVO: "+iv.getAtivo()+" data: "+data+" preco: "+preco;
				} else {
					return "Erro na requsição: "+responseCode;
				}

			} catch (Exception e) {
				return "Erro de conexão";
			}

		}
		
		return resposta;

	}

}
