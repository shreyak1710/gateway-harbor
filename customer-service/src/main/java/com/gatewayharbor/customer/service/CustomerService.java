
package com.gatewayharbor.customer.service;

import com.gatewayharbor.customer.model.Customer;
import com.gatewayharbor.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    
    public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }
    
    public Optional<Customer> findCustomerById(String id) {
        return customerRepository.findById(id);
    }
    
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
    
    public void deleteCustomer(String id) {
        customerRepository.deleteById(id);
    }
    
    public Optional<Customer> findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
}
