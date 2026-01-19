package com.orion.demo.service;

import com.orion.demo.entity.Customer;
import com.orion.demo.repo.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer create(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public Customer getById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public Customer update(Long id, Customer updated) {
        Customer customer = getById(id);
        customer.setName(updated.getName());
        customer.setEmail(updated.getEmail());
        customer.setPhone(updated.getPhone());
        return customerRepository.save(customer);
    }

    public void delete(Long id) {
        customerRepository.delete(getById(id));
    }
}

