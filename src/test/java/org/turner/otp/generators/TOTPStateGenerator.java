package org.turner.otp.generators;

import net.java.quickcheck.Generator;
import net.java.quickcheck.generator.support.IntegerGenerator;
import org.turner.oath.totp.TOTPSecretState;

/**
 *
 * @author turner
 */
public class TOTPStateGenerator implements Generator<TOTPSecretState> {

  private static final Generator<Integer> lengthGenerator = new IntegerGenerator(6, 8);
  private static final Generator<Integer> timeStepGenerator = new IntegerGenerator(0, Integer.MAX_VALUE);
  private static final Generator<byte[]> secretGenerator = new FixedLengthByteArrayGenerator(8);
  
  @Override
  public TOTPSecretState next() {
    byte[] secretBytes = secretGenerator.next();
    Integer timeStep = timeStepGenerator.next();
    Integer otpLength = lengthGenerator.next();
    
    return new TOTPSecretState(secretBytes, otpLength, timeStep);
  }
  
}
