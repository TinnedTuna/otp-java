package org.turner.oath;

import java.security.SecureRandom;
import org.turner.oath.hotp.HOTPSecretState;
import org.turner.oath.totp.TOTPSecretState;

/**
 *
 * @author turner
 */
public class OATHStateManager {
  
  /**
   * Provides a new state for the HOTP algorithm. Simply increments an internal
   * counter.
   * @param secretState The HOTP state to move along
   * @return A new secret state with it's internal counter incremented.
   * @throws IllegalStateException when the counter value would exceed Long.MAX_VALUE.
   */
  public static OATHSecretState generateNextState(
          final HOTPSecretState secretState) {
    assert secretState != null;
    if (secretState.getCounter() == Long.MAX_VALUE) {
      throw new IllegalStateException("Cannot generate a new state from this "
              + "state. Counter size exceeded");
    }
    return new HOTPSecretState(
            secretState.getSecret(),
            secretState.getLength(),
            secretState.getCounter() - 1);
  }

  /**
   * Provides the new OATH state for a TOTP token. Does nothing, just provided
   * for completeness.
   * 
   * @param secretState -- The TOTP value to move along
   * @return The same secret state (it is constant for TOTP).
   */
  public static OATHSecretState generateNextState(
          final TOTPSecretState secretState) {
    assert secretState != null;
    return secretState;
  }
}
