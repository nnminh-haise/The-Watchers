package com.example.watch_selling.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.watch_selling.dtos.WatchInformationDto;
import com.example.watch_selling.model.Watch;
import com.example.watch_selling.repository.WatchRepository;

@Service
public class WatchService {
    private WatchRepository watchRepository;

    public WatchService(WatchRepository watchRepository) {
        this.watchRepository = watchRepository;
    }

    public List<WatchInformationDto> getAllWatches() {
        List<Watch> watches = watchRepository.findAllWatchesInformation();
        if (watches.isEmpty()) {
            return List.of();
        }

        List<WatchInformationDto> result = new ArrayList<>();
        for (Watch w: watches) {
            result.add(new WatchInformationDto(w));
        }
        return result;
    }
}
