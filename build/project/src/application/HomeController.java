package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

import config.Config;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class HomeController implements Initializable{
	
	private Stage primaryStage = new Stage();
	private Stage stage;
	
    @FXML
    void novoDatabase(ActionEvent event) throws IOException {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Abrir arquivo");
    	Stage stage = new Stage();
    	fileChooser.getExtensionFilters().add(new ExtensionFilter("*.db", "*.db"));
    	File file = fileChooser.showOpenDialog(stage);

    	if(file != null) {
    		if(!Config.get("ambiente").equals("dev"))
    			Files.copy(Paths.get(file.getPath()), Paths.get("resources/financeiro.db"), StandardCopyOption.REPLACE_EXISTING);
    		else
    			Files.copy(Paths.get(file.getPath()), Paths.get("resources/financeirodev.db"), StandardCopyOption.REPLACE_EXISTING);
    		
    		this.usarRecente(null);
    	}
    	
    	
    }

    @FXML
    void usarRecente(ActionEvent event) {
    	
    	try {
			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("view/Dash.fxml"));
			Scene scene = new Scene(root, 800, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			Image imagem = new Image("file:src/images/logo.png");
			primaryStage.getIcons().add(imagem);
			primaryStage.setTitle("Financeiro - Dashboard");
			primaryStage.setMaximized(true);
			primaryStage.show();
			this.stage.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

    }


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}

}
