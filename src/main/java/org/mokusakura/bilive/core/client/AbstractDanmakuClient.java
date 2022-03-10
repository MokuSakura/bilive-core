package org.mokusakura.bilive.core.client;

import org.mokusakura.bilive.core.event.MessageReceivedEvent;
import org.mokusakura.bilive.core.event.StatusChangedEvent;
import org.mokusakura.bilive.core.exception.NoNetworkConnectionException;
import org.mokusakura.bilive.core.exception.NoRoomFoundException;
import org.mokusakura.bilive.core.model.BilibiliWebSocketFrame;
import org.mokusakura.bilive.core.model.GenericBilibiliMessage;
import org.mokusakura.bilive.core.model.GenericStatusChangedModel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author MokuSakura
 */
public abstract class AbstractDanmakuClient implements DanmakuClient {
    protected Collection<Consumer<MessageReceivedEvent>> messageReceivedListeners;
    protected Collection<Consumer<StatusChangedEvent>> statusChangedHandlers;

    public AbstractDanmakuClient(
            Collection<Consumer<MessageReceivedEvent>> messageReceivedListeners,
            Collection<Consumer<StatusChangedEvent>> statusChangedHandlers) {
        this.messageReceivedListeners = messageReceivedListeners;
        this.statusChangedHandlers = statusChangedHandlers;
    }

    public AbstractDanmakuClient() {
        this.messageReceivedListeners = new ArrayList<>();
        this.statusChangedHandlers = new ArrayList<>();
    }

    public static void sendMessage(SocketChannel socketChannel, BilibiliWebSocketFrame frame) throws IOException {
        socketChannel.write(ByteBuffer.wrap(frame.getBilibiliWebSocketHeader().array()));
        socketChannel.write(frame.getWebSocketBody());
    }

    abstract boolean connectToTrueRoomId(long roomId) throws NoNetworkConnectionException;

    abstract long getTrueRoomId(long roomId) throws NoNetworkConnectionException, NoRoomFoundException;

    abstract StatusChangedEvent createStatusChangedEvent(GenericStatusChangedModel model);

    abstract MessageReceivedEvent createMessageEvent(GenericBilibiliMessage message);

    @Override
    public Collection<Consumer<MessageReceivedEvent>> clearMessageReceivedListeners() {
        Collection<Consumer<MessageReceivedEvent>> res = new ArrayList<>(messageReceivedListeners);
        messageReceivedListeners.clear();
        return res;
    }


    @Override
    public Collection<Consumer<StatusChangedEvent>> clearStatusChangedListeners() {
        Collection<Consumer<StatusChangedEvent>> res = new ArrayList<>(statusChangedHandlers);
        statusChangedHandlers.clear();
        return res;
    }

    @Override
    public boolean connect(long roomId) throws NoNetworkConnectionException, NoRoomFoundException {
        return connectToTrueRoomId(getTrueRoomId(roomId));
    }

    @Override
    public void addMessageReceivedListener(Consumer<MessageReceivedEvent> consumer) {
        messageReceivedListeners.add(consumer);
    }

    @Override
    public void addStatusChangedListener(Consumer<StatusChangedEvent> consumer) {
        statusChangedHandlers.add(consumer);
    }

    @Override
    public Consumer<MessageReceivedEvent> removeMessageReceivedListener(Consumer<MessageReceivedEvent> consumer) {
        if (this.messageReceivedListeners.remove(consumer)) {
            return consumer;
        } else {
            return null;
        }
    }

    @Override
    public Consumer<StatusChangedEvent> removeStatusChangedListener(Consumer<StatusChangedEvent> consumer) {
        if (this.statusChangedHandlers.remove(consumer)) {
            return consumer;
        } else {
            return null;
        }
    }


    protected void callListeners(List<GenericBilibiliMessage> messages) {
        for (GenericBilibiliMessage message : messages) {
            if (message instanceof GenericStatusChangedModel) {
                statusChangedHandlers.forEach(
                        handler -> handler.accept(createStatusChangedEvent((GenericStatusChangedModel) message)));
            } else {
                messageReceivedListeners.forEach(handler -> handler.accept(createMessageEvent(message)));
            }
        }

    }


}
