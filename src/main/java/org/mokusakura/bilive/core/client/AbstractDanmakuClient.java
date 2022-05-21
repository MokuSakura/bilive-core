package org.mokusakura.bilive.core.client;

import org.mokusakura.bilive.core.event.GenericEvent;
import org.mokusakura.bilive.core.event.MessageReceivedEvent;
import org.mokusakura.bilive.core.event.StatusChangedEvent;
import org.mokusakura.bilive.core.exception.NoNetworkConnectionException;
import org.mokusakura.bilive.core.exception.NoRoomFoundException;
import org.mokusakura.bilive.core.factory.BilibiliMessageFactory;
import org.mokusakura.bilive.core.factory.BilibiliMessageFactoryDispatcher;
import org.mokusakura.bilive.core.factory.EventFactoryDispatcher;
import org.mokusakura.bilive.core.listener.Listener;
import org.mokusakura.bilive.core.model.BilibiliWebSocketFrame;
import org.mokusakura.bilive.core.model.GenericBilibiliMessage;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author MokuSakura
 */
public abstract class AbstractDanmakuClient implements DanmakuClient {
    protected Collection<Listener<MessageReceivedEvent<?>>> messageReceivedListeners;
    protected Collection<Listener<StatusChangedEvent<?>>> statusChangedHandlers;
    protected EventFactoryDispatcher eventFactory;
    protected BilibiliMessageFactory bilibiliMessageFactory;
    private final Lock timerLock = new ReentrantLock(true);
    private Timer timer;

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
        this.eventFactory = EventFactoryDispatcher.newDefault();
        this.bilibiliMessageFactory = BilibiliMessageFactoryDispatcher.newDefault();
    }


    abstract boolean connectToTrueRoomId(long roomId) throws NoNetworkConnectionException;

    abstract long getTrueRoomId(long roomId) throws NoNetworkConnectionException, NoRoomFoundException;

    abstract void heartBeatTask();

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

    protected void startHeartBeat() {
        try {
            timerLock.lock();
            if (timer != null) {
                return;
            }
            timer = new Timer(true);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    heartBeatTask();
                }
            }, 0, 20 * 1000);
        } finally {
            timerLock.unlock();
        }
    }

    protected void stopHeartBeat() {
        try {
            timerLock.lock();
            if (timer == null) {
                return;
            }
            timer.cancel();
            timer = null;
        } finally {
            timerLock.unlock();
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
            event.setDanmakuClient(this);
            res.add(event);
        }
        return res;
    }

}
