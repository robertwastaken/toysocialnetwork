package ro.ubbcluj.map.mavenfx2.service;


import ro.ubbcluj.map.mavenfx2.domain.User;
import ro.ubbcluj.map.mavenfx2.repository.Repository;

import java.util.Objects;

public class UserService extends AbstractService<Long, User>{

    public UserService(Repository<Long, User> repo) { repository = repo; }

    @Override
    public void addEntity(User entity) {
        for(User user:repository.findAll()){
            if(Objects.equals(user.getFirstName(), entity.getFirstName()) && Objects.equals(user.getLastName(), entity.getLastName())){
                throw new IllegalArgumentException("Utilizatoru' exista deja;");
            }
        }
        super.addEntity(entity);
    }
}