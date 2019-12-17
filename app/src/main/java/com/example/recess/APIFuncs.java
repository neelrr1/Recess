package com.example.recess;

import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Class to interact with and store Schoolloop API functions
 */
public class APIFuncs {

    public static Account login(String baseURL, String username, String password) throws AuthException {
        Account out = new Account(username, password);
        JSONObject jsonData = loginHelper(baseURL, out);

        try {
            out.baseURL = baseURL;
            out.email = jsonData.getString("email");
            out.userID = jsonData.getString("userID");
            out.name = jsonData.getString("fullName");
        } catch (Exception e) {
            Log.e("Recess", e.getClass().getSimpleName(), e);
        }

        return out;
    }

    private static JSONObject loginHelper(String baseURL, Account user) throws AuthException {
        String auth = "Basic " + Base64.encodeToString(String.format("%s:%s", user.username, user.password).getBytes(), Base64.URL_SAFE).trim();
        OkHttpClient client = new OkHttpClient();

        Request login = new Request.Builder()
                .url(String.format("https://%s/mapi/login?version=3", baseURL))
                .get()
                .addHeader("Authorization", auth)
                .build();

        try {
            Response response = client.newCall(login).execute();

            if (response.code() == 401) {
                throw new AuthException("Authorization failed on login. Please check that the correct username and password were entered.");
            }
            JSONObject jsonResponse = new JSONObject(response.body().string());
            response.body().close();

            return jsonResponse;
        } catch (AuthException e) {
            throw e;
        } catch (Exception e) {
            Log.e("Recess", e.getClass().getSimpleName(), e);
        }

        return null;
    }

    public static Map<String, String> getSchools() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://lol.schoolloop.com/mapi/schools")
                .build();
        HashMap<String, String> out = new HashMap<>();

        try {
            Response response = client.newCall(request).execute();
            JSONArray data = new JSONArray(response.body().string());
            response.body().close();

            for(int i = 0; i < data.length(); i++) {
                JSONObject school = data.getJSONObject(i);
                out.put(school.getString("name"), school.getString("domainName"));
            }

        } catch(Exception e) {
            Log.e("Recess", e.getClass().getSimpleName(), e);
        }

        return out;
    }
}

class AuthException extends Exception {
    public AuthException(String message) {
        super(message);
    }
}