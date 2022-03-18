package ro.ubbcluj.map.mavenfx2.service;

import ro.ubbcluj.map.mavenfx2.domain.Message;
import ro.ubbcluj.map.mavenfx2.repository.Repository;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MessageService extends AbstractService<Long, Message>{
    public MessageService(Repository<Long, Message> repo){ repository = repo; }

    public List<Message> getConversation(Long idFrom, Long idTo){
        ArrayList<Message> messages = (ArrayList<Message>) findAll();
        ArrayList<Message> conversation = new ArrayList<>();

        //Find all messages where user1 and user2 are the only participants
        messages.forEach(message -> { if (
                (Objects.equals(message.getIdFrom(), idFrom) && Objects.equals(message.getIdTo(), idTo)) ||
                        (Objects.equals(message.getIdFrom(), idTo) && Objects.equals(message.getIdTo(), idFrom))
        ) {conversation.add(message);}
        });

        //Sort messages by date
        return conversation.stream()
                .sorted((m1, m2) -> (m1.getDate().isAfter(m2.getDate())) ? 1 : 0)
                .collect(Collectors.toList());
    }
}