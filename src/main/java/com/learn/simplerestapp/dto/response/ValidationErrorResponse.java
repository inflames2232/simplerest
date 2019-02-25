package com.learn.simplerestapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorResponse {
    private List<FieldError> fieldErrors = new ArrayList<>();
    public void addFieldError(String path, String message) {
        fieldErrors.add(new FieldError(path, message));
    }
}
