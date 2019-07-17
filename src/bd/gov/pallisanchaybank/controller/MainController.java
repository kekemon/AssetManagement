package bd.gov.pallisanchaybank.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class MainController implements Initializable{

    @FXML private TabPane tabPane;
    @FXML private Tab entryPage;
    @FXML private Tab previewPage;
    @FXML private EntryController entryController;
    @FXML private PreviewController previewController;

    @Override
	public void initialize(URL location, ResourceBundle resources) {
        
    }
}