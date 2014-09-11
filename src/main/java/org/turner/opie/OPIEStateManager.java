package org.turner.opie;

import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 *
 * @author turner
 */
public class OPIEStateManager {

  /**
   * Generate the next state from the current state.
   * 
   * @param opieSecretState The current sate for this user
   * @param opieGenerator The generator for this user's authentication style
   * @return The new state
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
   * @param secureRandom
   * @param messageDigest
   * @param hashCounter
   * @return The new user's state.
   */
  public static OPIESecretState generateNewState(
          final SecureRandom secureRandom,
          final MessageDigest messageDigest,
          final long hashCounter) {
    assert secureRandom != null;
    assert messageDigest != null;
    assert hashCounter > 0;
    byte[] freshSecret = secureRandom.generateSeed(32);
    byte[] freshSeed = secureRandom.generateSeed(3);
    return new OPIESecretState(
            freshSecret, 
            freshSeed, 
            hashCounter, 
            messageDigest);
  }
}
