package CoreMailVerifier;

/*
 * Created By: Kesavan Jay
 * Purpose: This is primary Email Value Object. This Object is used to return results
 * from the REST API call and also to write Email Object to database along with various
 * other places.
 * 
 */


public class EmailVO {
	private int id;
	private String email;
	private String api_response;
	private Boolean is_valid;
	private Boolean smtp_check;
	private float score;
	private Boolean mx_found;
	private Boolean free;
	private String name;
	private String domain; 
	private java.sql.Timestamp createdDate;
	private String emailCollectionSource;
	private String externalId;
	
	private String extractedEmail;
	private String emailAlreadyVerified;
	private String emailVerificationCompleted;
	private int numEmailsProcessed;
	private String emailFromCollectionSource;

	
	public void setAPIResponse(String apiResponse){
		this.api_response = apiResponse;
	}
	
	public String getApiResponse(){
		return this.api_response;
	}
	
	
	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
	
	public String getEmail(){
		return email;
	}
	
	public void setIsValid(Boolean isValid){
		this.is_valid = isValid;
	}
	
	public Boolean getIsValid(){
		return is_valid;
	}
	
	
	public void setSMTPCheck(Boolean smtp_check){
		this.smtp_check = smtp_check;
	}
	
	public Boolean getSMTPCheck(){
		return smtp_check;
	}
	
	public void setScore(float score){
		this.score = score;
	}
	
	public float getScore(){
		return this.score;
	}
	
	public void setMXFound(Boolean mx_found){
		this.mx_found = mx_found;
	}
	
	public Boolean getMXFound(){
		return this.mx_found;
	}
	
	public void setFree(Boolean free){
		this.free = free;
	}
	
	public Boolean getFree(){
		return this.free;
	}

	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	
	public void setDomain(String domain){
		this.domain = domain;
	}
	
	public String getDomain(){
		return this.domain;
	}
	
	public void setLyteEmailsCreatedDate(java.sql.Timestamp createdDate){
		this.createdDate = createdDate;
	}
	
	public java.sql.Timestamp getLyteEmailsCreatedDate(){
		return this.createdDate;
	}
	
	public void setLyteEmailCollectionSource(String emailCollectionSource){
		this.emailCollectionSource = emailCollectionSource;
	}
	
	public String getLyteEmailCollectionSource(){
		return this.emailCollectionSource;
	}
	
	
	public String getEmailCollectionSource(){
		return this.emailCollectionSource;
	}
	
	public void setExternalId(String externalId){
		this.externalId = externalId;
	}
	
	public String getExternalId(){
		return this.externalId;
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
		
		return this.emailAlreadyVerified;
		
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
	
	public void setFromEmailCollectionSource(String value){
		
		this.emailFromCollectionSource = value; 
		
	}
	
	public String getFromEmailCollectionSource(){
		
		return this.emailFromCollectionSource; 
	
	}

}
