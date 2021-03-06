package org.turner.opie.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Encodes a series of bytes into a string, where each 11-bit "word" is encoded
 * as a word from Appendix D of RFC2289.
 *
 * @author turner
 * @since 1.0
 */
public final class WordEncoder {

  /** The dictionary of words for creating pass-phrases. */
  private static final String[] DICTIONARY;

  /** The size of buffer required to read the dictionary into memory. */
  private static final int DICTIONARY_BUFFER_SIZE = 9614;

  /** The expected size of the dictionary, after splitting on words. */
  private static final int DICTIONARY_SIZE = 2048;

  /** The number of bits represented by a dictionary word. */
  private static final int BITS_PER_DICTIONARY_WORD = 11;

  /** The number of bits in one Byte. */
  private static final int BITS_PER_BYTE = 8;

  /** The maximum length of any single word that's expected. */
  private static final int MAX_WORD_SIZE = 4;

  /** The highest index of any word of length 1, 2, or 3. */
  private static final int MAX_SHORT_WORD_DICTIONARY_LOCATION = 570;

  static {
    try {
      // Read the dictionary in from the command line.
      InputStream dictionaryStream
          = ClassLoader.getSystemResourceAsStream("rfc4226-dictionary.txt");
      byte[] inputBuffer = new byte[DICTIONARY_BUFFER_SIZE];
      int result = dictionaryStream.read(inputBuffer);
      if (result < 0) {
        throw new IOException("Could not load dictionary, return value was: "
                              + result);
      }
      String entireDictionary
          = new String(inputBuffer, Charset.forName("ASCII"));
      DICTIONARY = entireDictionary.split("\n");
      assert DICTIONARY.length == DICTIONARY_SIZE;
    } catch (IOException ex) {
      throw new IllegalStateException("Could not load dictionary.", ex);
    }
  }

  /**
   * Private constructor, never called.
   */
  private WordEncoder() {
    throw new IllegalArgumentException("Cannot instantiate WordEncoder.");
  }

  /**
   * Convert a byte array into a set of words drawn from the DICTIONARY list.
   *
   * @param inputBytes The bytes to convert.
   * @return A string of words, separated by spaces, which represents the byte
   *         array.
   */
  public static String encode(final byte[] inputBytes) {
    assert inputBytes != null;
    int bitCount = inputBytes.length * BITS_PER_BYTE;
    int requiredWords = (bitCount / BITS_PER_DICTIONARY_WORD);
    assert bitCount >= 0;
    assert requiredWords >= 0;

    StringBuilder passwordBuilder = new StringBuilder();
    // TODO, Parity?

    for (int i = 0; i < requiredWords; i++) {
      int bits
          = extract11BitsFromBytes(inputBytes, i * BITS_PER_DICTIONARY_WORD);
      assert DICTIONARY != null;
      assert bits < DICTIONARY.length;
      assert bits >= 0;
      passwordBuilder.append(DICTIONARY[bits]);
      if (i < (requiredWords - 1)) {
        passwordBuilder.append(" ");
      }
    }
    String password = passwordBuilder.toString();
    assert password.split(" ").length == requiredWords;
    return password;
  }

  /**
   * Converts a string of words, separated by spaces, into an array of bytes,
   * assuming that all of the words are found in the DICTIONARY array.
   *
   * @param userSuppliedOtp The string to convert into bytes.
   * @return The byte array represented by the input.
   */
  public static byte[] decode(final String userSuppliedOtp) {
    assert userSuppliedOtp != null;
    String[] splitOtp = userSuppliedOtp.split(" ");
    int bitsRequired = splitOtp.length * BITS_PER_DICTIONARY_WORD;
    int bytesRequired = bitsRequired * BITS_PER_BYTE;
    byte[] userSuppliedOtpBytes = new byte[bytesRequired];

    // TODO, Parity?
    // TODO, check that we have the correct number of words?

    int i = 0;
    for (String word : splitOtp) {
      int findWordLocation = findWordLocation(word);

      assert findWordLocation >= 0;
      assert findWordLocation < DICTIONARY_SIZE;

      insert11BitsIntoBytes(
          userSuppliedOtpBytes,
          findWordLocation,
          i * BITS_PER_DICTIONARY_WORD);
      i += 1;
    }
    return userSuppliedOtpBytes;
  }

