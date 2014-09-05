package org.turner.opie;

/**
 *
 * @author turner
 */
public interface OPIEGenerator {
  
  public String generateOPIEString(OPIESecretState opieSecretState);
  public byte[] generateOPIEBytes(OPIESecretState opieSecretState);
  
}
