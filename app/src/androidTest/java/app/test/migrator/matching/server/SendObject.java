package app.test.migrator.matching.server;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.test.migrator.matching.util.Event;
import app.test.migrator.matching.util.Pair;
import app.test.migrator.matching.util.uiautomator.UiNode;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.Route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SendObject {

    private static OkHttpClient client = new OkHttpClient.Builder()
            .authenticator(new Authenticator() {
                public Request authenticate(@NonNull Route route, @NonNull Response response) throws IOException {
                    String credential = Credentials.basic("neo4j", "neo4j");
                    return response.request().newBuilder().header("Authorization", credential).build();
                }
            }).build();

    public static String sendObject(Object jsonObject, String path) throws IOException {
        Request request = new Request.Builder().url("http://10.0.3.2:5000/" + path)
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString()))
                .build();

        Response response = client.newCall(request).execute();
        if (response.body() != null) {
            return response.body().string();
        }
        return null;
    }

    public static List<Integer> sendDescriptors(Object object) throws IOException {
        String response = sendObject(object, "/ranking");
        if (response != null) {

            List<Object> stringNumbersList = null;
            try {
                stringNumbersList = convertJsonArrayToList(new JSONArray(response));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return convertToNumberList(stringNumbersList);
        }
        return null;
    }

    public static void sendException(Exception e) {
        String stringE = e.toString();
        Map<String, String> map = new HashMap<>();
        map.put("exception", stringE);
        JSONObject object = new JSONObject(map);
        try {
            sendObject(object, "exception");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private static List<Integer> convertToNumberList(List<Object> stringNumbersList) {
        ArrayList<Integer> numberList = new ArrayList<>();
        for (Object stringNumber : stringNumbersList) {
            int number = Integer.parseInt((String) stringNumber);
            numberList.add(number);
        }
        return numberList;
    }

    private static List<Object> convertJsonArrayToList(JSONArray jArray) throws JSONException {
        ArrayList<Object> listData = new ArrayList<>();
        if (jArray != null) {
            for (int i = 0; i < jArray.length(); i++) {
                listData.add(jArray.get(i));
            }
        }
        return listData;
    }
}
