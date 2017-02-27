package CoreMailVerifier;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import java.io.*;


/*
 * Created By: Kesavan Jay
 * Purpose: This function calls the API Layer Verification REST API and returns 
 * the results.
 *
 */



public class CoreMailVerify {

	   /*
	    * This function called the API Layer Rest API with an Email Address
	    * and returns the full set of results. The full set of request and response
	    * can be found at https://mailboxlayer.com/documentation
	    * 
	    * Devnotes: This should return a success of failure if the rest APILayer.net is not 
	    * available
	    * 
	    * @param EmailAddress The email address that needs to be send to the REST API.
	    * @returns EmailVo A value object with the results from REST API Call.
	    * 
	    */
	   
	
	
	public EmailVO callEmailVerifier(String emailToCheck){
		
		
		System.out.println("Entering CallEmail Verifier Method.");
		EmailVO evo = new EmailVO(); 	
		
		try{
			
			  HttpClient httpclient = HttpClientBuilder.create().build();
		      String reqString = "https://apilayer.net/api/check?access_key=9628a27f5af5053c101877144296c68b&"+
		    		  			 "email=" + emailToCheck + "&format=1";
				
			  //HttpGet httpGetRequest = new HttpGet("https://apilayer.net/api/check?access_key=9628a27f5af5053c101877144296c68b&email=support@apilayer.com&format=1");
			  HttpGet httpGetRequest = new HttpGet(reqString);
		      
			  HttpResponse httpResponse = httpclient.execute(httpGetRequest);
		      HttpEntity entity = httpResponse.getEntity();
		      
		      InputStream instream = entity.getContent();
	          
	          byte[] buffer = new byte[1024];
	          
	          int bytesRead = 0;
	          BufferedInputStream bis = new BufferedInputStream(instream);
	          StringBuilder sb = new StringBuilder();
	          while ((bytesRead = bis.read(buffer)) != -1) {
	            String chunk = new String(buffer, 0, bytesRead);
	            //System.out.println(chunk);
	            sb.append(chunk);
	          }
		      
	          JsonObject jsonObj = Json.parse(sb.toString()).asObject();
	          String email = jsonObj.get("email").asString();
	          
	          String didyoumean = jsonObj.get("did_you_mean").asString();
	          String user1 = jsonObj.get("user").asString();
	          String domain = jsonObj.get("domain").asString();
	          Boolean format_valid = jsonObj.get("format_valid").asBoolean();
	          
	          Boolean mx_found;
	          
	          if(jsonObj.get("mx_found").isBoolean()){
	        	  mx_found = jsonObj.get("mx_found").asBoolean();  
	          }else{
	        	  mx_found = new Boolean("false");
	          }
	          
 	          Boolean smtp_check = jsonObj.get("smtp_check").asBoolean();
	          //String catch_all = jsonObj.get("catch_all").asString();
	          //Boolean role = jsonObj.get("role").asBoolean();
	          //Boolean disposable = jsonObj.get("disposable").asBoolean();
	          Boolean free = jsonObj.get("free").asBoolean();
	          float score = jsonObj.get("score").asFloat();
	          
	          evo.setAPIResponse(sb.toString());
	          evo.setDomain(domain);
	          evo.setEmail(email);
	          evo.setFree(free);
	          evo.setIsValid(format_valid);
	          evo.setMXFound(mx_found);
	          evo.setSMTPCheck(smtp_check);
	          evo.setFree(free);
	          evo.setScore(score);
		      evo.setName(user1);
		      
	          System.out.println("Exiting Call Email Verifier Method.");
	   	      
		      
		}catch(Exception e){
		    	  
			System.out.print("Error in method EmailVerifier:"+e.toString());
		}
		
		return evo;
		
	}
	
	
}
