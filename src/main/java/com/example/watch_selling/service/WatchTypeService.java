package com.example.watch_selling.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.watch_selling.dtos.ResponseDto;
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

    public ResponseDto<WatchType> createNewWatchType(String name) {
        Optional<WatchType> watch = watchTypeRepository.findByName(name);
        if (watch.isPresent()) {
            return new ResponseDto<>(
                null,
                "Type's name must be unique! Invalid type name!",
                HttpStatus.BAD_REQUEST.value()
            );    
        }

        WatchType newType = new WatchType(null, name, false);
        watchTypeRepository.save(newType);

        return new ResponseDto<>(
            newType,
            "New watch type created successfully!",
            HttpStatus.CREATED.value()
        );
    }

    public ResponseDto<WatchType> updateWatchTypeName(UUID id, String name) {
        if (!watchTypeRepository.findById(id).isPresent()) {
            return new ResponseDto<>(
                null,
                "Cannot find the watch type with the given ID! Invalid ID!",
                HttpStatus.BAD_REQUEST.value()
            );
        }
        
        Optional<WatchType> watch = watchTypeRepository.findByName(name);
        if (
            watch.isPresent() &&
            !watch.get().getId().equals(id) &&
            watch.get().getName().equals(name)
        ) {
            return new ResponseDto<>(
                null,
                "Type's name must be unique! Invalid type name!",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        watchTypeRepository.updateNameById(id, name);
        return new ResponseDto<>(
            null,
            "New watch type created successfully!",
            HttpStatus.OK.value()
        );
    }

    public ResponseDto<String> updateWatchTypeDeleteStatus(UUID id, Boolean deleteStatus) {
        watchTypeRepository.updateDeleteStatusbyId(id, deleteStatus);
        return new ResponseDto<>(
            null,
            "Watch type delete status updated successfully!",
            HttpStatus.OK.value()
        );
    }
}
