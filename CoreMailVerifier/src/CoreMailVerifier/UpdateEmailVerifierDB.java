package CoreMailVerifier;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateEmailVerifierDB {
	 // JDBC driver name and database URL
	   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://159.203.239.91/bluemix";

	   //  Database credentials
	   static final String USER = "remoteroot";
	   static final String PASS = "lytepoleAnalytics";
	   
	   Connection conn = null;
	   
	   
	   public Connection getDBConnection(){
		   
		   if (conn == null){
			   
			   //System.out.println("(UpdateEmailVerifierDB:getDBConnection)Connection do not exists - Creating Connection:"+DB_URL);
			   try{
				   Class.forName("com.mysql.jdbc.Driver");
				   conn = DriverManager.getConnection(DB_URL,USER,PASS);
			   }catch(Exception e){
				   System.out.println("(UpdateEmailVerifierDB:getDBConnection)Unable to connect to Data Base:"+DB_URL);
				   System.out.println("(UpdateEmailVerifierDB:getDBConnection)Error:"+e.getMessage());
			   }
		   }
		   return conn;
	   }
	   
	   
	   private java.sql.Timestamp getCurrentTimeStamp() {
				java.util.Date today = new java.util.Date();
				return new java.sql.Timestamp(today.getTime());
	   }	
	   
	   
	   
	   public void insertEmailIntoDB(EmailVO evo){
		   
		   
		   
		   try{
			      
			   	  conn = this.getDBConnection();
			      //System.out.println("(UpdateEmailVerifierDB:insertEmailIntoDB) Inserting into emails_List table:"+evo.getEmail());
			      
			      String insertQuery = "INSERT INTO emails_list"
			      			+ "(id,smtp_check,score,mx_found, free, Name, Domain, api_response, email,is_valid,created_date,EmailCollectedDate,EmailCollectionSource,EmailVerifiedDate,ExternalId)"
			      			+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			      	
			      
			      PreparedStatement ps = conn.prepareStatement(insertQuery);
				  ps.setString(1,evo.getId()+"");
				  ps.setString(2,evo.getSMTPCheck().toString());
				  ps.setString(3,evo.getScore()+"");
				  ps.setString(4, evo.getMXFound().toString());
				  ps.setString(5, evo.getFree().toString());
				  ps.setString(6,evo.getName().toString());
				  ps.setString(7, evo.getDomain());
				  ps.setString(8,evo.getApiResponse());
				  ps.setString(9,evo.getEmail());
				  ps.setString(10,  evo.getIsValid().toString());    
				  ps.setTimestamp(11, this.getCurrentTimeStamp());
				  ps.setTimestamp(12, evo.getLyteEmailsCreatedDate());
				  ps.setString(13, evo.getEmailCollectionSource());
				  ps.setTimestamp(14, this.getCurrentTimeStamp());
				  ps.setString(15, evo.getExternalId());
				  
				    
				  ps.executeUpdate();
				  ps.close();
			      
				  }catch(Exception e){
					   System.out.println("(UpdateEmailVerifierDB:insertEmailIntoDB)Error Trace:"+e.getStackTrace());
					   System.out.println("(UpdateEmailVerifierDB:insertEmailIntoDB)Error Message:"+e.getMessage());
				  }
	   }
	   
	   
	   public int isEmailVerifiedAlreadyCheckDB(String checkEmailAddress) {   
		   
		   int resultSetCounter = 0;
		   try{
			   conn = this.getDBConnection();   
			   String sql;
			   sql = "SELECT email FROM emails_list WHERE email = ?";
			   
			   	  PreparedStatement ps = conn.prepareStatement(sql);
				  ps.setString(1,checkEmailAddress);
				  ResultSet rs = ps.executeQuery();
				  
				  while(rs.next()){
					  
					  String email = rs.getString("email");
					  //System.out.println("(UpdateEmailVerifierDB:isEmailVerifiedAlreadyCheckDB) Email Already Verified in emails_list:"+email);	
					  resultSetCounter++;
				  }
				
				  ps.close();
				  
	
		   }catch(Exception e){
			   System.out.println("(UpdateEmailVerifierDB:isEmailVerifiedAlreadyCheckDB)Error:"+e.toString());
		   }
		   		return resultSetCounter;
	   }
	   

	   public void updateEmailVerifiedAlreadyCheckDB(String checkEmailAddress) {   
		  
		   try{
			   conn = this.getDBConnection();
			   
			   //System.out.println("(UpdateEmailVerifierDB:updateEmailVerifiedAlreadyCheckDB) Email Address Verified - Updating emails_list:"+checkEmailAddress);
			   String sql = "update emails_list set EmailVerifiedDate = ? where email = ?";

		   	   PreparedStatement ps = conn.prepareStatement(sql);
			   ps.setTimestamp(1, this.getCurrentTimeStamp());
			   ps.setString(2,checkEmailAddress);
			 
			   ps.executeUpdate();
			   ps.close();
			   
	
		   }catch(Exception e){
			   System.out.println("(UpdateEmailVerifierDB:updateEmailVerifiedAlreadyCheckDB)Error:"+e.toString());
		   }
		   		
	   }
	   
	   
	   
	   public void queryLyteEmailsDB() {
		   
		   Statement stmt = null;
		   try{
			   
			   conn = this.getDBConnection();
			   stmt = conn.createStatement();
			   String sql1 = "select email, EmailCollectionSource from emails_list where EmailVerifiedDate = '0000-00-00 00:00:00'";
			   stmt.setFetchSize(2);
			   
			   ResultSet rs = stmt.executeQuery(sql1);
			   EmailVO evo = new EmailVO();
			   
			   CoreMailVerify cmf = new CoreMailVerify();
			   int recordProcessed = 0;
			   
			   int totalNumberofEmailsQueried = 0;
			   int totalNumberofEmailsProcessed = 0;
			   int totalNumberofEmailsSkipped = 0;
			   
			
			   FileWrite logWriter = new FileWrite();
			   
			   while(rs.next()){
		
				   String email = rs.getString("email");
				   Pattern pm = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
				   Matcher m = pm.matcher(email);
				   totalNumberofEmailsQueried++;
				   
				   if(m.find()){
					   System.out.print("(UpdateEmailVerifierDB:queryLyteEmailsDB) Extracted Email:"+ m.group());
					   evo.setExtractedEmail(m.group());
				   	   email = m.group();
				   }
				   else{
					   System.out.println("UpdateEmailVerifierDB:queryLyteEmailsDB) Extracted Email:NA");
					   email = "NA";
					   evo.setExtractedEmail("NA");
				   }
				   
				   String EmailCollectionSource = rs.getString("EmailCollectionSource");
				   if(this.isEmailVerifiedAlreadyCheckDB(email) > 0){
					   
					   System.out.print(" EmailCollectionSource:"+EmailCollectionSource);
					   System.out.print(" Email Exists ");
					   
					   evo.setEmailAlreadyVerified("True");
					   evo.setLyteEmailCollectionSource("emails_list");
					   evo.setEmailVerificationCompleted("False");
					   evo.setFromEmailCollectionSource(EmailCollectionSource);
					   
					   
					   this.updateEmailVerifiedAlreadyCheckDB(email);
					   totalNumberofEmailsSkipped++;
				   }else{
					   
					   System.out.print(" Email Not Exists ");
					   evo = cmf.callEmailVerifier(email);
					   evo.setExternalId(rs.getString("email"));
					   this.updateEmailVerifiedAlreadyCheckDB(email);
					   System.out.print(" Email verification completed ");
					   
					   totalNumberofEmailsProcessed++;
					   
					   evo.setEmailAlreadyVerified("False");
					   evo.setLyteEmailCollectionSource("emails_list");
					   evo.setEmailVerificationCompleted("True");
					   evo.setFromEmailCollectionSource(EmailCollectionSource);
					   
					   
				   }
				   
				   logWriter.writeFile(evo);
				   System.out.println("Records Processed:"+recordProcessed++);
			
			   }
			   
			   System.out.println("************Completed processing all emails**************");
			   System.out.println("Total Number of Emails Queried:"+totalNumberofEmailsQueried);
			   System.out.println("Total Number of Emails Processed:"+totalNumberofEmailsProcessed);
			   System.out.println("Total Number of Emails Skipped:"+totalNumberofEmailsSkipped);
			   System.out.println("************End Processing All Emails********************");
			   
			   
			   rs.close();
			   stmt.close();
			   //conn.close();
	  
		   }catch(Exception e){
			   System.out.println("Error Querying LyteEmails Table."+e.toString());
		   }

	   } 
	   


}
