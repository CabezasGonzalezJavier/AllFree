package com.lumbralessoftware.freeall.webservice;

import android.util.Log;

import com.lumbralessoftware.freeall.models.Item;
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
        @GET("/items")
        void getItems(Callback<List<Item>> callback);
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

    public static void getAllItems(final ResponseHandler responseHandler){
        Callback<List<Item>> callback = new Callback<List<Item>>() {
            @Override
            public void success(List<Item> allfrees, Response response) {
                responseHandler.sendResponseSusccesful(allfrees);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Client","error");
            }
        };
        Client.initRestAdapter().getItems(callback);
    }
}
