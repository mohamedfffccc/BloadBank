package com.example.bloadbank.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloadbank.R;
import com.example.bloadbank.Views.Activity.BaseActivity;
import com.example.bloadbank.Views.Activity.ui.notifications.NotificationsFragment;
import com.example.bloadbank.data.api.UserApi;
import com.example.bloadbank.data.local.Saveddata;
import com.example.bloadbank.data.model.generalResponse.GeneralResponseData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by medo on 13/11/2016.
 */

public class NotificationSettingAdapter extends RecyclerView.Adapter<NotificationSettingAdapter.ArticlesViewHolder> {
    UserApi userApi;
    private Context context;
    private BaseActivity activity;
    private List<GeneralResponseData> generalResponseDataList = new ArrayList<>();
    private List<String> oldSelectedIds = new ArrayList<>();
    public List<Integer> newSelectedIds = new ArrayList<>();
    private Saveddata saveddata;

    public NotificationSettingAdapter(Context context, BaseActivity activity,
       List<GeneralResponseData> generalResponseDataList , ArrayList<String> oldSelectedIds) {
        this.context = context;
//        userApi = GetClient().create(UserApi.class);
//        saveddata = new Saveddata(context);
        this.activity = activity;
        this.generalResponseDataList = generalResponseDataList;
        this.oldSelectedIds = oldSelectedIds;
    }

    @NonNull
    @Override
    public ArticlesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notificationsettingitem, parent, false);
        return new ArticlesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticlesViewHolder holder, int position) {
        GeneralResponseData data = generalResponseDataList.get(position);
        holder.checkbox.setText(data.getName());

        if (oldSelectedIds.contains(String.valueOf(generalResponseDataList.get(position).getId())))
         {
//            String.valueOf(generalResponseDataList.get(position).getId())
            if (!newSelectedIds.contains(generalResponseDataList.get(position).getId())) {
                newSelectedIds.add(generalResponseDataList.get(position).getId());
            }
            holder.checkbox.setChecked(true);
        } else {
            holder.checkbox.setChecked(false);
        }

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkbox.isChecked()) {
                    if (!newSelectedIds.contains(generalResponseDataList.get(position).getId())) {
                        newSelectedIds.add(generalResponseDataList.get(position).getId());
                    }
                } else {

                    for (int i = 0; i < newSelectedIds.size(); i++) {
                        if (newSelectedIds.get(i).equals(generalResponseDataList.get(position).getId())) {
                            newSelectedIds.remove(i);
                            break;
                        }
                    }

                }
                NotificationsFragment notificationsFragment = new NotificationsFragment();
                notificationsFragment.newSelectedIdnotifications=newSelectedIds;
            }
        });

    }

    @Override
    public int getItemCount() {
        return generalResponseDataList.size();
    }

    public class ArticlesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.checkbox)
        CheckBox checkbox;
        View myview;

        public ArticlesViewHolder(@NonNull View itemView) {
            super(itemView);
            myview = itemView;
            ButterKnife.bind(this, myview);
        }

    }



}
