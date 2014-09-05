package org.turner.oath.hotp;

import org.turner.oath.utils.AbstractOATHSecretState;

/**
 *
 * @author turner
 */
public class HOTPSecretState extends AbstractOATHSecretState {
  
  private final long counter;
  
  public HOTPSecretState(byte[] secret, int length, long counter) {
    super(secret, length);
    assert counter > 0;
    this.counter = counter;
  }
  
  public long getCounter() {
    return this.counter;
  }
  
}
