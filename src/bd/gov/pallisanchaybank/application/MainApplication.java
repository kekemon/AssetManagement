package bd.gov.pallisanchaybank.application;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApplication extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
    	Parent mask = FXMLLoader.load(getClass()
                .getResource("/bd/gov/pallisanchaybank/view/mainview.fxml"));

		Scene scene = new Scene(mask);
		primaryStage.setTitle("Asset Calan");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/bd/gov/pallisanchaybank/view/appicon.png")));
    }
}