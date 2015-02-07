package org.turner.opie.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Utilities, such as converting to and from words using the RFC2289 dictionary.
 *
 * @author turner
 * @since 1.0
 */
public final class OPIEUtils {

  /**
   * The dictionary of words from RFC2289.
   */
  private static final String[] DICTIONARY;

  /**
   * The length of the dictionary in bytes.
   */
  private static final int DICTIONARY_LENGTH_BYTES = 9614;

  /**
   * The number of words in the dictionary.
   */
  private static final int DICTIONARY_LENGTH_WORDS = 2048;

  /**
   * Number of bits of entropy per word.
   */
  private static final int ENTROPY_PER_WORD = 11;

  /**
   * The highest location in the dictionary of any 3-letter word.
   */
  private static final int LAST_THREE_LETTER_WORD = 570;

  /**
   * The length of the longest word in the dictionary.
   */
  private static final int LONG_WORD_LENGTH = 4;

  /**
   * A byte with all of the bits set.
   */
  private static final byte ALL_BITS_SET = (byte) 0xFF;

  /**
   * Private constructor. Never called, Always throws an
   * IllegalArgumentException.
   */
  private OPIEUtils() {
    throw new IllegalArgumentException("Utility class, OPIEUtils, "
            + "cannot be instantiated.");
  }

  static {
    try {
      // Read the dictionary in from the resource path.
      InputStream dictionaryStream
              = ClassLoader.getSystemResourceAsStream("rfc4226-dictionary.txt");
      byte[] inputBuffer = new byte[DICTIONARY_LENGTH_BYTES];
      int result = dictionaryStream.read(inputBuffer);
      if (result < 0) {
        throw new IOException(
                "Could not load dictionary, return value was: " + result);
      }
      String entireDictionary = new String(
              inputBuffer,
              Charset.forName("ASCII"));
      DICTIONARY = entireDictionary.split("\n");
      assert DICTIONARY != null;
      assert DICTIONARY.length == DICTIONARY_LENGTH_WORDS;
    } catch (IOException ex) {
      throw new IllegalStateException(
              "Could not load dictionary, IOException.",
              ex);
    }
  }

