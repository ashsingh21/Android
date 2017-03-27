package bu.ashu.com.chatapp.ChatView;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bu.ashu.com.chatapp.R;

/**
 * Created by ashu on 3/27/2017.
 */

public class MessageViewAdapter extends RecyclerView.Adapter<MessageViewAdapter.MessageViewHolder> {

    private List<Message> messages;
    private RelativeLayout.LayoutParams paramsLeft;
    private RelativeLayout.LayoutParams paramsRight;
    private final int WIDTH = 275;

    public MessageViewAdapter(Context ctx) {
        messages = new ArrayList<>();

        DisplayMetrics displaymetrics = new DisplayMetrics();

        WindowManager windowManager = (WindowManager) ctx
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);


        int width = (displaymetrics.widthPixels * 67) / 100;

        paramsLeft = new RelativeLayout.LayoutParams(width,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramsRight = new RelativeLayout.LayoutParams(width,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        // rules to adjust card view, according to message type(incoming or outgoint)
        paramsLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);

        paramsRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
    }

    public void addMessage(Message message) {
        messages.add(message);
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder {

        CardView message_card;
        TextView incoming_text, outgoing_text;

        public MessageViewHolder(View itemView) {
            super(itemView);
            message_card = (CardView) itemView.findViewById(R.id.message_card);
            incoming_text = (TextView) itemView.findViewById(R.id.incoming_text);
            outgoing_text = (TextView) itemView.findViewById(R.id.outgoing_text);
        }
    }

    @Override
    public MessageViewAdapter.MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_layout, parent, false);
        MessageViewHolder mvh = new MessageViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MessageViewAdapter.MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        if (message.incoming) {
            holder.message_card.setLayoutParams(paramsLeft);
            holder.message_card.setCardBackgroundColor(Color.parseColor("#FFF176"));
            holder.incoming_text.setText(message.text);
            holder.outgoing_text.setText("");
        } else {
            holder.message_card.setLayoutParams(paramsRight);
            holder.message_card.setCardBackgroundColor(Color.parseColor("#69F0AE"));
            holder.outgoing_text.setText(message.text);
            holder.incoming_text.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
