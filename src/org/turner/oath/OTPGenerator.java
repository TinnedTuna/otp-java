package org.turner.oath;

/**
 *
 * @author turner
 */
public interface OTPGenerator {
  
  public String generateOtp(OTPSecretState secretState, int length);
  
}
