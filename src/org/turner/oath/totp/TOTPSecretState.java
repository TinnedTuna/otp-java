package org.turner.oath.totp;

import org.turner.oath.OATHSecretState;
import org.turner.oath.utils.AbstractOATHSecretState;

/**
 *
 * @author turner
 */
public class TOTPSecretState extends AbstractOATHSecretState implements OATHSecretState {
  
  private final long timeStepSeconds;
  private final long initialTime;
  private final long currentTime;
  
  public TOTPSecretState(byte[] secret, int length, long timeStepSeconds, long initialTime, long currentTime) {
    super(secret, length);
    assert timeStepSeconds > 0;
    assert initialTime >= 0;
    assert currentTime > initialTime;
    this.timeStepSeconds = timeStepSeconds;
    this.initialTime = initialTime;
    this.currentTime = currentTime;
  }

  public long getCurrentTimeStep() {
    return (currentTime - initialTime) / timeStepSeconds;
  }
  
}
