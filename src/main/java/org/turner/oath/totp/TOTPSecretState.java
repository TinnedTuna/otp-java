package org.turner.oath.totp;

import org.turner.oath.OATHSecretState;
import org.turner.oath.utils.AbstractOATHSecretState;

/**
 *
 * @author turner
 */
public class TOTPSecretState extends AbstractOATHSecretState implements OATHSecretState {
  
  private final long timeStepValue;
  
  public TOTPSecretState(
          final byte[] secret, 
          final int length, 
          final long timeStepValue) {
    super(secret, length);
    assert timeStepValue >= 0;
    this.timeStepValue = timeStepValue;
  }

  public long getTimeStepValue() {
    return timeStepValue;
  }
  
}
