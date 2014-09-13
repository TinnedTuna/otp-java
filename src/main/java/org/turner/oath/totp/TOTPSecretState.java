package org.turner.oath.totp;

import org.turner.oath.OATHSecretState;
import org.turner.oath.utils.AbstractOATHSecretState;

/**
 *
 * @author turner
 */
public class TOTPSecretState extends AbstractOATHSecretState implements OATHSecretState {
  
  private final long timeStep;
  private final long initialUnixTime;
  private final long currentUnixTime;
  
  public TOTPSecretState(
          final byte[] secret, 
          final int length, 
          final long initialUnixTime,
          final long currentUnixTime,
          final long timeStep) {
    super(secret, length);
    assert timeStep >= 0;
    assert initialUnixTime >= 0;
    assert currentUnixTime >= 0;
    this.timeStep = timeStep;
    this.initialUnixTime = initialUnixTime;
    this.currentUnixTime = currentUnixTime;
  }

  public long getCurrentTimeStepValue() {
    return (currentUnixTime - initialUnixTime) / timeStep;
  }
  
}
