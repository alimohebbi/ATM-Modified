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
import org.json.JSONObject;

public class SendJson {

    private static OkHttpClient client = new OkHttpClient.Builder()
            .authenticator(new Authenticator() {
                public Request authenticate(@NonNull Route route, @NonNull Response response) throws IOException {
                    String credential = Credentials.basic("neo4j", "neo4j");
                    return response.request().newBuilder().header("Authorization", credential).build();
                }
            }).build();

    public static void send(Object jsonObject) throws IOException {
        Request request = new Request.Builder()
                .url("http://10.0.3.2:5000/")
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        jsonObject.toString()))
                .build();


            Response response = client.newCall(request).execute();
            String result = "";
            if (response.body() != null) {
                result = response.body().string();
                System.out.println(result);
            }



    }

    public static void toJson(List<Pair<Event, List<Double>>> eventList) throws IOException {
        ArrayList<Map<String, String>> nodes = new ArrayList<>();
        for (Pair<Event, List<Double>> clickableNode : eventList) {
            UiNode node = clickableNode.first.getTargetElement();
            Map<String, String> attributes = node.getAttributes();
            nodes.add(attributes);
        }
        JSONArray jsonObject =  new JSONArray(nodes);
        System.out.println(jsonObject);
        send(jsonObject);
    }

    public static void sendException(Exception e)
    {
        String stringE = e.toString();
        Map<String, String> map = new HashMap<>();
        map.put("exception", stringE);
        JSONObject object = new JSONObject(map);
        try {
            send(object);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
