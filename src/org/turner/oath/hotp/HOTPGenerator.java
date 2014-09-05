package org.turner.oath.hotp;

import javax.crypto.Mac;
import org.turner.oath.OATHSecretState;
import org.turner.oath.utils.AbstractOATHGenerator;

/**
 *
 * @author turner
 */
public class HOTPGenerator extends AbstractOATHGenerator {

  public HOTPGenerator(Mac mac) {
    super(mac);
  }

  @Override
  protected long getInternalState(final OATHSecretState secretState) {
    assert secretState instanceof HOTPSecretState;
    HOTPSecretState hotpSecretState = (HOTPSecretState) secretState;
    return hotpSecretState.getCounter();
  }
  
}
