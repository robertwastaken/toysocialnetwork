package ro.ubbcluj.map.mavenfx2.service;


import ro.ubbcluj.map.mavenfx2.domain.FriendRequest;
import ro.ubbcluj.map.mavenfx2.repository.Repository;


import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FriendRequestService extends AbstractService<Long, FriendRequest>{
    public FriendRequestService(Repository<Long, FriendRequest> friendRequestRepository) {
        repository = friendRequestRepository;
    }

    @Override
    public void addEntity(FriendRequest entity) {
        for(FriendRequest friendRequest:repository.findAll()){
            if(((Objects.equals(friendRequest.getTo(), entity.getTo()) &&
                    Objects.equals(friendRequest.getFrom(), entity.getFrom())) ||
                    (Objects.equals(friendRequest.getFrom(), entity.getTo()) &&
                            Objects.equals(friendRequest.getTo(), entity.getFrom()))) &&
                            Objects.equals(friendRequest.getStatus(), "pending")){
                throw new IllegalArgumentException("Cei doi au deja o cerere intre ei;");
            }
        }
        super.addEntity(entity);
    }

    public List<FriendRequest> getRequestsOf(Long id){
        return ((List<FriendRequest>) findAll())
                .stream()
                .filter(fr -> Objects.equals(fr.getTo(), id) || Objects.equals(fr.getFrom(), id))
                .collect(Collectors.toList());
    }

    public void updateRequestStatus(Long id, String status){
        FriendRequest friendRequest = findOne(id);
        deleteEntity(id);
        friendRequest.setStatus(status);
        addEntity(friendRequest);
    }
}