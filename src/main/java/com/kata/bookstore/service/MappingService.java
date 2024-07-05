package com.kata.bookstore.service;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MappingService {

    @Autowired
    private ModelMapper modelMapper;

    public <D, T> List<D> mapList(List<T> entityList, Class<D> outClass) {
        return entityList.stream()
                .map(entity -> modelMapper.map(entity, outClass))
                .collect(Collectors.toList());
    }

    public <D, T> List<T> mapListFromDTO(List<D> dtoList, Class<T> outClass) {
        return dtoList.stream()
                .map(dto -> modelMapper.map(dto, outClass))
                .collect(Collectors.toList());
    }
}
