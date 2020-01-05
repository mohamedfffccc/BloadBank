package com.example.bloadbank.Views.Activity.ui.favourite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloadbank.R;
import com.example.bloadbank.adapters.FavouriteAdapter;
import com.example.bloadbank.data.local.Saveddata;
import com.example.bloadbank.data.api.UserApi;
import com.example.bloadbank.data.model.posts.PostData;
import com.example.bloadbank.data.model.posts.Posts;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bloadbank.data.api.ClientApi.GetClient;

public class FavouriteFragment extends Fragment {

    UserApi userApi;
    LinearLayoutManager linearLayoutManager;
    Saveddata saveddata;
    FavouriteAdapter adapter;
    String postTitle, postImage;
    int postid;
    @BindView(R.id.favourite_list)
    RecyclerView favouriteList;
    private List<PostData> posts = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_favourite, container, false);
        saveddata = new Saveddata(getActivity());
        userApi = GetClient().create(UserApi.class);
        ButterKnife.bind(this, root);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        favouriteList.setHasFixedSize(true);
        favouriteList.setLayoutManager(linearLayoutManager);
        GetFavourites();
        adapter = new FavouriteAdapter(posts, getActivity());
        favouriteList.setAdapter(adapter);

        return root;
    }

    public void GetFavourites() {
        saveddata.Read_data();
        userApi.GetFav(saveddata.api_token).enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                if (response.body().getStatus() == 1) {
                    posts.addAll(response.body().getData().getData());
                    adapter.notifyDataSetChanged();


                }
            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {

            }
        });

    }
}