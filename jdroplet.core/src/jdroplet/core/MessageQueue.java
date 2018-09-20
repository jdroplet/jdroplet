package jdroplet.core;

import com.rabbitmq.client.*;
import org.apache.commons.lang.SerializationUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by kuibo on 2018/3/27.
 */
public class MessageQueue {
    private static Logger logger = Logger.getLogger(MessageQueue.class);
    private String QUEUE_NAME = "APP_QUEUE";

    private static MessageQueue inst = null;

    private MessageQueue() {}

    public static MessageQueue getInstance() {
        if (inst == null)
            inst = new MessageQueue();

        return inst;
    }

    public void send(ProductHandler product) {
        ConnectionFactory cf = null;
        Connection conn = null;
        Channel channel = null;

        cf = new ConnectionFactory();
        cf.setHost("127.0.0.1");
        cf.setPort(5672);
        cf.setUsername("guest");
        cf.setPassword("guest");

        try {
            conn = cf.newConnection();
            channel = conn.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, SerializationUtils.serialize(product));
        } catch (Exception ex) {
            logger.error("Excerpt:", ex);
        } finally {
            try {channel.close();} catch (Exception e) {}
            try {conn.close();} catch (Exception e) {}
        }
    }

    public void start() {
        ConnectionFactory cf = null;
        Connection conn = null;
        Channel channel = null;

        cf = new ConnectionFactory();
        cf.setHost("127.0.0.1");
        cf.setPort(5672);
        cf.setUsername("guest");
        cf.setPassword("guest");

        try {
            conn = cf.newConnection();
            channel = conn.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {

                    ProductHandler product = null;

                    try {
                        product = (ProductHandler) SerializationUtils.deserialize(body);
                        product.process();
                    } catch (Exception ex) {
                        logger.error("Exception", ex);
                    }
                }
            };
            channel.basicConsume(QUEUE_NAME, true, consumer);
        } catch (Exception e) {
            logger.error("Excerpt:", e);
        }
    }

    public interface ProductHandler extends Serializable {
        void process();
    }
}
