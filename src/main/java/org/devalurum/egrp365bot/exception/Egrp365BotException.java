package org.devalurum.egrp365bot.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Egrp365BotException extends RuntimeException {

    public Egrp365BotException(String message) {
        super(message);
    }

    public Egrp365BotException(Exception e) {
        super(e);
    }

    public Egrp365BotException(String message, Exception e) {
        super(message, e);
    }
}
