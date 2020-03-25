package com.dmslob.service;

import com.dmslob.domain.BookOrder;
import com.dmslob.domain.ProcessedBookOrder;
import com.dmslob.enums.OrderState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.listener.adapter.JmsResponse;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class WarehouseProcessingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseProcessingService.class);

    private static final String PROCESSED_QUEUE = "book.order.processed.queue";
    private static final String CANCELED_QUEUE = "book.order.canceled.queue";

    @Transactional
    public JmsResponse<Message<ProcessedBookOrder>> processOrder(BookOrder bookOrder, OrderState orderState, String storeId) {
        Message<ProcessedBookOrder> message;
        JmsResponse<Message<ProcessedBookOrder>> jmsResponse;
        switch (orderState) {
            case NEW:
                message = add(bookOrder, storeId);
                LOGGER.info("Stored ID is ADDED = {}", storeId);
                jmsResponse = JmsResponse.forQueue(message, PROCESSED_QUEUE);
                break;
            case UPDATE:
                message = update(bookOrder, storeId);
                LOGGER.info("Stored ID is UPDATED = {}", storeId);
                jmsResponse = JmsResponse.forQueue(message, PROCESSED_QUEUE);
                break;
            case DELETE:
                message = delete(bookOrder, storeId);
                LOGGER.info("Stored ID is DELETED = {}", storeId);
                jmsResponse = JmsResponse.forQueue(message, CANCELED_QUEUE);
                break;
            default:
                throw new IllegalArgumentException("WarehouseProcessingService.processOrder(...) - orderState does not match expected criteria!");
        }

        return jmsResponse;
    }

    private Message<ProcessedBookOrder> add(BookOrder bookOrder, String storeId) {
        LOGGER.info("ADDING A NEW ORDER TO DB");
        //TODO - some type of db operation
        return build(new ProcessedBookOrder(
                bookOrder,
                new Date(),
                new Date()
        ), "ADDED", storeId);
    }

    private Message<ProcessedBookOrder> update(BookOrder bookOrder, String storeId) {
        LOGGER.info("UPDATING A ORDER TO DB");
        //TODO - some type of db operation
        return build(new ProcessedBookOrder(
                bookOrder,
                new Date(),
                new Date()
        ), "UPDATED", storeId);
    }

    private Message<ProcessedBookOrder> delete(BookOrder bookOrder, String storeId) {
        LOGGER.info("DELETING ORDER FROM DB");
        //TODO - some type of db operation
        return build(new ProcessedBookOrder(
                bookOrder,
                new Date(),
                null
        ), "DELETED", storeId);
    }

    private Message<ProcessedBookOrder> build(ProcessedBookOrder bookOrder, String orderState, String storeId) {
        return MessageBuilder
                .withPayload(bookOrder)
                .setHeader("orderState", orderState)
                .setHeader("storeId", storeId)
                .build();
    }
}
