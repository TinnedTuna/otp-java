package org.turner.opie.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Collection of common, low-level utilities used when generating OPIE one-time
 * passwords.
 *
 * @author turner
 * @since 1.0
 */
public final class OPIEUtils {

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
  private OPIEUtils() {
    throw new IllegalArgumentException("Cannot instantiate OPIEUtils.");
  }

  /**
   * Convert a byte array into a set of words drawn from the DICTIONARY list.
   *
   * @param input The bytes to convert.
   * @return A string of words, separated by spaces, which represents the byte
   *         array.
   */
  public static String bytesToWords(final byte[] input) {
    assert input != null;
    int bitCount = input.length * BITS_PER_BYTE;
    int requiredWords = (bitCount / BITS_PER_DICTIONARY_WORD);
    assert bitCount >= 0;
    assert requiredWords >= 0;

    StringBuilder passwordBuilder = new StringBuilder();
    // TODO, Parity?

    for (int i = 0; i < requiredWords; i++) {
      int bits = extract11BitsFromBytes(input, i * BITS_PER_DICTIONARY_WORD);
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
  public static byte[] wordsToBytes(final String userSuppliedOtp) {
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
    assert false;
    return 1;
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
    assert word.length() <= MAX_WORD_SIZE;
    assert word.length() > 0;
    assert DICTIONARY != null;
    assert DICTIONARY.length > 0;
    assert DICTIONARY.length <= DICTIONARY_SIZE;
    assert Arrays.asList(DICTIONARY).contains(word);

    if (word.length() == MAX_WORD_SIZE) {
      return findLongWordLocation(word);
    } else if (word.length() < MAX_WORD_SIZE) {
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

  /**
   * Compare two byte arrays in constant time.
   *
   * @param left A byte array to compare.
   * @param right A byte array to compare.
   * @return True, iff left contains the same elements as right, in the same
   *         order.
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
