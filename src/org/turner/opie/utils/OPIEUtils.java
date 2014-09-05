package org.turner.opie.utils;

/**
 *
 * @author turner
 */
public class OPIEUtils {

  public static String toDictionary(byte[] input) {
    assert input != null;
    assert input.length == 8;
    return "UNIMPLEMENTED";
  }
  
  public static byte[] fromDictionaryWords(String userSuppliedOtp) {
    assert userSuppliedOtp != null;
    byte[] userSuppliedOtpBytes = new byte[8];
    String[] splitOtp = userSuppliedOtp.split(" ");
    
  }
  
  public static boolean constantTimeEquals(final byte[] left, final byte[] right) {
    assert left != null;
    assert right != null;
    
    if (left.length != right.length) {
      return false;
    }
    
    int result = 0;
    for (int i = 0; i < left.length; i++) {
      result |= left[i] ^ right[i];
    }
    return result == 0;
  }
}
