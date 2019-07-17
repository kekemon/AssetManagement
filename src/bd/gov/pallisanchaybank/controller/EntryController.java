package bd.gov.pallisanchaybank.controller;
import java.awt.Desktop;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import bd.gov.pallisanchaybank.helpers.BranchUtils;
import bd.gov.pallisanchaybank.helpers.ReportUtils;
import bd.gov.pallisanchaybank.model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class EntryController implements Initializable{

	@FXML private DatePicker datePicker;
	@FXML private TextField textFieldBranchCode;
	@FXML private TextField textFieldBranchName;
	@FXML private TextField textFieldDistrict;
	@FXML private TextField textFieldMobile;
	@FXML private TextField textFieldSerial;
	@FXML private TextArea textAreaComment;
	@FXML private TextField textFieldDespatch1;
	@FXML private TextField textFieldDespatch2;
	@FXML private ComboBox<String> comboBoxAsset;
	@FXML private Button btnAdd;
	@FXML private Button btnSave;
	@FXML private TableView<TableModel> tbData;

	@FXML public TableColumn<TableModel, Integer> slColumn;
	@FXML public TableColumn<TableModel, String> assetColumn;
	@FXML public TableColumn<TableModel, String> serialColumn;
	@FXML public TableColumn<TableModel, String> commentColumn;
	@FXML public TableColumn<TableModel, Button> actionColumn;
	private Branch branch;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tableinit();
		ObservableList<String> list = FXCollections.observableArrayList("CPU", "Monitor", "Printer", "Router", "Data SIM", "Extension Socket(5 Point)");
		comboBoxAsset.setItems(list);
		String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate localDate = LocalDate.parse(date , formatter);
		datePicker.setValue(localDate);
		datePicker.setFocusTraversable(false);		
		addListner();
	}

	private void tableinit() {
		slColumn.setCellValueFactory(new PropertyValueFactory<>("sl"));
		assetColumn.setCellValueFactory(new PropertyValueFactory<>("asset"));
		serialColumn.setCellValueFactory(new PropertyValueFactory<>("serial"));
		commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
		actionColumn.setCellValueFactory(new PropertyValueFactory<>("delete"));
		slColumn.setStyle( "-fx-alignment: CENTER;");
		assetColumn.setStyle( "-fx-alignment: CENTER;");
		serialColumn.setStyle( "-fx-alignment: CENTER;");
		commentColumn.setStyle( "-fx-alignment: CENTER;");
		actionColumn.setStyle( "-fx-alignment: CENTER;");
	}

	private void checkSaveBtn() {
		if(!textFieldDespatch1.getText().isEmpty() && 
				!textFieldBranchName.getText().isEmpty() && tbData.getItems().size()>0){
			btnSave.setDisable(false);
		}else{
			btnSave.setDisable(true);
		}
	}

	private void checkAddBtn(){
		if(!textFieldDespatch1.getText().isEmpty() && 
				!textFieldBranchName.getText().isEmpty()){
			btnAdd.setDisable(false);
		}else{
			btnAdd.setDisable(true);
		}
	}

	private void addListner(){
		comboBoxAsset.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue){
					comboBoxAsset.show();
				}
			}
		});
		
		textFieldSerial.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.isEmpty() && comboBoxAsset.getSelectionModel().getSelectedIndex() != -1) {
				checkAddBtn();
			}
		});

		textFieldBranchCode.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("[0-9]*")) {
				textFieldBranchCode.setText(newValue.replaceAll("[^\\d]", ""));
			}
		});

		textFieldDespatch1.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("[0-9]*")) {
				textFieldDespatch1.setText(newValue.replaceAll("[^\\d]", ""));
				return;
			}
			checkAddBtn();
			checkSaveBtn();
		});

		textFieldDespatch2.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("[0-9]*")) {
				textFieldDespatch2.setText(newValue.replaceAll("[^\\d]", ""));
			}
		});

		
		textFieldBranchCode.setOnKeyPressed(e -> {
		    if (e.getCode() == KeyCode.ENTER) { 
		    	try {
					int branchCode = Integer.parseInt(textFieldBranchCode.getText());
					branch = BranchUtils.getBranchBy(branchCode);
					textFieldBranchName.setText(branch.getBranchName());
					textFieldDistrict.setText(branch.getDistrict());
					textFieldMobile.setText(branch.getMobileNumber());
					textFieldSerial.requestFocus();
				} catch (Exception ex) {
					clearBranch();
					Alert alert = new Alert(AlertType.ERROR, "Invalid Branch Code "+textFieldBranchCode.getText(), ButtonType.OK);
					alert.showAndWait();

					if (alert.getResult() == ButtonType.OK) {
						textFieldBranchCode.requestFocus();
					}
				}
		    }
		});

		comboBoxAsset.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
			if (!textFieldSerial.getText().isEmpty()) {
				checkAddBtn();
			}
		});
	}
	
	private boolean isExistinTable(String serial){
		ObservableList<TableModel> list = tbData.getItems();
		for (TableModel tableModel : list) {
			if(tableModel.getSerial().equals(serial)){
				return true;
			}
		}
		return false;
	}
	
	public void onAddBtnClicked() {
		if(!isExistinTable(textFieldSerial.getText())){
			ObservableList<TableModel> list = tbData.getItems();
			int sl = list.size() + 1;
			String asset = comboBoxAsset.getSelectionModel().getSelectedItem();
			String serial = textFieldSerial.getText();
			String comment = textAreaComment.getText();
			Button delete = new Button();
			Image imageDecline = new Image(getClass().getResourceAsStream("/bd/gov/pallisanchaybank/view/delicon.png"));
			ImageView imv = new ImageView(imageDecline);
			imv.setFitHeight(17);
			imv.setFitWidth(17);
			delete.setGraphic(imv);
			TableModel tableModel = new TableModel(sl, asset, serial, comment, delete);
			delete.setBackground(Background.EMPTY);
			delete.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					list.remove(tableModel);
					int index = 1;
					for (TableModel tableModel : list) {
						tableModel.setSl(index++);
					}
					tbData.setItems(list);
					checkSaveBtn();
				}
			});

			list.add(tableModel);
			tbData.setItems(list);
			clearAsset();
			btnAdd.setDisable(true);
			checkSaveBtn();
		} else {
			Alert alert = new Alert(AlertType.ERROR, "Serial Number already added in list", ButtonType.OK);
			alert.showAndWait();
		}
	}

	public void onSaveFieldBranchCode() {
		Set<Calan> calans = branch.getCalans();
		Set<Asset> assets = new HashSet<Asset>();

		String dp1 = textFieldDespatch1.getText();
		String dp2 = null;
		if (!textFieldDespatch2.getText().isEmpty()){
			dp2 = textFieldDespatch2.getText();
		}
		branch.setBranchName(textFieldBranchName.getText());
		
		System.out.println(branch.getBranchName());
		branch.setDistrict(textFieldDistrict.getText());
		branch.setDistrict(textFieldDistrict.getText());
		ObservableList<TableModel> list = tbData.getItems();
		for (TableModel tableModel : list) {
			assets.add(new Asset(tableModel.getAsset(), tableModel.getSerial(), tableModel.getComment()));
		}
		BranchUtils.openSession();
		Calan calan = new Calan(datePicker.getValue().toString(), dp1, dp2, assets);
		calans.add(calan);

		BranchUtils.update(branch);
		clearField();
		
		ReportUtils.createReport(branch, calan);
	}

	private void clearField() {
		tbData.getItems().clear();
		clearBranch();
		clearAsset();
		textFieldDespatch1.clear();
		textFieldDespatch2.clear();
		textFieldBranchCode.requestFocus();
	}

	private void clearBranch() {
		textFieldBranchCode.clear();
		textFieldBranchName.clear();
		textFieldDistrict.clear();
		textFieldMobile.clear();
	}

	private void clearAsset() {
		comboBoxAsset.getSelectionModel().clearSelection();
		textFieldSerial.clear();
		textAreaComment.clear();
	}
}