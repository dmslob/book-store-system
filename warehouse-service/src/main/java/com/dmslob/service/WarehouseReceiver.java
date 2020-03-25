package com.dmslob.service;

import com.dmslob.domain.BookOrder;
import com.dmslob.domain.ProcessedBookOrder;
import com.dmslob.enums.OrderState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.listener.adapter.JmsResponse;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class WarehouseReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseReceiver.class);
    private static final String BOOK_QUEUE = "book.order.queue";

    @Autowired
    private WarehouseProcessingService warehouseProcessingService;

    @JmsListener(destination = BOOK_QUEUE)
    public JmsResponse<Message<ProcessedBookOrder>> receive(@Payload BookOrder bookOrder,
                                                            @Header(name = "orderState") OrderState orderState,
                                                            @Header(name = "bookOrderId") String bookOrderId,
                                                            @Header(name = "storeId") String storeId
    ) {
        LOGGER.info("Received message is {}", bookOrder);
        LOGGER.info("Message property orderState = {}, bookOrderId = {}, storeId = {}", orderState, bookOrder, storeId);

        // just for error handling testing
        if (bookOrder.getBook().getTitle().startsWith("L")) {
            throw new IllegalArgumentException("bookOrderId=" + bookOrder.getBookOrderId() + " is of a book not allowed!");
        }

        return warehouseProcessingService.processOrder(bookOrder, orderState, storeId);
    }
}
