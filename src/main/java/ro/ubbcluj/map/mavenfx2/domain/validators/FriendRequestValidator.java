package ro.ubbcluj.map.mavenfx2.domain.validators;


import ro.ubbcluj.map.mavenfx2.domain.FriendRequest;
import ro.ubbcluj.map.mavenfx2.repository.UserRepository;

import java.util.Objects;

public class FriendRequestValidator implements Validator<FriendRequest>{

    private final UserRepository repo;

    public FriendRequestValidator(UserRepository repo) { this.repo = repo; }

    @Override
    public void validate(FriendRequest entity) throws ValidationException {
        String erori = "";
        if(entity.getTo() == null){
            erori += "Utilizatoru' care primeste cererea nu exista; ";
        }
        else if(repo.findOne(entity.getTo()) == null){
            erori += "Utilizatoru' care primeste cererea nu exista; ";
        }
        if(entity.getFrom() == null) {
            erori += "Utilizatoru' care trimite cererea nu exista; ";
        }
        else if(repo.findOne(entity.getFrom()) == null){
            erori += "Utilizatoru' care trimite cererea nu exista; ";
        }
        if(!Objects.equals(entity.getStatus(), "approved") &&
                !Objects.equals(entity.getStatus(), "pending") &&
                !Objects.equals(entity.getStatus(), "rejected")){
            erori += "Statusul trebuie sa fie unul dintre ['approved', 'pending', 'rejected']; ";
        }
        if(Objects.equals(entity.getTo(), entity.getFrom())){
            erori += "Utilizatoru' nu isi poate cere propria prietenie; ";
        }
        if(!erori.equals(""))
            throw new ValidationException(erori);
    }
}
