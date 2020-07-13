package io.github.nearchos.favourite.Favourite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.github.nearchos.favourite.Database.CountryDatabase;
import io.github.nearchos.favourite.R;



public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {

    private Context context;
    private List<CountryFavItem> countryfavItemList;
    private CountryDatabase db;


    public FavAdapter(Context context, List<CountryFavItem> countryfavItemList) {
        this.context = context;
        this.countryfavItemList = countryfavItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_favorite_list_item,
                parent, false);
        db = new CountryDatabase(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.favTextView.setText(countryfavItemList.get(position).getItem_title());
    }

    @Override
    public int getItemCount() {
        return countryfavItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView favTextView;
        Button favBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            favTextView = itemView.findViewById(R.id.favTextView);
            favBtn = itemView.findViewById(R.id.favBtn);


            //remove from fav
            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final CountryFavItem countryfavItem = countryfavItemList.get(position);
                    db.remove_fav(countryfavItem.getKey_id(),countryfavItem.getUsername());
                    removeItem(position);

                }
            });

        }
    }



    private void removeItem(int position) {
        countryfavItemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,countryfavItemList.size());
    }



}
