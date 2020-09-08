package com.aaratechnologies.lawtrend.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aaratechnologies.lawtrend.R;
import com.aaratechnologies.lawtrend.models.ExampleAdapter;
import com.aaratechnologies.lawtrend.models.ModelMenus;

import java.util.ArrayList;
import java.util.List;

public class SearchNewsPostAdapter extends RecyclerView.Adapter<SearchNewsPostAdapter.ExampleViewHolder> implements Filterable {

    private List<ModelMenus> exampleList;
    private List<ModelMenus> exampleListFull;
    Context context;

    public SearchNewsPostAdapter(List<ModelMenus> exampleList,Context context) {
        this.exampleList = exampleList;
        exampleListFull = new ArrayList<>(exampleList);
        this.context = context;

    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_menus,
                parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        final ModelMenus currentItem = exampleList.get(position);

//        holder.imageView.setImageResource(currentItem.getImageResource());
        holder.menu.setText(currentItem.getMenu());
//        holder.textView2.setText(currentItem.getObject_id());
        holder.cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(, "", Toast.LENGTH_SHORT).show();
                ModelMenus currentItem = exampleList.get(position);
                Log.d("datata", "onClick: " + currentItem.getObject_id());

                if (currentItem.getObject_id().isEmpty()){
                    Toast.makeText(context, "Sorry Link is not Available", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(context, ""+currentItem.getObject_id(), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context, SeachedDataShowActivity.class);
                    intent.putExtra("url",currentItem.getObject_id());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return exampleList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }


    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        TextView menu;
        LinearLayout cover;
        public ExampleViewHolder(@NonNull View itemView) {

            super(itemView);
            menu = itemView.findViewById(R.id.menu);
            cover = itemView.findViewById(R.id.cover);

        }
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ModelMenus> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ModelMenus item : exampleListFull) {
                    if (item.getMenu().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            exampleList.clear();
            exampleList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
