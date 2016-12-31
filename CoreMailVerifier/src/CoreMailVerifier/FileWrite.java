package CoreMailVerifier;

import java.io.BufferedWriter;
import java.io.FileWriter;


public class FileWrite {
	
	private String strLine;
	
    public void writeFile(EmailVO evo){
    	try{
    		
    		/*strLine = evo.getExtractedEmail() 
    		+ "|" + evo.getFromEmailCollectionSource() 
    		+ "|" + evo.getLyteEmailCollectionSource() 
    		+ "|" + evo.getEmailAlreadyVerified() 
    		+ "|" + evo.getEmailVerificationCompleted();*/
    		
    		StringBuilder sb = new StringBuilder(14);
            sb.append(evo.getExtractedEmail()).append("|");
            sb.append(evo.getFromEmailCollectionSource()).append("|");
            sb.append(evo.getLyteEmailCollectionSource()).append("|");
            sb.append(evo.getEmailAlreadyVerified()).append("|");
            sb.append(evo.getEmailVerificationCompleted()).append("|");
    		
    		FileWriter fstream1 = new FileWriter("C:\\Users\\jayakeshpk1\\EmailVerificationLog.csv",true);
    		BufferedWriter out = new BufferedWriter(fstream1);
    		out.write(sb.toString());
    		out.newLine();
    		out.close();
    	
    		}catch (Exception e){//Catch exception if any
    			System.out.println("Error printing log file" + e.getMessage());
    		}

    }
}

