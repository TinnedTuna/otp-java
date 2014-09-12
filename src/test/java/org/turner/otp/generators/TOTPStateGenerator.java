package org.turner.otp.generators;

import net.java.quickcheck.Generator;
import net.java.quickcheck.generator.support.IntegerGenerator;
import net.java.quickcheck.generator.support.LongGenerator;
import org.turner.oath.totp.TOTPSecretState;

/**
 *
 * @author turner
 */
public class TOTPStateGenerator implements Generator<TOTPSecretState> {

  private static final Generator<Integer> lengthGenerator = new IntegerGenerator(6, 8);
  private static final Generator<Long> timeStepGenerator = new LongGenerator(1, Long.MAX_VALUE);
  private static final Generator<Long> currentTimeGenerator = new LongGenerator(1, Long.MAX_VALUE);
  private static final Generator<Long> epochGenerator = new LongGenerator(1, Long.MAX_VALUE);
  private static final Generator<byte[]> secretGenerator = new FixedLengthByteArrayGenerator(8);
  
  @Override
  public TOTPSecretState next() {
    byte[] secretBytes = secretGenerator.next();
    Integer otpLength = lengthGenerator.next();
    Long currentTime = currentTimeGenerator.next();
    Long timeStep = timeStepGenerator.next();
    Long epoch = epochGenerator.next();
    if (currentTime < epoch) {
      currentTime = epoch - currentTime;
    }
    assert currentTime <= epoch;
    assert currentTime >= 0;
    
    return new TOTPSecretState(secretBytes, otpLength, timeStep, epoch, currentTime);
  }
  
}
