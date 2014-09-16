package org.turner.yubi;

/**
 *
 * @author turner
 */
public class YubiSecretState {

  private final byte[] publicId, secretId, key, usageCounter, sessionCounter;

  public YubiSecretState(
          final byte[] publicId,
          final byte[] secretId,
          final byte[] key,
          final byte[] usageCounter,
          final byte[] sessionCounter) {
    assert secretId != null;
    assert secretId.length == 6;
    assert key != null;
    assert usageCounter != null;
    assert usageCounter.length == 2;
    assert sessionCounter != null;
    assert sessionCounter.length == 1;
    this.publicId = publicId;
    this.secretId = secretId;
    this.key = key;
    this.usageCounter = usageCounter;
    this.sessionCounter = sessionCounter;
  }

  public byte[] getKey() {
    return key;
  }

  public byte[] getPublicId() {
    return publicId;
  }

  public byte[] getSecretId() {
    return secretId;
  }

  public byte[] getUsageCounter() {
    return usageCounter;
  }

  public byte[] getSessionCounter() {
    return sessionCounter;
  }
  
}
