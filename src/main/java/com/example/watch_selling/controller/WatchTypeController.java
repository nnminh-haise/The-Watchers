package com.example.watch_selling.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.dtos.RequestDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.WatchType;
import com.example.watch_selling.service.WatchTypeService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/watch/type")
public class WatchTypeController {
    private WatchTypeService watchTypeService;

    public WatchTypeController(WatchTypeService watchTypeService) {
        this.watchTypeService = watchTypeService;
    }

    @GetMapping("")
    public ResponseEntity<ResponseDto<WatchType>> getWatchTypeById(@RequestParam("id") UUID id) {
        Optional<WatchType> type = watchTypeService.getWatchTypeById(id);
        if (!type.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto<>(
                null,
                "Cannot find watch type with the given ID",
                HttpStatus.NOT_FOUND
            ));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(
            type.get(),
            "Successfully found the watch type!",
            HttpStatus.OK
        ));
    }
    
    @GetMapping("search")
    public ResponseEntity<ResponseDto<WatchType>> getWatchTypeByName(@RequestParam("name") String name) {
        Optional<WatchType> type = watchTypeService.getWatchTypeByName(name);
        if (!type.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto<>(
                null,
                "Cannot find watch type with the given name",
                HttpStatus.NOT_FOUND
            ));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(
            type.get(),
            "Successfully found the watch type!",
            HttpStatus.OK
        ));
    }

    @GetMapping("all")
    public ResponseEntity<ResponseDto<List<WatchType>>> getAllWatchType() {
        ResponseDto<List<WatchType>> response = watchTypeService.getAllWatchTypes();
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("new")
    public ResponseEntity<ResponseDto<WatchType>> createNewWatchType(@RequestBody RequestDto<String> newTypeName) {
        ResponseDto<WatchType> response = watchTypeService.createNewWatchType(newTypeName.getData());
        if (!response.getStatus().equals(HttpStatus.CREATED.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);    
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("update")
    public ResponseEntity<ResponseDto<WatchType>> updateWatchTypeName(
        @RequestParam("id") UUID id,
        @RequestBody RequestDto<String> newTypeName
    ) {
        ResponseDto<WatchType> response = watchTypeService.updateWatchTypeName(id, newTypeName.getData());
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);    
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("delete")
    public ResponseEntity<ResponseDto<String>> updateWatchTypeDeleteStatus(@RequestParam("id") UUID id) {
        ResponseDto<String> response = watchTypeService.updateWatchTypeDeleteStatus(id, true);
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);    
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
