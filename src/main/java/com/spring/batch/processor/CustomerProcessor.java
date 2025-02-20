package com.spring.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.spring.batch.entity.Customer;

@Component
public class CustomerProcessor implements ItemProcessor<Customer, Customer>
{
    @Override
    public Customer process(Customer customer) throws Exception
    {
        return customer;
    }
}
