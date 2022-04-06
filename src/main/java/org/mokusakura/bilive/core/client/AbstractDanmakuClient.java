package org.mokusakura.bilive.core.client;

import org.mokusakura.bilive.core.event.GenericEvent;
import org.mokusakura.bilive.core.event.MessageReceivedEvent;
import org.mokusakura.bilive.core.event.StatusChangedEvent;
import org.mokusakura.bilive.core.exception.NoNetworkConnectionException;
import org.mokusakura.bilive.core.exception.NoRoomFoundException;
import org.mokusakura.bilive.core.factory.BilibiliMessageFactory;
import org.mokusakura.bilive.core.factory.EventFactoryDispatcher;
import org.mokusakura.bilive.core.listener.Listener;
import org.mokusakura.bilive.core.model.BilibiliWebSocketFrame;
import org.mokusakura.bilive.core.model.GenericBilibiliMessage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author MokuSakura
 */
public abstract class AbstractDanmakuClient implements DanmakuClient {
    protected Collection<Listener<MessageReceivedEvent<?>>> messageReceivedListeners;
    protected Collection<Listener<StatusChangedEvent<?>>> statusChangedHandlers;
    protected EventFactoryDispatcher eventFactory;
    protected BilibiliMessageFactory bilibiliMessageFactory;

    public AbstractDanmakuClient(
            Collection<Listener<MessageReceivedEvent<?>>> messageReceivedListeners,
            Collection<Listener<StatusChangedEvent<?>>> statusChangedHandlers,
            EventFactoryDispatcher eventFactory,
            BilibiliMessageFactory bilibiliMessageFactory) {
        this.messageReceivedListeners = messageReceivedListeners;
        this.statusChangedHandlers = statusChangedHandlers;
        this.eventFactory = eventFactory;
        this.bilibiliMessageFactory = bilibiliMessageFactory;
    }

    public AbstractDanmakuClient() {
        this.messageReceivedListeners = new ArrayList<>();
        this.statusChangedHandlers = new ArrayList<>();
    }

    public static void sendMessage(SocketChannel socketChannel, BilibiliWebSocketFrame frame) throws IOException {
        socketChannel.write(ByteBuffer.wrap(frame.getBilibiliWebSocketHeader().array()));
        if (frame.getWebSocketBody() != null) {
            socketChannel.write(ByteBuffer.wrap(frame.getWebSocketBody().array()));
        }
    }

    public static void sendHeartBeat(SocketChannel socketChannel) throws IOException {
        sendMessage(socketChannel, BilibiliWebSocketFrame.newHeartBeat());
    }

    abstract boolean connectToTrueRoomId(long roomId) throws NoNetworkConnectionException;

    abstract long getTrueRoomId(long roomId) throws NoNetworkConnectionException, NoRoomFoundException;

    @Override
    public Collection<Listener<MessageReceivedEvent<?>>> getMessageReceivedListener() {
        return new ArrayList<>(messageReceivedListeners);
    }

    @Override
    public Collection<Listener<StatusChangedEvent<?>>> getStatusChangedListener() {
        return new ArrayList<>(statusChangedHandlers);
    }

    @Override
    public Collection<Listener<MessageReceivedEvent<?>>> clearMessageReceivedListeners() {
        Collection<Listener<MessageReceivedEvent<?>>> res = new ArrayList<>(messageReceivedListeners);
        messageReceivedListeners.clear();
        return res;
    }


    @Override
    public Collection<Listener<StatusChangedEvent<?>>> clearStatusChangedListeners() {
        Collection<Listener<StatusChangedEvent<?>>> res = new ArrayList<>(statusChangedHandlers);
        statusChangedHandlers.clear();
        return res;
    }

    @Override
    public boolean connect(long roomId) throws NoNetworkConnectionException, NoRoomFoundException {
        return connectToTrueRoomId(getTrueRoomId(roomId));
    }

    @Override
    public void addMessageReceivedListener(Listener<MessageReceivedEvent<?>> consumer) {
        messageReceivedListeners.add(consumer);
    }

    @Override
    public void addStatusChangedListener(Listener<StatusChangedEvent<?>> consumer) {
        statusChangedHandlers.add(consumer);
    }

    @Override
    public Listener<MessageReceivedEvent<?>> removeMessageReceivedListener(Listener<MessageReceivedEvent<?>> consumer) {
        if (this.messageReceivedListeners.remove(consumer)) {
            return consumer;
        } else {
            return null;
        }
    }

    @Override
    public Listener<StatusChangedEvent<?>> removeStatusChangedListener(Listener<StatusChangedEvent<?>> consumer) {
        if (this.statusChangedHandlers.remove(consumer)) {
            return consumer;
        } else {
            return null;
        }
    }

    protected void callListeners(long roomId, List<GenericEvent<?>> events) {
        for (var event : events) {
            if (event == null) {
                continue;
            }
            if (event instanceof StatusChangedEvent) {
                for (var listener : statusChangedHandlers) {
                    try {
                        listener.onEvent((StatusChangedEvent<?>) event);
                    } catch (Exception ignored) {
                    }
                }
            } else if (event instanceof MessageReceivedEvent) {
                for (var listener : messageReceivedListeners) {
                    try {
                        listener.onEvent((MessageReceivedEvent<?>) event);
                    } catch (Exception ignored) {
                    }
                }
            }
        }
    }

    protected List<GenericBilibiliMessage> createMessages(BilibiliWebSocketFrame frame) {
        return bilibiliMessageFactory.create(frame);
    }

    protected GenericEvent<?> createEvent(long roomId, GenericBilibiliMessage message) {
        return eventFactory.createEvent(roomId, message);
    }

    protected List<GenericEvent<?>> createEvents(long roomId, List<GenericBilibiliMessage> messages) {
        List<GenericEvent<?>> res = new ArrayList<>(messages.size());
        for (var message : messages) {
            var event = createEvent(roomId, message);
            if (event == null) {
                continue;
            }
            res.add(event);
        }
        return res;
    }

}
