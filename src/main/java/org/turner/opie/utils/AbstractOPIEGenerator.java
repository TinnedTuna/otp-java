package org.turner.opie.utils;

import java.security.MessageDigest;
import org.turner.opie.OPIEGenerator;
import org.turner.opie.OPIESecretState;

/**
 * Provides the ability to generate OPIE OTPs, as strings or byte arrays.
 *
 * @author turner
 * @since 1.0
 */
public abstract class AbstractOPIEGenerator implements OPIEGenerator {

  /**
   * The number of bytes that should be output by foldTo64Bits.
   */
  protected static final int OUTPUT_BYTES_COUNT = 8;

  /**
   * Takes an input stream of bytes, >64b length and folds it down to 64 bits.
   *
   * @param input The output of a hashing algorithm.
   * @return a 64b array of bytes
   */
  public abstract byte[] foldTo64Bits(byte[] input);

  /**
   * Generate an OPIE OTP, and convert it to a space-separated string of
   * dictionary words.
   *
   * @param opieSecretState The state used to generate the password.
   * @return The space-separated string of dictionary words.
   */
  public final String generateOPIEString(
          final OPIESecretState opieSecretState) {
    assert opieSecretState != null;
    return OPIEUtils.bytesToWords(generateOPIEBytes(opieSecretState));
  }

  /**
   * Generate an OPIE OTP, and return the array of bytes.
   *
   * @param opieSecretState The state used to generate the password.
   * @return The generated byte array.
   */
  public final byte[] generateOPIEBytes(final OPIESecretState opieSecretState) {
    assert opieSecretState != null;

    MessageDigest messageDigest = opieSecretState.getMessageDigest();
    messageDigest.update(opieSecretState.getSeed());
    byte[] digested = messageDigest.digest(opieSecretState.getSecret());

    byte[] nextSecret = foldTo64Bits(digested);
    assert nextSecret != null;
    assert nextSecret.length == OUTPUT_BYTES_COUNT;
    return nextSecret;
  }
}
