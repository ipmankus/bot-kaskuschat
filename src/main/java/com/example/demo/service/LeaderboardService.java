package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LeaderboardService {
    @Autowired
    UserRepository userRepository;

    @Value("${kaskus.bot.id}")
    private String BOT_ID;


    public MassToBeSent getMass(String recipient) {
        List<User> allUser = userRepository.findAll();
        TextOnly textOnly = new TextOnly();
        List<ChatFromBot> ret = new ArrayList<>();
        textOnly.setRecipient(recipient);

        String test = "";
        for (int i = 0; i < allUser.size(); ++i) {
            test += allUser.get(i).getName() + ' ' + allUser.get(i).getScore() + '\n';
        }
        textOnly.setBody(test);
        System.out.println(test);
        ret.add(textOnly);
        return new MassToBeSent(BOT_ID, ret);
    }

    public void newUser(ChatFromUser request) {
        User user = new User();
        user.setName(request.getFromPlain());
        user.setLose(0L);
        user.setWin(0L);
        user.setScore(0L);
        user.setEmail(request.getFrom());
        user.setInGame(false);
        userRepository.save(user);
    }
}
