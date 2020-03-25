package com.dmslob.controller;

import com.dmslob.domain.BookOrder;
import com.dmslob.service.BookOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookOrderService bookOrderService;

    @GetMapping("/")
    public String getHome(ModelMap model) {
        model.addAttribute("customers", bookOrderService.getCustomers());
        model.addAttribute("books", bookOrderService.getBooks());
        return "index";
    }

    @RequestMapping(path = "/process/store/{storeId}/order/{orderId}/{customerId}/{bookId}/{orderState}/", method = RequestMethod.GET)
    public @ResponseBody
    String processOrder(@PathVariable("storeId") String storeId,
                        @PathVariable("orderId") String orderId,
                        @PathVariable("customerId") String customerId,
                        @PathVariable("bookId") String bookId,
                        @PathVariable("orderState") String orderState) {

        try {
            LOGGER.info("Stored ID is = {}", storeId);
            BookOrder bookOrder = bookOrderService.build(customerId, bookId, orderId);
            bookOrderService.send(bookOrder, storeId, orderState);
        } catch (Exception ex) {
            LOGGER.warn("Error occurred! = {}", ex.getLocalizedMessage());
            return "Error occurred!" + ex.getLocalizedMessage();
        }
        return "Order sent to warehouse for bookId = " + bookId + " from customerId = " + customerId + " successfully processed!";
    }
}
