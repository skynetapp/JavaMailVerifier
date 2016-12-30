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
	   
	   
	   private java.sql.Timestamp getCurrentTimeStamp() {
				java.util.Date today = new java.util.Date();
				return new java.sql.Timestamp(today.getTime());
		}	
	   
	   public void insertEmailIntoDB(EmailVO evo){
		   
		   Connection conn = null;
		   
		   try{
			      Class.forName("com.mysql.jdbc.Driver");
			      System.out.println("Connecting to database bluemix@159.203.239.91");
			      conn = DriverManager.getConnection(DB_URL,USER,PASS);
			      System.out.println("Connected to database bluemix@159.203.239.91");
			      System.out.println("Inserting into Emails_List table:"+evo.getEmail());
			      
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
					   System.out.println("Error in UpdateEmailVerifiedDB:"+e.getStackTrace());
					   System.out.println("Error in UpdateEmailVerifiedDB:"+e.getMessage());
				  }
	   }
	   
	   
	   public int isEmailVerifiedAlreadyCheckDB(String checkEmailAddress) {   
		   Connection conn = null;
		   int resultSetCounter = 0;
		   try{
			   Class.forName("com.mysql.jdbc.Driver");
			   System.out.println("Connecting to database...");
			   conn = DriverManager.getConnection(DB_URL,USER,PASS);

			   System.out.println("Creating SQL Statement in isEmailVerifiedAlreadyCheckDB Method.");
			  
			   String sql;
			   sql = "SELECT email FROM emails_list WHERE email = ?";
			   
			   	  PreparedStatement ps = conn.prepareStatement(sql);
				  ps.setString(1,checkEmailAddress);
				  ResultSet rs = ps.executeQuery();
				  
				  while(rs.next()){
					  
					  String email = rs.getString("email");
					  System.out.println("Email Found in isEmailVerifiedAlreadyCheckDB:"+email);	
					  resultSetCounter++;
				  }
				
				  ps.close();
				  conn.close();
	
		   }catch(Exception e){
			   System.out.println("Error Querying:"+e.toString());
		   }
		   		return resultSetCounter;
	   }
	   

	   public void updateEmailVerifiedAlreadyCheckDB(String checkEmailAddress) {   
		   Connection conn = null;
		   try{
			   Class.forName("com.mysql.jdbc.Driver");
			   System.out.println("Connecting to database...");
			   conn = DriverManager.getConnection(DB_URL,USER,PASS);

			   System.out.println("Creating SQL Statement in isEmailVerifiedAlreadyCheckDB Method.");
			  
			   String sql;
			   //sql = "SELECT email FROM emails_list WHERE email = ?";
			   System.out.println("Updating the email Address:"+checkEmailAddress);
			   sql = "update emails_list set EmailVerifiedDate = ? where email = ?";

		   	   PreparedStatement ps = conn.prepareStatement(sql);
			   ps.setTimestamp(1, this.getCurrentTimeStamp());
			   ps.setString(2,checkEmailAddress);
			 
			   ps.executeUpdate();
			   ps.close();
			   conn.close();
	
		   }catch(Exception e){
			   System.out.println("Error Updating the Email Address:"+e.toString());
		   }
		   		
	   }
	   
	   
	   
	   public void queryLyteEmailsDB() {
		   Connection conn = null;
		   Statement stmt = null;
		   try{
			   
			   System.out.println("Entering method Query Lyte Emails DB.");
			   Class.forName("com.mysql.jdbc.Driver");
			   System.out.println("Connecting to database...");
			   conn = DriverManager.getConnection(DB_URL,USER,PASS);
			   System.out.println("Connected to database...");

			   stmt = conn.createStatement();
			   String sql1;
			   
			   //sql1 = "select from1, idLyteEmails, CreatedDate, LyteEmailsName from LyteEmails";
			   sql1 = "select from1, idLyteEmails, CreatedDate, LyteEmailsName from LyteEmails";
			   sql1 = "SELECT email, EmailCollectionSource FROM bluemix.emails_list where is_valid is null and EmailCollectionSource = 'Scribd'";
			   sql1 = "select email, EmailCollectionSource from emails_list where EmailVerifiedDate = '0000-00-00 00:00:00'";
   
			   stmt.setFetchSize(2);
			   ResultSet rs = stmt.executeQuery(sql1);
			   System.out.println("Executing Result Set.");

			   
			   EmailVO evo = new EmailVO();
			   CoreMailVerify cmf = new CoreMailVerify();
			   int recordProcessed = 0;
			   
			   System.out.println("Total Count:" + rs.getFetchSize());
			   
			   int totalNumberofEmailsQueried = 0;
			   int totalNumberofEmailsProcessed = 0;
			   int totalNumberofEmailsSkipped = 0;
			   
			   while(rs.next()){
		
				   //Retrieve by column name
				   //String email = rs.getString("From1");
				   String email = rs.getString("email");
				   Pattern pm = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
				   Matcher m = pm.matcher(email);
				   totalNumberofEmailsQueried++;
				   if(m.find()){
					   System.out.println("Extracted Email:"+m.group());
				   	   email = m.group();
				   }
				   else{
					   System.out.println("No Email found in the String.");
					   email = "NA";
				   }
				   
				   if(this.isEmailVerifiedAlreadyCheckDB(email) > 0){
					   String EmailCollectionSource = rs.getString("EmailCollectionSource");
					   System.out.println("EmailCollectionSource:"+EmailCollectionSource);
					   System.out.println("Email Already exists:"+email+": Skipping verification.");
					   this.updateEmailVerifiedAlreadyCheckDB(email);
					   totalNumberofEmailsSkipped++;
				   }else{
					   
					   System.out.println("Email is not verified:"+email+": Sending for verification.");
					   evo = cmf.callEmailVerifier(email);
					   //evo.setId(rs.getInt("idLyteEmails"));
					   evo.setExternalId(rs.getString("email"));
					   //evo.setLyteEmailsCreatedDate(rs.getTimestamp("CreatedDate"));
					   //evo.setLyteEmailCollectionSource(rs.getString("LyteEmailsName"));
					   evo.setLyteEmailCollectionSource("emails_list");
					   //this.insertEmailIntoDB(evo);
					   this.updateEmailVerifiedAlreadyCheckDB(email);
					   System.out.println("Email verification completed:"+email);
					   totalNumberofEmailsProcessed++;
				   }
				   System.out.println("Records Processed:"+recordProcessed++);
			
			   }
			   
			   System.out.println("Total Number of Emails Queried:"+totalNumberofEmailsQueried);
			   System.out.println("Total Number of Emails Processed:"+totalNumberofEmailsProcessed);
			   System.out.println("Total Number of Emails Skipped:"+totalNumberofEmailsSkipped);
			   
			   rs.close();
			   stmt.close();
			   conn.close();
	  
		   }catch(Exception e){
			   System.out.println("Error Querying LyteEmails Table."+e.toString());
		   }

	   } 
	   


}
