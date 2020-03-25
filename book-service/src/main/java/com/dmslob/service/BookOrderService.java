package com.dmslob.service;

import com.dmslob.domain.Book;
import com.dmslob.domain.BookOrder;
import com.dmslob.domain.Customer;
import com.dmslob.repository.BookRepository;
import com.dmslob.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class BookOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookOrderService.class);

    private static final String BOOK_QUEUE = "book.order.queue";

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public void send(BookOrder bookOrder, String storeId, String orderState) {
        jmsTemplate.convertAndSend(BOOK_QUEUE, bookOrder, message -> {
            message.setStringProperty("bookOrderId", bookOrder.getBookOrderId());
            message.setStringProperty("storeId", storeId);
            message.setStringProperty("orderState", orderState);

            return message;
        });
    }

    public BookOrder build(String customerId, String bookId, String orderId) {
        Book myBook = null;
        Customer customer = null;

        for (Book book : getBooks()) {
            if (book.getBookId().equalsIgnoreCase(bookId)) {
                myBook = book;
            }
        }

        if (null == myBook) {
            LOGGER.warn("Book selected does not exist in inventory!");
            throw new IllegalArgumentException("Book selected does not exist in inventory!");
        }

        for (Customer c : getCustomers()) {
            if (c.getCustomerId().equalsIgnoreCase(customerId)) {
                customer = c;
            }
        }

        if (null == customer) {
            LOGGER.warn("Customer selected does not appear to be valid!");
            throw new IllegalArgumentException("Customer selected does not appear to be valid!");
        }

        return new BookOrder(orderId, myBook, customer);
    }

    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }
}
