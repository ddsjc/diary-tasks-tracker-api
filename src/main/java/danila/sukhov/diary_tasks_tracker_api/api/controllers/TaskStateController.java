package danila.sukhov.diary_tasks_tracker_api.api.controllers;

import danila.sukhov.diary_tasks_tracker_api.api.controllers.helpers.ControllerHelper;
import danila.sukhov.diary_tasks_tracker_api.api.dtos.TaskStateDTO;
import danila.sukhov.diary_tasks_tracker_api.api.exceptions.BadRequestException;
import danila.sukhov.diary_tasks_tracker_api.api.exceptions.NotFoundException;
import danila.sukhov.diary_tasks_tracker_api.api.factories.ProjectDTOFactory;
import danila.sukhov.diary_tasks_tracker_api.api.factories.TaskStateDTOFactory;
import danila.sukhov.diary_tasks_tracker_api.store.entities.ProjectEntity;
import danila.sukhov.diary_tasks_tracker_api.store.entities.TaskEntity;
import danila.sukhov.diary_tasks_tracker_api.store.entities.TaskStateEntity;
import danila.sukhov.diary_tasks_tracker_api.store.repositories.ProjectRepository;
import danila.sukhov.diary_tasks_tracker_api.store.repositories.TaskStateRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskStateController {
    TaskStateRepository taskStateRepository;
    TaskStateDTOFactory taskStateDTOFactory;

    ControllerHelper controllerHelper;

    public static final String GET_TASK_STATES = "api/projects/fetch/{project_id}/task-states";
    public static final String CREATE_TASK_STATE = "api/projects/create/{project_id}/task-states";
    public static final String UPDATE_TASK_STATE = "api/task-state/{task_state_id}";
    public static final String CHANGE_TASK_STATE_POSITION = "api/task-state/{task_state_id}/position/change";

    @GetMapping(GET_TASK_STATES)
    public List<TaskStateDTO> getTaskStates(@PathVariable(name = "project_id") Long project_id){
        ProjectEntity project = controllerHelper.getProjectOrThrowException(project_id);

        return project.getTaskStates().stream().map(taskStateDTOFactory::createTaskStateDTO).collect(Collectors.toList());
    }

    @PostMapping(CREATE_TASK_STATE)
    public TaskStateDTO createTaskState(@PathVariable(name = "project_id" )Long projectId,
                                        @RequestParam(name = "task_state_name") String taskStateName) {

        if(taskStateName.trim().isBlank()){
            throw new BadRequestException("Task state name is empty!");
        }

        ProjectEntity project = controllerHelper.getProjectOrThrowException(projectId);

        Optional<TaskStateEntity> optionalTaskStateEntity = Optional.empty();

        for(TaskStateEntity taskState: project.getTaskStates()){

            if(taskState.getName().equalsIgnoreCase(taskStateName)){
                throw new BadRequestException(String.format("Task state with this name (\"%s\") already exists!", taskStateName));
            }

            if(!taskState.getRightTaskState().isPresent()){
                optionalTaskStateEntity = Optional.of(taskState);
                break;
            }
        }
        TaskStateEntity taskStateEntity = taskStateRepository.saveAndFlush(
                TaskStateEntity.builder()
                        .name(taskStateName)
                        .project(project)
                        .build()
                );

        optionalTaskStateEntity
                .ifPresent(anotherTaskState -> {
                     taskStateEntity.setLeftTaskState(anotherTaskState);
                     anotherTaskState.setRightTaskState(taskStateEntity);
                     taskStateRepository.saveAndFlush(anotherTaskState);
        });

        final TaskStateEntity savedTaskState = taskStateRepository.saveAndFlush(taskStateEntity);

        return taskStateDTOFactory.createTaskStateDTO(savedTaskState);
    }

    @PatchMapping(UPDATE_TASK_STATE)
    public TaskStateDTO updateTaskState(@PathVariable(name = "task_state_id" )Long taskStateId,
                                        @RequestParam(name = "task_state_name") String taskStateName) {

        if(taskStateName.trim().isBlank()){
            throw new BadRequestException("Task state name is empty!");
        }

        TaskStateEntity taskStateEntity = getTaskStateOrThrowException(taskStateId);

        taskStateRepository.findTaskStateEntityByProjectIdAndNameContainsIgnoreCase(taskStateEntity.getProject().getId(), taskStateName)
                .filter(anotherTaskState -> !anotherTaskState.getId().equals(taskStateId))
                .ifPresent(anotherTaskState -> {
                    throw new BadRequestException(String.format("Task state with this name already exist!", taskStateName));
                });

        taskStateEntity.setName(taskStateName);

        taskStateEntity = taskStateRepository.saveAndFlush(taskStateEntity);

        return taskStateDTOFactory.createTaskStateDTO(taskStateEntity);
    }

    @PatchMapping(CHANGE_TASK_STATE_POSITION)
    public TaskStateDTO changeTaskStatePosition(@PathVariable(name = "task_state_id" )Long taskStateId,
                                        @RequestParam(name = "left_task_state_id") Optional<Long> optionalLeftTaskStateId) {

        TaskStateEntity changeTaskState = getTaskStateOrThrowException(taskStateId);

        ProjectEntity project = changeTaskState.getProject();

        Optional<Long> oldLeftTaskStateId = changeTaskState.getLeftTaskState().map(TaskStateEntity::getId);

        if(oldLeftTaskStateId.equals(optionalLeftTaskStateId)){
            return taskStateDTOFactory.createTaskStateDTO(changeTaskState);
        }

        TaskStateEntity newLeftTaskState = optionalLeftTaskStateId
                .map(leftTaskStateId -> {
                    if(taskStateId.equals(leftTaskStateId)){
                        throw new BadRequestException("Left task state id equals with changed task state id");
                    }

                    TaskStateEntity leftTaskStateEntity = getTaskStateOrThrowException(leftTaskStateId);

                    if(!project.getId().equals(leftTaskStateEntity.getProject().getId())){
                        throw new BadRequestException("Task state belongs to another project ");
                    }

                    return  leftTaskStateEntity;
                })
                .orElse(null);



        if(!project.getId().equals(leftTaskState.getProject().getId())){
            throw new BadRequestException("This task state belongs to another project");
        }

        if(taskStateName.trim().isBlank()){
            throw new BadRequestException("Task state name is empty!");
        }

        TaskStateEntity taskStateEntity = getTaskStateOrThrowException(taskStateId);

        taskStateRepository.findTaskStateEntityByProjectIdAndNameContainsIgnoreCase(taskStateEntity.getProject().getId(), taskStateName)
                .filter(anotherTaskState -> !anotherTaskState.getId().equals(taskStateId))
                .ifPresent(anotherTaskState -> {
                    throw new BadRequestException(String.format("Task state with this name already exist!", taskStateName));
                });

        taskStateEntity.setName(taskStateName);

        taskStateEntity = taskStateRepository.saveAndFlush(taskStateEntity);

        return taskStateDTOFactory.createTaskStateDTO(taskStateEntity);
    }

    private TaskStateEntity getTaskStateOrThrowException(Long taskStateId){
        return taskStateRepository
                .findById(taskStateId)
                .orElseThrow(() -> new NotFoundException(String.format("Task state with this id is not found", taskStateId)));
    }
}
