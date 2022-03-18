package ro.ubbcluj.map.mavenfx2.domain.validators;


import ro.ubbcluj.map.mavenfx2.domain.Message;
import ro.ubbcluj.map.mavenfx2.repository.UserRepository;

import java.util.Objects;

public class MessageValidator implements Validator<Message>{
    private final UserRepository repoUser;

    public MessageValidator(UserRepository repoUser) {
        this.repoUser = repoUser;
    }

    @Override
    public void validate(Message entity) throws ValidationException {
        String erori = "";
        if(repoUser.findOne(entity.getIdTo()) == null){
            erori += "Utilizatoru' care primeste mesajul nu exista; ";
        }
        if(repoUser.findOne(entity.getIdFrom()) == null){
            erori += "Utilizatoru' care trimite mesajul nu exista; ";
        }
        if(entity.getMessage() == null || Objects.equals(entity.getMessage(), ""))
            erori += "Mesajul nu poate fi gol; ";
        if(!erori.equals(""))
            throw new ValidationException(erori);
    }
}