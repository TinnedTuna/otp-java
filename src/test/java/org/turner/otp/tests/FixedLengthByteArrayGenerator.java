package org.turner.otp.tests;

import java.util.ArrayList;
import net.java.quickcheck.Generator;
import net.java.quickcheck.generator.support.ByteGenerator;

/**
 *
 * @author turner
 */
public class FixedLengthByteArrayGenerator implements Generator<byte[]> {

  private static final ByteGenerator BYTE_GENERATOR = new ByteGenerator();
  private final int byteArrayLength;
  
  public FixedLengthByteArrayGenerator(
          final int byteArrayLength) {
    assert byteArrayLength > 0;
    this.byteArrayLength = byteArrayLength;
  }
  
  @Override
  public byte[] next() {
    byte[] resultBytes = new byte[byteArrayLength];
    for (int i = 0; i < byteArrayLength; i++) {
      resultBytes[i] = BYTE_GENERATOR.next();
    }
    return resultBytes;
  }
  
}
