package io.github.nearchos.favourite.Country;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.github.nearchos.favourite.Database.CountryDatabase;
import io.github.nearchos.favourite.Database.DatabaseHelper;
import io.github.nearchos.favourite.R;


public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> implements Filterable {

    private Context context;
    ArrayList<ModelCountryList> modelCountryLists, filterList;
    CountryFilter customCountryFilter;
    CountryDatabase db;

    public CountryAdapter(ArrayList<ModelCountryList> modelCountryLists, Context context) {
        this.context = context;
        this.modelCountryLists = modelCountryLists;
        this.filterList = modelCountryLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        db = new CountryDatabase(context);

        //create table on first
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        Log.d("msg", "first start value: "+firstStart);
        if (firstStart) {
          createTableOnFirstStart();
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_row_displaycountrylist, parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CountryAdapter.ViewHolder holder, int position)
    {
       final ModelCountryList modelCountryList = modelCountryLists.get(position);

       readCursorData(modelCountryList,holder);

       holder.titleTextView.setText(modelCountryLists.get(position).getUsername_country());
       byte[] foodImage = modelCountryList.getImg();
       Bitmap bitmap = BitmapFactory.decodeByteArray(foodImage, 0, foodImage.length);
       holder.imageView.setImageBitmap(bitmap);

    }



    @Override
    public int getItemCount() {
        return modelCountryLists.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView titleTextView;
        Button favBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_country_display);
            titleTextView = itemView.findViewById(R.id.list_country_item);
            favBtn = itemView.findViewById(R.id.favBtn);

            titleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String country_username = modelCountryLists.get(position).getUsername_country();


                    //get data with intent
                    //putting country name into shared preferences
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("country_name",country_username);
                    editor.apply();

                    Intent intent = new Intent(context, CountryMapsActivity.class);
                    context.startActivity(intent);
                   // intent.putExtra("country_name", country_username);
                    //  intent.putExtra("image",bytes);

                }
            });
            //add to fav btn
            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    ModelCountryList modelCountryList = modelCountryLists.get(position);

                    if(modelCountryList.getFavStatus().equals("0"))
                    {
                        modelCountryList.setFavStatus("1");
                        db.insertIntoTheDatabase(modelCountryList.getUsername_country(),modelCountryList.getKey_id(),modelCountryList.getFavStatus(),modelCountryList.getUsername());
                        favBtn.setBackgroundResource(R.drawable.ic_favorite);
                    }
                    else
                    {
                        modelCountryList.setFavStatus("0");
                        db.remove_fav(modelCountryList.getKey_id(),modelCountryList.getUsername());
                        favBtn.setBackgroundResource(R.drawable.ic_favorite_border_white);
                    }

                    //   likeClick(coffeeItem, favBtn, likeCountTextView);
                }
            });
        }
    }

    private void createTableOnFirstStart() {
        db.insertEmpty();

        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }


    private void readCursorData(ModelCountryList modelCountryList, ViewHolder viewHolder) {
        Cursor cursor = db.read_all_data(modelCountryList.getKey_id(),modelCountryList.getUsername());
        SQLiteDatabase db1 = db.getReadableDatabase();
        try {
            while (cursor.moveToNext()) {
                String item_fav_status = cursor.getString(cursor.getColumnIndex(CountryDatabase.FAVORITE_STATUS));
                modelCountryList.setFavStatus(item_fav_status);

                //check fav status
                if (item_fav_status != null && item_fav_status.equals("1")) {
                    viewHolder.favBtn.setBackgroundResource(R.drawable.ic_favorite);
                } else if (item_fav_status != null && item_fav_status.equals("0")) {
                    viewHolder.favBtn.setBackgroundResource(R.drawable.ic_favorite_border_white);
                }
            }
        } finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            db1.close();
        }

    }




    @Override
    public Filter getFilter() {
        if (customCountryFilter == null) {
            customCountryFilter = new CountryFilter(filterList, this);
        }
        return customCountryFilter;
    }




}
