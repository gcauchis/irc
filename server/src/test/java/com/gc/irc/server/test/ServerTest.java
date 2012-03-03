package com.gc.irc.server.test;

import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gc.irc.common.api.IClientMessageLine;
import com.gc.irc.common.api.IIRCMessageSender;
import com.gc.irc.common.api.impl.BasicClientMessage;
import com.gc.irc.common.connector.ConnectionThread;
import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.common.protocol.chat.IRCMessageChat;
import com.gc.irc.common.protocol.command.IRCMessageCommandLogin;
import com.gc.irc.server.ServerStarter;
import com.gc.irc.server.test.handler.LoginMessageHandler;

public class ServerTest {

	private static Thread starterThread;

	private static ServerStarter starter;

	private ConnectionThread connectionThread;

	@BeforeClass
	public static void init() throws InterruptedException {
		starter = new ServerStarter();
		starterThread = new Thread(starter);
		starterThread.start();
		System.out.println("Wait for server to be up");
		while (!starter.isInitialized()) {
			Thread.sleep(500);
		}
		System.out.println("Server up");
	}

	@Before
	public void prepare() throws InterruptedException {
		connectionThread = new ConnectionThread(null, 1973);
		connectionThread.start();
		System.out.println("Wait for connectionThread to be up");
		while (!connectionThread.isInitialized()) {
			Thread.sleep(500);
		}
		System.out.println("ConnectionThread up");
	}

	@AfterClass
	public static void cleanAll() {
		starterThread.interrupt();
	}

	@After
	public void clean() {
		connectionThread.interrupt();
	}

	@Test
	public void basicTest() throws InterruptedException {
		for (int i = 0; i < 5; i++) {
			System.out.println("send msg");
			sendMessage(connectionThread, getBasicMessage());
			Thread.sleep(500);
		}
	}

	@Test
	public void login() throws InterruptedException {
		connectionThread.setMessageHandler(new LoginMessageHandler());
		final IRCMessage login = new IRCMessageCommandLogin("test", "test");
		sendMessage(connectionThread, login);
		Thread.sleep(1000);
	}

	private void sendMessage(final IIRCMessageSender messageSender,
			final IRCMessage message) {
		messageSender.send(message);
	}

	private IRCMessage getBasicMessage() {
		return new IRCMessageChat(0,
				Arrays.asList((IClientMessageLine) new BasicClientMessage(
						"message")), null);
	}
}
