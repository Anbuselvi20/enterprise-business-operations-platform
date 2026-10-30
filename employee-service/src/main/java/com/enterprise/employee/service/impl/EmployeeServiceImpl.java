package com.enterprise.employee.service.impl;

import com.enterprise.employee.dto.EmployeeRequest;
import com.enterprise.employee.dto.EmployeeResponse;
import com.enterprise.employee.entity.Employee;
import com.enterprise.employee.exception.DuplicateResourceException;
import com.enterprise.employee.exception.ResourceNotFoundException;
import com.enterprise.employee.mapper.EmployeeMapper;
import com.enterprise.employee.repository.EmployeeRepository;
import com.enterprise.employee.service.EmployeeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public EmployeeResponse create(EmployeeRequest request) {
        ensureEmployeeCodeIsAvailable(request.getEmployeeCode(), null);
        ensureEmailIsAvailable(request.getEmail(), null);

        Employee employee = employeeMapper.toEntity(request);
        return employeeMapper.toResponse(employeeRepository.saveAndFlush(employee));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> findAll() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse findById(Long id) {
        return employeeMapper.toResponse(getEmployee(id));
    }

    @Override
    public EmployeeResponse update(Long id, EmployeeRequest request) {
        Employee employee = getEmployee(id);
        ensureEmployeeCodeIsAvailable(request.getEmployeeCode(), id);
        ensureEmailIsAvailable(request.getEmail(), id);

        Employee updatedEmployee = employeeMapper.toEntity(request);
        updatedEmployee.setId(employee.getId());
        return employeeMapper.toResponse(employeeRepository.saveAndFlush(updatedEmployee));
    }

    @Override
    public void delete(Long id) {
        employeeRepository.delete(getEmployee(id));
    }

    private Employee getEmployee(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id: " + id));
    }

    private void ensureEmployeeCodeIsAvailable(String employeeCode, Long excludedId) {
        boolean alreadyExists = excludedId == null
                ? employeeRepository.existsByEmployeeCode(employeeCode)
                : employeeRepository.existsByEmployeeCodeAndIdNot(employeeCode, excludedId);
        if (alreadyExists) {
            throw new DuplicateResourceException(
                    "Employee code '" + employeeCode + "' is already in use.");
        }
    }

    private void ensureEmailIsAvailable(String email, Long excludedId) {
        boolean alreadyExists = excludedId == null
                ? employeeRepository.existsByEmail(email)
                : employeeRepository.existsByEmailAndIdNot(email, excludedId);
        if (alreadyExists) {
            throw new DuplicateResourceException(
                    "Email '" + email + "' is already in use.");
        }
    }
}
