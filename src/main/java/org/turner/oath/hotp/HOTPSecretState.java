package org.turner.oath.hotp;

import org.turner.oath.utils.AbstractOATHSecretState;

/**
 *
 * @author turner
 * @since 1.0
 */
public class HOTPSecretState extends AbstractOATHSecretState {

  /** The counter for how many OTPs this state has generated. */
  private final long otpsGenerated;

  public HOTPSecretState(
          final byte[] secret,
          final int length,
          final long counter) {
    super(secret, length);
    assert counter >= 0;
    this.otpsGenerated = counter;
  }

  /**
   * The number of OTPs this state has generated.
   *
   * @return The number of OTPs this state has generated.
   */
  public final long getOtpsGenerated() {
    return this.otpsGenerated;
  }
}
