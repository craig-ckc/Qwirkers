package Server.messages.client;

import Server.messages.Message;

public class Leave extends Message{
    private static final long serialVersionUID = 3L;

    public String session;
    public String name;

    public Leave(String session, String name){
        this.session = session;
        this.name = name;
    }
}
