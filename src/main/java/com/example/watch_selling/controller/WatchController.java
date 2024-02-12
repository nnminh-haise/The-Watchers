package com.example.watch_selling.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.dtos.WatchInformationDto;
import com.example.watch_selling.service.WatchService;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
public class WatchController {
    private WatchService watchService;

    public WatchController(WatchService watchService) {
        this.watchService = watchService;
    }
    
    // @GetMapping("api/watch/all")
    // public List<WatchInformationDto> getAllWatches() {
    //     return watchService.getAllWatches();
    // }

    @GetMapping("api/watch")
    public ResponseEntity<ResponseDto<WatchInformationDto>> getWatchByName(@RequestParam String name) {
        Optional<WatchInformationDto> watch = watchService.getWatchByName(name);
        if (!watch.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto<>(
                null,
                "Cannot found watch with the given name",
                HttpStatus.NOT_FOUND.value()
            ));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto<>(
            watch.get(),
            "Watch found successfully!",
            HttpStatus.FOUND.value()
        ));
    }
    
    
}
