package com.gc.irc.common.message;


/**
 * The Class BasicClientMessage.
 *
 * @version 0.0.4
 * @author x472511
 */
public class BasicClientMessageLine implements ClientMessageLine {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6741145004079482180L;

    /** The message. */
    private String message;

    /**
     * Instantiates a new basic client message.
     */
    public BasicClientMessageLine() {
        super();
    }

    /**
     * Instantiates a new basic client message.
     *
     * @param message
     *            the message
     */
    public BasicClientMessageLine(String message) {
        super();
        this.message = message;
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message.
     *
     * @param message
     *            the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "BasicClientMessageLine(" + super.toString() + ") [message=" + message + "]";
    }

}
