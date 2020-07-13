package io.github.nearchos.favourite.Home;

import android.os.Bundle;

import io.github.nearchos.favourite.R;

public class Settings extends NavigationDrawerActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_settings, frameLayout);
    }
}
