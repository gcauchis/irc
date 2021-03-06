package com.gc.irc.server.client.connector.factory;

import org.springframework.beans.factory.annotation.Autowired;

import com.gc.irc.common.AbstractLoggable;
import com.gc.irc.server.bridge.ServerBridgeProducer;
import com.gc.irc.server.client.connector.AbstractClientConnection;
import com.gc.irc.server.client.connector.management.UsersConnectionsManagement;
import com.gc.irc.server.core.user.management.UserManagement;
import com.gc.irc.server.core.user.management.UserManagementAware;
import com.gc.irc.server.core.user.management.UserPicturesManagement;
import com.gc.irc.server.service.AuthenticationService;
import com.gc.irc.server.service.UserPictureService;

/**
 * <p>Abstract AbstractClientConnectionFactory class.</p>
 *
 * @author x472511
 * @version 0.0.4
 */
public abstract class AbstractClientConnectionFactory extends AbstractLoggable implements UserManagementAware {

    /** The authentication service. */
    @Autowired
    private AuthenticationService authenticationService;

    /** The jms producer. */
    @Autowired
    private ServerBridgeProducer serverBridgeProducer;

    /** The user picture service. */
    @Autowired
    private UserPictureService userPictureService;

    /** The users connections management. */
    @Autowired
    private UsersConnectionsManagement usersConnectionsManagement;
    
    /** The users pictures management. */
    @Autowired
    private UserPicturesManagement userPicturesManagement;
    
    /** The user management */
    private UserManagement userManagement;

	/**
	 * <p>fillDependency.</p>
	 *
	 * @param clientConnection a {@link com.gc.irc.server.client.connector.AbstractClientConnection} object.
	 */
	protected final void fillDependency(final AbstractClientConnection clientConnection) {
		clientConnection.setUsersConnectionsManagement(usersConnectionsManagement);
        clientConnection.setServerBridgeProducer(serverBridgeProducer);
        clientConnection.setAuthenticationService(authenticationService);
        clientConnection.setUserPictureService(userPictureService);
        clientConnection.setUserManagement(userManagement);
        clientConnection.setUserPicturesManagement(userPicturesManagement);
	}

    /**
     * Sets the authentication service.
     *
     * @param authenticationService
     *            the new authentication service
     */
    public void setAuthenticationService(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Sets the jms producer.
     *
     * @param jmsProducer
     *            the new jms producer
     */
    public void setJmsProducer(final ServerBridgeProducer jmsProducer) {
        this.serverBridgeProducer = jmsProducer;
    }

    /**
     * Sets the user connections management.
     *
     * @param userConnectionsManagement
     *            the new user connections management
     */
    public void setUserConnectionsManagement(final UsersConnectionsManagement userConnectionsManagement) {
        usersConnectionsManagement = userConnectionsManagement;
    }

    /**
     * <p>Setter for the field <code>userPictureService</code>.</p>
     *
     * @param userPictureService
     *            the userPictureService to set
     */
    public void setUserPictureService(final UserPictureService userPictureService) {
        this.userPictureService = userPictureService;
    }

    /**
     * Sets the users connections management.
     *
     * @param usersConnectionsManagement
     *            the new users connections management
     */
    public void setUsersConnectionsManagement(final UsersConnectionsManagement usersConnectionsManagement) {
        this.usersConnectionsManagement = usersConnectionsManagement;
    }
    
    /** {@inheritDoc} */
    @Override
	@Autowired
	public void setUserManagement(UserManagement userManagement) {
		this.userManagement = userManagement;
	}

	/**
	 * <p>Setter for the field <code>userPicturesManagement</code>.</p>
	 *
	 * @param userPicturesManagement a {@link com.gc.irc.server.core.user.management.UserPicturesManagement} object.
	 */
	public void setUserPicturesManagement(UserPicturesManagement userPicturesManagement) {
		this.userPicturesManagement = userPicturesManagement;
	}

}
