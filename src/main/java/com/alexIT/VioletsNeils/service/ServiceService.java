package com.alexIT.VioletsNeils.service;

import com.alexIT.VioletsNeils.repository.ServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceService {

    private final ServiceRepository repository;

    public ServiceService(ServiceRepository repository) {
        this.repository = repository;
    }

    public List<com.alexIT.VioletsNeils.entity.Service> findAllByCategoryId(int categoryId) {
        return repository.findAllByCategoryId(categoryId);
    }
}
