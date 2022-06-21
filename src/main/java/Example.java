import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mokusakura.bilive.core.client.SubscribableClient;
import org.mokusakura.bilive.core.client.SubscribableClientImpl;
import org.mokusakura.bilive.core.event.*;
import org.mokusakura.bilive.core.exception.NoNetworkConnectionException;
import org.mokusakura.bilive.core.exception.NoRoomFoundException;
import org.mokusakura.bilive.core.listener.Listener;

import java.io.IOException;
import java.util.concurrent.Semaphore;

public class Example {
    //Replace the room id you want to connect here.
    private static final Long ROOM_ID = 555L;
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(1);
        try (SubscribableClient subscribableClient = new SubscribableClientImpl()) {

            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            subscribableClient.subscribe(CommentEvent.class, new CommentListener());

            //noinspection unchecked
            subscribableClient.subscribe(GenericEvent.class, new MultipleListener());
            // lambda expression can be used here
            subscribableClient.subscribe(StatusChangedEvent.class, event -> {
                if (event instanceof LiveBeginEvent) {
                    logger.info("Live Begin");
                } else if (event instanceof LiveEndEvent) {
                    logger.info("Live End");
                    semaphore.release();
                } else if (event instanceof DisconnectedEvent) {
                    logger.info("Connection is disconnected unexpectedly.");
                } else {
                    logger.info("Other situation.");
                }
            });

            subscribableClient.subscribe(SuperChatEvent.class, event -> logger.info("{} send super chat: {} {}￥",
                                                                                    event.getMessage().getUserInfo().getUname(),
                                                                                    event.getMessage().getMessage(),
                                                                                    event.getMessage().getPrice()));

            try {
                subscribableClient.connect(ROOM_ID);
            } catch (NoNetworkConnectionException | NoRoomFoundException e) {
                e.printStackTrace();
            }
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // A listener to receive event from DanmakuClient
    // Need to implement org.mokusakura.bilive.core.listener.Listener
    // Event class should be inherited from GenericEvent
    static class CommentListener implements Listener<CommentEvent> {
        @Override
        public void onEvent(CommentEvent event) {
            // Log comment text
            logger.info(event.getMessage().getCommentText());
        }
    }

    // To receive all events, raw type should be used
    @SuppressWarnings("rawtypes")
    static class MultipleListener implements Listener {
        private double price = 0.;

        @SuppressWarnings("rawtypes")
        @Override
        public void onEvent(GenericEvent event) {
            double price;
            if (event instanceof SendGiftEvent) {
                // the price of gift and guard is scaled 1000 times
                price = ((SendGiftEvent) event).getMessage().getPrice() / 1000. *
                        ((SendGiftEvent) event).getMessage().getNum();

            } else if (event instanceof BuyGuardEvent) {
                // the price of gift and guard is scaled 1000 times
                price = ((BuyGuardEvent) event).getMessage().getPrice() / 1000. *
                        ((BuyGuardEvent) event).getMessage().getNum();
            } else if (event instanceof SuperChatEvent) {
                price = ((SuperChatEvent) event).getMessage().getPrice().doubleValue();
            } else {
                //Other situation. You can add here.
                price = 0.;
            }
            this.price += price;
        }
    }
}
