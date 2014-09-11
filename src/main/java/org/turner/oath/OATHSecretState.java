package org.turner.oath;

/**
 *
 * @author turner
 */
public interface OATHSecretState {
  
  public byte[] getSecret();
  public int getLength();
  
}
