package com.example.watch_selling.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.dtos.WatchInformationDto;
import com.example.watch_selling.model.Watch;
import com.example.watch_selling.model.WatchBrand;
import com.example.watch_selling.model.WatchType;
import com.example.watch_selling.repository.WatchBrandRepository;
import com.example.watch_selling.repository.WatchRepository;
import com.example.watch_selling.repository.WatchTypeRepository;

@Service
public class WatchService {
    @Autowired
    private WatchRepository watchRepository;
    
    @Autowired
    private WatchTypeRepository watchTypeRepository;

    @Autowired
    private WatchBrandRepository watchBrandRepository;

    public ResponseDto<Watch> findWatchByName(String name) {
        ResponseDto<Watch> response = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (name.isEmpty() || name.isBlank()) {
            return response.setMessage("Invalid name!");
        }

        Optional<Watch> watch = watchRepository.findByName(name);
        if (!watch.isPresent()) {
            return response.setMessage("Cannot find any watch with the given name!");
        }

        return response
            .setData(watch.get())
            .setStatus(HttpStatus.OK)
            .setMessage("Successfully!");
    }

    public ResponseDto<Watch> findWatchById(UUID id) {
        ResponseDto<Watch> response = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (id.equals(null)) {
            return response.setMessage("Invalid ID!");
        }

        Optional<Watch> watch = watchRepository.findById(id);
        if (!watch.isPresent()) {
            return response.setMessage("Cannot find any watch with the given ID!");
        }

        return response
            .setData(watch.get())
            .setStatus(HttpStatus.OK)
            .setMessage("Successfully!");
    }

    public ResponseDto<List<Watch>> findAll(
        int page, int size, UUID typeId, UUID brandId, String sortBy
    ) {
        ResponseDto<List<Watch>> response = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (!(sortBy.equalsIgnoreCase("desc") || sortBy.equalsIgnoreCase("asc"))) {
            return response.setMessage("Invalid sort by value!");
        }

        Pageable selectingPage = PageRequest.of(page, size);
        List<Watch> watches = sortBy.equalsIgnoreCase("asc") ? watchRepository.findWatchesByTypeASC(typeId, brandId, selectingPage) : watchRepository.findWatchesByTypeDESC(typeId, brandId, selectingPage);

        if (watches == null || watches.isEmpty()) {
            return response.setMessage("Cannot find any watch!");
        }

        return response
            .setData(watches)
            .setStatus(HttpStatus.OK)
            .setMessage("Successfully!");
    }

    public ResponseDto<Watch> createWatch(WatchInformationDto watchInfomation) {
        ResponseDto<Watch> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (watchRepository.findByName(watchInfomation.getName()).isPresent()) {
            return res.setMessage("Duplicated watch's name! Watch's name must be unique!");
        }

        Optional<WatchType> watchType = watchTypeRepository.findByName(watchInfomation.getTypeName());
        if (!watchType.isPresent()) {
            return res
                .setMessage("Cannot find any watch type with the given type name! Invalid type name!")
                .setStatus(HttpStatus.NOT_FOUND);
        }

        Optional<WatchBrand> watchBrand = watchBrandRepository.findByName(watchInfomation.getBrandName());
        if (!watchBrand.isPresent()) {
            return res
                .setMessage("Cannot find any watch brand with the given brand name! Invalid brand name!")
                .setStatus(HttpStatus.NOT_FOUND);
        }
        
        ResponseDto<String> dtoValidationResponse = WatchInformationDto.validDto(watchInfomation);
        if (!dtoValidationResponse.getStatus().equals(HttpStatus.OK)) {
            return res
                .setStatus(dtoValidationResponse.getStatus())
                .setMessage(dtoValidationResponse.getMessage());
        }


        Optional<Watch> newWatch = WatchInformationDto.toModel(
            watchInfomation, false, watchType.get(), watchBrand.get()
        );
        if (!newWatch.isPresent()) {
            return res
                .setMessage("Cannot convert Watch DTO to Watch model!")
                .setStatus(HttpStatus.CONFLICT);
        }

        watchRepository.save(newWatch.get());

        return res
            .setData(newWatch.get())
            .setMessage("New watch created successfully!")
            .setStatus(HttpStatus.OK);
    }

    public ResponseDto<Watch> updateWatchInformation(UUID targetingWatchId, WatchInformationDto newWatchInformation) {
        ResponseDto<Watch> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (targetingWatchId == null) {
            return res.setMessage("Invalid ID!");
        }

        Optional<Watch> watch = watchRepository.findById(targetingWatchId);
        if (!watch.isPresent()) {
            return res
                .setMessage("Cannot find any watch with the given ID!")
                .setStatus(HttpStatus.NOT_FOUND);
        }

        Optional<WatchType> watchType = watchTypeRepository.findByName(newWatchInformation.getTypeName());
        if (!watchType.isPresent()) {
            return res.setMessage("Cannot find any watch type with the given watch type name");
        }

        Optional<WatchBrand> watchBrand = watchBrandRepository.findByName(newWatchInformation.getBrandName());
        if (!watchBrand.isPresent()) {
            return res.setMessage("Cannot find any watch brand with the given watch brand name");
        }

        ResponseDto<String> dtoValidationResponse = WatchInformationDto.validDto(newWatchInformation);
        if (!dtoValidationResponse.getStatus().equals(HttpStatus.OK)) {
            return res
                .setMessage(dtoValidationResponse.getMessage())
                .setStatus(dtoValidationResponse.getStatus());
        }

        watchRepository.updateWatchById(targetingWatchId, newWatchInformation);
        return res
            .setMessage("Watch updated successfuly!")
            .setStatus(HttpStatus.OK)
            .setData(WatchInformationDto.toModel(
                newWatchInformation, false, watchType.get(), watchBrand.get()
            ).get());
    }

    public Integer updateDeleteStatus(UUID id, Boolean status) {
        return watchRepository.updateDeleteStatus(id, status);
    }
}
