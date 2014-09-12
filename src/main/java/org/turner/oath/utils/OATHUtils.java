package org.turner.oath.utils;

import java.security.InvalidKeyException;
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
                                            , 1000000 
                                            };
  
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
    return truncated;
  }
  
  public static String integerify(final byte[] inputIntBytes, final int numberOfOutputDigits) {
    assert inputIntBytes != null;
    assert inputIntBytes.length == 4;
    assert numberOfOutputDigits != 0;
    assert numberOfOutputDigits <= 8;
    
    int binary = (
            inputIntBytes[0] << 24 |
            inputIntBytes[1] << 16 |
            inputIntBytes[2] << 8  |
            inputIntBytes[3]
            );
    
    int truncatedOtp = binary % DIGITS_POWER[numberOfOutputDigits - 1];
    assert truncatedOtp >= 0;
    String result = Integer.toString(truncatedOtp);
    
    if (result.length() < numberOfOutputDigits) {
      return prependPad(result, '0', numberOfOutputDigits);
    } else {
      return result;
    }
  }
  
  public static String prependPad(final String value, final char prependedChar, final int size) {
    assert value != null;
    assert size > value.length();
    
    int requiredPadSize = value.length() - size;
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
  
  public static byte[] macBytes(final Mac mac, final byte[] secretKey, final long message) {
    assert secretKey != null;
    assert mac != null;
    
    try {
      mac.init(new SecretKeySpec(secretKey, "RAW"));
      byte[] timeStepBytes = OATHUtils.longBytes(message);
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
}