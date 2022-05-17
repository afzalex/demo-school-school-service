package com.fz.demoschool.schoolservice.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fz.demoschool.core.dto.TeacherModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SchoolModel {
    private final String id;
    private final int schoolId;
    private final String name;
    private List<TeacherModel> teachers;
}
