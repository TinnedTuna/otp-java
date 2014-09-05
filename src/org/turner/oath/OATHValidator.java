package org.turner.oath;

/**
 *
 * @author turner
 */
public interface OATHValidator {
  
  public boolean validateOtp(String otp, OATHSecretState secretState, OATHGenerator oathGenerator);
  
}
