package bu.ashu.com.chatapp.ChatView;

/**
 * Created by ashu on 3/27/2017.
 */

public class Message {
    public String text;
    public boolean incoming;

    public Message(String text, boolean incoming){
        this.text = text;
        this.incoming = incoming;
    }
}
