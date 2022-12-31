package dsa.hcmiu.a2048pets.entities.handle;

import android.content.Context;
import android.util.JsonWriter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;

import dsa.hcmiu.a2048pets.MyApplication;
import dsa.hcmiu.a2048pets.entities.model.Features;
import dsa.hcmiu.a2048pets.entities.model.User;

import static dsa.hcmiu.a2048pets.entities.model.Features.usersList;

public class HandleFile {

    private static HandleFile instance;
    private static Context context;
    private static String file = "feature.json";
    private static StringWriter output;

    private HandleFile() {
        context = MyApplication.getContext();
    }

    public static HandleFile get() {
        if (instance == null) {
            instance = new HandleFile();
        }
        return instance;
    }
    public static void readFeaturesJSONFile() throws IOException, JSONException {

        // Đọc nội dung text của file company.json
        String jsonText = readText();
        if (jsonText == null) {
            usersList.put(String.valueOf(Features.user.getUID()),Features.user);
            return;
        }
        // Đối tượng JSONObject gốc mô tả toàn bộ tài liệu JSON.
        JSONObject jsonRoot = new JSONObject(jsonText);
        //text.write(jsonText); //show
        Log.d("HandleFile", "readFeaturesJSONFile: " + jsonText);
        Features.sound = jsonRoot.getBoolean("sound");
        JSONArray jsonArrayData = jsonRoot.getJSONArray("user");
        for (int c = 0 ; c< jsonArrayData.length(); c++) {
            User user = new User();
            JSONObject jsonUser = jsonArrayData.getJSONObject(c);
            user.setUID(jsonUser.getLong("uid"));
            user.setName(jsonUser.getString("name"));
            user.setLogged(jsonUser.getBoolean("logged"));
            user.setSocialType(jsonUser.getString("SocialMedia"));
            user.setEmail(jsonUser.getString("email"));
            user.setIDFacebook(jsonUser.getString("IDFb"));
            user.setProfilePic(jsonUser.getString("profilePic"));
            user.setAvatar(jsonUser.getInt("avatar"));
            user.highScore = jsonUser.getLong("highScore");
            user.undo = jsonUser.getInt("undo");
            user.hammer = jsonUser.getInt("hammer");
            user.totalGold = jsonUser.getLong("totalGold");

            JSONArray jsonArray = jsonUser.getJSONArray("purchasedIdItem");
            int[] id = new int[jsonArray.length()];
            user.purchasedIdItem = new ArrayList<>();
            for (int i = 0 ; i< jsonArray.length(); i++) {
                user.purchasedIdItem.add(jsonArray.getInt(i));
            }
           usersList.put(user.getIDFacebook(),user);
        }
        Features.user =  usersList.get(jsonRoot.getString("currUser"));
    }


    // Đọc nội dung text của một file nguồn.
    private static String readText() throws IOException {
        File f = new File(context.getFilesDir().getPath().toString() +"/" + file);
        if(!f.exists()) {
            f.createNewFile(); //creating it
            return null;
        }
        InputStream is = context.openFileInput(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String s = null;
        while ((s = br.readLine()) != null) {
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void convertToJSON() throws IOException {
        output = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(output);
        User user;
        jsonWriter.beginObject();// begin root
        jsonWriter.name("sound").value(Features.sound);
        jsonWriter.name("sound").value(Features.user.getIDFacebook());
        jsonWriter.name("user").beginArray(); // begin user
        for (String id: Features.usersList.keySet()) {
            user = usersList.get(id);
            // "user": { ... }
            jsonWriter.name("uid").value(user.getUID());
            jsonWriter.name("name").value(user.getName());
            jsonWriter.name("logged").value(user.isLogged());
            jsonWriter.name("SocialMedia").value(user.getSocialType());
            jsonWriter.name("email").value(user.getEmail());
            jsonWriter.name("IDFb").value(user.getIDFacebook());
            jsonWriter.name("profilePic").value(user.getProfilePic());
            jsonWriter.name("avatar").value(user.getAvatar());
            jsonWriter.name("highScore").value(user.highScore);
            jsonWriter.name("undo").value(user.undo);
            jsonWriter.name("hammer").value(user.hammer);
            jsonWriter.name("totalGold").value(user.totalGold);

            // "purchase array": [ ....]
            jsonWriter.name("purchasedIdItem").beginArray(); // begin purchase array
            for(int item: user.purchasedIdItem) {
                jsonWriter.value(item);
            }
            jsonWriter.endArray();// end purchase array
            jsonWriter.endObject();// end user
        }
        jsonWriter.endArray();// end user list

        // end root
        jsonWriter.endObject();
    }

    public static void writeToFile() throws IOException {
        convertToJSON();
        File f = new File(context.getFilesDir().getPath().toString() +"/" + file);
        if(!f.exists()) {
            f.createNewFile(); //creating it
        }
        FileOutputStream fos = context.openFileOutput(file,Context.MODE_PRIVATE);
        fos.write(output.toString().getBytes());
        fos.close();
    }
}
