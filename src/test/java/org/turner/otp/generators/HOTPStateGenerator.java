package org.turner.otp.generators;

import net.java.quickcheck.Generator;
import net.java.quickcheck.generator.support.IntegerGenerator;
import org.turner.oath.hotp.HOTPSecretState;

/**
 *
 * @author turner
 */
public class HOTPStateGenerator implements Generator<HOTPSecretState> {

  private static final Generator<Integer> lengthGenerator = new IntegerGenerator(6, 8);
  private static final Generator<byte[]> secretGenerator = new FixedLengthByteArrayGenerator(8);
  private static final Generator<Integer> counterGenerator = new IntegerGenerator(1, Integer.MAX_VALUE);
  
  @Override
  public HOTPSecretState next() {
    byte[] secretBytes = secretGenerator.next();
    Integer otpLength = lengthGenerator.next();
    Integer counter = counterGenerator.next();
    return new HOTPSecretState(secretBytes, otpLength, counter);
  }
  
}
