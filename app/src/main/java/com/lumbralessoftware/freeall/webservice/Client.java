package com.lumbralessoftware.freeall.webservice;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.lumbralessoftware.freeall.interfaces.ItemRequestResponseHandler;
import com.lumbralessoftware.freeall.interfaces.ItemResponseHandler;
import com.lumbralessoftware.freeall.interfaces.RegistrationResponseHandler;
import com.lumbralessoftware.freeall.interfaces.VoteResponseHandler;
import com.lumbralessoftware.freeall.models.Item;
import com.lumbralessoftware.freeall.models.ItemRequest;
import com.lumbralessoftware.freeall.models.Registration;
import com.lumbralessoftware.freeall.models.Token;
import com.lumbralessoftware.freeall.models.VotingResult;
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
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public class Client {

    public static final String REGISTRATION = "REGISTRATION";

    public interface ClientInterface {
        @GET("/api/items")
        void getItems(@Query("lat") Double latitude, @Query("lon") Double longitude, Callback<List<Item>> callback);
        @GET("/api/items")
        void getForNames(@Query("q") String name, Callback<List<Item>> callback);

        @Headers({
                "Content-Type: application/json",
        })
        @POST("/oauth/register-by-token/{backend}/")
        void postRegistration(@Path("backend") String backend, @Body Token token, Callback<Registration> callback);

        @GET("/vote/{itemId}/")
        void getItemVote(@Path("itemId") Integer itemId, @Query("punctuation") Double score, @Header("Authorization") String authorization, Callback<VotingResult> callback);
        @POST("/request/{itemId}/")
        void postRequestItem(@Path("itemId") Integer itemId, @Body ItemRequest item, @Header("Authorization") String authorization, Callback<ItemRequest> callback);

    }

    public static ClientInterface initRestAdapter() {
        OkHttpClient client = new OkHttpClient();

        return (ClientInterface) new RestAdapter.Builder()
                .setClient(new OkClient(client))
                .setEndpoint(Constants.URL)
                .build()
                .create(ClientInterface.class);

    }

    public static void getAllItems(final ItemResponseHandler responseHandler, LatLng coordinates) {
        Callback<List<Item>> callback = new Callback<List<Item>>() {
            @Override
            public void success(List<Item> allfrees, Response response) {
                responseHandler.sendResponseSusccesful(allfrees);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Client", "error");
            }
        };
        Client.initRestAdapter().getItems(coordinates.latitude, coordinates.longitude, callback);
    }

    public static void getSearch(final ItemResponseHandler responseHandler, String object){
        Callback<List<Item>> callback = new Callback<List<Item>>() {
            @Override
            public void success(List<Item> items, Response response) {
                responseHandler.sendResponseSusccesful(items);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Client", "error");
            }
        };
        Client.initRestAdapter().getForNames(object,callback);
    }

    public static void voteItem(final VoteResponseHandler responseHandler, Integer itemId, Double score) {
        Callback<VotingResult> callback = new Callback<VotingResult>() {
            @Override
            public void success(VotingResult data, Response response) {
                responseHandler.sendResponseSusccesful(data);
            }

            @Override
            public void failure(RetrofitError error) {
                responseHandler.sendResponseFail(Utils.logResponse("VOTING", error));
            }
        };

        Client.initRestAdapter().getItemVote(itemId, score, Utils.getAuthorizationHeader(), callback);
    }

    public static void wantItem(final ItemRequestResponseHandler responseHandler, Integer itemId, ItemRequest item) {
        Callback<ItemRequest> callback = new Callback<ItemRequest>() {
            @Override
            public void success(ItemRequest data, Response response) {
                responseHandler.sendResponseSusccesful(data);
            }

            @Override
            public void failure(RetrofitError error) {
                responseHandler.sendResponseFail(Utils.logResponse("WANTITEM", error));
            }
        };

        Client.initRestAdapter().postRequestItem(itemId, item, Utils.getAuthorizationHeader(), callback);
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
