package org.turner.opie;

import java.security.MessageDigest;

/**
 * Data transfer object which contains the relevant information for creating and
 * generating OPIE OTPs.
 *
 * @author turner
 * @since 1.0
 */
public final class OPIESecretState {

  /**
   * The secret key used for generating and verifying OTPs.
   */
  private final byte[] secret;

  /**
   * A small seed to ensure that equal secrets result in different OTPs.
   */
  private final byte[] seed;

  /**
   * The number of OTPs which may be generated using the given secret.
   */
  private final long hashCounts;

  /**
   * The digest algorithm to convert the seed into a stream of random bytes.
   */
  private final MessageDigest messageDigest;

  /**
   * Construct a new secret state.
   *
   * @param providedSecret The secret used to generate OTPs.
   * @param providedSeed The seed used to prevent rainbow table attacks.
   * @param providedHashCounts The maximum number of OTPs to be generated.
   * @param providedMessageDigest The digest algorithm to use.
   */
  public OPIESecretState(
          final byte[] providedSecret,
          final byte[] providedSeed,
          final long providedHashCounts,
          final MessageDigest providedMessageDigest) {
    assert providedSecret != null;
    assert providedSeed != null;
    assert providedHashCounts > 0;
    assert providedMessageDigest != null;
    this.secret = providedSecret;
    this.seed = providedSeed;
    this.hashCounts = providedHashCounts;
    this.messageDigest = providedMessageDigest;
  }

  /**
   * The maximum number of OTPs that can be generated from this state.
   *
   * @return The number of hash counts.
   */
  public long getHashCounts() {
    return hashCounts;
  }

  /**
   * The secret used to generate and verify OTPs for this state.
   *
   * @return The secret.
   */
  public byte[] getSecret() {
    return secret;
  }

  /**
   * The seed used to prevent pre-computation attacks.
   *
   * @return The seed.
   */
  public byte[] getSeed() {
    return seed;
  }

  /**
   * The message digest algorithm used to generate randomness from the seed and
   * the secret.
   *
   * @return The message digest algorithm.
   */
  public MessageDigest getMessageDigest() {
    return messageDigest;
  }
}
