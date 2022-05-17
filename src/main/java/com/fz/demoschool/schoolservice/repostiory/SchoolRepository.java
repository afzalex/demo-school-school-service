package com.fz.demoschool.schoolservice.repostiory;

import com.fz.demoschool.schoolservice.models.SchoolModel;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class SchoolRepository {
    @Getter
    private final List<SchoolModel> list = List.of(
            SchoolModel.builder().id(UUID.randomUUID().toString()).schoolId(1).name("My School 1").build(),
            SchoolModel.builder().id(UUID.randomUUID().toString()).schoolId(2).name("My School 2").build(),
            SchoolModel.builder().id(UUID.randomUUID().toString()).schoolId(3).name("My School 3").build()
    ).stream().collect(Collectors.toList());

    private final AtomicInteger idIncrementer = new AtomicInteger(list.size());

    public SchoolModel add(SchoolModel model) {
        model = model.toBuilder()
                .id(UUID.randomUUID().toString())
                .schoolId(idIncrementer.incrementAndGet())
                .build();
        this.list.add(model);
        return model;
    }
}
