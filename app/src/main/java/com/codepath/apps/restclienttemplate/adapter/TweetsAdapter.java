package com.codepath.apps.restclienttemplate.adapter;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.DetailedView;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;


import org.parceler.Parcels;

import java.util.List;

import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    Context context;
    List<Tweet> tweets;
    // Pass in the context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets){
        this.tweets = tweets;
        this.context = context;
    }

    // For each row, inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);

        return new ViewHolder(view);
    }
    // Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);
        holder.bind(tweet);

    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }


    // Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        ImageView ivMediaImage;
        TextView tvTimeStamp;
        ConstraintLayout clTweet;
        ImageButton ibFavorite;
        TextView tvFavoriteCount;

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
        }
    }
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of tweets
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }


//    public void onClick(View v) {
//        // gets item position
//        int position = getAdapterPosition();
//        if(tweet!=null){
//            // create intent for the new activity
//            Intent intent = new Intent(context, DetailedView.class);
//            // serialize the movie using parceler, use its short name as a key
//            intent.putExtra(DetailedView.EXTRA_CONTACT, Parcels.wrap(tweet));
//            // show the activity
//            context.startActivity(intent);
//        }
//      }


}
