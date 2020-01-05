package com.example.bloadbank.Views.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloadbank.R;
import com.example.bloadbank.adapters.DonationAdapter;
import com.example.bloadbank.adapters.EmptySpinnerAdapter;
import com.example.bloadbank.data.api.UserApi;
import com.example.bloadbank.data.local.Saveddata;
import com.example.bloadbank.data.model.donationrequests.DonationData;
import com.example.bloadbank.data.model.donationrequests.Donationrequests;
import com.example.bloadbank.data.model.generalResponse.GeneralResponse;
import com.example.bloadbank.data.model.generalResponse.GeneralResponseData;
import com.example.bloadbank.helper.OnEndLess;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bloadbank.data.api.ClientApi.GetClient;
import static com.example.bloadbank.helper.HelperMethod.ReplaceFragment;

public class DonationOrdersFragment extends BaseFragment {
    ArrayList<GeneralResponseData> bloads, cites;
    EmptySpinnerAdapter generalresponseadapter;

    @BindView(R.id.bloadtype_donation)
    Spinner bloadtypeDonation;
    @BindView(R.id.city_donation)
    Spinner cityDonation;
    @BindView(R.id.donation_list)
    RecyclerView donationList;
    @BindView(R.id.donationrequests_addorder)
    ImageView donationrequestsAddorder;
    Saveddata saveddata;
    LinearLayoutManager linearLayoutManager;
    UserApi userApi;
    DonationAdapter adapter;
    ArrayList<DonationData> data;
    String name, bloadtype, hospital, city;
    @BindView(R.id.button_filter)
    ImageView buttonFilter;

    @BindView(R.id.orders_activity)
    RelativeLayout ordersActivity;
    private OnEndLess onEndLess;
    private int Maxpage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setUpActivity();
        View root = inflater.inflate(R.layout.fragment_donation_orders, container, false);
        saveddata = new Saveddata(getActivity());
        userApi = GetClient().create(UserApi.class);
        ButterKnife.bind(this, root);
        bloads = new ArrayList<>();
        getTypes();
        generalresponseadapter = new EmptySpinnerAdapter(getActivity(), bloads, "اختر فصيلة الدم");
        bloadtypeDonation.setAdapter(generalresponseadapter);
        cites = new ArrayList<>();
        getCites();
        generalresponseadapter = new EmptySpinnerAdapter(getActivity(), cites, "اختر المدينة ");
        cityDonation.setAdapter(generalresponseadapter);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        donationList.setHasFixedSize(true);
        donationList.setLayoutManager(linearLayoutManager);
        data = new ArrayList<DonationData>();
        GetDonations(1);
        adapter = new DonationAdapter(getActivity(), baseActivity, data);
        donationList.setAdapter(adapter);
        onEndLess = new OnEndLess(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= Maxpage) {
                    if (Maxpage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        GetDonations(current_page);
                        adapter.notifyDataSetChanged();
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;

                }


            }
        };
        donationList.addOnScrollListener(onEndLess);


        return root;
    }


    public void GetDonations(int page) {
        saveddata.Read_data();
//        showProgressDialog(getActivity(), "loading");
        userApi.GetRequests(saveddata.api_token, page).enqueue(new Callback<Donationrequests>() {
            @Override
            public void onResponse(Call<Donationrequests> call, Response<Donationrequests> response) {
                try {
                    if (response.body().getStatus() == 1) {
//                    dismissProgressDialog();
                        Maxpage = response.body().getData().getLastPage();
                        data.addAll(response.body().getData().getData());
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Donationrequests> call, Throwable t) {

            }
        });
    }


    public void getTypes() {
        userApi.GetBloads().enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                bloads.addAll(response.body().getData());

            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    public void getCites() {
        saveddata.Read_data();

        userApi.GetCites(Integer.parseInt(saveddata.govid)).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                cites.addAll(response.body().getData());
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    public void filterDonationOrders(int page) {
        data = new ArrayList<>();
        saveddata.Read_data();
        userApi.filterRequests(saveddata.api_token, bloadtypeDonation.getSelectedItemPosition() , cityDonation.getSelectedItemPosition() , page)
                .enqueue(new Callback<Donationrequests>() {
                    @Override
                    public void onResponse(Call<Donationrequests> call, Response<Donationrequests> response) {
                        try {
                            if (response.body().getStatus() == 1) {
//                    dismissProgressDialog();
                                Maxpage = response.body().getData().getLastPage();
                                data.addAll(response.body().getData().getData());
                                adapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {

                        }

                    }

                    @Override
                    public void onFailure(Call<Donationrequests> call, Throwable t) {

                    }
                });
    }

    @OnClick({R.id.button_filter, R.id.donationrequests_addorder})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_filter:
                filterDonationOrders(1);
                adapter = new DonationAdapter(getActivity(), baseActivity, data);
                donationList.setAdapter(adapter);

                break;
            case R.id.donationrequests_addorder:
                ReplaceFragment(getActivity().getSupportFragmentManager(),
                        new NewDonationFragment(), R.id.nav_host_fragment
                        , null, "medo");
                break;
        }
    }
}
