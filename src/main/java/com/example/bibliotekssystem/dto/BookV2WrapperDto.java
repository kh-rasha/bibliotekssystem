package com.example.bibliotekssystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Wrapper response for API v2")
public class BookV2WrapperDto {

    private List<BookV2ResponseDto> data;
    private String version;

    public BookV2WrapperDto() {
    }

    public BookV2WrapperDto(List<BookV2ResponseDto> data, String version) {
        this.data = data;
        this.version = version;
    }

    public List<BookV2ResponseDto> getData() {
        return data;
    }

    public String getVersion() {
        return version;
    }

    public void setData(List<BookV2ResponseDto> data) {
        this.data = data;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
