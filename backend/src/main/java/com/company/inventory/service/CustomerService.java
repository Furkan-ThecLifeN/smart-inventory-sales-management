package com.company.inventory.service;

import com.company.inventory.dto.CustomerRequestDto;
import com.company.inventory.dto.CustomerResponseDto;
import com.company.inventory.mapper.CustomerMapper;
import com.company.inventory.model.Customer;
import com.company.inventory.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Transactional
    public CustomerResponseDto createCustomer(CustomerRequestDto dto) {
        boolean emailExists = customerRepository.findAll().stream()
                .anyMatch(c -> c.getEmail().equals(dto.getEmail()) && !c.isDeleted());

        if (emailExists) {
            throw new RuntimeException("Customer email already exists: " + dto.getEmail());
        }

        Customer customer = customerMapper.toEntity(dto);
        return customerMapper.toDto(customerRepository.save(customer));
    }

    @Transactional(readOnly = true)
    public List<CustomerResponseDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .filter(c -> !c.isDeleted())
                .map(customerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CustomerResponseDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return customerMapper.toDto(customer);
    }
}
