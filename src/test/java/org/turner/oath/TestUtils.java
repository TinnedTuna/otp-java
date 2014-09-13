package org.turner.oath;

import java.util.Arrays;

/**
 *
 * @author turner
 */
public class TestUtils {

  private static final char[] HEX_DIGITS = { '0', '1', '2', '3'
                                           , '4', '5', '6', '7'
                                           , '8', '9', 'A', 'B'
                                           , 'C', 'D', 'E', 'F'};
  
  public static byte[] hexStringToBytes(
          final String hexString) {
    assert hexString != null;
    assert hexString.length() % 2 == 0;
    String upperCaseHesString = hexString.toUpperCase();
    
    byte[] results = new byte[upperCaseHesString.length() / 2];
    for (int i = 0; i<upperCaseHesString.length(); i+=2) {
      char msb = upperCaseHesString.charAt(i);
      char lsb = upperCaseHesString.charAt(i+1);
      int result = Arrays.binarySearch(HEX_DIGITS, msb);
      assert result >= 0;
      assert result <= 16;
      byte resultByte = (byte) result;
      resultByte <<= 4;
      result = Arrays.binarySearch(HEX_DIGITS, lsb);
      assert result >= 0;
      assert result < 16;
      resultByte |= result;
      assert (i/2) < results.length;
      results[(i/2)] = resultByte;
    }
    assert results != null;
    assert results.length != 0;
    return results;
  }
}
