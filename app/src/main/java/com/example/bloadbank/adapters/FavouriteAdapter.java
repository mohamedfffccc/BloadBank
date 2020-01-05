package com.example.bloadbank.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bloadbank.R;
import com.example.bloadbank.data.local.Saveddata;
import com.example.bloadbank.data.api.UserApi;
import com.example.bloadbank.data.model.posts.PostData;
import com.example.bloadbank.data.model.posts.Posts;
import com.example.bloadbank.data.model.posttogglefavourite.PostToggleFavourite;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bloadbank.data.api.ClientApi.GetClient;

/**
 * Created by medo on 13/11/2016.
 */

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ArticlesViewHolder> {
    private Context context;
    private Activity activity;
    private List<PostData> favouritelist = new ArrayList<>();
    View myview;
    UserApi userApi;
    private Integer postid;
    Saveddata saveddata;

    public FavouriteAdapter(List<PostData> favouritelist, Context context) {
        this.favouritelist = favouritelist;
        this.context = context;
        userApi = GetClient().create(UserApi.class);
        saveddata = new Saveddata(context);
    }

    @NonNull
    @Override
    public ArticlesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favourite_item, parent, false);
        return new ArticlesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticlesViewHolder holder, int position) {

        PostData favourite = favouritelist.get(position);
        postid = favourite.getId();
        holder.favtitle.setText(favourite.getTitle());
        Glide.with(context).load(favourite.getThumbnailFullPath()).into(holder.favimage);
        holder.rmfromfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddToFav(favourite.getId());
                favouritelist.remove(postid);
                notifyDataSetChanged();
                holder.rmfromfav.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favouritelist.size();
    }

    public class ArticlesViewHolder extends RecyclerView.ViewHolder {
        TextView favtitle;
        ImageView favimage;
        ImageView rmfromfav;

        public ArticlesViewHolder(@NonNull View itemView) {
            super(itemView);
            myview = myview;
            favtitle = (TextView) itemView.findViewById(R.id.favourite_title);
            favimage = (ImageView) itemView.findViewById(R.id.favourite_image);
            rmfromfav = (ImageView) itemView.findViewById(R.id.addedtofav);
        }
    }

    public void AddToFav(int id) {
        saveddata.Read_data();
        userApi.AddToggleFav(id, saveddata.api_token).enqueue(new Callback<PostToggleFavourite>() {
            @Override
            public void onResponse(Call<PostToggleFavourite> call, Response<PostToggleFavourite> response) {
                if (response.body().getStatus() == 1) {
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<PostToggleFavourite> call, Throwable t) {

            }
        });
        // PostId=articleslist.

    }

    public static void CheckFav() {


    }
}
