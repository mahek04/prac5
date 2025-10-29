package com.example;

import com.rabbitmq.client.*;
import java.nio.charset.StandardCharsets;

public class StudentConsumer {
    private final static String QUEUE_NAME = "library_books";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost"); // RabbitMQ server
        try (Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            System.out.println(" [*] Student waiting for books...");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Student borrowed book: " + message);
            };

            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
            });
            // Keep program alive
            Thread.currentThread().join();
        }
    }
}
