package bd.gov.pallisanchaybank.model;

import javafx.beans.property.*;
import javafx.scene.control.Button;

public class TableModel {
	private SimpleIntegerProperty sl;
	private SimpleStringProperty asset;
	private SimpleStringProperty comment;
    private SimpleStringProperty serial;
    private SimpleObjectProperty<Button> delete;
    
    public TableModel(int sl, String asset, String serial, String comment, Button delete) {
    	this.setSl(sl);
    	this.setAsset(asset);
    	this.setSerial(serial);
    	this.setComment(comment);
    	this.setDelete(delete);
	}
    
	public String getAsset() {
		return asset.get();
	}
	public void setAsset(String asset) {
		this.asset = new SimpleStringProperty(asset);
	}
	public String getSerial() {
		return serial.get();
	}
	public void setSerial(String serial) {
		this.serial = new SimpleStringProperty(serial);
	}

	public int getSl() {
		return sl.get();
	}

	public void setSl(int sl) {
		this.sl = new SimpleIntegerProperty(sl);
	}

	public Button getDelete() {
		return delete.get();
	}

	public void setDelete(Button delete) {
		this.delete = new SimpleObjectProperty<Button>(delete);
	}

	public String getComment() {
		return comment.get();
	}

	public void setComment(String comment) {
		this.comment = new SimpleStringProperty(comment);
	}
}
