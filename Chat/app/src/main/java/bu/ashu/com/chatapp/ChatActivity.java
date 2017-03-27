package bu.ashu.com.chatapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URI;
import java.net.URISyntaxException;

import bu.ashu.com.chatapp.ChatConnection.WebSocket;
import bu.ashu.com.chatapp.ChatView.Message;
import bu.ashu.com.chatapp.ChatView.MessageViewAdapter;

public class ChatActivity extends AppCompatActivity implements WebSocket.SocketCallBack {

    private WebSocket webSocket;
    private RecyclerView messageView;
    private LinearLayoutManager llm;
    private MessageViewAdapter mva;
    private URI uri;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private EditText editText;
    private int message_reverse_count = 3;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        initializeAlert();
        editText = (EditText) findViewById(R.id.text_to_send);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        String address = "ws://10.0.0.15:8080/chat?username=";
        username = "user" + System.currentTimeMillis();

        initializeViews(address,username);

    }

    private void initializeViews(String address, String username){
        try {
            uri = new URI(address + username);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (uri != null) {
            webSocket = new WebSocket(this, uri);
            webSocket.connect();

            messageView = (RecyclerView) findViewById(R.id.message_recycle_view);

            llm = new LinearLayoutManager(this);
            llm.setReverseLayout(true);
            llm.setStackFromEnd(true);

            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

            int height = (displaymetrics.heightPixels * 72) / 100;

            ViewGroup.LayoutParams params = messageView.getLayoutParams();
            params.height = height;
            messageView.setLayoutParams(params);

            mva = new MessageViewAdapter(this);
            messageView.setLayoutManager(llm);
            messageView.setAdapter(mva);
        } else
            Toast.makeText(this, "Something went wrong, please restart", Toast.LENGTH_LONG).show();

    }

    public void send(View view) {
        webSocket.sendMessage();
    }

    public void initializeAlert() {
        builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to leave the chat?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        alertDialog = builder.create();
    }

    @Override
    public void onBackPressed() {
        alertDialog.show();
    }

    @Override
    public void incomingMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mva.getItemCount() > message_reverse_count) llm.setReverseLayout(false);
                mva.addMessage(new Message(message, true));
                mva.notifyDataSetChanged();
            }
        });
    }

    @Override
    public String outgoingMessage() {
        String msg = editText.getText().toString();
        Log.e("Message", msg);
        if (!msg.trim().isEmpty()) {
            if(mva.getItemCount() > message_reverse_count) llm.setReverseLayout(false);
            mva.addMessage(new Message(msg, false));
            mva.notifyDataSetChanged();
            editText.setText("");
            return msg;
        }
        return null;
    }

    @Override
    public void onOpen() {
        webSocket.send(username + " connected");
    }
}




