package com.lumbralessoftware.freeall.webservice;

import com.lumbralessoftware.freeall.models.Allfree;
import com.lumbralessoftware.freeall.utils.Constants;
import com.squareup.okhttp.OkHttpClient;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.http.GET;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public class Client {
    public interface ClientInterface{
        @GET("//items/")
        void getItems(Callback<List<Allfree>> callback);
    }

    public static ClientInterface initRestAdapter()
    {
        OkHttpClient client = new OkHttpClient();

        return (ClientInterface) new RestAdapter.Builder()
                .setClient(new OkClient(client))
                .setEndpoint(Constants.URL)
                .build()
                .create(ClientInterface.class);
    }

    public static void getAllItems(){
        Callback<Allfree> callback = new Callback<Allfree>() {
            @Override
            public void success(Allfree allfree, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        };
    }
}
