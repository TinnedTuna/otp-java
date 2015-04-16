package org.turner.opie;

import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * Allows one to generate OPIESecretStates from a given secret state, or
 * generate a fresh one.
 *
 * @author turner
 * @since 1.0
 */
public final class OPIEStateManager {

  /** The length of the secret required. */
  private static final int SECRET_LENGTH_BYTES = 32;

  /** The length of the seed required. */
  private static final int SEED_LENGTH_BYTES = 3;

  /**
   * Private constructor, never called.
   */
  private OPIEStateManager() {
    throw new IllegalStateException("OPIEStateManager is a static class and "
                                    + "cannot be instantiated.");
  }

  /**
   * Generate the next state from the current state.
   *
   * @param opieSecretState The current sate for this user.
   * @param opieGenerator The generator for this user's authentication style.
   * @return The new state.
   */
  public static OPIESecretState generateNextState(
          final OPIESecretState opieSecretState,
          final OPIEGenerator opieGenerator) {
    assert opieSecretState != null;
    assert opieGenerator != null;
    byte[] nextSecret = opieGenerator.generateOPIEBytes(opieSecretState);
    return new OPIESecretState(
            nextSecret,
            opieSecretState.getSeed(),
            opieSecretState.getHashCounts() - 1,
            opieSecretState.getMessageDigest());
  }

  /**
   * Generate a new state, usually used to enroll a new user in the system.
   *
   * @param secureRandom The secure random source.
   * @param messageDigest The message digest algorithm to use.
   * @param hashCounter The number of times this hash is to be used.
   * @return The new user's state.
   */
  public static OPIESecretState generateNewState(
          final SecureRandom secureRandom,
          final MessageDigest messageDigest,
          final long hashCounter) {
    assert secureRandom != null;
    assert messageDigest != null;
    assert hashCounter > 0;
    byte[] freshSecret = secureRandom.generateSeed(SECRET_LENGTH_BYTES);
    byte[] freshSeed = secureRandom.generateSeed(SEED_LENGTH_BYTES);
    return new OPIESecretState(
            freshSecret,
            freshSeed,
            hashCounter,
            messageDigest);
  }
}
