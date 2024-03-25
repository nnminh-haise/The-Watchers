package com.example.watch_selling.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.WatchType;
import com.example.watch_selling.service.WatchTypeService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/watch-type")
public class WatchTypeController {
    private WatchTypeService watchTypeService;

    public WatchTypeController(WatchTypeService watchTypeService) {
        this.watchTypeService = watchTypeService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request success!"),
            @ApiResponse(responseCode = "400", description = "Bad request!"),
            @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<WatchType>> getWatchTypeById(@PathVariable UUID id) {
        Optional<WatchType> type = watchTypeService.getWatchTypeById(id);
        if (!type.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto<>(
                    null,
                    "Cannot find watch type with the given ID",
                    HttpStatus.NOT_FOUND));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(
                type.get(),
                "Successfully found the watch type!",
                HttpStatus.OK));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request success!"),
            @ApiResponse(responseCode = "400", description = "Bad request!"),
            @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
    })
    @GetMapping("all")
    public ResponseEntity<ResponseDto<List<WatchType>>> getAllWatchType() {
        ResponseDto<List<WatchType>> response = watchTypeService.getAllWatchTypes();
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
    public ResponseEntity<ResponseDto<WatchType>> createNewWatchType(
            @RequestBody String newTypeName) {
        ResponseDto<WatchType> response = watchTypeService.createNewWatchType(newTypeName);
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
    @PatchMapping("update")
    public ResponseEntity<ResponseDto<WatchType>> updateWatchTypeName(
            @RequestParam("id") UUID id,
            @RequestBody String newTypeName) {
        ResponseDto<WatchType> response = watchTypeService.updateWatchTypeName(id, newTypeName);
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
    public ResponseEntity<ResponseDto<String>> updateWatchTypeDeleteStatus(@PathVariable UUID id) {
        ResponseDto<String> response = watchTypeService.updateWatchTypeDeleteStatus(id, true);
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
