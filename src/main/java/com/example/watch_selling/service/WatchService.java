package com.example.watch_selling.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
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
    private WatchRepository watchRepository;
    
    private WatchTypeRepository watchTypeRepository;

    private WatchBrandRepository watchBrandRepository;

    public WatchService(
        WatchRepository watchRepository,
        WatchTypeRepository watchTypeRepository,
        WatchBrandRepository watchBrandRepository
    ) {
        this.watchRepository = watchRepository;
        this.watchTypeRepository = watchTypeRepository;
        this.watchBrandRepository = watchBrandRepository;
    }

    public Optional<WatchInformationDto> getWatchByName(String name) {
        return watchRepository.findByName(name);
    }

    public Optional<WatchInformationDto> getWatchById(UUID id) {
        return watchRepository.findById(id);
    }

    public List<Watch> getAllWatches(int page, int size) {
        List<Watch> watches = watchRepository.findAll(PageRequest.of(page, size)).getContent();
        return watches;
    }

    public ResponseDto<WatchInformationDto> createWatch(WatchInformationDto watchInfomation) {
        if (watchRepository.findByName(watchInfomation.getName()).isPresent()) {
            return new ResponseDto<>(
                null,
                "Duplicated watch name! Watch name must be unique!",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        Optional<WatchType> watchType = watchTypeRepository.findByName(watchInfomation.getType());
        if (!watchType.isPresent()) {
            return new ResponseDto<>(
                null,
                "Invalid type name!",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        Optional<WatchBrand> watchBrand = watchBrandRepository.findByName(watchInfomation.getBrand());
        if (!watchBrand.isPresent()) {
            return new ResponseDto<>(
                null,
                "Invalid brand name!",
                HttpStatus.BAD_REQUEST.value()
            );
        }
        
        if (watchInfomation.getPrice() < 0) {
            return new ResponseDto<>(
                null,
                "Price must greater or equal 0!",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        if (watchInfomation.getQuantity() < 0) {
            return new ResponseDto<>(
                null,
                "Quantity must greater than 0!",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        if (
            !watchInfomation.getStatus().equals("Đang kinh doanh") &&
            !watchInfomation.getStatus().equals("Ngừng kinh doanh")
        ) {
            return new ResponseDto<>(
                null,
                "Invalid status!",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        Watch newWatch = watchInfomation.build();
        newWatch.setId(null);
        newWatch.setType(watchType.get());
        newWatch.setBrand(watchBrand.get());
        watchRepository.save(newWatch);

        return new ResponseDto<>(
            watchInfomation,
            "Created new watch successfully!",
            HttpStatus.CREATED.value()
        );
    }

    public ResponseDto<WatchInformationDto> updateWatchInformation(UUID targetingWatchId, WatchInformationDto newWatchInformation) {
        if (!targetingWatchId.equals(newWatchInformation.getId())) {
            return new ResponseDto<>(
                null,
                "Watch's ID cannot be changed! Invalid watch id!",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        Optional<WatchInformationDto> watch = watchRepository.findByName(newWatchInformation.getName());
        if (
            watch.isPresent() &&
            !watch.get().getId().equals(targetingWatchId) &&
            watch.get().getName().equals(newWatchInformation.getName())
            ) {
            return new ResponseDto<>(
                null,
                "Watch's name has been taken! Invalid watch name!",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        Optional<WatchType> watchType = watchTypeRepository.findByName(newWatchInformation.getType());
        if (!watchType.isPresent()) {
            return new ResponseDto<>(
                null,
                "Invalid type name!",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        Optional<WatchBrand> watchBrand = watchBrandRepository.findByName(newWatchInformation.getBrand());
        if (!watchBrand.isPresent()) {
            return new ResponseDto<>(
                null,
                "Invalid brand name!",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        if (newWatchInformation.getPrice() < 0) {
            return new ResponseDto<>(
                null,
                "Price must greater or equal 0!",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        if (newWatchInformation.getQuantity() < 0) {
            return new ResponseDto<>(
                null,
                "Quantity must greater than 0!",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        if (
            !newWatchInformation.getStatus().equals("Đang kinh doanh") &&
            !newWatchInformation.getStatus().equals("Ngừng kinh doanh")
        ) {
            return new ResponseDto<>(
                null,
                "Invalid status!",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        watchRepository.updateWatchById(newWatchInformation.getId(), newWatchInformation);
        return new ResponseDto<>(
            newWatchInformation,
            "Update watch information successfully!",
            HttpStatus.OK.value()
        );
    }

    public Integer updateDeleteStatus(UUID id, Boolean status) {
        return watchRepository.updateDeleteStatus(id, status);
    }
}
