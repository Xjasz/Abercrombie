package core.parser;

import android.util.Log;

import com.jaszapps.abercrombie.ItemMain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import core.Singleton;
import core.webservice.WebService;

public class WebDataParser {
    private static final String TAG = WebDataParser.class.getSimpleName();

    public void parseRawWebObject(String data, int type) throws JSONException {
        JSONArray array = new JSONArray(data);
        Singleton.AdapterItems.clear();
        for (int i = 0; i < array.length(); i++) {
            if (type == WebService.GET_ABERCROMBIE_DATA) {
                parseAbercrombieData(array.getJSONObject(i));
            }
        }
    }

    private void parseAbercrombieData(JSONObject jObject) throws JSONException, OutOfMemoryError {
        Log.d(TAG, "parseAbercrombieData");

        String title = jObject.optString("title");
        String backgroundImage = jObject.optString("backgroundImage");
        String promoMessage = jObject.optString("promoMessage");
        String topDescription = jObject.optString("topDescription");
        String bottomDescription = jObject.optString("bottomDescription");

        String contentTitle1 = null;
        String contentLink1 = null;
        String contentTitle2 = null;
        String contentLink2 = null;

        Log.d(TAG, bottomDescription);
        while (bottomDescription.contains("\\")){
            bottomDescription = bottomDescription.replace("\\","");
        }


        JSONArray content = jObject.optJSONArray("content");
        if (content != null) {
            if (content.optJSONObject(0) != null) {
                contentTitle1 = content.optJSONObject(0).optString("title");
                contentLink1 = content.optJSONObject(0).optString("target");
            }
            if (content.optJSONObject(1) != null) {
                contentTitle2 = content.optJSONObject(1).optString("title");
                contentLink2 = content.optJSONObject(1).optString("target");
            }
        }


        ItemMain item = new ItemMain(backgroundImage, topDescription, title, promoMessage, bottomDescription, contentTitle1, contentLink1, contentTitle2, contentLink2);

        Singleton.AdapterItems.add(item);
    }

}
