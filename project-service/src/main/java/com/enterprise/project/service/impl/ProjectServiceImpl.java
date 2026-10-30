package com.enterprise.project.service.impl;

import com.enterprise.project.dto.ProjectRequest;
import com.enterprise.project.dto.ProjectResponse;
import com.enterprise.project.entity.Project;
import com.enterprise.project.exception.DuplicateResourceException;
import com.enterprise.project.exception.InvalidRequestException;
import com.enterprise.project.exception.ResourceNotFoundException;
import com.enterprise.project.repository.ProjectRepository;
import com.enterprise.project.service.ProjectService;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public ProjectResponse create(ProjectRequest request) {
        validateDateRange(request);
        String projectCode = normalizeProjectCode(request.projectCode());
        if (projectRepository.existsByProjectCodeIgnoreCase(projectCode)) {
            throw new DuplicateResourceException("Project code is already in use.");
        }

        Project project = new Project();
        applyRequest(project, request, projectCode);
        return toResponse(projectRepository.saveAndFlush(project));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> findAll() {
        return projectRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponse findById(Long id) {
        return toResponse(getProject(id));
    }

    @Override
    public ProjectResponse update(Long id, ProjectRequest request) {
        Project project = getProject(id);
        validateDateRange(request);
        String projectCode = normalizeProjectCode(request.projectCode());
        if (projectRepository.existsByProjectCodeIgnoreCaseAndIdNot(projectCode, id)) {
            throw new DuplicateResourceException("Project code is already in use.");
        }

        applyRequest(project, request, projectCode);
        return toResponse(projectRepository.saveAndFlush(project));
    }

    @Override
    public void delete(Long id) {
        projectRepository.delete(getProject(id));
    }

    private Project getProject(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Project not found with id: " + id));
    }

    private void validateDateRange(ProjectRequest request) {
        if (request.startDate() != null
                && request.endDate() != null
                && request.endDate().isBefore(request.startDate())) {
            throw new InvalidRequestException("endDate must be on or after startDate.");
        }
    }

    private void applyRequest(Project project, ProjectRequest request, String projectCode) {
        project.setProjectCode(projectCode);
        project.setProjectName(request.projectName().trim());
        project.setClientId(request.clientId());
        project.setProjectManagerId(request.projectManagerId());
        project.setStartDate(request.startDate());
        project.setEndDate(request.endDate());
        project.setBudget(request.budget());
        project.setProjectStatus(request.projectStatus());
        project.setDescription(request.description() == null
                ? null
                : request.description().trim());
    }

    private ProjectResponse toResponse(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getProjectCode(),
                project.getProjectName(),
                project.getClientId(),
                project.getProjectManagerId(),
                project.getStartDate(),
                project.getEndDate(),
                project.getBudget(),
                project.getProjectStatus(),
                project.getDescription());
    }

    private String normalizeProjectCode(String projectCode) {
        return projectCode.trim().toUpperCase(Locale.ROOT);
    }
}
