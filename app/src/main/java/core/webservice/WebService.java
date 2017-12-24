package core.webservice;

import android.util.Log;
import android.webkit.MimeTypeMap;

import core.Singleton;
import core.parser.WebDataParser;
import core.reflection.listeners.Listener;
import core.reflection.messages.NetworkMessage;
import core.util.DialogProgress;
import core.util.contstant.Constants;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class WebService {
    private final String TAG = WebService.class.getSimpleName();

    private static final String APP_DOMAIN = "http://www.delvedapps.com/app/Other/Abercrombie";
    private static final String ROUTE_ABERCROMBIE_DATA = "/data.json";

    public final static int POST = 1;
    public final static int GET = 2;
    public final static int GET_ABERCROMBIE_DATA = 12;

    //Default Responses
    public final static String IO_ERROR = "Please check network connection.";
    public final static String UNKNOWN_ERROR = "Unknown Error: ";
    public final static String APP_ERROR = "Application Error: ";
    public final static String CLIENT_ERROR = "Client Error: ";
    public final static String SERVER_ERROR = "Server Error: ";
    public final static String VALID = "Valid Response.";

    private int retryCount = 1;

    public void webEvent(final int[] type, final String[] data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int status;
                String content;
                try {
                    final OkHttpClient client = new OkHttpClient();
                    client.setConnectTimeout(Constants.WEB_SERVICE_TIMEOUT, TimeUnit.MILLISECONDS);
                    client.setReadTimeout(Constants.WEB_SERVICE_TIMEOUT, TimeUnit.MILLISECONDS);

                    final Request.Builder builder = new Request.Builder();
                    builder.url(buildRoute(type[1]));

                    if (type[0] == GET) {
                        builder.addHeader(Constants.CONTENT_TYPE, Constants.MIME_TYPE_JSON);
                    } else if (type[0] == POST) {
                        builder.post(buildRequestBody(type[1], data));
                    }

                    Response response = client.newCall(builder.build()).execute();
                    status = response.code();
                    content = response.body().string();

                    Log.d(TAG, "Response Url  : " + response.request().url());
                    Log.d(TAG, "Response Code : " + String.valueOf(status));


                    if (response.isSuccessful()) {
                        WebDataParser webDataParser = new WebDataParser();
                        webDataParser.parseRawWebObject(content, type[1]);
                    }


                } catch (Exception e) {
                    status = ((e instanceof IOException) ? Constants.IO_ERROR_CODE : Constants.JSON_ERROR_CODE);
                    e.printStackTrace();
                }

                if (status == Constants.IO_ERROR_CODE && retryCount <= Constants.WEB_RETRY_MAX) {
                    DialogProgress.updateProgressDialog("Trying again " + String.valueOf(retryCount++) + " of " + String.valueOf(Constants.WEB_RETRY_MAX));
                    webEvent(type, data);
                } else {
                    retryCount = 1;
                    Listener.send(new NetworkMessage<>(type[1], getInternalResponse(status)));
                }
            }
        }).start();
    }

    private static String getInternalResponse(int status) {
        if (status >= 200 && status < 300)
            return Constants.VALID_WEB_EVENT;
        else if (status == Constants.IO_ERROR_CODE)
            return Constants.IO_ERROR;
        else if (status == Constants.JSON_ERROR_CODE)
            return Constants.APP_ERROR + String.valueOf(Constants.JSON_ERROR_CODE);
        else if (status >= 500 && status < 600)
            return Constants.SERVER_ERROR + String.valueOf(status);
        else if (status >= 400 && status < 500)
            return Constants.CLIENT_ERROR + String.valueOf(status);
        else
            return Constants.UNKNOWN_ERROR + String.valueOf(status);
    }

    private static RequestBody buildRequestBody(int type, String[] data) throws JSONException {
        final MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);

        switch (type) {
            default:
                //default
                builder.addFormDataPart("id", Singleton.DEVICE_ID);
                //encoder
                new FormEncodingBuilder().add("p1", data[0]).add("p2", data[1]).add("p3", data[2]).build();
                // json
                RequestBody.create(MediaType.parse(Constants.MIME_TYPE_JSON), new JSONObject().toString());
                //file
                builder.addFormDataPart("file", data[3],
                    RequestBody.create(MediaType.parse(MimeTypeMap.getSingleton()
                        .getMimeTypeFromExtension(Singleton.getShortFileExtension(data[3]))), new File(data[3])));
                throw new RuntimeException("(Invalid Post Body) type: " + String.valueOf(type));
        }

        //return builder.build();
    }


    private static String buildRoute(int type) {
        String url = APP_DOMAIN;
        switch (type) {
            case GET_ABERCROMBIE_DATA:
                return url + ROUTE_ABERCROMBIE_DATA;
            default:
                return null;
        }
    }

}