package com.example.demo.controller;

import com.example.demo.model.ChatFromUser;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.BotService;
import com.example.demo.service.LeaderboardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class BotController {
    @Value("${kaskus.api.webhook-key}")
    private String webhookKey;

    @Autowired
    private BotService botService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LeaderboardService leaderboardService;


    @PostMapping(value = "/kaskus")
    public void processRequest(@RequestHeader HttpHeaders httpHeaders,
                               @RequestBody String requestBody,
                               HttpServletResponse response) throws IOException {

        ChatFromUser chatFromUser = objectMapper.readValue(requestBody, ChatFromUser.class);

        if (userRepository.findByEmail(chatFromUser.getFrom()) == null) {
            leaderboardService.newUser(chatFromUser);
        }

        botService.sendMass(chatFromUser);
    }


}
