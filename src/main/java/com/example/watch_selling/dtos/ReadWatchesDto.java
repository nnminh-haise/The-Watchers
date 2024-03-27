package com.example.watch_selling.dtos;

import java.util.List;

import com.example.watch_selling.model.Watch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadWatchesDto {
    private List<Watch> watches;

    private Integer total;
}
