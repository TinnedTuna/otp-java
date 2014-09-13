package org.turner.oath.totp;

import org.turner.oath.OATHSecretState;
import org.turner.oath.utils.AbstractOATHSecretState;

/**
 *
 * @author turner
 */
public class TOTPSecretState extends AbstractOATHSecretState implements OATHSecretState {
  
  private final long timeStepSeconds;
  private final long initialUnixTime;
  private final long currentUnixTime;
  
  public TOTPSecretState(
          final byte[] secret, 
          final int length, 
          final long timeStepSeconds, 
          final long initialUnixTime, 
          final long currentUnixTime) {
    super(secret, length);
    assert timeStepSeconds > 0;
    assert initialUnixTime >= 0;
    this.timeStepSeconds = timeStepSeconds;
    this.initialUnixTime = initialUnixTime;
    this.currentUnixTime = currentUnixTime;
  }

  public long getCurrentTimeStep() {
    return (currentUnixTime - initialUnixTime) / timeStepSeconds;
  }
  
}
