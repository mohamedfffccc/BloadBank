package com.example.bloadbank.Views.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.bloadbank.R;
import com.example.bloadbank.data.local.Saveddata;
import com.example.bloadbank.data.api.UserApi;
import com.example.bloadbank.data.model.posts.PostData;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.bloadbank.data.api.ClientApi.GetClient;


public class PostDetailFragment extends Fragment {
    UserApi userApi;
    @BindView(R.id.image_postdetails)
    ImageView imagePostdetails;
    @BindView(R.id.title_postdetail)
    TextView titlePostdetail;
    @BindView(R.id.content_postdetail)
    TextView contentPostdetail;
    Saveddata saveddata;
    public PostData post;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_post_detail, container, false);
        saveddata = new Saveddata(getActivity());
        userApi = GetClient().create(UserApi.class);
        ButterKnife.bind(this, root);
        //TODO how to send page number??
        GetDetails();
//TODo page number

        // Inflate the layout for this fragment
        return root;
    }

    public void GetDetails() {

                            titlePostdetail.setText(post.getTitle());
                            contentPostdetail.setText(post.getContent());
                            Glide.with(getActivity()).load(post.getThumbnailFullPath()).into(imagePostdetails);


    }


}
