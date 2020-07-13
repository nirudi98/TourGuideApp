package io.github.nearchos.favourite.Country;

import android.widget.Filter;

import java.util.ArrayList;

public class CountryFilter extends Filter
{
    ArrayList<ModelCountryList> filterList;
    CountryAdapter countryAdapter;

    public CountryFilter(ArrayList<ModelCountryList> filterList, CountryAdapter countryAdapter) {
        this.filterList = filterList;
        this.countryAdapter = countryAdapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint)
    {
        FilterResults results = new FilterResults();

        if(constraint != null && constraint.length()>0)
        {
            constraint = constraint.toString().toUpperCase();

            ArrayList<ModelCountryList> filterModels = new ArrayList<>();
            for( int i=0; i <filterList.size(); i++)
            {
                if(filterList.get(i).getUsername_country().toUpperCase().contains(constraint))
                {
                    filterModels.add(filterList.get(i));
                }
            }
            results.count = filterModels.size();
            results.values = filterModels;
        }
        else
        {
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results)
    {
        countryAdapter.modelCountryLists = (ArrayList<ModelCountryList>) results.values;
        countryAdapter.notifyDataSetChanged();
    }


}
