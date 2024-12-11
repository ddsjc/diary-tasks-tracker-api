package danila.sukhov.diary_tasks_tracker_api.api.controllers;

import danila.sukhov.diary_tasks_tracker_api.api.controllers.helpers.ControllerHelper;
import danila.sukhov.diary_tasks_tracker_api.api.dtos.ProjectDTO;
import danila.sukhov.diary_tasks_tracker_api.api.dtos.TaskDTO;
import danila.sukhov.diary_tasks_tracker_api.api.exceptions.BadRequestException;
import danila.sukhov.diary_tasks_tracker_api.api.factories.TaskDTOFactory;
import danila.sukhov.diary_tasks_tracker_api.store.entities.ProjectEntity;
import danila.sukhov.diary_tasks_tracker_api.store.entities.TaskEntity;
import danila.sukhov.diary_tasks_tracker_api.store.entities.TaskStateEntity;
import danila.sukhov.diary_tasks_tracker_api.store.repositories.TaskRepository;
import danila.sukhov.diary_tasks_tracker_api.store.repositories.TaskStateRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskController {

    TaskDTOFactory taskDTOFactory;
    TaskRepository taskRepository;
    ControllerHelper controllerHelper;
    TaskStateRepository taskStateRepository;
    public static final String FETCH_PROJECT = "/task/fetch";
    public static final String CREATE_TASK = "/task/create/{task_state_id}";
    public static final String UPDATE_TASK = "/task/change/task-state";
    public static final String DELETE_PROJECT = "api/project/delete/{project_id}";

    @PostMapping(CREATE_TASK)
    public TaskDTO createTask(@PathVariable(name = "task_state_id") Long taskStateId,
                                    @RequestBody TaskDTO taskDTO){

        if(taskDTO.getName().trim().isEmpty()){
            throw new BadRequestException(String.format("Invalid Task Name of task: ", taskDTO.getName()));
        }

        TaskStateEntity taskStateEntity = controllerHelper.getTaskStateOrThrowException(taskStateId);

        TaskEntity task = taskRepository.saveAndFlush(
                TaskEntity.builder()
                        .name(taskDTO.getName())
                        .priority(taskDTO.getPriority())
                        .taskStateEntity(taskStateEntity)
                        .createdAt(taskDTO.getCreatedAt())
                        .description(taskDTO.getDescription())
                        .build()
        );

        /*taskStateEntity.getTasks().add(task);
        taskStateRepository.saveAndFlush(taskStateEntity);*/

        return taskDTOFactory.createTaskDTO(task);
    }

    @PatchMapping(UPDATE_TASK)
    public TaskDTO changeTaskPosition(@RequestParam (name = "task_state_id") Long taskStateId, @RequestParam (name = "task") Long taskId){
        TaskStateEntity taskStateEntity = controllerHelper.getTaskStateOrThrowException(taskStateId);
        TaskEntity task = controllerHelper.getTaskOrThrowException(taskId);

       /* TaskStateEntity taskStateOldEntity = task.getTaskStateEntity();
        taskStateOldEntity.getTasks().remove(task);*/

        task.setTaskStateEntity(taskStateEntity);

        taskRepository.saveAndFlush(task);
      //  taskStateRepository.saveAndFlush(taskStateOldEntity);

        return taskDTOFactory.createTaskDTO(task);
    }
}
