package bu.ashu.com.chatapp.ChatConnection;

import android.os.Build;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by ashu on 3/27/2017.
 */

public class WebSocket extends WebSocketClient {

    private static final String TAG = WebSocket.class.getSimpleName();
    private SocketCallBack callBack;

    public WebSocket(SocketCallBack callBack, URI uri) {
        super(uri, new Draft_17());
        this.callBack = callBack;

    }



    public interface SocketCallBack {
        void incomingMessage(String message);
        String outgoingMessage();
        void onOpen();

    }


    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        Log.e(TAG, "Opened");
        callBack.onOpen();
    }


    @Override
    public void onMessage(String s) {
        Log.e(TAG, s);
        callBack.incomingMessage(s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        Log.e("Websocket", "Closed " + s);
    }

    @Override
    public void onError(Exception e) {
        Log.e("Websocket", "Error " + e.getMessage());
    }


    public void sendMessage() {
        String outgoingMessage = callBack.outgoingMessage();
        if(outgoingMessage != null) {
            send(outgoingMessage);
        }
    }
}
