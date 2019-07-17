package bd.gov.pallisanchaybank.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

public class PreviewTableModel {
	
	public PreviewTableModel(int branchCode, String branchName,
			String district, String despatch, Button print) {
		this.setBranchCode(branchCode);
		this.setBranchName(branchName);
		this.setDistrict(district);
		this.setDespatch(despatch);
		this.setPrint(print);
	}

	private SimpleIntegerProperty branchCode;
	private SimpleStringProperty branchName;
	private SimpleStringProperty district;
    private SimpleStringProperty despatch;
    private SimpleObjectProperty<Button> print;
    
  
    
    public int getBranchCode() {
		return branchCode.get();
	}
    
	public void setBranchCode(int branchCode) {
		this.branchCode = new SimpleIntegerProperty(branchCode);
	}
	
	public String getBranchName() {
		return branchName.get();
	}
	
	public void setBranchName(String branchName) {
		this.branchName = new SimpleStringProperty(branchName);
	}
	
	public String getDistrict() {
		return district.get();
	}
	
	public void setDistrict(String district) {
		this.district = new SimpleStringProperty(district);
	}
	
	public String getDespatch() {
		return despatch.get();
	}
	
	public void setDespatch(String despatch) {
		this.despatch = new SimpleStringProperty(despatch);
	}
	
	public Button getPrint() {
		return print.get();
	}
	
	public void setPrint(Button print) {
		this.print = new SimpleObjectProperty<Button>(print);
	}
}
