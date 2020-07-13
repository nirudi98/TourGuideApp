package io.github.nearchos.favourite.Country;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.github.nearchos.favourite.R;


public class CountryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    ImageView myimg;
    TextView title;
    TextView tvUsername;
    ModelCountryList modelCountryList;

    CountryClickListner countryClickListner;



    CountryViewHolder(@NonNull View itemView)
    {
        super(itemView);

      //  this.tvUsername=itemView.findViewById(R.id.username_users);
        this.myimg= itemView.findViewById(R.id.image_country_display);
        this.title= itemView.findViewById(R.id.list_country_item);

        modelCountryList= new ModelCountryList();
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        this.countryClickListner.onItemClickListener(v, getLayoutPosition());
    }

    public void setCountryClickListner(CountryClickListner cc)
    {
        this.countryClickListner = cc;
    }
}
