package CoreMailVerifier;

public class CoreMailVerifyMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		CoreMailVerify cmf = new CoreMailVerify();
		//EmailVO evo = cmf.callEmailVerifier("jay@lytepole.com");
		//System.out.println("After Calling EmailVerifier:"+evo.getEmail());
		UpdateEmailVerifierDB eddb = new UpdateEmailVerifierDB();
		eddb.queryLyteEmailsDB();
		//eddb.isEmailVerifiedAlreadyCheckDB("jay@lytepole.com");
		
		//UpdateEmailVerifierDB emDB = new UpdateEmailVerifierDB();
		//emDB.queryMailDB();

	}

}
