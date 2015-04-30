package org.turner.opie;

import java.security.MessageDigest;

/**
 * Holds secret state required for OPIE one-time passwords.
 *
 * @author turner
 * @since 1.0
 */
public class OPIESecretState {

  /** The secret used for generating OPIE one-time passwords. */
  private final byte[] secret;

  /**
   * The seed used to ensure that two identical secrets don't create the same
   * passwords.
   */
  private final byte[] seed;

  /** The number of passwords that can be generated from this state. */
  private final long hashCounts;

  /** The message digest algorithm used to generate each password. */
  private final MessageDigest messageDigest;

  /**
   * Constructs an OPIE secret state.
   *
   * @param randomBytes The secret used to generate OTPs.
   * @param uniqueSeed The unique seed used when generating OTPs.
   * @param otpsToGenerate The number OTPs that may be generated from this
   *                       state.
   * @param hashAlgorithm The hash algorithm used to generate OTPs.
   */
  public OPIESecretState(
      final byte[] randomBytes,
      final byte[] uniqueSeed,
      final long otpsToGenerate,
      final MessageDigest hashAlgorithm) {
    assert randomBytes != null;
    assert uniqueSeed != null;
    assert otpsToGenerate > 0;
    assert hashAlgorithm != null;
    this.secret = randomBytes;
    this.seed = uniqueSeed;
    this.hashCounts = otpsToGenerate;
    this.messageDigest = hashAlgorithm;
  }

  /**
   * The number of OTPs that may be generated from this state.
   *
   * @return The number of OTPs that may be generated from this state.
   */
  public final long getHashCounts() {
    return hashCounts;
  }

  /**
   * The secret used to generate OTPs.
   *
   * @return The secret used to generate OTPs.
   */
  public final byte[] getSecret() {
    return secret;
  }

  /**
   * The unique seed that is used to ensure that passwords generated are
   * unique, even when two users share a secret.
   *
   * @return The seed.
   */
  public final byte[] getSeed() {
    return seed;
  }

  /**
   * The hashing algorithm that this state requires.
   *
   * @return The hashing algorithm.
   */
  public final MessageDigest getMessageDigest() {
    return messageDigest;
  }
}
