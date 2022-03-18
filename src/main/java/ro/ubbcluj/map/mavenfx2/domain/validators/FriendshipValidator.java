package ro.ubbcluj.map.mavenfx2.domain.validators;


import ro.ubbcluj.map.mavenfx2.domain.Friendship;
import ro.ubbcluj.map.mavenfx2.repository.UserRepository;


import java.util.Objects;

public class FriendshipValidator implements Validator<Friendship> {
    private final UserRepository repo;

    public FriendshipValidator(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public void validate(Friendship entity) throws ValidationException {
        String erori = "";
        if(repo.findOne(entity.getId1()) == null){
            erori += "Utilizatoru' 1 nu exista; ";
        }
        if(repo.findOne(entity.getId2()) == null){
            erori += "Utilizatoru' 2 nu exista; ";
        }
        if(Objects.equals(entity.getId1(), entity.getId2())){
            erori += "Utilizatoru' nu poate fi prieten cu el insusi; ";
        }
        if(!erori.equals(""))
            throw new ValidationException(erori);
    }
}