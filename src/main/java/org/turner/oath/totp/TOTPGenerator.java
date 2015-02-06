package org.turner.oath.totp;

import javax.crypto.Mac;
import org.turner.oath.utils.AbstractOATHGenerator;
import org.turner.oath.utils.OATHUtils;

/**
 * A generator for time-based OTPs, as per RFC6238.
 *
 * @author turner
 * @since 1.0
 */
public class TOTPGenerator extends AbstractOATHGenerator<TOTPSecretState> {

  /**
   * Create a new TOTP OATH generator.
   *
   * @param macAlgorithm The MAC instance to use for generating OTPs.
   */
  public TOTPGenerator(final Mac macAlgorithm) {
    super(macAlgorithm);
  }

  /**
   * Extract the current time step value, use it as a long, and convert it into
   * a byte representation.
   *
   * @param secretState The OATHSecretState used to derive the internal state.
   * @return The time-step value, represented as a byte array of length 8.
   */
  protected final byte[] getInternalState(final TOTPSecretState secretState) {
    assert secretState instanceof TOTPSecretState;
    TOTPSecretState totpSecretState = secretState;
    return OATHUtils.longBytes(totpSecretState.getCurrentTimeStepValue());
  }
}
