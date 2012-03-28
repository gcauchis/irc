package com.gc.irc.common.connector;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.gc.irc.common.api.IIRCMessageHandler;
import com.gc.irc.common.api.IIRCMessageSender;
import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.common.utils.IOStreamUtils;

/**
 * The thread wich connects the client to the server, and manages the serialized
 * objects wich are transmitted (they are defined in the com.irc.share.protocol
 * package)
 */
public class ConnectionThread extends Thread implements IIRCMessageSender {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger
			.getLogger(ConnectionThread.class);

	/** The connected to server. */
	private boolean connectedToServer = false;

	/** The authenticated. */
	private boolean authenticated = false;

	/** The waiting for authentication. */
	private boolean waitingForAuthentication = false;

	/** The manual disconnection. */
	private boolean manualDisconnection = false;

	/** The server disconnection. */
	private boolean serverDisconnection = false;

	/** The server name. */
	private String serverName;

	/** The port. */
	private int port;

	/** The host. */
	private InetAddress host = null;

	/** The socket. */
	private Socket socket = null;

	/** The in object. */
	private ObjectInputStream inObject;

	/** The out object. */
	private ObjectOutputStream outObject;

	/** The message handler. */
	private IIRCMessageHandler messageHandler;

	/** The initialized. */
	private boolean initialized;

	/**
	 * Instantiates a new connection thread.
	 * 
	 * @param serverName
	 *            the ip address or name of the server If the server is on
	 *            localhost, out an empty string
	 * @param port
	 *            the port of the connexion
	 */
	public ConnectionThread(final String serverName, final int port) {

		this.serverName = serverName;
		this.port = port;

		if (StringUtils.isEmpty(serverName)) {
			LOGGER.info("The server parameters will be : name=localhost"
					+ " port=" + port);
		} else {
			LOGGER.info("The server parameters will be : name=" + serverName
					+ " port=" + port);
		}

		while (true) {
			LOGGER.debug("Initialisation of the connection thread. Server name/ip : "
					+ serverName + " port : " + port);

			try {
				if (StringUtils.isNotEmpty(serverName)) {
					host = InetAddress.getByName(serverName);
				} else {
					host = InetAddress.getLocalHost();
				}

				break;

			} catch (final UnknownHostException e) {
				LOGGER.error("Impossible to find the host", e);
			}

			try {
				Thread.sleep(2000);
			} catch (final InterruptedException e1) {
				e1.printStackTrace();
			}

		}

	}

	/**
	 * The thread infinite loop. Here, the client will try to connect to the
	 * server (opening a socket, and getting input and output streams). Then,
	 * the loop will wait for new serialized objects sent by the server, and
	 * execute the corresponding actions.
	 */
	@Override
	public void run() {

		while (true) {

			LOGGER.info("Trying to connect");

			try {
				socket = new Socket(host, port);
				setConnectedToServer(true);
				manualDisconnection = false;

				LOGGER.info("Socket successfully created.  Local port : "
						+ socket.getLocalPort());

				/**
				 * Gestion par objet
				 */
				LOGGER.debug("Trying to open streams...");
				outObject = new ObjectOutputStream(socket.getOutputStream());
				LOGGER.debug("Output stream opened");
				inObject = new ObjectInputStream(socket.getInputStream());
				LOGGER.debug("Input stream opened");
				initialized = true;
				while (true) {
					/**
					 * Reception du message.
					 */
					LOGGER.debug("Waiting for an object message");
					IRCMessage messageObject = null;
					try {
						messageObject = IOStreamUtils.receiveMessage(inObject);
					} catch (final ClassNotFoundException e) {
						e.printStackTrace();
						LOGGER.error(e.getMessage());
						break;
					} catch (final StreamCorruptedException e) {
						e.printStackTrace();
						LOGGER.error(e.getMessage());
						break;
					}

					if (messageObject == null) {
						LOGGER.error("Empty IRCMessage Object received");

						try {
							Thread.sleep(500);
						} catch (final InterruptedException e) {
							LOGGER.warn(e.getMessage());
						}

					} else {
						LOGGER.debug("Message received : "
								+ messageObject.getClass());
						if (messageHandler != null) {
							messageHandler.handle(messageObject);
						} else {
							LOGGER.warn("No messageHandler to handle "
									+ messageObject);
						}
					}
				}
			} catch (final IOException e) {
				setConnectedToServer(false);
				setWaitingForAuthentication(false);
				if (!manualDisconnection && isAuthenticated()) {
					setServerDisconnection(true);
				}
				setAuthenticated(false);
				if (!manualDisconnection) {
					LOGGER.error("The connection with the server lost", e);
				}
			}
		}
	}

