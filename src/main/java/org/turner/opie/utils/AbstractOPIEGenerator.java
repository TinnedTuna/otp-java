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

  /**
   * Takes an input stream of bytes, >64b length and folds it down to 64 bits.
   *
   * @param input The stream of bytes to fold down.
   * @return a 64b array of bytes.
   */
  public abstract byte[] foldTo64Bits(byte[] input);

  @Override
  public final String generateOPIEString(
      final OPIESecretState opieSecretState) {
    assert opieSecretState != null;
    return OPIEUtils.bytesToWords(generateOPIEBytes(opieSecretState));
  }

  @Override
  public final byte[] generateOPIEBytes(
      final OPIESecretState opieSecretState) {
    assert opieSecretState != null;

    MessageDigest messageDigest = opieSecretState.getMessageDigest();
    messageDigest.update(opieSecretState.getSeed());
    byte[] digested = messageDigest.digest(opieSecretState.getSecret());

    return foldTo64Bits(digested);
  }
}
