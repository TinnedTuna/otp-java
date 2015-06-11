package org.turner.opie.utils;

import java.security.MessageDigest;
import org.turner.opie.OPIEGenerator;
import org.turner.opie.OPIESecretState;

/**
 * Gives the basic structure for generating an OPIE one-time password; hash the
 * secret state, then "fold" it down to 64 bits. The folding is specific to the
 * hash.
 *
 * @author turner
 * @since 1.0
 */
public abstract class AbstractOPIEGenerator implements OPIEGenerator {

  /** The expected number of output bytes. */
  protected static final int EXPECTED_OUTPUT_LENGTH_BYTES = 8;

  /**
   * Takes an input stream of bytes, >64b length and folds it down to 64 bits.
   *
   * @param input The stream of bytes to fold down.
   * @return a 64b array of bytes.
   */
  public abstract byte[] foldTo64Bits(byte[] input);

  @Override
  public final byte[] generateOPIEBytes(
      final OPIESecretState opieSecretState) {
    assert opieSecretState != null;

    byte[] digested = initialStep(
            opieSecretState.getSeed(),
            opieSecretState.getSecret(),
            opieSecretState.getMessageDigest());

    if (opieSecretState.getHashCounts() == 0L) {
      return digested;
    }

    for (long i = 0; i < opieSecretState.getHashCounts(); i++) {
      digested = opieSecretState.getMessageDigest().digest(digested);
    }

    byte[] foldedBits = foldTo64Bits(digested);
    assert foldedBits != null;
    assert foldedBits.length == EXPECTED_OUTPUT_LENGTH_BYTES;
    return foldedBits;
  }

  /**
   * Perform the initial step of an OPIE OTP. Concatenates the seed and the
   * secret, then digests and folds it.
   *
   * @param seed The OPIE seed.
   * @param secret The user's secret.
   * @param messageDigest The messageDigest algorithm to use.
   * @return The result of the RFC2289 initial step.
   */
  private byte[] initialStep(
          final byte[] seed,
          final byte[] secret,
          final MessageDigest messageDigest) {
    messageDigest.update(seed);
    final byte[] result = foldTo64Bits(messageDigest.digest(secret));
    messageDigest.reset();
    assert result != null;
    assert result.length == EXPECTED_OUTPUT_LENGTH_BYTES;
    return result;
  }
}
