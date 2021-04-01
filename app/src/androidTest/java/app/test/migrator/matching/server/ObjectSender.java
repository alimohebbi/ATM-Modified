package app.test.migrator.matching.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

public class ObjectSender {
    static long duration = 20;
    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(duration, TimeUnit.MINUTES)
            .readTimeout(duration, TimeUnit.MINUTES)
            .authenticator(new Authenticator() {
        public Request authenticate(Route route, Response response) throws IOException {
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

    public static Map<String, Double> sendDescriptors(Object object) throws IOException {
        String response = sendObject(object, "/ranking");
        if (response != null) {
            try {
                return new ObjectMapper().readValue(response, HashMap.class);
            } catch (JsonMappingException e) {
                e.printStackTrace();
            }
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
}
