package com.example.watch_selling.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.dtos.RequestDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.dtos.WatchInformationDto;
import com.example.watch_selling.model.Watch;
import com.example.watch_selling.service.WatchService;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(path = "api/watch")
public class WatchController {
    @Autowired
    private WatchService watchService;

    @SuppressWarnings("null")
    @GetMapping("")
    public ResponseEntity<ResponseDto<Watch>> readWatchById(
            @RequestParam UUID id) {
        ResponseDto<Watch> res = watchService.findWatchById(id);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @SuppressWarnings("null")
    @GetMapping("all")
    public ResponseEntity<ResponseDto<List<Watch>>> readAllWatches(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "type_id", required = false) UUID typeId,
            @RequestParam(name = "brand_id", required = false) UUID brandId,
            @RequestParam(name = "sort_by", defaultValue = "asc", required = false) String sortBy) {
        ResponseDto<List<Watch>> res = watchService.findAll(page, size, typeId, brandId, sortBy);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @SuppressWarnings("null")
    @PutMapping("new")
    public ResponseEntity<ResponseDto<Watch>> createNewWatch(
            @RequestBody RequestDto<WatchInformationDto> watchInformation) {
        ResponseDto<Watch> res = watchService.createWatch(watchInformation.getData());
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @SuppressWarnings("null")
    @PatchMapping("update")
    public ResponseEntity<ResponseDto<Watch>> updateWatchInformation(
            @RequestParam("id") UUID id,
            @RequestBody RequestDto<WatchInformationDto> newWatchInformation) {
        ResponseDto<Watch> response = watchService.updateWatchInformation(id, newWatchInformation.getData());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @SuppressWarnings("null")
    @DeleteMapping("delete")
    public ResponseEntity<ResponseDto<String>> deleteWatchById(
            @RequestParam("id") UUID id) {
        ResponseDto<String> res = watchService.updateDeleteStatus(id, true);
        return ResponseEntity.status(res.getStatus()).body(res);
    }
}
