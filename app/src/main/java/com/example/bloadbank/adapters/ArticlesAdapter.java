package com.example.bloadbank.adapters;

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
;
import com.example.bloadbank.Views.Activity.BaseActivity;
import com.example.bloadbank.Views.Fragment.PostDetailFragment;
import com.example.bloadbank.data.local.Saveddata;
import com.example.bloadbank.data.api.UserApi;
import com.example.bloadbank.data.model.posts.PostData;
import com.example.bloadbank.data.model.posttogglefavourite.PostToggleFavourite;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bloadbank.helper.HelperMethod.ReplaceFragment;
import static com.example.bloadbank.data.api.ClientApi.GetClient;


/**
 * Created by medo on 13/11/2016.
 */

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticlesViewHolder> {
    private Context context;
    private BaseActivity activity;
    private List<PostData> articleslist = new ArrayList<>();
    public boolean pressed = false;
    int PostId;
    View myview;
    UserApi userApi;
    public boolean IsFav;
    Saveddata saveddata;

    public ArticlesAdapter(List<PostData> articleslist, Context context, BaseActivity activity) {
        this.articleslist = articleslist;
        this.activity = activity;
        this.context = context;
        saveddata = new Saveddata(context);
        userApi = GetClient().create(UserApi.class);
    }

    @NonNull
    @Override
    public ArticlesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.article_item, parent, false);
        return new ArticlesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticlesViewHolder holder, int position) {

        PostData article = articleslist.get(position);


        IsFav = article.getIsFavourite();
        if (article.getIsFavourite() == true) {
            holder.addtofav.setImageResource(R.drawable.addedtofav);


        } else if ((article.getIsFavourite() == false)) {
            //TODO remove fav
            holder.addtofav.setImageResource(R.drawable.fav);


        }

        holder.articletitle.setText(article.getTitle());


        Glide.with(context).load(article.getThumbnailFullPath()).into(holder.articleimage);
        holder.addtofav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (IsFav == false) {
                    holder.addtofav.setImageResource(R.drawable.addedtofav);
                    AddToFav(article.getId());
                    IsFav = true;


                } else if ((IsFav == true)) {
                    //TODO remove fav
                    AddToFav(article.getId());
                    holder.addtofav.setImageResource(R.drawable.fav);

                }

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostDetailFragment postDetailFragment = new PostDetailFragment();
                postDetailFragment.post = article;
                ReplaceFragment(activity.getSupportFragmentManager(), postDetailFragment
                        , R.id.nav_host_fragment
                        , null, "medo");

                //TODO send the object
            }
        });


    }

    @Override
    public int getItemCount() {
        return articleslist.size();
    }

    public class ArticlesViewHolder extends RecyclerView.ViewHolder {
        TextView articletitle;
        ImageView articleimage;
        ImageView addtofav;

        public ArticlesViewHolder(@NonNull View itemView) {
            super(itemView);
            myview = itemView;
            articletitle = (TextView) itemView.findViewById(R.id.article_title);
            articleimage = (ImageView) itemView.findViewById(R.id.article_image);
            addtofav = (ImageView) itemView.findViewById(R.id.addtofav);

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
