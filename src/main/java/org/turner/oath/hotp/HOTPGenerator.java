package org.turner.oath.hotp;

import javax.crypto.Mac;

import org.turner.oath.utils.AbstractOATHGenerator;
import org.turner.oath.utils.OATHUtils;

/**
 * A generator for HMAC-based OTPs, as per RFC4226.
 *
 * @author turner
 * @since 1.0
 */
public class HOTPGenerator extends AbstractOATHGenerator<HOTPSecretState> {

  /**
   * Create a new OATH generator.
   *
   * @param macAlgorithm The MAC instance to use for generating OTPs.
   */
  public HOTPGenerator(final Mac macAlgorithm) {
    super(macAlgorithm);
  }

  /**
   * Extract the internal counter of an HOTP state and convert it to an array
   * of bytes with length 8.
   *
   * @param secretState The OATHSecretState used to derive the internal state.
   * @return The internal
   */
  protected final byte[] getInternalState(final HOTPSecretState secretState) {
    assert secretState instanceof HOTPSecretState;
    HOTPSecretState hotpSecretState = secretState;
    return OATHUtils.longBytes(hotpSecretState.getCounter());
  }
}
