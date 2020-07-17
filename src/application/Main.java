package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("view/Home.fxml"));
			AnchorPane root = (AnchorPane) loader.load();
			HomeController homeController = (HomeController) loader.getController();

			Scene scene = new Scene(root, 668, 286);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Image imagem = new Image("file:src/images/logo.png");
			primaryStage.getIcons().add(imagem);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Bem vindo");
			primaryStage.setMaximized(false);
			homeController.setStage(primaryStage);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);

	}
}
