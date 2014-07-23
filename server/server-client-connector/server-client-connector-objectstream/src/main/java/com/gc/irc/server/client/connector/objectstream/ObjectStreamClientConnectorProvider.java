package com.gc.irc.server.client.connector.objectstream;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.gc.irc.server.client.connector.ClientConnector;
import com.gc.irc.server.client.connector.ClientConnectorProvider;

/**
 * <p>ObjectStreamClientConnectorProvider class.</p>
 *
 * @author gcauchis
 * @version 0.0.4
 */
@Component("objectStreamClientConnectorProvider")
public class ObjectStreamClientConnectorProvider implements
		ClientConnectorProvider {

	/**
	 * <p>getClientConnectors.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<ClientConnector> getClientConnectors() {
		ClientConnector clientConnector = new ObjectStreamClientConnector();
		return Arrays.asList(clientConnector);
	}

}
