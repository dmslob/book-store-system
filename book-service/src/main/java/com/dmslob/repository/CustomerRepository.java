package com.dmslob.repository;

import com.dmslob.domain.Customer;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public class CustomerRepository {
    private List<Customer> customers = Arrays.asList(
            new Customer("1", "Michael Rodgers"),
            new Customer("2", "Jeff Peek"),
            new Customer("3", "Ana Ryan"),
            new Customer("4", "Eduardo Meyer"),
            new Customer("5", "Lisa Peters")
    );

    public List<Customer> findAll() {
        return customers;
    }
}
