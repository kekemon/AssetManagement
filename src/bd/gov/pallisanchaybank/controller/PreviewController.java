package bd.gov.pallisanchaybank.controller;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import bd.gov.pallisanchaybank.helpers.BranchUtils;
import bd.gov.pallisanchaybank.helpers.ReportUtils;
import bd.gov.pallisanchaybank.model.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;

public class PreviewController implements Initializable{

	@FXML private DatePicker datePickerPreview;
	@FXML private TextField branchCodePreview;
	@FXML private Button submitBtn;
	
	@FXML public TableColumn<PreviewTableModel, Integer> pbranchCodeCol;
	@FXML public TableColumn<PreviewTableModel, String> pbranchNameCol;
	@FXML public TableColumn<PreviewTableModel, String> pdistrictCol;
	@FXML public TableColumn<PreviewTableModel, String> pdespatchCol;
	@FXML public TableColumn<PreviewTableModel, Button> pactionCol;
	@FXML private TableView<PreviewTableModel> previewtbl;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		pbranchCodeCol.setCellValueFactory(new PropertyValueFactory<>("branchCode"));
		pbranchNameCol.setCellValueFactory(new PropertyValueFactory<>("branchName"));
		pdistrictCol.setCellValueFactory(new PropertyValueFactory<>("district"));
		pdespatchCol.setCellValueFactory(new PropertyValueFactory<>("despatch"));
		pactionCol.setCellValueFactory(new PropertyValueFactory<>("print"));
		pbranchCodeCol.setStyle( "-fx-alignment: CENTER;");
		pbranchNameCol.setStyle( "-fx-alignment: CENTER;");
		pdistrictCol.setStyle( "-fx-alignment: CENTER;");
		pdespatchCol.setStyle( "-fx-alignment: CENTER;");
		pactionCol.setStyle( "-fx-alignment: CENTER;");
		
		String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate localDate = LocalDate.parse(date , formatter);
		datePickerPreview.setValue(localDate);
		datePickerPreview.setFocusTraversable(false);	
		
//		branchCodePreview.textProperty().addListener((observable, oldValue, newValue) -> {
//			if (!newValue.matches("[0-9]*")) {
//				branchCodePreview.setText(newValue.replaceAll("[^\\d]", ""));
//			}
//		});
		
		branchCodePreview.setOnKeyPressed(e -> {
		    if (e.getCode() == KeyCode.ENTER) { 
		    	onsubmitBtnClicked();
		    }
		});
	}
	
    public void onsubmitBtnClicked(){
    	try {
    		previewtbl.getItems().clear();
    		String brcode = branchCodePreview.getText();
    		if (brcode.trim().toLowerCase().equals("all")){
    			List<Branch> branches = BranchUtils.getBranchList();
    			for (Branch branch : branches) {
    				addCalaninTable(branch);
				}
    		}else{
    			Branch branch = BranchUtils.getBranchBy(Integer.parseInt(branchCodePreview.getText()));
    			addCalaninTable(branch);
    		}
    		
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR, "Invalid Branch Code", ButtonType.OK);
			alert.showAndWait();
		}
    	
    }

	private void addCalaninTable(Branch branch) {
		Set<Calan> calans = branch.getCalans();
		for (Calan calan : calans) {

			if (calan.getDate().equals(datePickerPreview.getValue().toString())) {
				String despatch = calan.getDespatchMain();
				
				if(calan.getDespatchBraket() != null){
					despatch += "(" + calan.getDespatchBraket() + ")";
				}
				
				Button print = new Button();
				Image imageDecline = new Image(getClass().getResourceAsStream("/bd/gov/pallisanchaybank/view/print.png"));
				ImageView imv = new ImageView(imageDecline);
				imv.setFitHeight(17);
				imv.setFitWidth(17);
				print.setGraphic(imv);
				
				print.setBackground(Background.EMPTY);
				print.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						ReportUtils.createReport(branch, calan);
					}
				});
				
				PreviewTableModel previewTableModel = new PreviewTableModel(branch.getBranchCode(), branch.getBranchName(), branch.getDistrict(), despatch, print);
				previewtbl.getItems().add(previewTableModel);
			}
		}
	}
}