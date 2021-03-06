package com.gc.irc.server.handler.message;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;

import com.gc.irc.common.entity.User;
import com.gc.irc.common.protocol.chat.MessageChat;
import com.gc.irc.server.client.connector.management.UsersConnectionsManagement;
import com.gc.irc.server.core.user.management.UserManagement;
import com.gc.irc.server.handler.message.test.AbstractMessageHandlerTest;

/**
 * The Class IRCMessageChatHandlerTest.
 *
 * @version 0.0.4
 * @since 0.0.4
 */
public class MessageChatHandlerTest extends AbstractMessageHandlerTest<MessageChatHandler, MessageChat> {

    /** The users connections management. */
    private UsersConnectionsManagement usersConnectionsManagement;
    
    /** The user management */
    private UserManagement userManagement;

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.handler.message.test.api.AbstractIRCMessageHandlerTest#buildMessageInstance()
     */
    /** {@inheritDoc} */
    @Override
    protected MessageChat buildMessageInstance() {
        return buildMessageInstance(-1);
    }

    private MessageChat buildMessageInstance(final int userID) {
        return new MessageChat(userID, null);
    }

    /**
     * Handle.
     */
    @Test
    public void handle() {
        User sender = new User(1, "test");
        MessageChat messageChat = buildMessageInstance(1);
        expect(userManagement.getUser(1)).andReturn(sender);
        usersConnectionsManagement.sendMessageToAllUsers(messageChat);
        replay(usersConnectionsManagement, userManagement);

        getMessageHandler().handle(messageChat);

        verify(usersConnectionsManagement, userManagement);
    }

    /**
     * Handle user not exit.
     */
    @Test
    public void handleUserNotExit() {
        MessageChat messageChat = buildMessageInstance(1);
        expect(userManagement.getUser(1)).andReturn(null);
        replay(userManagement);

        getMessageHandler().handle(messageChat);

        verify(userManagement);
    }

    /**
     * Inits the.
     */
    @Before
    public void init() {
        MessageChatHandler ircMessageChatHandler = new MessageChatHandler();
        usersConnectionsManagement = createMock(UsersConnectionsManagement.class);
        ircMessageChatHandler.setUsersConnectionsManagement(usersConnectionsManagement);
        userManagement = createMock(UserManagement.class);
        ircMessageChatHandler.setUserManagement(userManagement);
        setMessageHandler(ircMessageChatHandler);
    }

}
