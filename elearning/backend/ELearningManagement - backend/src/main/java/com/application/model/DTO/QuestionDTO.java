package com.application.model.DTO;

import java.util.List;

import lombok.Data;

@Data
public class QuestionDTO {
    private String content;
    private List<OptionDTO> options;
}