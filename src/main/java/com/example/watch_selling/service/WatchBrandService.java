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

    public ResponseDto<WatchBrand> findWatchTypeByID(UUID id) {
        ResponseDto<WatchBrand> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);
        if (id == null) {
            return res.setMessage("Invalid ID!");
        }

        Optional<WatchBrand> brand = watchBrandRepository.findById(id);
        if (!brand.isPresent()) {
            return res.setMessage("Cannot find any watch brand with the given ID!");
        }

        return res
                .setData(brand.get())
                .setMessage("Watch brand found successfully!")
                .setStatus(HttpStatus.OK);
    }

    public ResponseDto<List<WatchBrand>> getAllWatchBrands() {
        ResponseDto<List<WatchBrand>> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        List<WatchBrand> brands = watchBrandRepository.findAll();
        if (brands.size() == 0) {
            return res
                    .setMessage("Cannot find any watch brand!")
                    .setStatus(HttpStatus.NOT_FOUND);
        }

        return res
                .setData(brands)
                .setMessage("Watch brands found successfully!")
                .setStatus(HttpStatus.OK);
    }

    public ResponseDto<WatchBrand> createNewWatchBrand(String name) {
        ResponseDto<WatchBrand> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (name.equals(null) || name.length() == 0) {
            return res
                    .setMessage("Brand name must be at least one character! Invalid name!");
        }

        if (watchBrandRepository.findByName(name).isPresent()) {
            return res
                    .setMessage("Brand's name must be unique! Invalid brand name!");
        }

        WatchBrand newBrand = new WatchBrand(null, name, false);
        watchBrandRepository.save(newBrand);

        return res
                .setData(newBrand)
                .setMessage("New watch brand create successfully!")
                .setStatus(HttpStatus.OK);
    }

    public ResponseDto<WatchBrand> updateWatchBrandName(UUID id, String name) {
        ResponseDto<WatchBrand> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (id == null) {
            return res.setMessage("Invalid ID!");
        }

        if (name == null || name.length() == 0) {
            return res.setMessage("Invalid name");
        }

        if (!watchBrandRepository.findById(id).isPresent()) {
            return res
                    .setMessage("Cannot find the watch brand with the given ID! Invalid ID!")
                    .setStatus(HttpStatus.NOT_FOUND);
        }

        Optional<WatchBrand> watch = watchBrandRepository.findByName(name);
        if (watch.isPresent() && !watch.get().getId().equals(id) && watch.get().getName().equals(name)) {
            return res.setMessage("Brand's name must be unique! Invalid brand name!");
        }

        watchBrandRepository.updateNameById(id, name);
        WatchBrand updatedBrand = watch.get();
        updatedBrand.setName(name);
        return res
                .setMessage("New watch brand created successfully!")
                .setStatus(HttpStatus.OK)
                .setData(updatedBrand);
    }

    public ResponseDto<String> updateWatchBrandDeleteStatus(UUID id, Boolean deleteStatus) {
        ResponseDto<String> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);
        if (id == null) {
            return res.setMessage("Invalid ID!");
        }

        if (!watchBrandRepository.findById(id).isPresent()) {
            return res
                    .setMessage("Cannot find the watch brand with the given ID! Invalid ID!")
                    .setStatus(HttpStatus.NOT_FOUND);
        }

        watchBrandRepository.updateDeleteStatusbyId(id, deleteStatus);
        return res
                .setMessage("Watch brand delete status updated successfully!")
                .setStatus(HttpStatus.OK);
    }
}
