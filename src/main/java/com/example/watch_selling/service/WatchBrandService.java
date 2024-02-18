package com.example.watch_selling.service;

import java.util.List;
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

    public Optional<WatchBrand> getWatchBrandById(UUID id) {
        return watchBrandRepository.findById(id);
    }

    public Optional<WatchBrand> getWatchBrandByName(String name) {
        return watchBrandRepository.findByName(name);
    }

    public ResponseDto<List<WatchBrand>> getAllWatchBrands() {
        List<WatchBrand> types = watchBrandRepository.findAll();
        if (types.size() == 0) {
            return new ResponseDto<>(
                null,
                "Cannot find any watch brand!",
                HttpStatus.NOT_FOUND.value()
            );
        }
        return new ResponseDto<>(
            types,
            "Watch brand found successfullt!",
            HttpStatus.OK.value()
        );
    }

    public ResponseDto<WatchBrand> createNewWatchBrand(String name) {
        if (name.equals(null) || name.length() == 0) {
            return new ResponseDto<>(
                null,
                "Brand name must be at least one character! Invalid name!",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        if (watchBrandRepository.findByName(name).isPresent()) {
            return new ResponseDto<>(
                null,
                "Brand's name must be unique! Invalid brand name!",
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

    public ResponseDto<WatchBrand> updateWatchBrandName(UUID id, String name) {
        if (id.equals(null)) return new ResponseDto<>(
            null,
            "Invalid ID!",
            HttpStatus.BAD_REQUEST.value()
        );

        if (name.equals(null) || name.length() == 0) return new ResponseDto<>(
            null,
            "Name cannot be null! Invalid name!",
            HttpStatus.BAD_REQUEST.value()
        );

        if (!watchBrandRepository.findById(id).isPresent()) return new ResponseDto<>(
            null,
            "Cannot find the watch brand with the given ID! Invalid ID!",
            HttpStatus.BAD_REQUEST.value()
        );
        
        Optional<WatchBrand> watch = watchBrandRepository.findByName(name);
        if (watch.isPresent() && !watch.get().getId().equals(id) && watch.get().getName().equals(name)) {
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

    public ResponseDto<String> updateWatchBrandDeleteStatus(UUID id, Boolean deleteStatus) {
        if (id.equals(null)) return new ResponseDto<>(
            null,
            "Invalid ID!",
            HttpStatus.BAD_REQUEST.value()
        );

        if (!watchBrandRepository.findById(id).isPresent()) return new ResponseDto<>(
            null,
            "Cannot find the watch brand with the given ID! Invalid ID!",
            HttpStatus.BAD_REQUEST.value()
        );

        watchBrandRepository.updateDeleteStatusbyId(id, deleteStatus);
        return new ResponseDto<>(
            null,
            "Watch brand delete status updated successfully!",
            HttpStatus.OK.value()
        );
    }
}
