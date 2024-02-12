package com.example.watch_selling.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.dtos.WatchInformationDto;
import com.example.watch_selling.service.WatchService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class WatchController {
    private WatchService watchService;

    public WatchController(WatchService watchService) {
        this.watchService = watchService;
    }
    
    @GetMapping("api/watch/all")
    public List<WatchInformationDto> getAllWatches() {
        return watchService.getAllWatches();
    }
    
}
