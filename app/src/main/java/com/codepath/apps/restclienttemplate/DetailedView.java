package com.codepath.apps.restclienttemplate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.adapter.TweetsAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class DetailedView extends AppCompatActivity {
    public static final String EXTRA_CONTACT = "EXTRA_CONTACT";
    ImageView ivProfileImage2;
    TextView tvTimeStamp2;
    TextView tvScreenName2;
    TextView tvBody2;
    ImageView ivMediaImage2;
    ImageButton ibFavorite;
    TextView tvFavoriteCount;
    ImageButton ibReply;
    private Tweet mTweet;
    Context context;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);

        // resolve the view objects
        ivProfileImage2 = (ImageView) findViewById(R.id.ivProfileImage);
        tvTimeStamp2 = (TextView) findViewById(R.id.tvTimeStamp);
        tvScreenName2 = (TextView) findViewById(R.id.tvScreenName);
        tvBody2 = (TextView) findViewById(R.id.tvBody);
        ivMediaImage2 = (ImageView) findViewById(R.id.ivMediaImage);


        //Extract from bundle
        mTweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_CONTACT));

        //Fill views
        Glide.with(DetailedView.this).load(mTweet.user.profileImageUrl).into(ivProfileImage2);
        tvScreenName2.setText(mTweet.user.screenName);
        tvTimeStamp2.setText(mTweet.getFormattedTimestamp());
        tvBody2.setText(mTweet.body);
        Glide.with(DetailedView.this).load(mTweet.mediaURL).into(ivMediaImage2);

    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        ImageView ivMediaImage;
        TextView tvTimeStamp;
        ConstraintLayout clTweet;
        ImageButton ibFavorite;
        TextView tvFavoriteCount;
        ImageButton ibReply;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            ivMediaImage = itemView.findViewById(R.id.ivMediaImage);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
            clTweet = itemView.findViewById(R.id.tweet);
            ibFavorite = itemView.findViewById(R.id.ibFavorite);
            tvFavoriteCount = itemView.findViewById(R.id.tvFavoriteCount);
            ibReply = itemView.findViewById(R.id.ibReply);
        }

        // Clean all elements of the recycler

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.screenName);
            tvTimeStamp.setText(tweet.getFormattedTimestamp());
            Glide.with(context).load(tweet.user.profileImageUrl).into(ivProfileImage);
            Glide.with(context).load(tweet.mediaURL).into(ivMediaImage);
            tvFavoriteCount.setText(String.valueOf(tweet.favoriteCount));
            //if the tweet is already liked, changes the icon to 'liked'
            if(tweet.isFavorited){
                Drawable newImage = context.getDrawable(android.R.drawable.btn_star_big_on);
                ibFavorite.setImageDrawable(newImage);
            }else{
                Drawable newImage = context.getDrawable(android.R.drawable.btn_star_big_off);
                ibFavorite.setImageDrawable(newImage);
            }
            tvBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //create intent for the new activity
                    Intent intent = new Intent(context, DetailedView.class);
                    // serialize the movie using parceler, use its short name as a key
                    intent.putExtra(DetailedView.EXTRA_CONTACT, Parcels.wrap(tweet));
                    // show the activity
                    context.startActivity(intent);
                }
            });

            ibFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //if not favorited then >
                    if(!tweet.isFavorited){
                        // call the API to favorite the tweet
                        tweet.isFavorited = true;
                        TwitterApp.getRestClient(context).favoriteTweet(tweet.id, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.i("adapter", "This should've been favorited go check");
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

                            }
                        });

                        //change the drawable to star on (btn_star_big_on)
                        Drawable newImage = context.getDrawable(android.R.drawable.btn_star_big_on);
                        ibFavorite.setImageDrawable(newImage);
                        //change tvFavoriteCount ++
                        tweet.favoriteCount++;
                        tvFavoriteCount.setText(String.valueOf(tweet.favoriteCount));
                    }else {


                        //else unfavorite
                        //tell tiwtter I dont want to favorite this
                        tweet.isFavorited = false;
                        TwitterApp.getRestClient(context).favoriteTweet(tweet.id, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.i("adapter", "This shoul've been unfavorited go check");
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

                            }
                        });

                        //change drawable back to (btn_star_big_off)
                        Drawable newImage = context.getDrawable(android.R.drawable.btn_star_big_off);
                        ibFavorite.setImageDrawable(newImage);
                        //decrement tvFavoriteCount --
                        tweet.favoriteCount--;
                        tvFavoriteCount.setText(String.valueOf(tweet.favoriteCount));
                    }
                }
            });
            ibReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //egg!!! OMG WOWOW
                    //pop up a compose screen
                    Intent i = new Intent(context, ComposeActivity.class);
                    i.putExtra("should_reply_to_tweet", true);
//                    i.putExtra("id_of_tweet_to_reply_to", tweet.id);
//
//                    i.putExtra("screenname_of_tweet_to_reply_to", tweet.user.screenName);
                    i.putExtra("tweet", Parcels.wrap(tweet));
                    ((Activity) context).startActivityForResult(i, TimelineActivity.REQUEST_CODE);

                    // it is gonna be a brand new tweet, but it'll have an extra attribute
                    //extra attribute: "in_reply_to_status_id"
                }
            });
        }
    }


}