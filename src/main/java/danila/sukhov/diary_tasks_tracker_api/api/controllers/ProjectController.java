package danila.sukhov.diary_tasks_tracker_api.api.controllers;

import danila.sukhov.diary_tasks_tracker_api.api.controllers.helpers.ControllerHelper;
import danila.sukhov.diary_tasks_tracker_api.api.dtos.AskDTO;
import danila.sukhov.diary_tasks_tracker_api.api.dtos.ProjectDTO;
import danila.sukhov.diary_tasks_tracker_api.api.exceptions.BadRequestException;
import danila.sukhov.diary_tasks_tracker_api.api.exceptions.NotFoundException;
import danila.sukhov.diary_tasks_tracker_api.api.factories.ProjectDTOFactory;
import danila.sukhov.diary_tasks_tracker_api.store.entities.ProjectEntity;
import danila.sukhov.diary_tasks_tracker_api.store.repositories.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ProjectController {

    ProjectDTOFactory projectDTOFactory;

    ProjectRepository projectRepository;

    ControllerHelper controllerHelper;

    //Посмотреть именование эндпоинтов
    public static final String FETCH_PROJECT = "api/projects/fetch";
    public static final String CREATE_PROJECT = "api/projects/create";
    public static final String UPDATE_PROJECT = "api/project/update/{project_id}";
    public static final String CREATE_OR_UPDATE_PROJECT = "api/projects/create-or-update";
    public static final String DELETE_PROJECT = "api/project/delete/{project_id}";

    @GetMapping(FETCH_PROJECT)
    public List<ProjectDTO> fetchProject(
            @RequestParam(value = "prefix_name", required = false)Optional<String> optionalPrefixName){

        optionalPrefixName = optionalPrefixName.filter(prefixName -> !prefixName.trim().isEmpty());

        Stream<ProjectEntity> projectEntityStream = optionalPrefixName
                .map(projectRepository::streamAllByNameStartsWithIgnoreCase)
                .orElseGet(projectRepository::streamAllBy);

        return projectEntityStream.map(projectDTOFactory::createProjectDto).collect(Collectors.toList());
    }

    @PostMapping(CREATE_PROJECT)
    public ProjectDTO createProject(@RequestParam(value = "project_name") String projectName){

        if(projectName.trim().isEmpty()){
            throw new BadRequestException(String.format("Invalid projectName of project : ", projectName));
        }

        projectRepository.findByName(projectName)
                .ifPresent(project -> {
                    throw new BadRequestException(String.format("Such a projectName \"%s\" already exists", projectName));
                });

        ProjectEntity project = projectRepository.saveAndFlush(
                ProjectEntity.builder()
                            .name(projectName)
                            .build()
        );

        return projectDTOFactory.createProjectDto(project);
    }


    @PatchMapping(UPDATE_PROJECT)
    public ProjectDTO editProject(@PathVariable("project_id") Long projectId,  @RequestParam String name){

        if(name.trim().isEmpty()){
            throw new BadRequestException(String.format("Invalid name of project : ", name));
        }

        ProjectEntity project = controllerHelper.getProjectOrThrowException(projectId);

        projectRepository
                .findByName(name)
                .filter(anotherProject -> !Objects.equals(anotherProject.getId(), projectId))
                .ifPresent(anotherProject -> {
                    throw new BadRequestException(String.format("Such a name \"%s\" already exists", name));
                });

        project.setName(name);
        project = projectRepository.saveAndFlush(project);

        return projectDTOFactory.createProjectDto(project);
    }

    @PutMapping(CREATE_OR_UPDATE_PROJECT)
    public ProjectDTO createOrUpdateProject(
                                            @RequestParam(value = "project_id", required = false) Optional<Long> optionalProjectId,
                                            @RequestParam(value =  "project_name", required = false) Optional<String> optionalProjectName){

        optionalProjectName = optionalProjectName.filter(projectName -> !projectName.trim().isEmpty());

        boolean isCreate = !optionalProjectId.isPresent();

        if(isCreate && !optionalProjectName.isPresent() ){
            throw new BadRequestException(String.format("Project name can't be empty"));
        }

        final ProjectEntity projectEntity = optionalProjectId
                .map(controllerHelper::getProjectOrThrowException)
                .orElseGet(() -> ProjectEntity.builder().build());

        optionalProjectName.ifPresent(projectName -> {
            projectRepository
                    .findByName(projectName)
                    .filter(anotherProject -> !Objects.equals(anotherProject.getId(), projectEntity.getId()))
                    .ifPresent(anotherProject -> {
                        throw new BadRequestException(String.format("Such a name \"%s\" already exists", projectName));
                    });
            projectEntity.setName(projectName);
        });

        final ProjectEntity savedProject = projectRepository.saveAndFlush(projectEntity);
        return projectDTOFactory.createProjectDto(savedProject);
    }

    @DeleteMapping(DELETE_PROJECT)
    public AskDTO deleteProject(@PathVariable("project_id") Long projectId){

        controllerHelper.getProjectOrThrowException(projectId);

        projectRepository.deleteById(projectId);

        return AskDTO.makeDefault(true);
    }


}
