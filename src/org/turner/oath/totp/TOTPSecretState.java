package org.turner.oath.totp;

import org.turner.oath.OTPSecretState;
import org.turner.oath.utils.AbstractOTPSecretState;

/**
 *
 * @author turner
 */
public class TOTPSecretState extends AbstractOTPSecretState implements OTPSecretState {
  
  private final long timeStepSeconds;
  private final long initialTime;
  private final long currentTime;
  
  public TOTPSecretState(byte[] secret, long timeStepSeconds, long initialTime, long currentTime) {
    super(secret);
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