  /**
   * Extracts 11 bits from a byte array, starting at the given offset. The
   * result is packed into an int.
   *
   * @param input The byte array to extract the bits from.
   * @param offset The offset (in bits) to extract the bits from.
   * @return The bits requested, packed into an int.
   */
  private static int extract11BitsFromBytes(
      final byte[] input,
      final int offset) {
    assert input != null;
    assert offset >= 0;

    // First byte, integer rounding gives floor semantics.
    int startByte = offset / BITS_PER_BYTE;

    // Last byte, floor semantics
    int lastBit = offset + BITS_PER_DICTIONARY_WORD;
    int lastByteIdx = (lastBit / BITS_PER_BYTE) + 1;
    assert lastByteIdx < input.length;
    assert lastByteIdx > 0;

    int result;
    if (lastByteIdx - startByte == 2) {
      // The number of bits in the first byte.
      int unwantedBitsInFirstByte = startByte * BITS_PER_BYTE;
      int desiredFirstByteBits
          = BITS_PER_BYTE - offset - unwantedBitsInFirstByte;
      assert desiredFirstByteBits <= BITS_PER_BYTE;
      assert desiredFirstByteBits > 0;

      // Get rid of the unwanted bits.
      result = input[startByte] << unwantedBitsInFirstByte;

      // Add in desired bits from the first byte.
      result >>= unwantedBitsInFirstByte;
      result <<= desiredFirstByteBits;

      // Get the bits out of the second byte.
      int bitsInSecondByte = desiredFirstByteBits - BITS_PER_DICTIONARY_WORD;
      assert bitsInSecondByte <= BITS_PER_BYTE;
      int unwantedBitsInSecondByte = BITS_PER_BYTE - bitsInSecondByte;
      result |= input[lastByteIdx] >> unwantedBitsInSecondByte;
    } else {
      int bitsInRightMostByte = lastBit - ((lastByteIdx - 1) * BITS_PER_BYTE);
      int shiftAmount = BITS_PER_BYTE - bitsInRightMostByte;
      assert shiftAmount > 0;
      assert shiftAmount < BITS_PER_BYTE;
      final byte lastByte = input[lastByteIdx];
      // Pack into the result int.
      result = 1;
      result |= input[lastByte - 1];
      result <<= BITS_PER_BYTE;
      result |= (lastByte >> shiftAmount);
      assert result < DICTIONARY_SIZE;
    }

    return result;
  }

  /**
   * Insert 11 bits into a byte array, starting at the offset given.
   *
   * @param bytes The byte array which will "receive" the bits.
   * @param bitsToInsert The bits to insert, packed into an int.
   * @param offset The offset (in bits) at which to insert the bits.
   */
  private static void insert11BitsIntoBytes(
      final byte[] bytes,
      final int bitsToInsert,
      final int offset) {
    assert false;
  }

  /**
   * Find the index of a word in the dictionary.
   *
   * @param word The word to lookup.
   * @return The int such that DICTIONARY[i] = word, if it exists
   */
  private static int findWordLocation(final String word) {
    assert word != null;
    assert DICTIONARY != null;
    assert DICTIONARY.length > 0;
    assert DICTIONARY.length <= DICTIONARY_SIZE;
    assert Arrays.asList(DICTIONARY).contains(word);

    if (word.length() == MAX_WORD_SIZE) {
      return findLongWordLocation(word);
    } else if (word.length() < MAX_WORD_SIZE && word.length() > 0) {
      return findShortWordLocation(word);
    } else {
      throw new IllegalStateException("Word " + word + " had illegal length.");
    }
  }

  /**
   * Find the location of a "long" word in the dictionary. A long word is one
   * with length 4.
   *
   * @param fourLetterWord The long word to find.
   * @return The integer i such that DICTIONARY[i] = fourLetterWord.
   */
  private static int findLongWordLocation(final String fourLetterWord) {
    assert fourLetterWord != null;
    assert fourLetterWord.length() == MAX_WORD_SIZE;
    assert DICTIONARY != null;
    assert DICTIONARY.length > 0;
    assert DICTIONARY.length <= DICTIONARY_SIZE;
    return Arrays.binarySearch(
        DICTIONARY,
        MAX_SHORT_WORD_DICTIONARY_LOCATION,
        DICTIONARY_SIZE - 1,
        fourLetterWord);
  }

  /**
   * Finds the location of a "short" word in the dictionary. A short word is one
   * with length of 1, 2, or 3.
   *
   * @param shortWord The short word to find.
   * @return The integer i such that DICTIONARY[i] = shortWord.
   */
  private static int findShortWordLocation(final String shortWord) {
    assert shortWord != null;
    assert shortWord.length() > 0;
    assert shortWord.length() < MAX_WORD_SIZE - 1;
    assert DICTIONARY != null;
    assert DICTIONARY.length > 0;
    assert DICTIONARY.length <= DICTIONARY_SIZE;
    return Arrays.binarySearch(
        DICTIONARY,
        0,
        MAX_SHORT_WORD_DICTIONARY_LOCATION,
        shortWord);
  }
}
