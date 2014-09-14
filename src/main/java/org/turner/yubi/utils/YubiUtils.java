package org.turner.yubi.utils;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author turner
 */
public class YubiUtils {
  
  private static final char[] MOD_HEX_DIGITS = { 'c', 'b', 'd', 'e'
                                               , 'f', 'g', 'h', 'i'
                                               , 'j', 'k', 'l', 'n'
                                               , 'r', 't', 'u', 'v' };
  

  public static String bytesToModHex(final byte[] inputBytes) {
    throw new UnsupportedOperationException("Not yet implemented.");
  }
  
  public static byte[] modHexToBytes(final String modHexString) {
    throw new UnsupportedOperationException("Not yet implemented.");
  }
  
  public static boolean isModHex(final String testString) {
    assert testString != null;
    return false;
  }
}
