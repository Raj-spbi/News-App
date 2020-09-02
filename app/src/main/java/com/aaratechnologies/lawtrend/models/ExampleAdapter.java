package com.aaratechnologies.lawtrend.models;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aaratechnologies.lawtrend.R;

import java.util.ArrayList;
import java.util.List;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> implements Filterable {
    private List<ModelMenus> exampleList;
    private List<ModelMenus> exampleListFull;
    Context context;

    class ExampleViewHolder extends RecyclerView.ViewHolder {
//        ImageView imageView;
        TextView menu;
        LinearLayout cover;
//        TextView textView2;

        ExampleViewHolder(View itemView) {
            super(itemView);
//            imageView = itemView.findViewById(R.id.image_view);
            menu = itemView.findViewById(R.id.menu);
            cover = itemView.findViewById(R.id.cover);
//            textView2 = itemView.findViewById(R.id.text_view2);
        }
    }

    public ExampleAdapter(List<ModelMenus> exampleList,Context context) {
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
//                    Toast.makeText(context, ""+modelMenus.get(position).getObject_id(), Toast.LENGTH_SHORT).show();
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://lawtrend.in/" +currentItem.getObject_id()));
                        browserIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(browserIntent);
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