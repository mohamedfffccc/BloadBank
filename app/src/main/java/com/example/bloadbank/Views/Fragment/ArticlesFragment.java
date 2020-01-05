package com.example.bloadbank.Views.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloadbank.R;
import com.example.bloadbank.adapters.ArticlesAdapter;
import com.example.bloadbank.adapters.EmptySpinnerAdapter;
import com.example.bloadbank.data.api.UserApi;
import com.example.bloadbank.data.local.Saveddata;
import com.example.bloadbank.data.model.generalResponse.GeneralResponse;
import com.example.bloadbank.data.model.generalResponse.GeneralResponseData;
import com.example.bloadbank.data.model.postdetail.PostDetail;
import com.example.bloadbank.data.model.posts.PostData;
import com.example.bloadbank.data.model.posts.Posts;
import com.example.bloadbank.helper.OnEndLess;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bloadbank.data.api.ClientApi.GetClient;
import static com.example.bloadbank.data.local.Saveddata.showPositiveToast;

public class ArticlesFragment extends BaseFragment {
    ArrayList<GeneralResponseData> cats;
    EmptySpinnerAdapter generalresponseadapter;

    Saveddata saveddata;

    @BindView(R.id.articles_list)
    RecyclerView articlesList;
    UserApi userApi;
    LinearLayoutManager linearLayoutManager;

    ArticlesAdapter adapter;

    int postid;
    @BindView(R.id.articlefragment_btnsearch)
    ImageView articlefragmentBtnsearch;
    @BindView(R.id.articlefragment_search)
    EditText articlefragmentSearch;
    @BindView(R.id.articlefragment_category)
    Spinner articlefragmentCategory;
    //
    private List<PostData> posts = new ArrayList<>();
    private List<PostDetail> postFilterData = new ArrayList<>();
    private Integer Maxpage = 0;
    private OnEndLess onEndLess;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setUpActivity();
        View root = inflater.inflate(R.layout.fragment_articles, container, false);
        userApi = GetClient().create(UserApi.class);
        ButterKnife.bind(this, root);
        cats = new ArrayList<>();
        addCategories();
        generalresponseadapter = new EmptySpinnerAdapter(getActivity(), cats, "اختر الفءة ");
        articlefragmentCategory.setAdapter(generalresponseadapter);

        saveddata = new Saveddata(getActivity());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        articlesList.setHasFixedSize(true);
        articlesList.setLayoutManager(linearLayoutManager);
        //     AddCat();


        GetPosts(1);
        adapter = new ArticlesAdapter(posts, getActivity(), baseActivity);
        articlesList.setAdapter(adapter);


        onEndLess = new OnEndLess(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= Maxpage) {
                    if (Maxpage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        GetPosts(current_page);
                        adapter.notifyDataSetChanged();
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;

                }


            }
        };
        articlesList.addOnScrollListener(onEndLess);


        // Inflate the layout for this fragment
        return root;
    }

    public void GetPosts(int page) {
        saveddata.Read_data();
//        showProgressDialog(getActivity(), "loading");
        userApi.GetPosts(saveddata.api_token, page)
                .enqueue(new Callback<Posts>() {
                    @Override
                    public void onResponse(Call<Posts> call, Response<Posts> response) {
                        try {
                            if (response.body().getStatus() == 1) {
                                showPositiveToast(getActivity(), response.body().getMsg());
//                            dismissProgressDialog();
                                posts.addAll(response.body().getData().getData());
                                Maxpage = response.body().getData().getLastPage();
                                articlesList.setAdapter(adapter);

                                adapter.notifyDataSetChanged();


                            }


                        } catch (Exception e) {

                        }

                    }

                    @Override
                    public void onFailure(Call<Posts> call, Throwable t) {

                    }
                });

    }

    public void addCategories() {
        userApi.GetCat().enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                cats.addAll(response.body().getData());

            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });

    }

    @OnClick(R.id.articlefragment_btnsearch)
    public void onViewClicked() {
        FilterData(1);
    }

//

    public void FilterData(int page) {
        String keyword = articlefragmentSearch.getText().toString();
        userApi.FilterPosts(saveddata.api_token, page, keyword, articlefragmentCategory.getSelectedItemPosition())
                .enqueue(new Callback<Posts>() {
                    @Override
                    public void onResponse(Call<Posts> call, Response<Posts> response) {
                        if (response.body().getStatus() == 1) {
                            posts.clear();
                            Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                            posts.addAll(response.body().getData().getData());
                            Maxpage = response.body().getData().getLastPage();

                           // filteradapter = new FilterArticleAdapter(postFilterData, getActivity(), baseActivity);
                            adapter.notifyDataSetChanged();

                        }
                    }

                    @Override
                    public void onFailure(Call<Posts> call, Throwable t) {

                    }
                });
    }


}
