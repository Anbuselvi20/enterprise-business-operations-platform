package com.enterprise.employee.mapper;

import com.enterprise.employee.dto.EmployeeRequest;
import com.enterprise.employee.dto.EmployeeResponse;
import com.enterprise.employee.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "id", ignore = true)
    Employee toEntity(EmployeeRequest request);

    EmployeeResponse toResponse(Employee employee);
}
