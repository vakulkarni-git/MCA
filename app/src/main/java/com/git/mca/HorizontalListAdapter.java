package com.git.mca;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class HorizontalListAdapter extends RecyclerView.Adapter<HorizontalListAdapter.MyViewHolder> {
    private int[] mDataset;
    private int countCreate = 0;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public ImageView image;

        public MyViewHolder(ImageView v) {
            super(v);
            image = v;
        }

        @Override
        public void onClick(View v) {

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public HorizontalListAdapter() {
        mDataset = new int [0];
    }

    public void updateDataSet(int [] data) {
        mDataset = data;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HorizontalListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        Log.d("CreateViewHolder", "onCreateViewHolder" + countCreate);
        countCreate++;
        // create a new view
        ImageView v = (ImageView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontal_item, parent, false);

        HorizontalListAdapter.MyViewHolder vh = new HorizontalListAdapter.MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(HorizontalListAdapter.MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Log.d("CustomAdapter", "OnBindViewHolder" + position);
        //holder.textView.setText(mDataset[position]);
        //holder.imageView.setImageResource(mDataset[position]);
        //holder.textView.setText("Flag " + position);
        holder.image.setImageResource(mDataset[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}

