package com.tagsoft.registry.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
public class CustomerDataDTO {

    private String country, name, lastName, email, province, city;

    private Set<String> states;

    public void setStates(List<String> states) {
        if (states != null && states.size() > 0)
        this.states = new HashSet<>(states);
    }
}
