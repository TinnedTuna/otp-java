package org.turner.otp.generators;

import java.security.MessageDigest;
import net.java.quickcheck.Generator;
import net.java.quickcheck.generator.support.LongGenerator;
import org.turner.opie.OPIESecretState;

/**
 *
 * @author turner
 */
public class OPIEStateGenerator implements Generator<OPIESecretState> {

  private final MessageDigest messageDigest;
  private static final FixedLengthByteArrayGenerator secretGenerator = new FixedLengthByteArrayGenerator(8);
  private static final FixedLengthByteArrayGenerator seedGenerator = new FixedLengthByteArrayGenerator(3);
  private static final Generator<Long> hashCountsGenerator = new LongGenerator(1, Long.MAX_VALUE);
  
  public OPIEStateGenerator(final MessageDigest messageDigest) {
    assert messageDigest != null;
    this.messageDigest = messageDigest;
  }
  
  @Override
  public OPIESecretState next() {
    byte[] secretBytes = secretGenerator.next();
    byte[] seedBytes = seedGenerator.next();
    long hashCount = hashCountsGenerator.next();
    return new OPIESecretState(secretBytes, seedBytes, hashCount, messageDigest);
  }
  
}
