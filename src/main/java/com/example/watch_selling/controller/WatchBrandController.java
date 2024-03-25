package com.example.watch_selling.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.WatchBrand;
import com.example.watch_selling.service.WatchBrandService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("api/watch-brand")
public class WatchBrandController {
    private WatchBrandService watchBrandService;

    public WatchBrandController(WatchBrandService watchBrandService) {
        this.watchBrandService = watchBrandService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request success!"),
            @ApiResponse(responseCode = "400", description = "Bad request!"),
            @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<WatchBrand>> getWatchBrandById(@PathVariable UUID id) {
        Optional<WatchBrand> brand = watchBrandService.getWatchBrandById(id);
        if (!brand.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto<>(
                    null,
                    "Cannot find watch brand with the given ID",
                    HttpStatus.NOT_FOUND));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(
                brand.get(),
                "Successfully found the watch type!",
                HttpStatus.OK));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request success!"),
            @ApiResponse(responseCode = "400", description = "Bad request!"),
            @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
    })
    @GetMapping("all")
    public ResponseEntity<ResponseDto<List<WatchBrand>>> getAllWatchType() {
        ResponseDto<List<WatchBrand>> response = watchBrandService.getAllWatchBrands();
        if (!response.getStatus().equals(HttpStatus.OK)) {
            return ResponseEntity.status(response.getStatus()).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request success!"),
            @ApiResponse(responseCode = "400", description = "Bad request!"),
            @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
    })
    @PostMapping("new")
    public ResponseEntity<ResponseDto<WatchBrand>> createNewWatchBrand(
            @RequestBody String newTypeName) {
        ResponseDto<WatchBrand> response = watchBrandService.createNewWatchBrand(newTypeName);
        if (!response.getStatus().equals(HttpStatus.CREATED.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request success!"),
            @ApiResponse(responseCode = "400", description = "Bad request!"),
            @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
    })
    @PatchMapping("update/{id}")
    public ResponseEntity<ResponseDto<WatchBrand>> updateWatchBrandName(
            @PathVariable UUID id,
            @RequestBody String newTypeName) {
        ResponseDto<WatchBrand> response = watchBrandService.updateWatchBrandName(id, newTypeName);
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request success!"),
            @ApiResponse(responseCode = "400", description = "Bad request!"),
            @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
    })
    @DeleteMapping("delete/{id}")
    public ResponseEntity<ResponseDto<String>> updateWatchBrandDeleteStatus(
            @PathVariable UUID id) {
        ResponseDto<String> response = watchBrandService.updateWatchBrandDeleteStatus(id, true);
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
