package com.example.watch_selling.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.WatchType;
import com.example.watch_selling.service.WatchTypeService;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class WatchTypeController {
    private WatchTypeService watchTypeService;

    public WatchTypeController(WatchTypeService watchTypeService) {
        this.watchTypeService = watchTypeService;
    }

    @GetMapping("api/watch/type")
    public ResponseEntity<ResponseDto<WatchType>> getWatchTypeByName(@RequestParam String typeName) {
        Optional<WatchType> type = watchTypeService.getWatchTypeByName(typeName);
        if (!type.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto<>(
                null,
                "Cannot find watch type with the given name",
                HttpStatus.NOT_FOUND.value()
            ));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(
            type.get(),
            "Successfully found the watch type!",
            HttpStatus.OK.value()
        ));
    }
    
}
