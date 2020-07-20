package application;

import java.net.URL;
import java.util.ResourceBundle;

import config.Config;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class SobreController implements Initializable{
	
    @FXML
    private Label versao;

    @FXML
    private Label ambiente;

    @FXML
    private Label so;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.so.setText("Sistema Operacional: "+Config.get("so"));
		this.ambiente.setText("Ambiente: "+Config.get("ambiente"));
		this.versao.setText("Versão: "+Config.get("versao"));
		
	}
    
    

}