	/**
	 * Sets the connected to server.
	 * 
	 * @param connectedToServer
	 *            the new connected to server
	 */
	private void setConnectedToServer(final boolean connectedToServer) {
		this.connectedToServer = connectedToServer;
	}

	/**
	 * Is the client currently connected on the server ? (the client may not be
	 * authenticated).
	 * 
	 * @return true, if is connected to server
	 */
	public boolean isConnectedToServer() {
		return connectedToServer;
	}

	/**
	 * Send and IRC Message (will transmit a serialized object to the server).
	 * 
	 * @param message
	 *            the IRC message to send
	 */
	public void sendIRCMessage(final IRCMessage message) {
		try {
			synchronized (inObject) {
				synchronized (outObject) {
					IOStreamUtils.sendMessage(outObject, message);
				}
			}

		} catch (final SocketException e) {
			setConnectedToServer(false);
			setWaitingForAuthentication(false);
			if (!manualDisconnection && isAuthenticated()) {
				setServerDisconnection(true);
			}
			setAuthenticated(false);
			LOGGER.error("Socket error " + e.getMessage());
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the authenticated.
	 * 
	 * @param loggedIn
	 *            the new authenticated
	 */
	private void setAuthenticated(final boolean loggedIn) {
		authenticated = loggedIn;
	}

	/**
	 * Is the client currently authenticated on the server ?.
	 * 
	 * @return true, if is authenticated
	 */
	public boolean isAuthenticated() {
		return authenticated;
	}

	/**
	 * disconnect the client from the server After that the client will
	 * automatically try to reconnect.
	 */
	public void disconnect() {
		try {
			manualDisconnection = true;
			setServerDisconnection(false); // because it is a manual
											// disconnection, from the client
			socket.close();
		} catch (final IOException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
	}

	/**
	 * Sets the waiting for authentication.
	 * 
	 * @param waitingForAuthentication
	 *            the new waiting for authentication
	 */
	public void setWaitingForAuthentication(
			final boolean waitingForAuthentication) {
		this.waitingForAuthentication = waitingForAuthentication;
	}

	/**
	 * Checks if is waiting for authentication.
	 * 
	 * @return true, if is waiting for authentication
	 */
	public boolean isWaitingForAuthentication() {
		return waitingForAuthentication;
	}

	/**
	 * Gets the server name.
	 * 
	 * @return the name or ip address of the remote server
	 */
	public String getServerName() {
		if (!serverName.isEmpty()) {
			return serverName;
		} else {
			return "localhost";
		}
	}

	/**
	 * Gets the port.
	 * 
	 * @return the choosen port for the connexion with the server
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Sets the server disconnection.
	 * 
	 * @param serverDisconnection
	 *            the new server disconnection
	 */
	public void setServerDisconnection(final boolean serverDisconnection) {
		this.serverDisconnection = serverDisconnection;
	}

	/**
	 * Checks if is server disconnection.
	 * 
	 * @return true, if is server disconnection
	 */
	public boolean isServerDisconnection() {
		return serverDisconnection;
	}

	/**
	 * Sets the message handler.
	 * 
	 * @param messageHandler
	 *            the new message handler
	 */
	public void setMessageHandler(final IIRCMessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gc.irc.common.api.IIRCMessageSender#send(com.gc.irc.common.protocol
	 * .IRCMessage)
	 */
	public void send(final IRCMessage message) {
		try {
			/**
			 * Synchronize the socket.
			 */
			if (!socket.isOutputShutdown()) {
				synchronized (inObject) {
					synchronized (outObject) {
						LOGGER.debug("Send message");
						if (socket.isConnected()) {
							if (!socket.isOutputShutdown()) {
								IOStreamUtils.sendMessage(outObject, message);
							} else {
								LOGGER.warn("Output is Shutdown !");
							}
						} else {
							LOGGER.warn("Socket not connected !");
						}
					}
				}
			} else {
				LOGGER.warn("Fail to send message. Finalize because output is shutdown.");
				// TODO close all properly
			}
		} catch (final IOException e) {
			LOGGER.warn("Fail to send the message : " + e.getMessage());
			// TODO check the socket
		}

	}

	public boolean isInitialized() {
		return initialized;
	}

}
