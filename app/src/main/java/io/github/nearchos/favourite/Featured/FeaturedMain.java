package io.github.nearchos.favourite.Featured;


import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.github.nearchos.favourite.Home.NavigationDrawerActivity;
import io.github.nearchos.favourite.R;

public class FeaturedMain extends NavigationDrawerActivity
{

    private static final String TAG = "FeaturedActivity";

    //vars
    private ArrayList<Integer> mImage = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_featured, frameLayout);

        getImages();
    }

    private void getImages(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImage.add(R.drawable.featured_pic4);

        mImage.add(R.drawable.featured_pic2);

        mImage.add(R.drawable.featured_pic4);

        mImage.add(R.drawable.featured_pic2);

        initRecyclerView();

    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recycle_featured);
        recyclerView.setLayoutManager(layoutManager);
        FeaturedRecyclerViewAdapter adapter = new FeaturedRecyclerViewAdapter(this, mImage);
        recyclerView.setAdapter(adapter);
    }
}
