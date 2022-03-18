package ro.ubbcluj.map.mavenfx2.service;


import ro.ubbcluj.map.mavenfx2.domain.Friendship;
import ro.ubbcluj.map.mavenfx2.repository.Repository;


import java.util.Objects;

public class FriendshipService extends AbstractService<Long, Friendship> {
    public FriendshipService(Repository<Long, Friendship> friendshipRepository) {
        repository = friendshipRepository;
    }

    @Override
    public void addEntity(Friendship entity) {
        for(Friendship friendship:repository.findAll()){
            if((Objects.equals(friendship.getId1(), entity.getId1()) &&
                    Objects.equals(friendship.getId2(), entity.getId2())) ||
                    (Objects.equals(friendship.getId2(), entity.getId1()) &&
                            Objects.equals(friendship.getId1(), entity.getId2()))){
                throw new IllegalArgumentException("Cei doi sunt deja prieteni;");
            }
        }
        super.addEntity(entity);
    }
}
