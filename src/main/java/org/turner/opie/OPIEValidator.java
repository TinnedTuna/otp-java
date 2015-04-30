package org.turner.opie;

import org.turner.opie.utils.OPIEUtils;
import org.turner.opie.utils.WordEncoder;

/**
 * A simple static validator for working out if provided OPIE one-time
 * passwords are valid.
 *
 * @author turner
 * @since 1.0
 */
public final class OPIEValidator {

  /** Private constructor, should never be called. */
  private OPIEValidator() {
    throw new IllegalStateException("Attempted to instantiate a utility "
        + "class.");
  }

  /**
   * Validates an OPIE one-time password.
   *
   * @param userSuppliedOPIE The OPIE OTP supplied by the user.
   * @param secretState The secret state of attached to this user.
   * @param opieGenerator A generator which generates OPIE OTPs.
   * @return true, iff the userSuppliedOPIE OTP is valid.
   */
  public static boolean validateOPIE(
          final String userSuppliedOPIE,
          final OPIESecretState secretState,
          final OPIEGenerator opieGenerator) {
    assert userSuppliedOPIE != null;
    assert secretState != null;
    assert opieGenerator != null;
    byte[] generatedOPIE = opieGenerator.generateOPIEBytes(secretState);
    return OPIEUtils.constantTimeEquals(
        generatedOPIE,
        WordEncoder.decode(userSuppliedOPIE));
  }
}
