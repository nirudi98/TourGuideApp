package io.github.nearchos.favourite.Notes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.github.nearchos.favourite.R;


public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    LayoutInflater inflater;
    List<NoteModel> notes;

    Adapter(Context context,List<NoteModel> notes)
    {
        this.inflater = LayoutInflater.from(context);
        this.notes = notes;

    }
    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.note_custom_listview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder viewholder, int position)
    {
        String title = notes.get(position).getTitle();
        String country = notes.get(position).getCountry();
        String date = notes.get(position).getDate();
        String time = notes.get(position).getTime();

        viewholder.nTitle.setText(title);
        viewholder.nCountry.setText(country);
        viewholder.nDate.setText(date);
        viewholder.nTime.setText(time);

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView nTitle,nCountry,nDate,nTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nTitle = itemView.findViewById(R.id.noteTitle);
            nCountry = itemView.findViewById(R.id.noteCountryDisplay);
            nDate = itemView.findViewById(R.id.noteDate);
            nTime = itemView.findViewById(R.id.noteTime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Intent i = new Intent(v.getContext(),Details.class);
                   i.putExtra("ID",notes.get(getAdapterPosition()).getID());
                   v.getContext().startActivity(i);

                }
            });

        }
    }
}
