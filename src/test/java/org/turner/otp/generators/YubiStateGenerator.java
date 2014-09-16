package org.turner.otp.generators;

import net.java.quickcheck.Generator;
import org.turner.yubi.YubiSecretState;

/**
 *
 * @author turner
 */
public class YubiStateGenerator implements Generator<YubiSecretState> {

  private final Generator<byte[]> publicIdGenerator = new FixedLengthByteArrayGenerator(6);
  private final Generator<byte[]> secretIdGenerator = new FixedLengthByteArrayGenerator(6);
  private final Generator<byte[]> keyGenerator = new FixedLengthByteArrayGenerator(16);
  
  
  @Override
  public YubiSecretState next() {
    return new YubiSecretState(
            publicIdGenerator.next(),
            secretIdGenerator.next(),
            keyGenerator.next(),
            new byte[2],
            new byte[2]);
  }
  
}
