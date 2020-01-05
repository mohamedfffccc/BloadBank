package com.example.bloadbank.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloadbank.R;
import com.example.bloadbank.Views.Activity.BaseActivity;
import com.example.bloadbank.data.local.Saveddata;
import com.example.bloadbank.data.model.notifications.NotificationsData;

import java.util.ArrayList;
import java.util.List;

import static com.example.bloadbank.helper.HelperMethod.ReplaceFragment;

;


/**
 * Created by medo on 13/11/2016.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder> {
    private Context context;
    private BaseActivity activity;
    private List<NotificationsData> notificationsDataList = new ArrayList<>();

    Saveddata saveddata;

    public NotificationsAdapter(Context context, BaseActivity activity, List<NotificationsData> notificationsDataList) {
        this.context = context;
        this.activity = activity;
        this.notificationsDataList = notificationsDataList;
        saveddata = new Saveddata(context);
    }

    @NonNull
    @Override
    public NotificationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bloadbank_notificationitem, parent, false);
        return new NotificationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsViewHolder holder, int position) {

        NotificationsData notification = notificationsDataList.get(position);
        if(notification.getPivot().getIsRead().equals("1"))
        {
            holder.notificationimage.setImageResource(R.drawable.notification_off);
        }
        else if(notification.getPivot().getIsRead().equals("0"))
        {
            holder.notificationimage.setImageResource(R.drawable.notification_on);

        }
        holder.notificationdate.setText(notification.getCreatedAt());



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ReplaceFragment(activity.getSupportFragmentManager(), null
                        , R.id.nav_host_fragment
                        , null, "medo");

                //TODO send the object
            }
        });


    }

    @Override
    public int getItemCount() {
        return notificationsDataList.size();
    }

    public class NotificationsViewHolder extends RecyclerView.ViewHolder {
        TextView notificationdate;
        ImageView notificationimage;


        public NotificationsViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationdate = (TextView) itemView.findViewById(R.id.bloadnotifications_date);
            notificationimage = (ImageView) itemView.findViewById(R.id.bloadnotifications_notificationimage);


        }

    }


}
