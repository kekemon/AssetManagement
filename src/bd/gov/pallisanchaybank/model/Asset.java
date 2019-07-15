package bd.gov.pallisanchaybank.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Asset {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int assetID;
    
	@Column
	private String assetName;
	private String serial;
	private String comment;
	
	public Asset() {}
	
	public Asset(String assetName, String serial, String comment) {
		this.assetName = assetName;
		this.serial = serial;
		this.comment = comment;
	}
	
	public String getAssetName() {
		return assetName;
	}
	
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
	
	public String getSerial() {
		return serial;
	}
	
	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
