package com.parse.tuber;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Juleelala on 11/9/15.
 * MyAdapter class stitches UI cards together with the searchbundle data set
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.SearchBundleViewHolder> {
    private ArrayList<SearchBundle> mDataset;
    private Bitmap bmap;
    private ViewGroup parent;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    public MyAdapter(ArrayList<SearchBundle> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.SearchBundleViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_card, parent, false);
        // set the view's size, margins, paddings and layout parameters

        this.parent = parent;
        return new SearchBundleViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(SearchBundleViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        SearchBundle sb = mDataset.get(position);
        holder.vName.setText(sb.name);
        holder.rbRating.setStepSize(1f);
        holder.rbRating.setRating((float)sb.avgRating);
        holder.searchBundle = sb;


        if(sb.profilePicture!= null) {
            holder.ivProfilePicture.setImageBitmap(sb.profilePicture);
        } else {
            Drawable d = parent.getContext().getDrawable(R.drawable.ic_person_black_48dp);
            holder.ivProfilePicture.setImageDrawable(d);
        }

        //This can go wherever
        holder.vFee.setText(String.format("Fee: $%.2f", sb.fee));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class SearchBundleViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected RatingBar rbRating;
        protected TextView vFee;
        protected ImageView ivProfilePicture;
        public SearchBundle searchBundle;
        public View view;


        //Attach search bundle element values to UI elements
        public SearchBundleViewHolder(View v) {
            super(v);
            view = v;
            view.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Profile.class);
                    intent.putExtra("id", searchBundle.id);
                    v.getContext().startActivity(intent);
                }
            });
            vName =  (TextView) v.findViewById(R.id.tvName);
            rbRating = (RatingBar) v.findViewById(R.id.rbRating);
            vFee = (TextView) v.findViewById(R.id.tvFee);
            ivProfilePicture = (ImageView) v.findViewById(R.id.ivProfilePicture);


        }
    }
}