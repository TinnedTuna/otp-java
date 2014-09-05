package org.turner.oath.hotp;

import org.turner.oath.utils.AbstractOTPSecretState;

/**
 *
 * @author turner
 */
public class HOTPSecretState extends AbstractOTPSecretState {
  private final int counter;
  
  public HOTPSecretState(byte[] secret, int counter) {
    super(secret);
    assert counter > 0;
    this.counter = counter;
  }
  
  public int getCounter() {
    return this.counter;
  }
  
}
