package CoreMailVerifier;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Created By: Kesavan Jay
 * Purpose: This class provides a set of functions to update the EmailVerifierDB. The email
 * verifier DB is the master system of records which stores all validated email.
 *
 */




public class UpdateEmailVerifierDB {
	
	   
	   //Dev Notes: These credentials have to moved to a config file in the future.
	   // JDBC driver name and database URL
	   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://159.203.239.91/bluemix";

	   //  Database credentials
	   static final String USER = "remoteroot";
	   static final String PASS = "lytepoleAnalytics";
	   
	   Connection conn = null;
	   
	   /*
	    * Function: getDBConnection()
	    * This function connects to the DataBase and returns a connection object.
	    * 
	    * @param N/A
	    * @returns Connection
	    * 
	    */
	   
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
	   
	   /*
	    * This function is an utility to return the current System timestamp. 
	    * Dev Notes: This utility should be moved to a Utility class in the future.
	    * 
	    * @param N/A
	    * @returns Timestamp
	    * 
	    */
	   
	   
	   private java.sql.Timestamp getCurrentTimeStamp() {
				java.util.Date today = new java.util.Date();
				return new java.sql.Timestamp(today.getTime());
	   }	
	   
	   
	   /*
	    * This function creates a new record in the EmailVerifier DB.  
	    * This function takes a Email Value Object (EmailVO.java).
	    * Dev Notes: This function should return a success or failure after insertion for good error checking.
	    * This function should also throw the error instead of catching to make this function global and to
	    * make sure it can be used everywhere.
	    * 
	    * @param EmailVO 
	    * @returns N/A
	    * 
	    */
	   
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
	   
	   /*
	    * This function takes an email address as input and checks if the email already exists in the Database.
	    * 
	    * @param emailAddress The email address the needs to be checked in the database
	    * @returns resultSetCounter The number of times the email is present in the database.
	    * 
	    */
	   
	   
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
	   
	   /*
	    * This function takes an email address and updates the Email Verified Date of the record with the
	    * system timestamp. This is essentially done to assign a date to the email address and check when was the last time this email was
	    * validated.
	    * 
	    * Dev Notes: This function return a success or failure after updating timestamp.
	    * 
	    * @param emailAddress The email address whose date needs to be updated.
	    * @returns N/A 
	    * 
	    */
	   

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
			   
			   //Testing Git Comments. More testing
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
			   System.out.println("************End processing all Emails********************");
			   
			   
			   rs.close();
			   stmt.close();
			   //conn.close();
	  
		   }catch(Exception e){
			   System.out.println("Error Querying LyteEmails Table."+e.toString());
		   }

	   } 
	   


}
