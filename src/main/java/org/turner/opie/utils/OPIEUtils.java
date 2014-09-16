package org.turner.opie.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

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
      int result = dictionaryStream.read(inputBuffer);
      if (result < 0) {
        throw new IOException("Could not load dictionary, return value was: " + result);
      }
      String entireDictionary = new String(inputBuffer, Charset.forName("ASCII"));
      DICTIONARY = entireDictionary.split("\n");
      assert DICTIONARY != null;
      assert DICTIONARY.length == 2048;
    } catch (IOException ex) {
      throw new IllegalStateException("Could not load dictionary, IOException.", ex);
    }
  }
  
  public static String bytesToWords(final byte[] input) {
    assert input != null;
    int bitCount = input.length * 8;
    int requiredWords = (bitCount/11);
    assert bitCount >= 0;
    assert requiredWords >= 0;
    
    StringBuilder passwordBuilder = new StringBuilder(requiredWords*4);
    // TODO, Parity?
    
    for (int i = 0; i < requiredWords; i++) {
      int bits = extract11BitsFromBytes(input, i*11);
      assert DICTIONARY != null;
      assert bits < DICTIONARY.length;
      assert bits >= 0;
      passwordBuilder.append(DICTIONARY[bits]);
      if (i< (requiredWords - 1)) {
        passwordBuilder.append(" ");
      }
    }
    String password = passwordBuilder.toString();
    assert password.split(" ").length == requiredWords;
    return password;
  }
  
  public static byte[] wordsToBytes(final String userSuppliedOtp) {
    assert userSuppliedOtp != null;
    byte[] userSuppliedOtpBytes = new byte[9];
    String[] splitOtp = userSuppliedOtp.split(" ");
    
    // TODO, Parity?
    // TODO, check that we have the correct number of words?
    
    int i = 0;
    for (String word : splitOtp) {
      int findWordLocation = findWordLocation(word);
      
      assert findWordLocation >= 0;
      assert findWordLocation < 2048;
      
      insert11BitsIntoBytes(userSuppliedOtpBytes, findWordLocation, i*11);
      i += 1;
    }
    return userSuppliedOtpBytes;
  }

  private static int extract11BitsFromBytes(
          final byte[] input,
          final int offset) {
    assert input != null;
    int numberOfBits = 11;
    assert offset >= 0;
    assert (offset + numberOfBits) <= input.length*8;
    
    int result = 0;
    
    int leftMostByte = offset / 8;
    assert leftMostByte >= 0;
    assert leftMostByte < input.length;
    
    int rightMostByte = (offset +  numberOfBits)/8;
    assert rightMostByte >= 0;
    assert rightMostByte < input.length;
    assert rightMostByte > leftMostByte;
    
    assert rightMostByte - leftMostByte > 0;
    assert rightMostByte - leftMostByte <= 2;
    
    byte leftByte = input[leftMostByte];
    byte rightByte = input[rightMostByte];
    
    int shift = offset - (offset/8);
    assert shift >= 0;
    result |= leftByte & shift;
    result <<= (11-shift);
    if ((rightMostByte - leftMostByte) == 1) {
      // We only "straddle" two bytes.
      
    } else {
      // We "straddle" three bytes.
      assert leftMostByte + 1 == rightMostByte - 1;
      byte centreByte = input[leftMostByte + 1];
    }
    
    return result;
  }
  
  private static void insert11BitsIntoBytes(
          final byte[] bytes,
          final int bitsToInsert,
          final int offset) {
    assert bytes != null;
    assert offset >= 0;
    int bitLength = 11;
    assert (offset + bitLength) <= (bytes.length * 8);

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
