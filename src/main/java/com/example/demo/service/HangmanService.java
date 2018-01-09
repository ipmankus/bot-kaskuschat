package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class HangmanService {

    private boolean inGame;
    private long usedWords;
    private String randomWords, used, tertebak;
    private Random rand = new Random();
    private long nyawa, counter;

    @Value("${kaskus.bot.id}")
    private String BOT_ID;

    @Autowired
    UserRepository userRepository;

    private void getFromUser(String email) {
        User user = userRepository.findByEmail(email);
        inGame = user.isInGame();
        usedWords = user.getQuestionDetail().getUsedWords();
        randomWords = user.getQuestionDetail().getRandomWords();
        used = user.getQuestionDetail().getUsed();
        nyawa = user.getQuestionDetail().getNyawa();
        tertebak = user.getQuestionDetail().getTertebak();
        counter = user.getQuestionDetail().getCounter();
    }

    private void setToUser(String email) {
        User user = userRepository.findByEmail(email);
        user.setInGame(inGame);
        user.getQuestionDetail().setUsedWords(usedWords);
        user.getQuestionDetail().setRandomWords(randomWords);
        user.getQuestionDetail().setUsed(used);
        user.getQuestionDetail().setNyawa(nyawa);
        user.getQuestionDetail().setTertebak(tertebak);
        user.getQuestionDetail().setCounter(counter);
        userRepository.save(user);
    }

    private void resetGame(ChatFromUser request) {
        User user = userRepository.findByEmail(request.getFrom());
        user.setInGame(true);
        user.setQuestionDetail(new QuestionDetail());
        getFromUser(request.getFrom());
        userRepository.save(user);
    }

    public MassToBeSent start(ChatFromUser request) {
        resetGame(request);

        TextOnly textOnly = new TextOnly();
        List<ChatFromBot> ret = new ArrayList<>();
        User user = userRepository.findByEmail(request.getFrom());

        textOnly.setBody("Halo gan " + request.getFromPlain() +
                ", " + "Start Game!\n" +
                "Used words : " + used + "\n" +
                "Tertebak : " + tertebak + "\n" +
                "Nyawa : " + nyawa);
        textOnly.setRecipient(request.getFrom());
        ret.add(textOnly);

        return new MassToBeSent(BOT_ID, ret);
    }

    public MassToBeSent inGame(ChatFromUser request) {
        getFromUser(request.getFrom());
        TextOnly textOnly = new TextOnly();
        TextOnly textOnly2 = new TextOnly();
        List<ChatFromBot> ret = new ArrayList<>();

        textOnly.setRecipient(request.getFrom());
        textOnly2.setRecipient(request.getFrom());

        request.setBody(request.getBody().toUpperCase());

        if (request.getBody().equals(randomWords)) {
            textOnly.setBody("Selamat anda menang!");
            inGame = false;
            win(request.getFrom());
        } else {
            if (request.getBody().length() == randomWords.length()) {
                nyawa--;
                textOnly.setBody("Salah");
            } else if (request.getBody().length() > 1) {
                textOnly.setBody("Tebak Semua / Tebak Satu saja :)");
            } else {

                if ((usedWords & (1 << ((int) request.getBody().charAt(0) - (int) 'A'))) > 0) {
                    textOnly.setBody("Kata sudah dipakai");
                } else {
                    boolean ada = false;
                    String newTertebak = "";
                    System.out.println(tertebak);
                    for (int i = 0; i < tertebak.length(); ++i) {
                        if (i % 2 == 0) {
                            if (randomWords.charAt(i / 2) == request.getBody().charAt(0)) {
                                newTertebak += request.getBody();
                                ada = true;
                                counter++;
                            } else newTertebak += tertebak.charAt(i);
                        } else {
                            newTertebak += " ";
                        }
                    }

                    if (ada) {
                        textOnly.setBody("Ada :)!");
                        if (counter == randomWords.length()) {
                            textOnly.setBody("Ada :)!\nKamu Menang!");
                            win(request.getFrom());
                            inGame = false;
                        }
                    } else {
                        nyawa--;
                        textOnly.setBody("Belum ada :(!");
                    }
                    tertebak = newTertebak;
                    used += request.getBody();
                    usedWords |= (1 << ((int) request.getBody().charAt(0) - (int) 'A'));

                }
            }

            textOnly2.setBody("Used words : " + used + "\n" +
                    "Tertebak : " + tertebak + "\n" +
                    "Nyawa : " + nyawa);

            ret.add(textOnly2);
        }
        if (nyawa == 0) {
            ret.clear();
            textOnly.setBody("Salah!\nHanged !\nKata sebenarnya: " + randomWords);
            lose(request.getFrom());
            inGame = false;

        }

        ret.add(textOnly);
        setToUser(request.getFrom());
        return new MassToBeSent(BOT_ID, ret);
    }

    public MassToBeSent quit(ChatFromUser request) {
        TextOnly textOnly = new TextOnly();
        List<ChatFromBot> ret = new ArrayList<>();

        textOnly.setRecipient(request.getFrom());
        textOnly.setBody("Halo gan " + request.getFromPlain()
                + ", " + "Keluar dari Game!");

        ret.add(textOnly);

        inGame = false;
        setToUser(request.getFrom());
        return new MassToBeSent(BOT_ID, ret);
    }

    public void lose(String email) {
        User user;
        user = userRepository.findByEmail(email);
        user.setScore(user.getScore() - 1);
        user.setLose(user.getLose() + 1);
        userRepository.save(user);
    }

    public void win(String email) {
        User user;
        user = userRepository.findByEmail(email);
        user.setScore(user.getScore() + 4);
        user.setWin(user.getWin() + 1);
        userRepository.save(user);
    }


}
