package com.git.mca;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private int[][] mDataset;
    private Context context;
    private int countCreate = 0;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView textView;
        public RecyclerView horizontalView;
        public HorizontalListAdapter adapter;

        public MyViewHolder(FrameLayout v, Context context) {
            super(v);
            textView = v.findViewById(R.id.text_view);
            horizontalView = v.findViewById(R.id.horizontal_view);

            horizontalView.setHasFixedSize(true);
            horizontalView.setNestedScrollingEnabled(false);

            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.HORIZONTAL);
            horizontalView.setLayoutManager(layoutManager);

            // specify an adapter (see also next example)
            adapter = new HorizontalListAdapter();
            horizontalView.setAdapter(adapter);
        }

        @Override
        public void onClick(View v) {
            Log.d("ViewHolder", "Clicked item: " + textView.getText());
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CustomAdapter(int[][] myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        Log.d("CreateViewHolder", "onCreateViewHolder" + countCreate);
        countCreate++;
        // create a new view
        FrameLayout v = (FrameLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v, context);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Log.d("CustomAdapter", "OnBindViewHolder" + position);
        //holder.textView.setText(mDataset[position]);
        //holder.imageView.setImageResource(mDataset[position]);
        //holder.textView.setText("Flag " + position);

        holder.adapter.updateDataSet(null);
        holder.adapter.notifyDataSetChanged();

        holder.textView.setText("Countries " + position);
        holder.adapter.updateDataSet(mDataset[position]);
        holder.adapter.notifyDataSetChanged();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
