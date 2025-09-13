package com.alexIT.VioletsNeils.service;

import com.alexIT.VioletsNeils.entity.ServiceCategory;
import com.alexIT.VioletsNeils.repository.ServiceCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceCategoryService {

    private final ServiceCategoryRepository repository;

    public ServiceCategoryService(ServiceCategoryRepository repository) {
        this.repository = repository;
    }

    public List<ServiceCategory> findAll() {
       return repository.findAll();
    }
}
