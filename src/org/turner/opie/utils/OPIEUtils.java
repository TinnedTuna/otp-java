package org.turner.opie.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
      throw new IllegalStateException("Could not load dictionary, IOException.", ex);
    }
  }
  
  public static String bytesToWords(byte[] input) {
    assert input != null;
    assert input.length == 8;
    
    StringBuilder passwordBuilder = new StringBuilder(30);
    // TODO, Parity?
    
    for (int i = 0; i < 6; i++) {
      int bits = extractBitsFromBytes(input, i*11, 11);
      assert DICTIONARY != null;
      assert bits < DICTIONARY.length;
      assert bits >= 0;
      passwordBuilder.append(DICTIONARY[bits]);
      if (i<5) {
        passwordBuilder.append(" ");
      }
    }
    return passwordBuilder.toString();
  }
  
  public static byte[] wordsToBytes(String userSuppliedOtp) {
    assert userSuppliedOtp != null;
    byte[] userSuppliedOtpBytes = new byte[8];
    String[] splitOtp = userSuppliedOtp.split(" ");
    
    // TODO, Parity?
    // TODO, check that we have the correct number of words?
    
    int i = 0;
    for (String word : splitOtp) {
      int findWordLocation = findWordLocation(word);
      
      assert findWordLocation >= 0;
      assert findWordLocation < 2048;
      
      insertBitsIntoBytes(userSuppliedOtpBytes, findWordLocation, i*11, 11);
      i += 1;
    }
    return userSuppliedOtpBytes;
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
  
  private static int extractBitsFromBytes(
          final byte[] input,
          final int offset,
          final int numberOfBits
          ) {
    assert input != null;
    assert input.length > 0;
    assert numberOfBits <= 11;
    assert numberOfBits > 0;
    assert offset >= 0;
    assert offset + numberOfBits < input.length;
    
    byte cl, cc, cr;
    int result;
    
    cl = input[offset / 8]; 
    cc = input[offset / 8 + 1]; 
    cr = input[offset / 8 + 2]; 
    result = ((int)(cl << 8 | cc) << 8 | cr);
    result = result >> (24 - (numberOfBits + (offset % 8)));
    result = (result & (0xffff >> (16 - numberOfBits)));

    return result;
  }
  
  private static void insertBitsIntoBytes(
          final byte[] bytes,
          final int bitsToInsert,
          final int offset,
          final int bitLength) {
    assert bytes != null;
    assert offset > 0;
    assert bitLength > 0;
    assert offset + bitLength <= bytes.length;

    int shift;
    int y;
    byte cl, cc, cr;
    
    shift = ((8 - ((offset + bitLength) % 8)) % 8);
    y = bitsToInsert << shift;
    cl = (byte) ((y >> 16) & 0xff);
    cc = (byte) ((y >> 8) & 0xff);
    cr = (byte) (y & 0xff);
    if (shift + bitLength > 16) {
      bytes[offset / 8] |= cl;
      bytes[offset / 8 + 1] |= cc;
      bytes[offset / 8 + 2] |= cr;
    } else if (shift + bitLength > 8) {
      bytes[offset / 8] |= cc;
      bytes[offset / 8 + 1] |= cr;
    } else {
      bytes[offset / 8] |= cr;
    }
  }
  
  private static int findWordLocation(final String word) {
    assert word != null;
    assert word.length() <= 4;
    assert word.length() > 0;
    assert DICTIONARY != null;
    assert DICTIONARY.length > 0;
    assert DICTIONARY.length <= 2048;
    assert Arrays.asList(DICTIONARY).contains(word);
    
    if (word.length() == 4) {
      return findLongWordLocation(word);
    } else if (word.length() < 4) {
      return findShortWordLocation(word);
    } else {
      throw new IllegalStateException("Word " + word + " had illegal length.");
    }
  }
  
  private static int findLongWordLocation(final String fourLetterWord) {
    assert fourLetterWord != null;
    assert fourLetterWord.length() == 4;
    assert DICTIONARY != null;
    assert DICTIONARY.length > 0;
    assert DICTIONARY.length <= 2048;
    return Arrays.binarySearch(DICTIONARY, 571, 2047, fourLetterWord);
  }
  
  private static int findShortWordLocation(final String shortWord) {
    assert shortWord != null;
    assert shortWord.length() > 0;
    assert shortWord.length() < 3;
    assert DICTIONARY != null;
    assert DICTIONARY.length > 0;
    assert DICTIONARY.length <= 2048;
    return Arrays.binarySearch(DICTIONARY, 0, 570, shortWord);
  }
}
