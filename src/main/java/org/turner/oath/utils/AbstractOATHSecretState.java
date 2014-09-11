package org.turner.oath.utils;

import org.turner.oath.OATHSecretState;

/**
 *
 * @author turner
 */
public class AbstractOATHSecretState implements OATHSecretState {

  private final byte[] secret;
  private final int length;
  
  public AbstractOATHSecretState(byte[] secret, int length) {
    assert secret != null;
    assert length > 0;
    this.secret = secret;
    this.length = length;
  }
  
  public byte[] getSecret() {
    return secret;
  }

  public int getLength() {
    return length;
  }
}