  /**
   * Convert a byte array to a string of words from the RFC2289 dictionary.
   *
   * @param input The byte array to convert.
   * @return A space-separated string representing the byte array.
   */
  public static String bytesToWords(final byte[] input) {
    assert input != null;
    int bitCount = input.length * Byte.SIZE;
    int requiredWords = (bitCount / ENTROPY_PER_WORD);
    assert bitCount >= 0;
    assert requiredWords >= 0;

    StringBuilder passwordBuilder
            = new StringBuilder(requiredWords * DICTIONARY_LENGTH_WORDS);
    // TODO, Parity?

    for (int i = 0; i < requiredWords; i++) {
      int bits = extractElevenBitsFromBytes(input, i * ENTROPY_PER_WORD);
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
   * Convert a given user-supplied OTP into an array of bytes.
   *
   * @param userSuppliedOtp A non-null, space-separated, user-supplied OTP.
   * @return The byte-array representation of the user-supplied OTP.
   */
  public static byte[] wordsToBytes(final String userSuppliedOtp) {
    assert userSuppliedOtp != null;
    byte[] userSuppliedOtpBytes = new byte[Byte.SIZE + 1];
    String[] splitOtp = userSuppliedOtp.split(" ");

    // TODO, Parity?
    // TODO, check that we have the correct number of words?

    int i = 0;
    for (String word : splitOtp) {
      int findWordLocation = findWordLocation(word);

      assert findWordLocation >= 0;
      assert findWordLocation < DICTIONARY_LENGTH_WORDS;

      insertElevenBitsIntoBytes(
              userSuppliedOtpBytes,
              findWordLocation,
              i * ENTROPY_PER_WORD);
      i += 1;
    }
    return userSuppliedOtpBytes;
  }

  /**
   * Extract a number of bits from the byte array given.
   *
   * @param input The byte array to read from.
   * @param offset How many bits into the array to start reading.
   * @return A small byte array containing the bits requested.
   */
  private static int extractElevenBitsFromBytes(
          final byte[] input,
          final int offset) {
    assert input != null;
    int numberOfBits = ENTROPY_PER_WORD;
    assert offset >= 0;
    assert (offset + numberOfBits) <= input.length * Byte.SIZE;

    int result = 0;

    int leftMostByte = offset / Byte.SIZE;
    assert leftMostByte >= 0;
    assert leftMostByte < input.length;

    int rightMostByte = (offset +  numberOfBits) / Byte.SIZE;
    assert rightMostByte >= 0;
    assert rightMostByte < input.length;
    assert rightMostByte > leftMostByte;

    assert rightMostByte - leftMostByte > 0;
    assert rightMostByte - leftMostByte <= 2;

    byte leftByte = input[leftMostByte];
    byte rightByte = input[rightMostByte];

    int shift = offset - (offset / Byte.SIZE);
    assert shift >= 0;
    result |= leftByte & shift;
    result <<= (ENTROPY_PER_WORD - shift);
    if ((rightMostByte - leftMostByte) == 1) {
      // We only "straddle" two bytes.
      assert true;
    } else {
      // We "straddle" three bytes.
      assert leftMostByte + 1 == rightMostByte - 1;
      byte centreByte = input[leftMostByte + 1];
    }

    return result;
  }

  /**
   * Insert eleven bits of data into a given byte array, starting at a given
   * offset.
   *
   * @param bytes The byte array to insert into.
   * @param bitsToInsert The 11 bits to insert.
   * @param offset The location to start inserting at.
   */
  private static void insertElevenBitsIntoBytes(
          final byte[] bytes,
          final int bitsToInsert,
          final int offset) {
    assert bytes != null;
    assert offset >= 0;
    int bitLength = ENTROPY_PER_WORD;
    assert (offset + bitLength) <= (bytes.length * Byte.SIZE);

    int shift;
    int y;
    byte cl, cc, cr;

    shift = ((Byte.SIZE - ((offset + bitLength) % Byte.SIZE)) % Byte.SIZE);
    y = bitsToInsert << shift;
    cl = (byte) ((y >> Byte.SIZE * 2) & ALL_BITS_SET);
    cc = (byte) ((y >> Byte.SIZE) & ALL_BITS_SET);
    cr = (byte) (y & ALL_BITS_SET);
    if (shift + bitLength > Byte.SIZE * 2) {
      bytes[offset / Byte.SIZE] |= cl;
      bytes[offset / Byte.SIZE + 1] |= cc;
      bytes[offset / Byte.SIZE + 2] |= cr;
    } else if (shift + bitLength > Byte.SIZE) {
      bytes[offset / Byte.SIZE] |= cc;
      bytes[offset / Byte.SIZE + 1] |= cr;
    } else {
      bytes[offset / Byte.SIZE] |= cr;
    }
  }

  /**
   * Find the dictionary location of a given word, with length between one and
   * four letters.
   *
   * @param word The word to lookup.
   * @return The location of the word in the dictionary.
   */
  private static int findWordLocation(final String word) {
    assert word != null;
    assert word.length() <= LONG_WORD_LENGTH;
    assert word.length() > 0;
    assert DICTIONARY != null;
    assert DICTIONARY.length > 0;
    assert DICTIONARY.length <= DICTIONARY_LENGTH_WORDS;
    assert Arrays.asList(DICTIONARY).contains(word);

    if (word.length() == LONG_WORD_LENGTH) {
      return findLongWordLocation(word);
    } else if (word.length() < LONG_WORD_LENGTH) {
      return findShortWordLocation(word);
    } else {
      throw new IllegalStateException("Word " + word + " had illegal length.");
    }
  }

  /**
   * Find the dictionary location of a given four-letter word.
   *
   * @param fourLetterWord The word to lookup.
   * @return The location of the
   */
  private static int findLongWordLocation(final String fourLetterWord) {
    assert fourLetterWord != null;
    assert fourLetterWord.length() == LONG_WORD_LENGTH;
    assert DICTIONARY != null;
    assert DICTIONARY.length > 0;
    assert DICTIONARY.length <= DICTIONARY_LENGTH_WORDS;
    return Arrays.binarySearch(
            DICTIONARY,
            LAST_THREE_LETTER_WORD + 1,
            DICTIONARY_LENGTH_WORDS - 1,
            fourLetterWord);
  }

  /**
   * Find the dictionary location of a one, two, or three letter word.
   *
   * @param shortWord The word to lookup.
   * @return The location of the word in the dictionary.
   */
  private static int findShortWordLocation(final String shortWord) {
    assert shortWord != null;
    assert shortWord.length() > 0;
    assert shortWord.length() < LONG_WORD_LENGTH;
    assert DICTIONARY != null;
    assert DICTIONARY.length > 0;
    assert DICTIONARY.length <= DICTIONARY_LENGTH_WORDS;
    return Arrays.binarySearch(
            DICTIONARY,
            0,
            LAST_THREE_LETTER_WORD,
            shortWord);
  }

  /**
   * Determine if two byte arrays are equal in constant time.
   *
   * @param left An array to compare.
   * @param right Another array.
   * @return True, iff the arrays contain equal elements in equal positions.
   */
  public static boolean constantTimeEquals(
          final byte[] left,
          final byte[] right) {
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
