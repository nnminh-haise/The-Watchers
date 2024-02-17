package com.example.watch_selling.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.dtos.RequestDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.WatchBrand;
import com.example.watch_selling.service.WatchBrandService;

@RestController
@RequestMapping("api/watch/brand")
public class WatchBrandController {
    private WatchBrandService watchBrandService;

    public WatchBrandController(WatchBrandService watchBrandService) {
        this.watchBrandService = watchBrandService;
    }

    @GetMapping("")
    public ResponseEntity<ResponseDto<WatchBrand>> getWatchBrandById(@RequestParam("id") UUID id) {
        Optional<WatchBrand> type = watchBrandService.getWatchTypeById(id);
        if (!type.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto<>(
                null,
                "Cannot find watch type with the given ID",
                HttpStatus.NOT_FOUND.value()
            ));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(
            type.get(),
            "Successfully found the watch type!",
            HttpStatus.OK.value()
        ));
    }
    
    @GetMapping("search")
    public ResponseEntity<ResponseDto<WatchBrand>> getWatchTypeByName(@RequestParam("name") String name) {
        Optional<WatchBrand> type = watchBrandService.getWatchTypeByName(name);
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

    @PutMapping("new")
    public ResponseEntity<ResponseDto<WatchBrand>> createNewWatchType(@RequestBody RequestDto<String> newTypeName) {
        ResponseDto<WatchBrand> response = watchBrandService.createNewWatchType(newTypeName.getData());
        if (!response.getStatus().equals(HttpStatus.CREATED.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);    
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("update")
    public ResponseEntity<ResponseDto<WatchBrand>> updateWatchTypeName(
        @RequestParam("id") UUID id,
        @RequestBody RequestDto<String> newTypeName
    ) {
        ResponseDto<WatchBrand> response = watchBrandService.updateWatchTypeName(id, newTypeName.getData());
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);    
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("delete")
    public ResponseEntity<ResponseDto<String>> updateWatchTypeDeleteStatus(@RequestParam("id") UUID id) {
        ResponseDto<String> response = watchBrandService.updateWatchTypeDeleteStatus(id, true);
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);    
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
