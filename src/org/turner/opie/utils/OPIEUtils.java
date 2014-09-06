package org.turner.opie.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author turner
 */
public class OPIEUtils {

  private static final String[] DICTIONARY;
  
  static {
    try {
      // Read the dictionary in from the command line.
      InputStream dictionaryStream = ClassLoader.getSystemResourceAsStream("rfc4226-dictionary.txt");
      byte[] inputBuffer = new byte[9614];
      dictionaryStream.read(inputBuffer);
      String entireDictionary = new String(inputBuffer);
      DICTIONARY = entireDictionary.split("\n");
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
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
