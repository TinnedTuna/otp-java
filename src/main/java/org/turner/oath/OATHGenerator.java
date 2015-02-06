package org.turner.oath;

/**
 *
 * @author turner
 */
public interface OATHGenerator<T extends OATHSecretState> {
  
  public String generateOtp(T secretState);
  
}
