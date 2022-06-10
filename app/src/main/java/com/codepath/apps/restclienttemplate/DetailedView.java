package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.adapter.TweetsAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class DetailedView extends AppCompatActivity {
    public static final String EXTRA_CONTACT = "EXTRA_CONTACT";
    ImageView ivProfileImage2;
    TextView tvTimeStamp2;
    TextView tvScreenName2;
    TextView tvBody2;
    ImageView ivMediaImage2;
    private Tweet mTweet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);

        // resolve the view objects
        ivProfileImage2 = (ImageView) findViewById(R.id.ivProfileImage2);
        tvTimeStamp2 = (TextView) findViewById(R.id.tvTimeStamp2);
        tvScreenName2 = (TextView) findViewById(R.id.tvScreenName2);
        tvBody2 = (TextView) findViewById(R.id.tvBody2);
        ivMediaImage2 = (ImageView) findViewById(R.id.ivMediaImage2);


        //Extract from bundle
        mTweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_CONTACT));

        //Fill views
        Glide.with(DetailedView.this).load(mTweet.user.profileImageUrl).into(ivProfileImage2);
        tvScreenName2.setText(mTweet.user.screenName);
        tvTimeStamp2.setText(mTweet.getFormattedTimestamp());
        tvBody2.setText(mTweet.body);
        Glide.with(DetailedView.this).load(mTweet.mediaURL).into(ivMediaImage2);

    }
}