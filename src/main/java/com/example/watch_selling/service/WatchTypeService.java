package com.example.watch_selling.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.watch_selling.model.WatchType;
import com.example.watch_selling.repository.WatchTypeRepository;

@Service
public class WatchTypeService {
    WatchTypeRepository watchTypeRepository;

    public WatchTypeService(WatchTypeRepository watchTypeRepository) {
        this.watchTypeRepository = watchTypeRepository;
    }

    public Optional<WatchType> getWatchTypeById(UUID id) {
        return watchTypeRepository.findById(id);
    }

    public Optional<WatchType> getWatchTypeByName(String name) {
        return watchTypeRepository.findByName(name);
    }
}
