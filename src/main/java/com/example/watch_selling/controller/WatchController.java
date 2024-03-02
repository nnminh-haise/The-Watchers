package com.example.watch_selling.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.dtos.RequestDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.dtos.WatchInformationDto;
import com.example.watch_selling.model.Watch;
import com.example.watch_selling.repository.WatchRepository;
import com.example.watch_selling.service.WatchService;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    
    @GetMapping("")
    public ResponseEntity<ResponseDto<Watch>> getFullWatchById(@RequestParam UUID id) {
        ResponseDto<Watch> response = watchService.findWatchById(id);
        if (!response.getStatus().equals(HttpStatus.OK)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("name")
    public ResponseEntity<ResponseDto<Watch>> getFullWatchByName(@RequestParam String name) {
        ResponseDto<Watch> response = watchService.findWatchByName(name);
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("all")
    public ResponseEntity<ResponseDto<List<Watch>>> getFullAllWatch(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        ResponseDto<List<Watch>> response = watchService.findAll(page, size);
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("new")
    public ResponseEntity<ResponseDto<Watch>> createNewWatch(@RequestBody RequestDto<WatchInformationDto> watchInformation) {
        ResponseDto<Watch> response = watchService.createWatch(watchInformation.getData());
        if (response.getStatus() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("update")
    public ResponseEntity<ResponseDto<Watch>> updateWatchInformation(
        @RequestParam("id") UUID id,
        @RequestBody RequestDto<WatchInformationDto> newWatchInformation
    ) {
        ResponseDto<Watch> response = watchService.updateWatchInformation(id, newWatchInformation.getData());
        if (response.getStatus().equals(HttpStatus.BAD_REQUEST.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);    
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @DeleteMapping("delete")
    public ResponseEntity<ResponseDto<String>> deleteWatchById(@RequestParam("id") UUID id) {
        ResponseDto<String> res = new ResponseDto<>();
        Integer removedEntities = watchService.updateDeleteStatus(id, true);
        if (removedEntities == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res
                .setMessage("No record was deleted!")
                .setStatus(HttpStatus.NOT_FOUND)
            );
        }

        return ResponseEntity.status(HttpStatus.OK).body(res
            .setMessage("Watch deleted successfully!")
            .setStatus(HttpStatus.OK)
        );
    }
}
