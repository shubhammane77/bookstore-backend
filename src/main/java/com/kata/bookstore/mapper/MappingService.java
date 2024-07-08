package com.kata.bookstore.mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// Mapper service to map entity list objects to DTO
@Service
public class MappingService {

    @Autowired
    private ModelMapper modelMapper;

    public <D, T> List<D> mapList(List<T> entityList, Class<D> outClass) {
        return entityList.stream()
                .map(entity -> modelMapper.map(entity, outClass))
                .collect(Collectors.toList());
    }
}
