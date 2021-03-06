package com.gc.irc.common.utils.encoder.recursive;

import com.gc.irc.common.Loggable;
import com.gc.irc.common.exception.utils.EncoderException;

/**
 * The Interface IObjectEncoder.
 *
 * @version 0.0.4
 * @author x472511
 */
public interface ObjectEncoder extends Loggable {

    /**
     * Check if the class is encodable by the current Encoder
     *
     * @param clazz
     *            the clazz
     * @return a boolean.
     */
    boolean encodeClass(Class<?> clazz);

    /**
     * Encode object.
     *
     * @param value
     *            the value
     * @param stringEncoder
     *            the string encoder
     * @return the object
     * @throws com.gc.irc.common.exception.utils.EncoderException
     *             throh if an error occur
     */
    Object encodeObject(final Object value, final StringEncoder stringEncoder) throws EncoderException;
}
