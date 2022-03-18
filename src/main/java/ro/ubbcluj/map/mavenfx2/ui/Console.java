package ro.ubbcluj.map.mavenfx2.ui;


import ro.ubbcluj.map.mavenfx2.domain.FriendRequest;
import ro.ubbcluj.map.mavenfx2.domain.Friendship;
import ro.ubbcluj.map.mavenfx2.domain.Message;
import ro.ubbcluj.map.mavenfx2.domain.User;
import ro.ubbcluj.map.mavenfx2.domain.validators.ValidationException;
import ro.ubbcluj.map.mavenfx2.service.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Console {
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final Network networkAdjList;
    private final MessageService messageService;
    private final FriendRequestService friendRequestService;
    Scanner input = new Scanner(System.in);

    public Console(UserService userService, FriendshipService friendshipService, Network network,
                   MessageService messageService, FriendRequestService friendRequestService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.networkAdjList = network;
        this.messageService = messageService;
        this.friendRequestService = friendRequestService;
    }

    //Menu
    public static void Menu(){
        System.out.println("-----------------------------MENU----------------------------------");
        System.out.println("1. User");
        System.out.println("2. Friendship");
        System.out.println("3. Message");
        System.out.println("4. Friend Requests");
        System.out.println("5. Functions");
        System.out.println("x. Exit");
        System.out.println("-------------------------------------------------------------------");
    }

    //User Menu
    public static void userMenu(){
        System.out.println("-----------------------------USER----------------------------------");
        System.out.println("1. Add user");
        System.out.println("2. Delete user");
        System.out.println("su. Show all users");
        System.out.println("x. Back");
        System.out.println("-------------------------------------------------------------------");
    }

    //Friendship Menu
    public static void friendshipMenu(){
        System.out.println("--------------------------FRIENDSHIP-------------------------------");
        System.out.println("1. Add friendship");
        System.out.println("2. Delete friendship");
        System.out.println("sf. Show all friendships");
        System.out.println("x. Back");
        System.out.println("-------------------------------------------------------------------");
    }

    //Message Menu
    public static void messageMenu(){
        System.out.println("---------------------------MESSAGE--------------------------------");
        System.out.println("1. Send message");
        System.out.println("2. Delete message");
        System.out.println("3. Reply to message");
        System.out.println("sm. Show all messages");
        System.out.println("sc. Show conversation between 2 users");
        System.out.println("su. Show all users");
        System.out.println("x. Back");
        System.out.println("-------------------------------------------------------------------");
    }

    //Request Menu
    public static void requestMenu(){
        System.out.println("---------------------------REQUEST--------------------------------");
        System.out.println("1. Send friend request");
        System.out.println("2. Delete friend request");
        System.out.println("sr. Show all friend requests");
        System.out.println("sur. Show an user's friend request");
        System.out.println("rr. Resolve friend requests");
        System.out.println("su. Show all users");
        System.out.println("x. Back");
        System.out.println("-------------------------------------------------------------------");
    }

    //Functions Menu
    public static void functionMenu(){
        System.out.println("--------------------------FUNCTIONS-------------------------------");
        System.out.println("1. Number of communities");
        System.out.println("2. Most sociable community");
        System.out.println("3. Show user's friends");
        System.out.println("4. Show user's friends by month");
        System.out.println("su/sf/sm. Show all users/friendships/messages");
        System.out.println("sn. Show network");
        System.out.println("x. Back");
        System.out.println("-------------------------------------------------------------------");
    }

    public void run_console(){
        String command;
        boolean repeat = true;
        while(repeat){
            Menu();
            System.out.print("Insert command: ");
            command = input.next();
            switch (command) {
                case "1" -> run_User();
                case "2" -> run_Friendship();
                case "3" -> run_Messages();
                case "4" -> run_Requests();
                case "5" -> run_Functions();
                case "x" -> repeat = false;
                default -> System.out.println("Invalid command, try again.");
            }
        }
    }

    private void run_Functions() {
        String command;
        boolean repeat = true;
        while(repeat){
            functionMenu();
            System.out.print("Insert command: ");
            command = input.next();
            switch (command) {
                case "1" -> numberOfCommunities();
                case "2" -> mostSociableCommunity();
                case "3" -> showUserFriends();
                case "4" -> showUserFriendsMonth();
                case "su" -> showUsers();
                case "sf" -> showFriendships();
                case "sm" -> showMessages();
                case "sn" -> showNetwork();
                case "x" -> repeat = false;
                default -> System.out.println("Invalid command, try again.");
            }
        }
    }

    private void run_Requests() {
        String command;
        boolean repeat = true;
        while(repeat){
            requestMenu();
            System.out.print("Insert command: ");
            command = input.next();
            switch (command) {
                case "1" -> sendRequest();
                case "2" -> deleteRequest();
                case "sr" -> getAllRequests();
                case "sur" -> getRequests();
                case "rr" -> resolveRequest();
                case "su" -> showUsers();
                case "x" -> repeat = false;
                default -> System.out.println("Invalid command, try again.");
            }
        }
    }

    private void run_Messages() {
        String command;
        boolean repeat = true;
        while(repeat){
            messageMenu();
            System.out.print("Insert command: ");
            command = input.next();
            switch (command) {
                case "1" -> addMessage();
                case "2" -> deleteMessage();
                case "3" -> addReply();
                case "sm" -> showMessages();
                case "sc" -> showConversation();
                case "su" -> showUsers();
                case "x" -> repeat = false;
                default -> System.out.println("Invalid command, try again.");
            }
        }
    }

    private void run_Friendship() {
        String command;
        boolean repeat = true;
        while(repeat){
            friendshipMenu();
            System.out.print("Insert command: ");
            command = input.next();
            switch (command) {
                case "1" -> addFriendship();
                case "2" -> deleteFriendship();
                case "sf" -> showFriendships();
                case "x" -> repeat = false;
                default -> System.out.println("Invalid command, try again.");
            }
        }
    }

    private void run_User() {
        String command;
        boolean repeat = true;
        while(repeat){
            userMenu();
            System.out.print("Insert command: ");
            command = input.next();
            switch (command) {
                case "1" -> addUser();
                case "2" -> deleteUser();
                case "su" -> showUsers();
                case "x" -> repeat = false;
                default -> System.out.println("Invalid command, try again.");
            }
        }
    }

    private void showUserFriendsMonth() {
        try{
            System.out.println("Insert the id of the user to show its friends: ");
            Long id_user = input.nextLong();
            System.out.println("Insert the month of the friendship: ");
            String month = input.next();
            List<Friendship> friendships = new ArrayList<>();
            friendshipService.findAll().forEach(friendships::add);
            friendships
                    .stream()
                    .filter(x-> Objects.equals(x.getDate().toString().split("-")[1], month))
                    .filter(x->x.getId1().equals(id_user))
                    .forEach(friendship -> System.out.println(userService.findOne(friendship.getId2()).getLastName() + " | "
                            + userService.findOne(friendship.getId2()).getFirstName() + " | "
                            + friendship.getDate()
                    ));
            friendships
                    .stream()
                    .filter(x->Objects.equals(x.getDate().toString().split("-")[1], month))
                    .filter(x->x.getId2().equals(id_user))
                    .forEach(friendship -> System.out.println(userService.findOne(friendship.getId1()).getLastName() + " | "
                            + userService.findOne(friendship.getId2()).getFirstName() + " | "
                            + friendship.getDate()
                    ));
        }catch(IllegalArgumentException| InputMismatchException e){
            e.printStackTrace();
        }
    }

    private void showUserFriends() {
        try{
            System.out.println("Insert the id of the user to show its friends: ");
            Long id_user = input.nextLong();
            List<Friendship> friendships = new ArrayList<>();
            friendshipService.findAll().forEach(friendships::add);
            friendships
                    .stream()
                    .filter(x->x.getId1().equals(id_user))
                    .forEach(friendship -> System.out.println(userService.findOne(friendship.getId2()).getLastName() + " | "
                            + userService.findOne(friendship.getId2()).getFirstName() + " | "
                            + friendship.getDate()
                    ));
            friendships
                    .stream()
                    .filter(x->x.getId2().equals(id_user))
                    .forEach(friendship -> System.out.println(userService.findOne(friendship.getId1()).getLastName() + " | "
                            + userService.findOne(friendship.getId1()).getFirstName() + " | "
                            + friendship.getDate()
                    ));
        }catch(IllegalArgumentException|InputMismatchException e){
            e.printStackTrace();
        }
    }

    //Network
    private void showNetwork() {
        Map<Long, List<Long>> network = networkAdjList.getAdjList();
        for(Long i = 1L; i <= userService.getSize(); i++){
            System.out.println(userService.findOne(i) + "  \\||^_^||/  ");
            for(Long user:network.get(i)){
                System.out.println("\t" + userService.findOne(user) + "\t----\t");
            }
        }
    }

    private void mostSociableCommunity() {
        System.out.println("The most sociable community is: " + networkAdjList.longestConnectedComponent());
    }

    private void numberOfCommunities() {
        System.out.println("The Number of communities in the network is: " + networkAdjList.no_ofComponents());
    }

    //Friendship
    private void showFriendships() {
        try{
            for(Friendship friendship:friendshipService.findAll()){
                System.out.println(friendship);
            }
        }catch(ValidationException |IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    private void deleteFriendship() {
        try{
            System.out.println("Insert the id of the friendship to delete from the database");
            Long id_friendship = input.nextLong();
            networkAdjList.removeEdge(friendshipService.findOne(id_friendship));
            friendshipService.deleteEntity(id_friendship);
        }catch(ValidationException|IllegalArgumentException|InputMismatchException e){
            e.printStackTrace();
        }
    }

    private void addFriendship() {
        try{
            System.out.println("Insert the id of the first user");
            Long id_user1 = input.nextLong();
            System.out.println("Insert the id of the second user");
            Long id_user2 = input.nextLong();
            java.sql.Date date = new java.sql.Date(System.currentTimeMillis());

            Friendship friendship = new Friendship(id_user1, id_user2, date);

            friendshipService.addEntity(friendship);
            networkAdjList.addEdge(friendship);
        }catch(ValidationException|IllegalArgumentException|InputMismatchException e){
            e.printStackTrace();
        }
    }

    //User
    private void showUsers() {
        try{
            for(User user:userService.findAll()){
                System.out.println(user);
            }
        }catch(ValidationException|IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    private void deleteUser() {
        try{
            System.out.print("Insert the id of the user to delete from the database: ");
            Long id_user = input.nextLong();
            for(Friendship friendship:friendshipService.findAll()){
                if(Objects.equals(friendship.getId1(), id_user) || Objects.equals(friendship.getId2(), id_user))
                    friendshipService.deleteEntity(friendship.getId());
            }
            networkAdjList.removeVertex(userService.findOne(id_user));
            userService.deleteEntity(id_user);
        }catch(ValidationException|IllegalArgumentException|InputMismatchException e){
            e.printStackTrace();
        }
    }

    private void addUser() {
        try{
            System.out.println("Insert the first name of the user.");
            String first_name = input.next();
            System.out.println("Insert the last name of the user.");
            String last_name = input.next();

            User user = new User(first_name, last_name);

            userService.addEntity(user);
            networkAdjList.addVertex(user);
        }catch(ValidationException|IllegalArgumentException| InputMismatchException e){
            e.printStackTrace();
        }
    }

    private Long getNextId(Map<Long, Long> ids, int i, Long id_user) {
        int size = ids.size();
        while(size != i){
            Map.Entry<Long, Long> entry = ids.entrySet().iterator().next();
            if(ids.get(entry.getKey()) != i + 1){
                id_user = (long) i + 1;
                break;
            }
            ids.remove(entry.getKey());
            i++;
        }
        if(id_user == null)
            id_user = (long) (size + 1);
        return id_user;
    }

    //Message
    private void addMessage(){
        try{
            System.out.println("Message from (id): ");
            Long from = Long.parseLong(input.next());

            System.out.println("To user: ");
            Long to = Long.parseLong(input.next());

            System.out.println("Message: ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String m = reader.readLine();

            LocalDateTime localDateTime = LocalDateTime.now();

            Message message = new Message(from, to, m, localDateTime, null);

            messageService.addEntity(message);

        }catch(ValidationException | IllegalArgumentException | InputMismatchException | IOException e){
            e.printStackTrace();
        }
    }
    private void deleteMessage(){
        try{
            System.out.print("Insert the id of the message to delete from the database: ");
            Long id_message = input.nextLong();
            messageService.deleteEntity(id_message);
        }catch(ValidationException|IllegalArgumentException|InputMismatchException e){
            e.printStackTrace();
        }
    }
    private void showMessages(){
        try{
            messageService.findAll().forEach(m -> System.out.println(m.getId() + ":" + m.toString()));
        }catch(ValidationException|IllegalArgumentException e){
            e.printStackTrace();
        }
    }
    private void addReply(){
        try{
            System.out.println("Reply to message with id: ");
            Long replyId = input.nextLong();
            Message replyTo = messageService.findOne(replyId);
            System.out.print("-> Replying to message: \n" + replyTo.toString() + "\n");

            System.out.println("-> From (id): ");
            Long from = Long.parseLong(input.next());
            input.nextLine();

            Long to = replyTo.getIdFrom();

            System.out.println("Message: ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String m = reader.readLine();

            LocalDateTime localDateTime = LocalDateTime.now();

            Message message = new Message(from, to, m, localDateTime, replyId);
            messageService.addEntity(message);

        }catch(ValidationException | IllegalArgumentException | InputMismatchException | IOException e){
            e.printStackTrace();
        }
    }
    private void showConversation(){
        try{
            System.out.println("Insert id of the first user: ");
            Long id1 = input.nextLong();
            System.out.println("Insert id of the second user: ");
            Long id2 = input.nextLong();

            //List<Message> conversation = messageService.getConversation(id1, id2);

            messageService
                    .getConversation(id1, id2)
                    .forEach(m -> System.out.println(m.toString()));

        } catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    //Friend requests
    private void sendRequest(){
        try {
            System.out.println("Insert the id of the user sending the request: ");
            Long from = input.nextLong();

            System.out.println("Insert the id of the user receiving the request: ");
            Long to = input.nextLong();

            friendRequestService.addEntity(new FriendRequest(from, to, "pending"));
        } catch(ValidationException | IllegalArgumentException | InputMismatchException e){
            e.printStackTrace();
        }
    }
    private void deleteRequest(){
        try {
            System.out.println("Insert id of the request: ");
            friendRequestService.deleteEntity(input.nextLong());
        } catch(ValidationException | IllegalArgumentException | InputMismatchException e){
            e.printStackTrace();
        }
    }
    private void getRequests(){
        try{
            System.out.println("Insert the id the user: ");
            //List<FriendRequest> all = friendRequestService.getRequestsOf(input.nextLong());
            friendRequestService.getRequestsOf(input.nextLong()).forEach(System.out::println);
        } catch(ValidationException | IllegalArgumentException | InputMismatchException e){
            e.printStackTrace();
        }

    }
    private void getAllRequests(){
        try {
            friendRequestService.findAll().forEach(System.out::println);
        } catch(ValidationException | IllegalArgumentException | InputMismatchException e){
            e.printStackTrace();
        }
    }
    private void resolveRequest(){
        try {
            System.out.println("Inert the id of the request: ");
            Long id = input.nextLong();

            System.out.println("Accept or reject? (a/r): ");
            String action = input.next();

            FriendRequest fr = friendRequestService.findOne(id);

            if (Objects.equals(action, "a")) {
                friendRequestService.updateRequestStatus(id, "approved");

//                Friendship friendship = new Friendship(fr.getFrom(), fr.getTo(), Date.valueOf(LocalDate.now()));
//                friendshipService.addEntity(friendship);

            } else if (Objects.equals(action, "r")) {
                friendRequestService.updateRequestStatus(id, "rejected");
            } else {
                System.out.println("Wrong option!");
            }
        } catch(ValidationException | IllegalArgumentException | InputMismatchException e) {
            e.printStackTrace();
        }
    }

}
