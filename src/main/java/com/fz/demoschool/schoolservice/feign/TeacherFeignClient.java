package com.fz.demoschool.schoolservice.feign;

import com.fz.demoschool.core.dto.TeacherModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "${demo.appname.teacher}", path = "/teacher")
public interface TeacherFeignClient {

    @GetMapping("/{schoolId}")
    List<TeacherModel> getTeacherList(@PathVariable("schoolId") Integer schoolId);

}
