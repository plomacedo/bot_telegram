package br.com.ejprv;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class Greetings {
    private static final LocalTime MORNING = LocalTime.of(0, 0, 0);
    private static final LocalTime AFTER_NOON = LocalTime.of(12, 0, 0);
    private static final LocalTime EVENING = LocalTime.of(18, 0, 0);

    public static String initialGreeting(String nome) {
            return  String.format("Olá%s, %s! \nSeja bem vindo(a) ao bot da turma 1SCJR!", nome, periodo());
    }

    private static String periodo() {
        LocalTime now = LocalTime.now();
        if ((!now.isBefore(MORNING)) && now.isBefore(AFTER_NOON)) {
            return "bom dia";
        } else if ((!now.isBefore(AFTER_NOON)) && now.isBefore(EVENING)) {
            return "boa tarde";
        } else {
            return "boa noite";
        }
    }
}
