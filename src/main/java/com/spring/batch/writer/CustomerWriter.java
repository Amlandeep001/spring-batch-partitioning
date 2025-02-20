package com.spring.batch.writer;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.spring.batch.entity.Customer;
import com.spring.batch.repository.CustomerRepository;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class CustomerWriter implements ItemWriter<Customer>
{
	private final CustomerRepository customerRepository;

	public CustomerWriter(CustomerRepository customerRepository)
	{
		this.customerRepository = customerRepository;
	}

	@Override
	public void write(Chunk<? extends Customer> chunk) throws Exception
	{
		log.info("Thread Name : {} ", Thread.currentThread().getName());
		customerRepository.saveAll(chunk);
	}
}
