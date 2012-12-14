package com.acp.acs.common.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acp.acs.common.api.ILoggable;

/**
 * The Class AbstractLoggable.
 */
public abstract class AbstractLoggable implements ILoggable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1350185363531913931L;

    /** The log. */
    private Logger            log              = null;

    /**
     * {@inheritDoc}
     * 
     * @see com.acp.acs.common.api.vision.service.ILoggable#getLog()
     */
    @Override
    public Logger getLog() {
        if (log == null) {
            log = LoggerFactory.getLogger(this.getClass());
        }
        return log;
    }

}
