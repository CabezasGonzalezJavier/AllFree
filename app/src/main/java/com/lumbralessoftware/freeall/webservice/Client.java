package com.lumbralessoftware.freeall.webservice;

import android.util.Log;

import com.lumbralessoftware.freeall.interfaces.ItemResponseHandler;
import com.lumbralessoftware.freeall.interfaces.RegistrationResponseHandler;
import com.lumbralessoftware.freeall.models.Item;
import com.lumbralessoftware.freeall.models.Registration;
import com.lumbralessoftware.freeall.models.Token;
import com.lumbralessoftware.freeall.utils.Constants;
import com.lumbralessoftware.freeall.utils.Utils;
import com.squareup.okhttp.OkHttpClient;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public class Client {

    public static final String REGISTRATION = "REGISTRATION";

    public interface ClientInterface{
        @GET("/api/items")
        void getItems(Callback<List<Item>> callback);
        @Headers({
                "Content-Type: application/json",
        })
        @POST("/oauth/register-by-token/{backend}/")
        void postRegistration(@Path("backend") String backend, @Body Token token, Callback<Registration> callback);

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

    public static void getAllItems(final ItemResponseHandler responseHandler){
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

    public static void postRegistrationToken(final RegistrationResponseHandler responseHandler, String backend, Token token) {
        Callback<Registration> callback = new Callback<Registration>() {
            @Override
            public void success(Registration data, Response response) {
                responseHandler.sendResponseSusccesful(data);
                Log.d(data.getAccessToken(), "ok");
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.logResponse(REGISTRATION, error);
            }
        };
        Client.initRestAdapter().postRegistration(backend, token, callback);
    }
}
