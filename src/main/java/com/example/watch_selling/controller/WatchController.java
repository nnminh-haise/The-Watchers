package com.example.watch_selling.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.dtos.RequestDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.dtos.WatchInformationDto;
import com.example.watch_selling.model.Watch;
import com.example.watch_selling.service.WatchService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping(path = "api/watch")
public class WatchController {
    private WatchService watchService;

    public WatchController(WatchService watchService) {
        this.watchService = watchService;
    }
    
    @GetMapping("all")
    public ResponseEntity<ResponseDto<List<Watch>>> getAllWatches(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        
        List<Watch> watches = watchService.getAllWatches(page, size);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(
            watches,
            "Request process complete!",
            HttpStatus.OK.value()
        ));
    }

    @GetMapping("info")
    public ResponseEntity<ResponseDto<WatchInformationDto>> getWatchById(@RequestParam("id") UUID id) {
        Optional<WatchInformationDto> watch = watchService.getWatchById(id);
        if (!watch.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto<>(
                null,
                "Cannot found watch with the given name!",
                HttpStatus.NOT_FOUND.value()
            ));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(
            watch.get(),
            "Watch found successfully!",
            HttpStatus.OK.value()
        ));
    }

    @PutMapping("new")
    public ResponseEntity<ResponseDto<WatchInformationDto>> createNewWatch(@RequestBody RequestDto<WatchInformationDto> watchInformation) {
        // System.out.println("bug: " + watchInformation.getData().getName());
        ResponseDto<WatchInformationDto> response = watchService.createWatch(watchInformation.getData());
        if (response.getStatus() == HttpStatus.BAD_REQUEST.value()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("update")
    public ResponseEntity<ResponseDto<WatchInformationDto>> updateWatchInformation(
        @RequestParam("id") UUID id,
        @RequestBody RequestDto<WatchInformationDto> newWatchInformation
    ) {
        ResponseDto<WatchInformationDto> response = watchService.updateWatchInformation(id, newWatchInformation.getData());
        if (response.getStatus().equals(HttpStatus.BAD_REQUEST.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);    
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @DeleteMapping("delete")
    public ResponseEntity<ResponseDto<String>> deleteWatchById(@RequestParam("id") UUID id) {
        Integer removedEntities = watchService.updateDeleteStatus(id, true);
        if (removedEntities == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto<>(
                null,
                "No record was deleted!",
                HttpStatus.NOT_FOUND.value()
            ));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(
            "Deteled watch Id: " + id,
            "Delete process successfully!",
            HttpStatus.OK.value()
        ));
    }
}
