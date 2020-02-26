package com.naver.pubtrans.itn.api.common;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 비밀번호 암호화 클래스
 * @author westwind
 *
 */

@Component
public class PasswordEncoding implements PasswordEncoder {
	
   private final BCryptPasswordEncoder bCryptpasswordEncoder;
   
   public PasswordEncoding() {
      this.bCryptpasswordEncoder = new BCryptPasswordEncoder();
   }
   
   
   /**
    * 비밀번호를 암호화한다.
	* @param rawPassword
    */
   @Override
   public String encode(CharSequence rawPassword) {
      return bCryptpasswordEncoder.encode(rawPassword);
   }
   

   /**
    * 로그인 시 입력한 비밀번호와 암호화된 비밀번호를 비교한다. 
	* @param rawPassword
	* @param encodedPassword
	* @return
    */
   @Override
   public boolean matches(CharSequence rawPassword, String encodedPassword) {
      return bCryptpasswordEncoder.matches(rawPassword, encodedPassword);
   }
   
}
