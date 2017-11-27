package ua.ck.android.geekhub.mclaut.ui.userInfo;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.ck.android.geekhub.mclaut.R;
import ua.ck.android.geekhub.mclaut.data.entities.UserConnectionsInfo;

/**
 * Created by bogda on 25.11.2017.
 */

public class ConnectionsRecyclerAdapter extends RecyclerView.Adapter<ConnectionsViewHolder> {
    private List<UserConnectionsInfo> userConnectionsInfoList = new ArrayList<>();
    private Context context;

    public ConnectionsRecyclerAdapter(List<UserConnectionsInfo> userConnectionsInfoList, Context context) {
        this.userConnectionsInfoList = userConnectionsInfoList;
        this.context = context;
    }

    @Override
    public ConnectionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConnectionsViewHolder holder;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_connections_info_recycler, parent, false);
        holder = new ConnectionsViewHolder(layoutView,context);
        return holder;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ConnectionsViewHolder holder, int position) {
        holder.bind(userConnectionsInfoList.get(position));
    }

    @Override
    public int getItemCount() {
        if(userConnectionsInfoList != null){
            return userConnectionsInfoList.size();
        }
        return 0;
    }
}
class ConnectionsViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.connections_item_login_textview)
    TextView loginTextView;
    @BindView(R.id.connections_item_is_active_textview)
    TextView isActiveTextView;
    @BindView(R.id.connections_item_tariff_textview)
    TextView tariffTextView;
    @BindView(R.id.connections_item_pay_at_day_textview)
    TextView payAtDayTextView;
    private Context context;
    public ConnectionsViewHolder(View itemView, Context context) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.context = context;
    }
    void bind(UserConnectionsInfo info){
        loginTextView.setText(info.getLogin());
        if(info.getIsActive().equals("1")){
            isActiveTextView.setText(context.getString(R.string.active_label));
            isActiveTextView.setTextColor(ContextCompat.getColor(context,android.R.color.holo_green_dark));
        }
        else{
            isActiveTextView.setText(context.getString(R.string.no_active_label));
            isActiveTextView.setTextColor(ContextCompat.getColor(context,android.R.color.holo_red_dark));
        }
        String tariff = context.getString(R.string.tariff_label) + " " + info.getTariff();
        tariffTextView.setText(tariff);

        String payAtDay = context.getString(R.string.pay_at_day_label) + " " + info.getPayAtDay() + "₴";
        payAtDayTextView.setText(payAtDay);

    }
}
