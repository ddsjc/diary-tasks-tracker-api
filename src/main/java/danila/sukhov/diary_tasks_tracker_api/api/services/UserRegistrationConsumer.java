package danila.sukhov.diary_tasks_tracker_api.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import danila.sukhov.diary_tasks_tracker_api.api.dtos.UserDTO;
import danila.sukhov.diary_tasks_tracker_api.api.dtos.UserRegistrationDTO;
import danila.sukhov.diary_tasks_tracker_api.api.factories.UserDTOFactory;
import danila.sukhov.diary_tasks_tracker_api.store.entities.UserEntity;
import danila.sukhov.diary_tasks_tracker_api.store.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRegistrationConsumer {
    @Autowired
    UserDTOFactory userDTOFactory;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    public UserRegistrationConsumer(UserRepository userRepository, UserDTOFactory userDTOFactory) {
        this.userRepository = userRepository;
        this.userDTOFactory = userDTOFactory;
    }
    @KafkaListener(topics = "user_registration", groupId = "task-tracker-group")
    public void listenUserRegistration(String message) {
        try {
            //ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(message);
            JsonNode role = jsonNode.get("roles");
            String nameRole = role.get(0).get("name").toString();
            nameRole = nameRole.replace("\"", "");
            ObjectNode objectNode = (ObjectNode) jsonNode;
            objectNode.remove("roles");

            //[{}]
            UserRegistrationDTO userRegistrationDTO = objectMapper.readValue(jsonNode.toString(), UserRegistrationDTO.class);

            UserEntity user = UserEntity.builder()
                    .id(userRegistrationDTO.getId())
                    .name(userRegistrationDTO.getName())
                    .surname(userRegistrationDTO.getSurname())
                    .login(userRegistrationDTO.getLogin())
                    .password(userRegistrationDTO.getPassword())
                    .createdAt(userRegistrationDTO.getCreatedAt())
                    .role(nameRole)
                    .build();

            userRepository.save(user);

            //return userDTOFactory.createUserDTO(user);
        } catch (Exception e) {
            e.printStackTrace();
            //return null;
        }
    }

}
