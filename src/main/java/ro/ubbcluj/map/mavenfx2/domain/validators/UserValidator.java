package ro.ubbcluj.map.mavenfx2.domain.validators;


import ro.ubbcluj.map.mavenfx2.domain.User;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        String erori = "";
        if(entity.getFirstName() == null){
            erori += "Utilizatoru' " + entity.getId() + " nu poate fi null; ";
        }
        if(entity.getLastName() == null){
            erori += "Utilizatoru' " + entity.getId() + " nu poate fi null; ";
        }
        if(entity.getFriends().contains(entity)){
            erori += "Utilizatoru' " + entity.getId() + " nu poate fi prieten cu el insusi; ";
        }
        if(entity.getFirstName().equals("")){
            erori += "Utilizatoru' " + entity.getId() + " n-are prenume; ";
        }
        if(entity.getLastName().equals("")){
            erori += "Utilizatoru' " + entity.getId() + " n-are nume; ";
        }
        if(!erori.equals(""))
            throw new ValidationException(erori);
    }
}