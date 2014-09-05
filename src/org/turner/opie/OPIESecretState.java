package org.turner.opie;

import java.security.MessageDigest;

/**
 *
 * @author turner
 */
public class OPIESecretState {
  
  private final byte[] secret;
  private final byte[] seed;
  private final long hashCounts;
  private final MessageDigest messageDigest;

  public OPIESecretState(byte[] secret, byte[] seed, long hashCounts, MessageDigest messageDigest) {
    assert secret != null;
    assert seed != null;
    assert hashCounts > 0;
    assert messageDigest != null;
    this.secret = secret;
    this.seed = seed;
    this.hashCounts = hashCounts;
    this.messageDigest = messageDigest;
  }

  public long getHashCounts() {
    return hashCounts;
  }

  public byte[] getSecret() {
    return secret;
  }

  public byte[] getSeed() {
    return seed;
  }

  public MessageDigest getMessageDigest() {
    return messageDigest;
  }
  
}
