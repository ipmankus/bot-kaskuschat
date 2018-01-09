package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Service
public class BotService {
    @Value("${kaskus.bot.id}")
    private String BOT_ID;

    @Value("${kaskus.api.url}")
    private String API_URL;

    @Value("${kaskus.api.username}")
    private String API_USERNAME;
    @Value("${kaskus.api.password}")
    private String API_PASSWORD;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HangmanService hangmanService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeaderboardService leaderboardService;

    public void sendMass(ChatFromUser request) {
        MassToBeSent massToBeSent;
        User user = userRepository.findByEmail(request.getFrom());
        if (user.isInGame()) {

            if (request.getBody().equals("/quit")) {
                massToBeSent = hangmanService.quit(request);
            } else {
                massToBeSent = hangmanService.inGame(request);
            }
        } else if (request.getBody().equals("/hangman")) {
            massToBeSent = hangmanService.start(request);
        } else if (request.getBody().equals("/leaderboard")) {
            massToBeSent = leaderboardService.getMass(request.getFrom());
        } else {
            massToBeSent = normalChat(request);
        }

        HttpHeaders headers = createHeaders();
        HttpEntity<MassToBeSent> entity = new HttpEntity<>(massToBeSent, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity x = restTemplate.postForEntity(API_URL, entity, String.class);
        System.out.println(x);
    }

    public MassToBeSent normalChat(ChatFromUser request) {

        RestTemplate restTemplate = new RestTemplate();
        TextOnly textOnly = new TextOnly();
        List<ChatFromBot> ret = new ArrayList<>();

        Quote quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);

        textOnly.setRecipient(request.getFrom());
        textOnly.setBody("Halo gan " + request.getFromPlain()
                + ", " + quote.getValue().getQuote());
        ret.add(textOnly);
        return new MassToBeSent(BOT_ID, ret);
    }

    private HttpHeaders createHeaders() {
        return new HttpHeaders() {{
            String auth = API_USERNAME + ":" + API_PASSWORD;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }
}
