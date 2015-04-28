package org.turner.oath.hotp;

import javax.crypto.Mac;

import org.turner.oath.OATHSecretState;
import org.turner.oath.utils.AbstractOATHGenerator;
import org.turner.oath.utils.OATHUtils;

/**
 * Generator for HOTP one-time passwords.
 *
 * @author turner
 * @since 1.0
 */
public class HOTPGenerator extends AbstractOATHGenerator {

    /**
     * Simply calls super.
     *
     * @param mac The MAC algorithm to use for generating OTPs.
     */
    HOTPGenerator(final Mac mac) {
        super(mac);
    }

    @Override
    protected final byte[] getInternalState(final OATHSecretState secretState) {
        assert secretState != null;
        assert secretState instanceof HOTPSecretState;
        HOTPSecretState hotpSecretState = (HOTPSecretState) secretState;
        return OATHUtils.longBytes(hotpSecretState.getOtpsGenerated());
    }
}
