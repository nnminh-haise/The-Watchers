package com.example.watch_selling.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.dtos.ReadWatchesDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.dtos.WatchInformationDto;
import com.example.watch_selling.model.Watch;
import com.example.watch_selling.service.WatchService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(path = "api/watch")
public class WatchController {
        @Autowired
        private WatchService watchService;

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Request success!"),
                        @ApiResponse(responseCode = "400", description = "Bad request!"),
                        @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
        })
        @SuppressWarnings("null")
        @GetMapping("/{id}")
        public ResponseEntity<ResponseDto<Watch>> readWatchById(@PathVariable UUID id) {
                ResponseDto<Watch> res = watchService.findWatchById(id);
                return ResponseEntity.status(res.getStatus()).body(res);
        }

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Request success!"),
                        @ApiResponse(responseCode = "400", description = "Bad request!"),
                        @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
        })
        @SuppressWarnings("null")
        @GetMapping("all")
        public ResponseEntity<ResponseDto<ReadWatchesDto>> readAllWatches(
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "10") Integer size,
                        @RequestParam(name = "type_id", required = false) List<UUID> typeIds,
                        @RequestParam(name = "brand_id", required = false) List<UUID> brandIds,
                        @RequestParam(name = "sort_by", defaultValue = "asc", required = false) String sortBy) {
                ResponseDto<ReadWatchesDto> res = watchService.findAll(page - 1, size, typeIds, brandIds, sortBy);
                return ResponseEntity.status(res.getStatus()).body(res);
        }

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Request success!"),
                        @ApiResponse(responseCode = "400", description = "Bad request!"),
                        @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
        })
        @SuppressWarnings("null")
        @PostMapping("new")
        public ResponseEntity<ResponseDto<Watch>> createNewWatch(
                        @RequestBody WatchInformationDto watchInformation) {
                ResponseDto<Watch> res = watchService.createWatch(watchInformation);
                return ResponseEntity.status(res.getStatus()).body(res);
        }

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Request success!"),
                        @ApiResponse(responseCode = "400", description = "Bad request!"),
                        @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
        })
        @SuppressWarnings("null")
        @PatchMapping("update/{id}")
        public ResponseEntity<ResponseDto<Watch>> updateWatchInformation(
                        @PathVariable UUID id,
                        @RequestBody WatchInformationDto newWatchInformation) {
                ResponseDto<Watch> response = watchService.updateWatchInformation(id, newWatchInformation);
                return ResponseEntity.status(response.getStatus()).body(response);
        }

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Request success!"),
                        @ApiResponse(responseCode = "400", description = "Bad request!"),
                        @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
        })
        @SuppressWarnings("null")
        @DeleteMapping("delete/{id}")
        public ResponseEntity<ResponseDto<String>> deleteWatchById(
                        @PathVariable UUID id) {
                ResponseDto<String> res = watchService.updateDeleteStatus(id, true);
                return ResponseEntity.status(res.getStatus()).body(res);
        }
}
