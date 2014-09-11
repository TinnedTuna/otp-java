package org.turner.oath;

/**
 *
 * @author turner
 */
public interface OATHGenerator {
  
  public String generateOtp(OATHSecretState secretState);
  
}
