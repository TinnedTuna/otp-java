package org.turner.yubi.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author turner
 */
public class YubiUtils {
  
  private static final char[] MOD_HEX_DIGITS = { 'c', 'b', 'd', 'e'
                                               , 'f', 'g', 'h', 'i'
                                               , 'j', 'k', 'l', 'n'
                                               , 'r', 't', 'u', 'v' };
  private static final Map<String, Integer> MOD_HEX_DIGIT_LOCATIONS;
  
  static {
    Map<String,Integer> tempMap = new HashMap<>(16);
    for (int i = 0; i < MOD_HEX_DIGITS.length; i++) {
      tempMap.put(new String(new char[]{MOD_HEX_DIGITS[i]}), i);
    }
    assert tempMap != null;
    assert tempMap.size() == MOD_HEX_DIGITS.length;
    MOD_HEX_DIGIT_LOCATIONS = Collections.unmodifiableMap(tempMap);
    assert MOD_HEX_DIGIT_LOCATIONS != null;
    assert MOD_HEX_DIGIT_LOCATIONS.size() == MOD_HEX_DIGITS.length;
  }

  public static String bytesToModHex(final byte[] inputBytes) {
    assert inputBytes != null;
    assert MOD_HEX_DIGITS != null;
    assert MOD_HEX_DIGITS.length == 16;
    StringBuilder modHexOutput = new StringBuilder(inputBytes.length * 2);
    for (byte b : inputBytes) {
      int msb = (b >> 4) & 0xf;
      int lsb = b & 0xf;
      assert msb >= 0;
      assert msb < MOD_HEX_DIGITS.length;
      assert lsb >= 0;
      assert lsb < MOD_HEX_DIGITS.length;
      modHexOutput.append(MOD_HEX_DIGITS[msb]);
      modHexOutput.append(MOD_HEX_DIGITS[lsb]);
    }
    return modHexOutput.toString();
  }
  
  public static byte[] modHexToBytes(final String modHexString) {
    assert modHexString != null;
    assert modHexString.length() % 2 == 0;
    String upperCaseHesString = modHexString.toLowerCase();
    
    byte[] results = new byte[upperCaseHesString.length() / 2];
    for (int i = 0; i<upperCaseHesString.length(); i+=2) {
      char msb = upperCaseHesString.charAt(i);
      char lsb = upperCaseHesString.charAt(i+1);
      // MOD_HEX_DIGITS is not sorted! we have to use a custom method to do the
      // search.
      int result = findCharLocation(MOD_HEX_DIGITS, msb);
      assert result >= 0;
      assert result <= 16;
      byte resultByte = (byte) result;
      resultByte <<= 4;
      result = findCharLocation(MOD_HEX_DIGITS, lsb);
      assert result >= 0;
      assert result < 16;
      resultByte |= result;
      assert (i/2) < results.length;
      results[(i/2)] = resultByte;
    }
    assert results != null;
    assert (results.length * 2) == modHexString.length();
    return results;
  }
  
  public static boolean isModHex(final String testString) {
    assert testString != null;
    return false;
  }
  
  private static int findCharLocation(
          final char[] a,
          final char key) {
    assert a != null;
    assert MOD_HEX_DIGIT_LOCATIONS != null;
    Integer get = MOD_HEX_DIGIT_LOCATIONS.get(new String(new char[]{key}));
    assert get != null;
    assert get >= 0;
    assert get < 16;
    return get;
  }
}
