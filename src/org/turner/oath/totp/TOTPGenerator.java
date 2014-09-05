package org.turner.oath.totp;

import java.security.InvalidKeyException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.turner.oath.OTPGenerator;
import org.turner.oath.OTPSecretState;
import org.turner.oath.utils.OathUtils;

/**
 *
 * @author turner
 */
public class TOTPGenerator implements OTPGenerator {

  private final Mac mac;

  public TOTPGenerator(Mac mac) {
    assert mac != null;
    this.mac = mac;
  }
  
  @Override
  public String generateOtp(final OTPSecretState secretState, final int length) {
    assert secretState != null;
    assert secretState instanceof TOTPSecretState;
    
    TOTPSecretState totpSecretState = (TOTPSecretState) secretState;
    
    byte[] macOutput = mac(
            totpSecretState.getSecret(), 
            totpSecretState.getCurrentTimeStep());
    return OathUtils.integerify(
            OathUtils.truncateBytes(macOutput),
            length);
  }
  
  private byte[] mac(final byte[] secretKey, final long currentTimeStep) {
    try {
      mac.init(new SecretKeySpec(secretKey, "RAW"));
      byte[] timeStepBytes = OathUtils.longBytes(currentTimeStep);
      return mac.doFinal(timeStepBytes);
    } catch (InvalidKeyException ex) {
      throw new IllegalStateException();
    }
  }
  
}
