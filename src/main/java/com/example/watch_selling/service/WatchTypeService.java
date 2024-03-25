package com.example.watch_selling.service;

import java.util.List;
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

    public ResponseDto<WatchType> findWatchTypeByID(UUID id) {
        ResponseDto<WatchType> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);
        if (id == null) {
            return res.setMessage("Invalid ID!");
        }

        Optional<WatchType> type = watchTypeRepository.findById(id);
        if (!type.isPresent()) {
            return res.setMessage("Cannot find any watch type with the given ID!");
        }

        return res
                .setData(type.get())
                .setMessage("Watch type found successfully!")
                .setStatus(HttpStatus.OK);
    }

    public ResponseDto<List<WatchType>> getAllWatchTypes() {
        ResponseDto<List<WatchType>> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        List<WatchType> types = watchTypeRepository.findAll();
        if (types.size() == 0) {
            return res
                    .setMessage("Cannot find any watch type!")
                    .setStatus(HttpStatus.NOT_FOUND);
        }

        return res
                .setData(types)
                .setMessage("Watch types found successfully!")
                .setStatus(HttpStatus.OK);
    }

    public ResponseDto<WatchType> createNewWatchType(String name) {
        ResponseDto<WatchType> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (name.equals(null) || name.length() == 0) {
            return res
                    .setMessage("Type name must be at least one character! Invalid name!");
        }

        if (watchTypeRepository.findByName(name).isPresent()) {
            return res
                    .setMessage("Type's name must be unique! Invalid type name!");
        }

        WatchType newType = new WatchType(null, name, false);
        watchTypeRepository.save(newType);

        return res
                .setData(newType)
                .setMessage("New watch type create successfully!")
                .setStatus(HttpStatus.OK);
    }

    public ResponseDto<WatchType> updateWatchTypeName(UUID id, String name) {
        ResponseDto<WatchType> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (id == null) {
            return res.setMessage("Invalid ID!");
        }

        if (name == null || name.length() == 0) {
            return res.setMessage("Invalid name");
        }

        if (!watchTypeRepository.findById(id).isPresent()) {
            return res
                    .setMessage("Cannot find the watch type with the given ID! Invalid ID!")
                    .setStatus(HttpStatus.NOT_FOUND);
        }

        Optional<WatchType> watch = watchTypeRepository.findByName(name);
        if (watch.isPresent() && !watch.get().getId().equals(id) && watch.get().getName().equals(name)) {
            return res.setMessage("Type's name must be unique! Invalid type name!");
        }

        watchTypeRepository.updateNameById(id, name);
        WatchType updatedType = watch.get();
        updatedType.setName(name);
        return res
                .setMessage("New watch type created successfully!")
                .setStatus(HttpStatus.OK)
                .setData(updatedType);
    }

    public ResponseDto<String> updateWatchTypeDeleteStatus(UUID id, Boolean deleteStatus) {
        ResponseDto<String> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);
        if (id == null) {
            return res.setMessage("Invalid ID!");
        }

        if (!watchTypeRepository.findById(id).isPresent()) {
            return res
                    .setMessage("Cannot find the watch type with the given ID! Invalid ID!")
                    .setStatus(HttpStatus.NOT_FOUND);
        }

        watchTypeRepository.updateDeleteStatusbyId(id, deleteStatus);
        return res
                .setMessage("Watch type delete status updated successfully!")
                .setStatus(HttpStatus.OK);
    }
}
