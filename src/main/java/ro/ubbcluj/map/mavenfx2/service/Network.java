package ro.ubbcluj.map.mavenfx2.service;



import ro.ubbcluj.map.mavenfx2.domain.Friendship;
import ro.ubbcluj.map.mavenfx2.domain.User;
import ro.ubbcluj.map.mavenfx2.repository.FriendshipRepository;
import ro.ubbcluj.map.mavenfx2.repository.UserRepository;

import java.util.*;

public class Network implements Graph{
    UserRepository userRepository;
    FriendshipRepository friendshipRepository;
    private Map<Long, List<Long>> adjList;

    public Map<Long, List<Long>> getAdjList() {
        return adjList;
    }

    public Network(UserRepository userRepository, FriendshipRepository friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        this.adjList = new HashMap<>();
        createGraph();
    }

    @Override
    public void addVertex(User user) {
        adjList.putIfAbsent(user.getId(), new ArrayList<>());
    }

    @Override
    public void removeVertex(User user) {
        adjList.values().stream().forEach(e -> e.remove(user));
        adjList.remove(user);
    }

    @Override
    public void addEdge(Friendship friendship) {
        Long user1 = friendship.getId1();
        Long user2 = friendship.getId2();
        adjList.get(user1).add(user2);
        adjList.get(user2).add(user1);
    }

    @Override
    public void removeEdge(Friendship friendship) {
        Long user1 = friendship.getId1();
        Long user2 = friendship.getId2();
        List<Long> friend1 = adjList.get(user1);
        List<Long> friend2 = adjList.get(user2);
        if(friend1 != null){
            friend1.remove(user2);
        }
        if (friend2 != null) {
            friend2.remove(user1);
        }
    }

    @Override
    public void createGraph() {
        for(User user: userRepository.findAll()){
            addVertex(user);
        }
        for(Friendship friendship:friendshipRepository.findAll()){
            addEdge(friendship);
        }
    }

    @Override
    public Set<Long> DFS(User root) {
        Set<Long> visited = new LinkedHashSet<>();
        Stack<Long> stack = new Stack<>();
        stack.push(root.getId());
        while (!stack.isEmpty()){
            Long user = stack.pop();
            if(!visited.contains(user)){
                visited.add(user);
                for(Long u:getAdjList().get(user)){
                    stack.push(u);
                }
            }
        }
        return visited;
    }


    @Override
    public Set<Set<Long>> numberOfConnectedComponents() {
        Set<Set<Long>> components = new HashSet<>();
        ArrayList<Long> visited = new ArrayList<>();
        for (User user: userRepository.findAll()) {
            visited.add(user.getId());
        }
        for (Long user:visited) {
            Set<Long> userSet = DFS(userRepository.findOne(user));
            components.add(userSet);
        }
        return components;
    }

    @Override
    public int no_ofComponents() {
        Set<Set<Long>> components = numberOfConnectedComponents();
        return components.size();
    }

    @Override
    public Set<Long> longestConnectedComponent() {
        int max = 0;
        Set setMax = new HashSet<>();
        Set<Set<Long>> userSet = numberOfConnectedComponents();
        for(Set set:userSet){
            if(set.size() > max){
                max = set.size();
                setMax = set;
            }
        }
        return setMax;
    }
}
