package com.example.watch_selling.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
        Optional<WatchBrand> brand = watchBrandService.getWatchBrandById(id);
        if (!brand.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto<>(
                null,
                "Cannot find watch brand with the given ID",
                HttpStatus.NOT_FOUND.value()
            ));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(
            brand.get(),
            "Successfully found the watch type!",
            HttpStatus.OK.value()
        ));
    }
    
    @GetMapping("search")
    public ResponseEntity<ResponseDto<WatchBrand>> getWatchBrandByName(@RequestParam("name") String name) {
        Optional<WatchBrand> brand = watchBrandService.getWatchBrandByName(name);
        if (!brand.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto<>(
                null,
                "Cannot find watch brand with the given name",
                HttpStatus.NOT_FOUND.value()
            ));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(
            brand.get(),
            "Successfully found the watch brand!",
            HttpStatus.OK.value()
        ));
    }

    @GetMapping("all")
    public ResponseEntity<ResponseDto<List<WatchBrand>>> getAllWatchType() {
        ResponseDto<List<WatchBrand>> response = watchBrandService.getAllWatchBrands();
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("new")
    public ResponseEntity<ResponseDto<WatchBrand>> createNewWatchBrand(@RequestBody RequestDto<String> newTypeName) {
        ResponseDto<WatchBrand> response = watchBrandService.createNewWatchBrand(newTypeName.getData());
        if (!response.getStatus().equals(HttpStatus.CREATED.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);    
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("update")
    public ResponseEntity<ResponseDto<WatchBrand>> updateWatchBrandName(
        @RequestParam("id") UUID id,
        @RequestBody RequestDto<String> newTypeName
    ) {
        ResponseDto<WatchBrand> response = watchBrandService.updateWatchBrandName(id, newTypeName.getData());
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);    
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("delete")
    public ResponseEntity<ResponseDto<String>> updateWatchBrandDeleteStatus(@RequestParam("id") UUID id) {
        ResponseDto<String> response = watchBrandService.updateWatchBrandDeleteStatus(id, true);
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);    
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
