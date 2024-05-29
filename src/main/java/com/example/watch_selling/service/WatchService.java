package com.example.watch_selling.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.watch_selling.dtos.ReadWatchesDto;
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

    public ResponseDto<Watch> findWatchById(UUID id) {
        ResponseDto<Watch> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (id == null) {
            return res.setMessage("Invalid ID!");
        }

        Optional<Watch> watch = watchRepository.findById(id);
        if (!watch.isPresent()) {
            return res
                    .setMessage("Cannot find any watch with the given ID!")
                    .setStatus(HttpStatus.NOT_FOUND);
        }

        return res
                .setData(watch.get())
                .setStatus(HttpStatus.OK)
                .setMessage("Successful!");
    }

    public ResponseDto<ReadWatchesDto> findAll(
            int page, int size, String name, List<UUID> typeIds, List<UUID> brandIds, String sortBy) {
        ResponseDto<ReadWatchesDto> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);
        ReadWatchesDto dto = new ReadWatchesDto();

        if (!(sortBy.equalsIgnoreCase("desc") || sortBy.equalsIgnoreCase("asc"))) {
            return res.setMessage("Invalid sort by value!");
        }

        Pageable selectingPage = PageRequest.of(page, size);
        List<Watch> watches = watchRepository.findWatchesByTypeAndBrand(
                name, typeIds, brandIds, sortBy, selectingPage);
        Integer total = watchRepository.countWatchesByTypeAndBrand(name, typeIds, brandIds);

        if (watches == null || watches.isEmpty()) {
            dto.setTotal(0);
            dto.setWatches(List.of());
            return res
                    .setData(dto)
                    .setStatus(HttpStatus.OK)
                    .setMessage("Cannot find any watch!");
        }

        dto.setWatches(watches);
        dto.setTotal(total);
        return res
                .setData(dto)
                .setStatus(HttpStatus.OK)
                .setMessage("Successfully!");
    }

    public ResponseDto<Watch> createWatch(WatchInformationDto watchInformation) {
        ResponseDto<Watch> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (watchRepository.findByName(watchInformation.getName()).isPresent()) {
            return res.setMessage("Duplicated watch's name! Watch's name must be unique!");
        }

        Optional<WatchType> watchType = watchTypeRepository.findByName(watchInformation.getTypeName());
        if (!watchType.isPresent()) {
            return res
                    .setMessage("Cannot find any watch type with the given type name! Invalid type name!")
                    .setStatus(HttpStatus.NOT_FOUND);
        }

        Optional<WatchBrand> watchBrand = watchBrandRepository.findByName(watchInformation.getBrandName());
        if (!watchBrand.isPresent()) {
            return res
                    .setMessage("Cannot find any watch brand with the given brand name! Invalid brand name!")
                    .setStatus(HttpStatus.NOT_FOUND);
        }

        ResponseDto<String> dtoValidationResponse = WatchInformationDto.validDto(watchInformation);
        if (!dtoValidationResponse.getStatus().equals(HttpStatus.OK)) {
            return res
                    .setStatus(dtoValidationResponse.getStatus())
                    .setMessage(dtoValidationResponse.getMessage());
        }

        Optional<Watch> newWatch = WatchInformationDto.toModel(
                watchInformation, false, watchType.get(), watchBrand.get());
        if (!newWatch.isPresent()) {
            return res
                    .setStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .setMessage("Cannot convert Watch DTO to Watch model!");
        }

        try {
            watchRepository.save(newWatch.get());
            return res
                    .setData(newWatch.get())
                    .setStatus(HttpStatus.OK)
                    .setMessage("New watch created successfully!");
        } catch (Exception e) {
            return res
                    .setMessage(e.getMessage())
                    .setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseDto<Watch> updateWatchInformation(UUID targetingWatchId, WatchInformationDto newWatchInformation) {
        ResponseDto<Watch> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (targetingWatchId == null) {
            return res.setMessage("Invalid ID!");
        }

        Optional<Watch> existingWatchWithName = watchRepository.findByName(newWatchInformation.getName());
        if (existingWatchWithName.isPresent() && !existingWatchWithName.get().getId().equals(targetingWatchId)) {
            return res.setMessage("Duplicated watch's name! Watch's name must be unique!");
        }

        Optional<Watch> targetingWatch = watchRepository.findById(targetingWatchId);
        if (!targetingWatch.isPresent()) {
            return res
                    .setStatus(HttpStatus.NOT_FOUND)
                    .setMessage("Cannot find any watch with the given ID!");
        }

        Optional<WatchType> updatedWatchType = watchTypeRepository.findByName(newWatchInformation.getTypeName());
        if (!updatedWatchType.isPresent()) {
            return res.setMessage("Cannot find any watch type with the given watch type name");
        }

        Optional<WatchBrand> updatedWatchBrand = watchBrandRepository.findByName(newWatchInformation.getBrandName());
        if (!updatedWatchBrand.isPresent()) {
            return res.setMessage("Cannot find any watch brand with the given watch brand name");
        }

        ResponseDto<String> dtoValidationResponse = WatchInformationDto.validDto(newWatchInformation);
        if (!dtoValidationResponse.getStatus().equals(HttpStatus.OK)) {
            return res
                    .setStatus(dtoValidationResponse.getStatus())
                    .setMessage(dtoValidationResponse.getMessage());
        }

        try {
            watchRepository.updateWatchById(targetingWatchId, newWatchInformation);
            return res
                    .setStatus(HttpStatus.OK)
                    .setMessage("Watch updated successfully!")
                    .setData(watchRepository.findById(targetingWatchId).get());
        } catch (Exception e) {
            return res
                    .setMessage(e.getMessage())
                    .setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseDto<String> updateWatchQuantityById(UUID id, Integer newQuantity) {
        ResponseDto<String> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);
        if (id == null) {
            return res.setMessage("Invalid ID");
        }

        if (newQuantity < 0) {
            return res.setMessage("Invalid quantity!");
        }

        Optional<Watch> targetingWatch = watchRepository.findById(id);
        if (!targetingWatch.isPresent()) {
            return res
                    .setStatus(HttpStatus.NOT_FOUND)
                    .setMessage("Cannot find any watch with the given ID!");
        }

        try {
            watchRepository.updateWatchQuantityById(id, newQuantity);
            return res
                    .setStatus(HttpStatus.OK)
                    .setMessage("Success!");
        } catch (Exception e) {
            return res
                    .setStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .setMessage(e.getMessage());
        }
    }

    public ResponseDto<String> updateDeleteStatus(UUID id, Boolean status) {
        ResponseDto<String> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);
        if (id == null) {
            return res.setMessage("Invalid ID!");
        }
        try {
            watchRepository.updateDeleteStatus(id, status);
        } catch (Exception e) {
            return res.setMessage(e.getMessage());
        }

        return res
                .setStatus(HttpStatus.OK)
                .setMessage("Watch deleted successfully!");
    }
}
