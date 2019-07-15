package bd.gov.pallisanchaybank.model;

import java.util.*;
import javax.persistence.*;

@Entity
public class Calan {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int calanID;
	
	@Column
	private String date;
	private String despatchMain;
	private String despatchBraket;
	
	@OneToMany(cascade = CascadeType.ALL)
	private Set<Asset> assets;

	public Calan() {}
	
	public Calan(String date, String despatchMain,String despatchBraket, Set<Asset> assets) {
		this.date = date;
		this.assets = assets;
		this.despatchMain = despatchMain;
		this.despatchBraket = despatchBraket;
	}
	
	public String getDespatchMain() {
		return despatchMain;
	}

	public void setDespatchMain(String despatchMain) {
		this.despatchMain = despatchMain;
	}

	public String getDespatchBraket() {
		return despatchBraket;
	}

	public void setDespatchBraket(String despatchBraket) {
		this.despatchBraket = despatchBraket;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Set<Asset> getAssets() {
		return assets;
	}

	public void setAssets(Set<Asset> assets) {
		this.assets = assets;
	}
}
