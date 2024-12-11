package danila.sukhov.diary_tasks_tracker_api.api.controllers;

import danila.sukhov.diary_tasks_tracker_api.api.controllers.helpers.ControllerHelper;
import danila.sukhov.diary_tasks_tracker_api.api.dtos.MessageResponce;
import danila.sukhov.diary_tasks_tracker_api.api.dtos.ProjectDTO;
import danila.sukhov.diary_tasks_tracker_api.api.dtos.TaskDTO;
import danila.sukhov.diary_tasks_tracker_api.api.exceptions.BadRequestException;
import danila.sukhov.diary_tasks_tracker_api.api.factories.TaskDTOFactory;
import danila.sukhov.diary_tasks_tracker_api.store.entities.ProjectEntity;
import danila.sukhov.diary_tasks_tracker_api.store.entities.TaskEntity;
import danila.sukhov.diary_tasks_tracker_api.store.entities.TaskStateEntity;
import danila.sukhov.diary_tasks_tracker_api.store.entities.UserEntity;
import danila.sukhov.diary_tasks_tracker_api.store.repositories.TaskRepository;
import danila.sukhov.diary_tasks_tracker_api.store.repositories.TaskStateRepository;
import danila.sukhov.diary_tasks_tracker_api.store.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskController {

    @Autowired
    TaskDTOFactory taskDTOFactory;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    ControllerHelper controllerHelper;
    @Autowired
    TaskStateRepository taskStateRepository;
    @Autowired
    UserRepository userRepository;
    public static final String FETCH_PROJECT = "/task/fetch";
    public static final String CREATE_TASK = "/task/create/{task_state_id}";
    public static final String UPDATE_TASK = "/task/change/task-state";
    public static final String ADD_EXECUTOR_FOR_TASK = "/task/add-executor/{user_login}";

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

  /*  @PatchMapping(UPDATE_TASK)
    public TaskDTO changeTaskPosition(@RequestParam (name = "task_state_id") Long taskStateId, @RequestParam (name = "task") Long taskId){
        TaskStateEntity taskStateEntity = controllerHelper.getTaskStateOrThrowException(taskStateId);
        TaskEntity task = controllerHelper.getTaskOrThrowException(taskId);

        //TaskStateEntity taskStateOldEntity = task.getTaskStateEntity();
        //taskStateOldEntity.getTasks().remove(task);

        task.setTaskStateEntity(taskStateEntity);

        taskRepository.saveAndFlush(task);
      // taskStateRepository.saveAndFlush(taskStateOldEntity);

        return taskDTOFactory.createTaskDTO(task);
    }*/

    @PatchMapping(UPDATE_TASK)
    public TaskDTO changeTaskPosition(@RequestParam (name = "task_state_id") Long taskStateId, @RequestParam (name = "task") Long taskId,
                                      @AuthenticationPrincipal UserDetails userDetails){
        TaskStateEntity taskStateEntity = controllerHelper.getTaskStateOrThrowException(taskStateId);
        TaskEntity task = controllerHelper.getTaskOrThrowException(taskId);
        UserEntity user = userRepository.findUserByLogin(userDetails.getUsername());

        if(!task.getUserEntity().getLogin().equals(user.getLogin())){
            throw new BadRequestException(String.format("User " + user.getLogin() + " can't move this task, only " + task.getUserEntity().getLogin() + " can!"));
        }
        task.setTaskStateEntity(taskStateEntity);
        taskRepository.saveAndFlush(task);
        return taskDTOFactory.createTaskDTO(task);
    }

    /*@PatchMapping(UPDATE_TASK)
    public TaskDTO changeTaskPosition(@RequestParam (name = "task_state_id") Long taskStateId, @RequestParam (name = "task") Long taskId,
                                      @RequestParam(name = "user_login") String userName){
        TaskStateEntity taskStateEntity = controllerHelper.getTaskStateOrThrowException(taskStateId);
        TaskEntity task = controllerHelper.getTaskOrThrowException(taskId);
        UserEntity currentUser = controllerHelper.getUserOrThrowExceptiom(userName);

        if(!task.getUserEntity().getLogin().equals(currentUser.getName())){
            throw new BadRequestException(String.format("User " + currentUser.getLogin() + " can't move this task, only " + task.getUserEntity().getLogin() + " can!"));
        }

        task.setTaskStateEntity(taskStateEntity);
        taskRepository.saveAndFlush(task);
        return taskDTOFactory.createTaskDTO(task);
    }*/


    @PatchMapping(ADD_EXECUTOR_FOR_TASK)
    public MessageResponce addExecutor(@PathVariable(name = "user_login") String username, @RequestParam(name = "task_id") Long taskId){
        UserEntity user = controllerHelper.getUserOrThrowExceptiom(username);
        TaskEntity task = controllerHelper.getTaskOrThrowException(taskId);

        user.getTasks().add(task);

        userRepository.saveAndFlush(user);
        taskRepository.saveAndFlush(task);

        return new MessageResponce("User " + username + " take task " + task.getName());
    }
}
