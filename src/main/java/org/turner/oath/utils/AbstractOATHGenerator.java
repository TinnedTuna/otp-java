package org.turner.oath.utils;

import javax.crypto.Mac;
import org.turner.oath.OATHGenerator;
import org.turner.oath.OATHSecretState;

/**
 *
 * @author turner
 */
public abstract class AbstractOATHGenerator implements OATHGenerator {

  private final Mac mac;

  public AbstractOATHGenerator(Mac mac) {
    assert mac != null;
    this.mac = mac;
  }
  
  protected abstract long getInternalState(final OATHSecretState secretState);
  
  @Override
  public String generateOtp(final OATHSecretState secretState) {
    assert secretState != null;
    
    byte[] macOutput = OATHUtils.macBytes(
            mac,
            secretState.getSecret(), 
            getInternalState(secretState));
    return OATHUtils.integerify(
            OATHUtils.truncateBytes(macOutput),
            secretState.getLength());
  }
  
}
