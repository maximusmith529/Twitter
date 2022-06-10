package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import com.codepath.apps.restclienttemplate.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Parcel
public class Tweet {

    public static final String TAG = "Tweet";
    public String body;
    public String createdAt;
    public User user;
    public String mediaURL;
    public boolean isFavorited;
    public boolean isRetweeted;
    public int favoriteCount;
    public int retweetedCount;

    public String id;

    public Tweet(){}

    public static Tweet fromJson (JSONObject jsonObject) throws JSONException {
        if(jsonObject.has("retweeted_status")){
            return null;
        }


        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.isFavorited = jsonObject.getBoolean("favorited");
        tweet.isRetweeted = jsonObject.getBoolean("retweeted");
        tweet.favoriteCount = jsonObject.getInt("favorite_count");
        tweet.retweetedCount = jsonObject.getInt("retweet_count");
        tweet.id = jsonObject.getString("id_str");

        if(!jsonObject.getJSONObject("entities").has("media")){
            tweet.mediaURL = "none";
            Log.e(TAG,tweet.mediaURL);
        }else{
            tweet.mediaURL = jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url_https");

        }
        return tweet;
    }
    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            Tweet newTweet = fromJson(jsonArray.getJSONObject(i));
            if(newTweet != null)
                tweets.add(newTweet);

        }

        return tweets;
    }
    public String getFormattedTimestamp(){
        return TimeFormatter.getTimeDifference(createdAt);
    }
}
