package bd.gov.pallisanchaybank.model;

import java.util.List;
import java.util.Set;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.Iterator; 
import org.hibernate.HibernateException; 
import org.hibernate.Session; 
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;


public class BranchUtils {
	private static SessionFactory factory; 
	
	static{
		factory = new Configuration().configure().buildSessionFactory();
	}

	private static void addBranch() throws Exception{
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		InputStream inputStream = BranchUtils.class.getResourceAsStream("/bd/gov/pallisanchaybank/res/contacts.csv");
		Reader reader = new InputStreamReader(inputStream, "UTF-8");
		BufferedReader bufferedReader = new BufferedReader(reader);
		String line = null;
		while ((line = bufferedReader.readLine()) != null){
			System.out.println(line);
			String[] parts = line.split(",");
			String district = parts[0].trim();
			String upzilla = parts[1].trim();
			String branchName = parts[2].trim();
			String branchCode = parts[3].trim();
			String mobile = parts[4].trim();
			
			Branch branch = new Branch();
			branch.setBranchCode(Integer.parseInt(branchCode));
			branch.setBranchName(branchName);
			branch.setUpzilla(upzilla);
			branch.setMobileNumber(mobile);
			branch.setDistrict(district);
			session.save(branch);
		}
		tx.commit();
		bufferedReader.close();
	}
	
	public static Branch getBranchBy(int branchCode){
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			List<Branch> branches = session.createCriteria(Branch.class).list(); 
			for (Iterator<Branch> iterator = branches.iterator(); iterator.hasNext();){
				Branch branch = (Branch) iterator.next(); 
				if(branch.getBranchCode() == branchCode){
					return branch;
				}
			}
			tx.commit();
		} catch (HibernateException e) {
		} finally {
			session.close();
		}
		return null;
	}
	
	public static void openSession(){
		Session session = factory.openSession();
	}
	
	public static void update(Object object){
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		session.update(object);
		tx.commit();
	}
	
	public static void addDemoUser(){
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			
			Set<Asset> assets = new HashSet<Asset>();
			
			Asset asset1 = new Asset();
			asset1.setAssetName("CPU");
			asset1.setSerial("891234687");
			assets.add(asset1);
			
			Asset asset2 = new Asset();
			asset2.setAssetName("Monitor");
			asset2.setSerial("3578912413");
			assets.add(asset2);
			
			Calan calan1 = new Calan("13/07/2019", "5375", null, assets);
			Set<Calan> calans = new HashSet<Calan>();
			calans.add(calan1);
			
			Branch branch = new Branch(1001, "Head Office", "Dhaka", "Dhaka", "01938878958", calans);
			
			
			session.save(branch);
			tx.commit();
		} catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		} finally {
			session.close(); 
		}
	}
	
	public static void main(String[] args) throws Exception {
		addBranch();
	}
}