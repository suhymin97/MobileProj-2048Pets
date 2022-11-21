package dsa.hcmiu.a2048pets.entities.handle;

import android.content.Context;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

import dsa.hcmiu.a2048pets.MyApplication;
import dsa.hcmiu.a2048pets.R;
import dsa.hcmiu.a2048pets.entities.model.Features;

import static dsa.hcmiu.a2048pets.entities.model.Features.user;

public class HandleFile {

    private static HandleFile instance;
    private static String url = "highscore.txt";
    private Context context;
    private String file = "feature.json";
    private StringWriter output;

    private HandleFile() {
        context = MyApplication.getContext();
    }

    public static HandleFile get() {
        if (instance == null) {
            instance = new HandleFile();
        }
        return instance;
    }
    public void readFeaturesJSONFile() throws IOException, JSONException {

        // Đọc nội dung text của file company.json
        String jsonText = readText();

        // Đối tượng JSONObject gốc mô tả toàn bộ tài liệu JSON.
        JSONObject jsonRoot = new JSONObject(jsonText);
        //text.write(jsonText); //show
        Log.d("HandleFile", "readFeaturesJSONFile: " + jsonText);
        Features.sound = jsonRoot.getBoolean("sound");

        JSONObject jsonUser = jsonRoot.getJSONObject("user");
        user.setName(jsonUser.getString("name"));
        user.setEmail(jsonUser.getString("email"));
        user.setIDFacebook(jsonUser.getString("IDFb"));
        user.setProfilePic(jsonUser.getString("profilePic"));
        user.setLoggedFb(jsonUser.getBoolean("loggedFb"));
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
    }


    // Đọc nội dung text của một file nguồn.
    private String readText() throws IOException {
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

    public void convertToJSON() throws IOException {
        output = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(output);

        jsonWriter.beginObject();// begin root
        jsonWriter.name("sound").value(Features.sound);

        // "user": { ... }
        jsonWriter.name("user").beginObject(); // begin user
        jsonWriter.name("name").value(user.getName());
        jsonWriter.name("email").value(user.getEmail());
        jsonWriter.name("IDFb").value(user.getIDFacebook());
        jsonWriter.name("profilePic").value(user.getProfilePic());
        jsonWriter.name("loggedFb").value(user.isLoggedFb());
        jsonWriter.name("avatar").value(user.getAvatar());
        jsonWriter.name("highScore").value(user.highScore);
        jsonWriter.name("undo").value(user.undo);
        jsonWriter.name("hammer").value(user.hammer);
        jsonWriter.name("totalGold").value(user.totalGold);

        // "purchase array": [ ....]
        jsonWriter.name("purchasedIdItem").beginArray(); // begin purchase array
        for(int id: user.purchasedIdItem) {
            jsonWriter.value(id);
        }
        jsonWriter.endArray();// end purchase array
        jsonWriter.endObject();// end user
        // end root
        jsonWriter.endObject();
    }

    public void writeToFile() throws IOException {
        convertToJSON();
        FileOutputStream fos = context.openFileOutput(file,Context.MODE_PRIVATE);
        fos.write(output.toString().getBytes());
        fos.close();
    }
}
