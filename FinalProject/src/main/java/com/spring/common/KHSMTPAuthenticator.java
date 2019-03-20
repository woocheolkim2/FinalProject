package com.spring.common;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class KHSMTPAuthenticator extends Authenticator {
	
	@Override
	public PasswordAuthentication getPasswordAuthentication() { 
	
		// Gmail 인 경우 @gmail.com 을 제외한 아이디만 입력한다.
		return new PasswordAuthentication("godls3575","RHKRdhwlddj1234");
	}
}
