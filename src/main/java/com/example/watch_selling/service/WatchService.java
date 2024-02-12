package com.example.watch_selling.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.watch_selling.dtos.WatchInformationDto;
import com.example.watch_selling.repository.WatchRepository;

@Service
public class WatchService {
    private WatchRepository watchRepository;

    public WatchService(WatchRepository watchRepository) {
        this.watchRepository = watchRepository;
    }

    public Optional<WatchInformationDto> getWatchByName(String name) {
        return watchRepository.findByName(name);
    }

    // public List<WatchInformationDto> getAllWatches() {
    //     List<Watch> watches = watchRepository.findAllWatchesInformation();
    //     if (watches.isEmpty()) {
    //         return List.of();
    //     }

    //     List<WatchInformationDto> result = new ArrayList<>();
    //     for (Watch w: watches) {
    //         // result.add(new WatchInformationDto(w));
    //     }
    //     return result;
    // }
}
