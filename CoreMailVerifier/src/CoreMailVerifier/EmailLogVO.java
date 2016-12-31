package CoreMailVerifier;

public class EmailLogVO {

	private String emailFromCollectionSource;
	private String extractedEmail;
	private String emailAlreadyVerified;
	private String emailVerificationCompleted;
	private int numEmailsProcessed;
	
	
	public void setFromEmailCollectionSource(String value){
		
		this.emailFromCollectionSource = value; 
		
	}
	
	public String getFromEmailCollectionSource(){
		
		return this.emailFromCollectionSource; 
	
	}
	
	public void setExtractedEmail(String value){
		
		this.extractedEmail = value;
		
	}
	
	public String getExtractedEmail(){
		
		return this.extractedEmail;
		
	}
	
	public void setEmailAlreadyVerified(String value){
		
		this.emailAlreadyVerified = value;
		
	}
	
	public String getEmailAlreadyVerified(){
		
		return this.getEmailAlreadyVerified();
		
	}
	
	public void setEmailVerificationCompleted(String value){
		
		this.emailVerificationCompleted = value;
		
	}
	
	public String getEmailVerificationCompleted(){
		
		return this.emailVerificationCompleted;
		
	}
	
	public void setNumEmailProcessed(int value){
		
		this.numEmailsProcessed = value;
		
	}
	
	public int getNumEmailProcessed(){
		
		return this.numEmailsProcessed;
	
	}
	
	
	
	
	
}
