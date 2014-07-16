package com.gc.irc.common.utils.encoder.recursive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.irc.common.exception.utils.EncoderException;

/**
 * The Class AbstractStringEncoder.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public abstract class AbstractStringEncoder implements ObjectEncoder, StringEncoder {

    private static final transient Logger LOGGER = LoggerFactory.getLogger(AbstractObjectEncoder.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.common.utils.encoder.IObjectEncoder#encodeClass(java.lang.
     * Class)
     */
    /** {@inheritDoc} */
    public boolean encodeClass(final Class<?> clazz) {
        return String.class.equals(clazz);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.common.utils.encoder.IObjectEncoder#encodeObject(java.lang
     * .Object, com.gc.irc.common.utils.encoder.IStringEncoder)
     */
    /** {@inheritDoc} */
    public Object encodeObject(final Object value, final StringEncoder stringEncoder) throws EncoderException {
        if (value == null) {
            getLog().debug("null entry");
            return null;
        } else if (!encodeClass(value.getClass())) {
            getLog().warn("Not encodable class");
            throw new EncoderException("Not encodable class");
        } else if (stringEncoder != null && !equals(stringEncoder)) {
            return stringEncoder.encode((String) value);
        }
        return encode((String) value);
    }

    /**
     * <p>getLog.</p>
     *
     * @return a {@link org.slf4j.Logger} object.
     */
    public Logger getLog() {
        return LOGGER;
    }

}
