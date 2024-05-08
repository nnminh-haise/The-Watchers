package com.example.watch_selling.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;

import com.example.watch_selling.model.Watch;
import com.example.watch_selling.model.WatchBrand;
import com.example.watch_selling.model.WatchType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class WatchInformationDto {
    private String name;

    private Double price;

    private Integer quantity;

    private String description;

    private String status;

    private String photo;

    private String typeName;

    private String brandName;

    public static Boolean validName(String name) {
        return name != null && !name.isBlank() && !name.isEmpty();
    }

    public static Boolean validPrice(Double price) {
        return price >= 0;
    }

    public static Boolean validQuantity(Integer quantity) {
        return quantity > 0;
    }

    public static Boolean validStatus(String status) {
        if (status == null) {
            return false;
        }

        List<String> values = new ArrayList<>();
        values.add("Đang kinh doanh");
        values.add("Ngừng kinh doanh");

        for (String value : values) {
            if (status.equals(value)) {
                return true;
            }
        }

        return false;
    }

    public static ResponseDto<String> validDto(WatchInformationDto dto) {
        ResponseDto<String> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);
        if (dto == null) {
            return res.setMessage("Invalid DTO!");
        }

        if (!WatchInformationDto.validName(dto.getName())) {
            return res.setMessage("Invalid name");
        }

        if (!WatchInformationDto.validPrice(dto.getPrice())) {
            return res.setMessage("Invalid price");
        }

        if (!WatchInformationDto.validQuantity(dto.getQuantity())) {
            return res.setMessage("Invalid quantity");
        }

        if (!WatchInformationDto.validStatus(dto.getStatus())) {
            return res.setMessage("Invalid status");
        }

        return res
                .setMessage("Valid!")
                .setStatus(HttpStatus.OK);
    }

    public static Optional<Watch> toModel(
            WatchInformationDto dto,
            Boolean deleteStatus,
            WatchType type,
            WatchBrand brand) {
        if (!WatchInformationDto.validDto(dto).getStatus().equals(HttpStatus.OK)) {
            return Optional.empty();
        }

        Watch watch = new Watch();
        watch.setId(null);
        watch.setName(dto.getName());
        watch.setPrice(dto.getPrice());
        watch.setQuantity(dto.getQuantity());
        watch.setDescription(dto.getDescription());
        watch.setStatus(dto.getStatus());
        watch.setIsDeleted(deleteStatus);
        watch.setPhoto(dto.getPhoto());
        watch.setType(type);
        watch.setBrand(brand);
        return Optional.of(watch);
    }

    public static Optional<WatchInformationDto> toDto(Watch watch) {
        if (watch == null) {
            return Optional.empty();
        }

        WatchInformationDto dto = new WatchInformationDto();
        dto.setName(watch.getName());
        dto.setPrice(watch.getPrice());
        dto.setQuantity(watch.getQuantity());
        dto.setDescription(watch.getDescription());
        dto.setStatus(watch.getStatus());
        dto.setPhoto(watch.getPhoto());
        dto.setTypeName(watch.getType().getName());
        dto.setBrandName(watch.getBrand().getName());
        return Optional.of(dto);
    }
}
