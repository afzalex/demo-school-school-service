package com.fz.demoschool.schoolservice.models;

import com.fz.demoschool.core.TeacherModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SchoolModel {
    private final String id;
    private final int schoolId;
    private final String name;
    private List<TeacherModel> teachers;
}
