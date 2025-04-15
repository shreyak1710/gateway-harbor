
package com.gatewayharbor.customer.controller;

import com.gatewayharbor.customer.model.Customer;
import com.gatewayharbor.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customers")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.findAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable String id) {
        return customerService.findCustomerById(id)
                .map(customer -> new ResponseEntity<>(customer, HttpStatus.OK))
                .orElseGet(() -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Customer not found with id: " + id);
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                });
    }
    
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.saveCustomer(customer);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable String id, @RequestBody Customer customer) {
        return customerService.findCustomerById(id)
                .map(existingCustomer -> {
                    customer.setId(id);
                    customerService.saveCustomer(customer);
                    return new ResponseEntity<>(customer, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Customer not found with id: " + id);
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                });
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable String id) {
        return customerService.findCustomerById(id)
                .map(customer -> {
                    customerService.deleteCustomer(id);
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Customer deleted successfully");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Customer not found with id: " + id);
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                });
    }
}
