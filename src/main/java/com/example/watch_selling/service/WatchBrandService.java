package com.example.watch_selling.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.WatchBrand;
import com.example.watch_selling.repository.WatchBrandRepository;

@Service
public class WatchBrandService {
    WatchBrandRepository watchBrandRepository;

    public WatchBrandService(WatchBrandRepository watchBrandRepository) {
        this.watchBrandRepository = watchBrandRepository;
    }

    public Optional<WatchBrand> getWatchTypeById(UUID id) {
        return watchBrandRepository.findById(id);
    }

    public Optional<WatchBrand> getWatchTypeByName(String name) {
        return watchBrandRepository.findByName(name);
    }

    public ResponseDto<WatchBrand> createNewWatchType(String name) {
        Optional<WatchBrand> watch = watchBrandRepository.findByName(name);
        if (watch.isPresent()) {
            return new ResponseDto<>(
                null,
                "Type's name must be unique! Invalid brand name!",
                HttpStatus.BAD_REQUEST.value()
            );    
        }

        WatchBrand newBrand = new WatchBrand(null, name, false);
        watchBrandRepository.save(newBrand);

        return new ResponseDto<>(
            newBrand,
            "New watch brand created successfully!",
            HttpStatus.CREATED.value()
        );
    }

    public ResponseDto<WatchBrand> updateWatchTypeName(UUID id, String name) {
        if (!watchBrandRepository.findById(id).isPresent()) {
            return new ResponseDto<>(
                null,
                "Cannot find the watch brand with the given ID! Invalid ID!",
                HttpStatus.BAD_REQUEST.value()
            );
        }
        
        Optional<WatchBrand> watch = watchBrandRepository.findByName(name);
        if (
            watch.isPresent() &&
            !watch.get().getId().equals(id) &&
            watch.get().getName().equals(name)
        ) {
            return new ResponseDto<>(
                null,
                "Brand's name must be unique! Invalid brand name!",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        watchBrandRepository.updateNameById(id, name);
        return new ResponseDto<>(
            null,
            "New watch brand created successfully!",
            HttpStatus.OK.value()
        );
    }

    public ResponseDto<String> updateWatchTypeDeleteStatus(UUID id, Boolean deleteStatus) {
        watchBrandRepository.updateDeleteStatusbyId(id, deleteStatus);
        return new ResponseDto<>(
            null,
            "Watch brand delete status updated successfully!",
            HttpStatus.OK.value()
        );
    }
}
