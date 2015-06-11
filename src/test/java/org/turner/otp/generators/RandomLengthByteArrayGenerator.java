package org.turner.otp.generators;

import net.java.quickcheck.Generator;
import net.java.quickcheck.generator.support.ByteGenerator;
import net.java.quickcheck.generator.support.IntegerGenerator;

/**
 *
 * @author turner
 */
public class RandomLengthByteArrayGenerator implements Generator<byte[]> {

  private static final ByteGenerator BYTE_GENERATOR = new ByteGenerator();
  private final IntegerGenerator integerGenerator;

  public RandomLengthByteArrayGenerator(
          final int maxByteArrayLength) {
    assert maxByteArrayLength > 0;
    integerGenerator = new IntegerGenerator(0, maxByteArrayLength);
  }
  
  @Override
  public byte[] next() {
    int byteArrayLength = integerGenerator.nextInt();
    byte[] resultBytes = new byte[byteArrayLength];
    for (int i = 0; i < byteArrayLength; i++) {
      resultBytes[i] = BYTE_GENERATOR.next();
    }
    return resultBytes;
  }
  
}
