package org.turner.oath.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidKeyException;
import java.util.Arrays;
import java.util.regex.Pattern;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class OATHUtils {
  
  private static final int[] DIGITS_POWER = { 1
                                            , 10
                                            , 100
                                            , 1000
                                            , 10000
                                            , 100000
                                            , 1000000
                                            , 10000000 
                                            , 100000000
                                            };
  
  private static final char[] HEX_DIGITS = { '0', '1', '2', '3'
                                           , '4', '5', '6', '7'
                                           , '8', '9', 'A', 'B'
                                           , 'C', 'D', 'E', 'F'};
  
  public static byte[] truncateBytes(byte[] inputBytes) {
    assert inputBytes != null;
    assert inputBytes.length > 1;
    
    int offsetLocation = inputBytes[inputBytes.length - 1] & 0x0F;
    
    assert offsetLocation >= 0;
    assert offsetLocation + 3 < inputBytes.length;
    
    byte[] truncated = new byte[4];
    truncated[0] = (byte) (inputBytes[offsetLocation] & 0x7F) ; 
    truncated[1] = inputBytes[offsetLocation + 1];
    truncated[2] = inputBytes[offsetLocation + 2];
    truncated[3] = inputBytes[offsetLocation + 3];
    assert (truncated[0] & 0x80) != 0x80;
    return truncated;
  }
  
  public static String integerify(final byte[] inputIntBytes, final int numberOfOutputDigits) {
    assert inputIntBytes != null;
    assert inputIntBytes.length == 4;
    assert numberOfOutputDigits != 0;
    assert numberOfOutputDigits <= 8;
    ByteBuffer wrappedBytes = ByteBuffer.wrap(inputIntBytes);
    wrappedBytes.order(ByteOrder.BIG_ENDIAN);
    int binary = wrappedBytes.getInt();
    assert binary >= 0;
    int truncatedOtp = binary % DIGITS_POWER[numberOfOutputDigits];
    assert truncatedOtp >= 0;
    String result = Integer.toString(truncatedOtp);
    assert !result.startsWith("-");
    
    if (result.length() < numberOfOutputDigits) {
      return prependPad(result, '0', numberOfOutputDigits);
    } else {
      return result;
    }
  }
  
  public static String prependPad(final String value, final char prependedChar, final int size) {
    assert value != null;
    assert size > value.length();
    
    int requiredPadSize = size - value.length();
    assert requiredPadSize > 0;
    
    StringBuilder paddedResult = new StringBuilder(size);
    while (requiredPadSize > 0) {
      paddedResult.append(prependedChar);
      requiredPadSize -= 1;
      assert paddedResult.length() < size;
    }
    paddedResult.append(value);
    
    assert paddedResult.length() == size;
    return paddedResult.toString();
  }
  
  public static byte[] longBytes(final long inputLong) {
    byte[] longBytes = new byte[8];
    longBytes[0] = (byte) ((inputLong >> 56) & 0xff);
    longBytes[1] = (byte) ((inputLong >> 48) & 0xff);
    longBytes[2] = (byte) ((inputLong >> 40) & 0xff);
    longBytes[3] = (byte) ((inputLong >> 32) & 0xff);
    longBytes[4] = (byte) ((inputLong >> 24) & 0xff);
    longBytes[5] = (byte) ((inputLong >> 16) & 0xff);
    longBytes[6] = (byte) ((inputLong >> 8) & 0xff);
    longBytes[7] = (byte) (inputLong & 0xff);
    return longBytes;
  }
  
  public static byte[] intBytes(final int inputInt) {
    byte[] intBytes = new byte[4];
    intBytes[4] = (byte) ((inputInt >> 24) & 0xff);
    intBytes[5] = (byte) ((inputInt >> 16) & 0xff);
    intBytes[6] = (byte) ((inputInt >> 8) & 0xff);
    intBytes[7] = (byte) (inputInt & 0xff);
    return intBytes;        
  }
  
  public static byte[] macBytes(final Mac mac, final byte[] secretKey, final byte[] message) {
    assert secretKey != null;
    assert secretKey.length != 0;

    assert message != null;
    assert mac != null;
    
    try {
      mac.init(new SecretKeySpec(secretKey, "RAW"));
      byte[] timeStepBytes = message;
      return mac.doFinal(timeStepBytes);
    } catch (InvalidKeyException ex) {
      throw new IllegalStateException(ex);
    } finally {
      mac.reset();
    }
  }
  
  public static boolean constantTimeEquals(String left, String right) {
    assert left != null;
    assert right != null;
    
    if (left.length() != right.length()) {
      return false;
    }
    
    int result = 0;
    for (int i = 0; i < left.length(); i++) {
      result |= left.charAt(i) ^ right.charAt(i);
    }
    return result == 0;
  }
  
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
  
  public static long getCurrentTimeStep(
          final long initialUnixTime,
          final long currentUnixTime,
          final long timeStep
          ) {
    return (currentUnixTime - initialUnixTime) / timeStep;
  }
}