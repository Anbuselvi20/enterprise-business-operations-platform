package com.enterprise.project.service;

import com.enterprise.project.dto.ProjectRequest;
import com.enterprise.project.dto.ProjectResponse;
import java.util.List;

public interface ProjectService {

    ProjectResponse create(ProjectRequest request);

    List<ProjectResponse> findAll();

    ProjectResponse findById(Long id);

    ProjectResponse update(Long id, ProjectRequest request);

    void delete(Long id);
}
