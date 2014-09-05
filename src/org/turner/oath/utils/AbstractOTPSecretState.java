package org.turner.oath.utils;

import org.turner.oath.OTPSecretState;

/**
 *
 * @author turner
 */
public class AbstractOTPSecretState implements OTPSecretState {

  private final byte[] secret;

  public AbstractOTPSecretState(byte[] secret) {
    assert secret != null;
    this.secret = secret;
  }
  
  public byte[] getSecret() {
    return secret;
  }
  
}
