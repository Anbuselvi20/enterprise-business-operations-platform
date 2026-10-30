package com.enterprise.employee.service;

import com.enterprise.employee.dto.EmployeeRequest;
import com.enterprise.employee.dto.EmployeeResponse;
import java.util.List;

public interface EmployeeService {

    EmployeeResponse create(EmployeeRequest request);

    List<EmployeeResponse> findAll();

    EmployeeResponse findById(Long id);

    EmployeeResponse update(Long id, EmployeeRequest request);

    void delete(Long id);
}
