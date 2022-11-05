package Server.messages.client;

import Server.messages.Message;

public class FinishPlay extends Message {
    private static final long serialVersionUID = 1L;

    public String session;

    public FinishPlay(String session) {
        this.session = session;
    }
}
