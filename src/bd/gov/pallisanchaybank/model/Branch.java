package bd.gov.pallisanchaybank.model;

import java.util.*;
import javax.persistence.*;

@Entity
public class Branch {
	@Id
	protected int branchCode;

	@Column
	private String mobileNumber;
	private String branchName;
	private String district;
	private String upzilla;
	
	@OneToMany(cascade = CascadeType.ALL)
	private Set<Calan> calans;

	public Branch(int branchCode, String branchName, String district, String upzilla, String mobileNumber, Set<Calan> calans) {
		this.branchCode = branchCode;
		this.branchName = branchName;
		this.district = district;
		this.upzilla = upzilla;
		this.mobileNumber = mobileNumber;
		this.calans = calans;
	}
	
	public Branch() {
		
	}
	
	public int getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(int branchCode) {
		this.branchCode = branchCode;
	}
	
	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getUpzilla() {
		return upzilla;
	}

	public void setUpzilla(String upzilla) {
		this.upzilla = upzilla;
	}
	
	public Set<Calan> getCalans() {
		return calans;
	}

	public void setCalans(Set<Calan> calans) {
		this.calans = calans;
	}
	
}
