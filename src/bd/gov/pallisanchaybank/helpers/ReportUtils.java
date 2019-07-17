package bd.gov.pallisanchaybank.helpers;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Set;

import bd.gov.pallisanchaybank.model.Asset;
import bd.gov.pallisanchaybank.model.Branch;
import bd.gov.pallisanchaybank.model.Calan;

public class ReportUtils {
	
	public static void createReport(Branch branch, Calan calan) {
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
			
		}
		
	}
	
	private static String toBangla(String input){
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
}
