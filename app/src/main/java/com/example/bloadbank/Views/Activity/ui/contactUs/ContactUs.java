package com.example.bloadbank.Views.Activity.ui.contactUs;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.bloadbank.R;
import com.example.bloadbank.data.api.UserApi;
import com.example.bloadbank.data.local.Saveddata;
import com.example.bloadbank.data.model.contact.Contact;
import com.example.bloadbank.data.model.settings.Settings;

import java.net.URI;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bloadbank.data.api.ClientApi.GetClient;
import static com.example.bloadbank.helper.HelperMethod.dismissProgressDialog;
import static com.example.bloadbank.helper.HelperMethod.showProgressDialog;

public class ContactUs extends Fragment {

    @BindView(R.id.contact_ivlogo)
    ImageView contactIvlogo;
    @BindView(R.id.contact_call)
    ImageView contactCall;
    @BindView(R.id.contact_phonenu)
    TextView contactPhonenu;
    @BindView(R.id.contact_send)
    ImageView contactSend;
    @BindView(R.id.contact_email)
    TextView contactEmail;
    @BindView(R.id.contact_facebook)
    ImageView contactFacebook;
    @BindView(R.id.contact_twitter)
    ImageView contactTwitter;
    @BindView(R.id.contact_youtube)
    ImageView contactYoutube;
    @BindView(R.id.contact_instagram)
    ImageView contactInstagram;
    @BindView(R.id.contact_whatsup)
    ImageView contactWhatsup;
    @BindView(R.id.contact_googleplus)
    ImageView contactGoogleplus;
    @BindView(R.id.contact_relbuttons)
    LinearLayout contactRelbuttons;
    @BindView(R.id.contact_contacttv)
    TextView contactContacttv;
    @BindView(R.id.contaact_edname)
    EditText contaactEdname;
    @BindView(R.id.contaact_edemail)
    EditText contaactEdemail;
    @BindView(R.id.contaact_edphone)
    EditText contaactEdphone;
    @BindView(R.id.contaact_edaddress)
    EditText contaactEdaddress;
    @BindView(R.id.contaact_edmessage)
    EditText contaactEdmessage;
    @BindView(R.id.contact_btnsend)
    Button contactBtnsend;
    private UserApi userApi;
    private Saveddata saveddata;
    private String facebookurl,instaurl,googleurl,twitterurl,youtubeurl,whatsurl,phone,email;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_contactus, container, false);
        ButterKnife.bind(this , root);
        userApi = GetClient().create(UserApi.class);
        saveddata = new Saveddata(getActivity());
        GetAppSetting();

        return root;
    }

    private void GetAppSetting() {
        showProgressDialog(getActivity(), "please wait");

        saveddata.Read_data();
        userApi.GetSetting(saveddata.api_token).enqueue(new Callback<Settings>() {
            @Override
            public void onResponse(Call<Settings> call, Response<Settings> response) {
                try {
                    if (response.body().getStatus()==1) {
                        dismissProgressDialog();
                        facebookurl = response.body().getData().getFacebookUrl();
                        instaurl = response.body().getData().getInstagramUrl();
                        googleurl = response.body().getData().getGoogleUrl();
                        twitterurl = response.body().getData().getTwitterUrl();
                        youtubeurl = response.body().getData().getYoutubeUrl();
                        whatsurl = response.body().getData().getWhatsapp();
                        phone = response.body().getData().getPhone();
                        email = response.body().getData().getEmail();
                        contactPhonenu.setText(phone);
                        contactEmail.setText(email);

                    }

                }
                catch (Exception e)
                {

                }
            }

            @Override
            public void onFailure(Call<Settings> call, Throwable t) {

            }
        });
    }
    public void ContactUs(String title , String message)
    {
        showProgressDialog(getActivity(), "please wait");
        saveddata.Read_data();
        userApi.Contact(saveddata.api_token , title , message).enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {
                dismissProgressDialog();

                try {
                    if (response.body().getStatus()==1) {
                        Toast.makeText(getActivity() , response.body().getMsg() , Toast.LENGTH_SHORT).show();

                    }

                }
                catch (Exception e)
                {

                }
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {

            }
        });
    }
    public void StartAction(String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse(url));
        startActivity(intent);
    }

    @OnClick({R.id.contact_call, R.id.contact_send, R.id.contact_email, R.id.contact_facebook, R.id.contact_twitter, R.id.contact_youtube, R.id.contact_instagram, R.id.contact_whatsup, R.id.contact_googleplus, R.id.contact_btnsend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.contact_call:
                break;
            case R.id.contact_send:
                break;
            case R.id.contact_email:
                break;
            case R.id.contact_facebook:
                StartAction(facebookurl);
                break;
            case R.id.contact_twitter:
                StartAction(twitterurl);
                break;
            case R.id.contact_youtube:
                StartAction(youtubeurl);
                break;
            case R.id.contact_instagram:
                StartAction(instaurl);
                break;
            case R.id.contact_whatsup:
                StartAction(whatsurl);
                break;
            case R.id.contact_googleplus:
                StartAction(googleurl);
                break;
            case R.id.contact_btnsend:
                ContactUs(contaactEdaddress.getText().toString() , contaactEdmessage.getText().toString());
                break;
        }
    }
    public void sendEmail(String email,String subject ,String text)
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("vnd.android.cursor.item/email");
        intent.putExtra(Intent.EXTRA_EMAIL , email);
        intent.putExtra(Intent.EXTRA_SUBJECT , subject);
        intent.putExtra(Intent.EXTRA_TEXT , text);
        startActivity(Intent.createChooser(intent , "send email"));
    }
}