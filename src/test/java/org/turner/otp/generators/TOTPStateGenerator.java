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
  private static final Generator<Long> timeStepGenerator = new LongGenerator(0, Long.MAX_VALUE);
  private static final Generator<Long> currentUnixTimeGenerator = new LongGenerator(0, Long.MAX_VALUE);
  private static final Generator<Long> initialUnixTimeGenerator = new LongGenerator(0, Long.MAX_VALUE);
  private static final Generator<byte[]> secretGenerator = new FixedLengthByteArrayGenerator(8);
  
  @Override
  public TOTPSecretState next() {
    byte[] secretBytes = secretGenerator.next();
    Long timeStep = timeStepGenerator.next();
    Long currentTime = currentUnixTimeGenerator.next();
    Long initialTime = initialUnixTimeGenerator.next();
    if (initialTime > currentTime) {
      initialTime = initialTime % currentTime;
    }
    assert currentTime >= initialTime;
    Integer otpLength = lengthGenerator.next();
    
    return new TOTPSecretState(secretBytes, otpLength, initialTime, currentTime, timeStep);
  }
  
}
