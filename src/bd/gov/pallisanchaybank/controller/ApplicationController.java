package bd.gov.pallisanchaybank.controller;
import java.awt.Desktop;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import bd.gov.pallisanchaybank.model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;

public class ApplicationController {

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


	public void initialize() {
		tableinit();
		ObservableList<String> list = FXCollections.observableArrayList("CPU","Monitor","Printer","Table","Chair");
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
		
		createReport(branch, calan);
	}

	private String toBangla(String input){
		String bangla = "";
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == '0'){
				bangla += '০';
			}else if (input.charAt(i) == '1'){
				bangla += '১';
			} else if (input.charAt(i) == '2'){
				bangla += '২';
			} else if (input.charAt(i) == '3'){
				bangla += '৩';
			} else if (input.charAt(i) == '4'){
				bangla += '৪';
			} else if (input.charAt(i) == '5'){
				bangla += '৫';
			} else if (input.charAt(i) == '6'){
				bangla += '৬';
			} else if (input.charAt(i) == '7'){
				bangla += '৭';
			} else if (input.charAt(i) == '8'){
				bangla += '৮';
			} else if (input.charAt(i) == '9'){
				bangla += '৯';
			} else {
				bangla += input.charAt(i);
			} 
		}
		return bangla;
	}
	
	private void createReport(Branch branch, Calan calan) {
		try {
			InputStream inputStream = BranchUtils.class.getResourceAsStream("/bd/gov/pallisanchaybank/res/template.html");
			Reader reader = new InputStreamReader(inputStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(reader);
			String line = null;
			ArrayList<String> lines = new ArrayList<>();
			String despatch = calan.getDespatchMain();
			if(calan.getDespatchBraket() != null){
				despatch += "(" + calan.getDespatchBraket() + ")";
			}
			while ((line = bufferedReader.readLine()) != null){
				if (line.contains("BRANCH_NAME")){
					line = line.replaceAll("BRANCH_NAME", branch.getBranchName());
				} else if(line.contains("DISTRICT")){
					line = line.replaceAll("DISTRICT", branch.getDistrict());
				} else if(line.contains("DESPATH")){
					line = line.replaceAll("DESPATH", toBangla(despatch));
				}else if(line.contains("MOBILE_NUMBER")){
					line = line.replaceAll("MOBILE_NUMBER", branch.getMobileNumber());
				} else if(line.contains("DATE")){
					line = line.replaceAll("DATE", toBangla(calan.getDate()));
				} else if(line.contains("TABLE_ROW")){
					line = "";
					Set<Asset> assets = calan.getAssets();
					int index = 1;
					for (Asset asset : assets) {
						lines.add("<tr><td>" + index++ + "</td>");
						lines.add("<td>" + asset.getAssetName() + "</td>");
						lines.add("<td>" + asset.getSerial() + "</td>");
						lines.add("<td>1</td>");
						lines.add("<td>" + asset.getComment() + "</td></tr>");
					}
					lines.add("<tr><td colspan=\"3\">Total</td><td>" + (index-1) + "</td><td>&nbsp;</td></tr>");
				}
				
				lines.add(line);
			}
			bufferedReader.close();
			File file = new File("reports");
			if(!file.exists() || !file.isDirectory()){
				file.mkdirs();
			}
			String reportpath = "reports/" + calan.getDate() + "_" + despatch + ".html";
			FileOutputStream fos = new FileOutputStream(reportpath);
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fos,"UTF-8"));
			
			for (String mline : lines) {
				bufferedWriter.write(mline + "\n"); 
			}
			
			bufferedWriter.close();
			File htmlFile = new File(reportpath);
			Desktop.getDesktop().browse(htmlFile.toURI());
		} catch (Exception e) {
			// TODO: handle exception
		}
		
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