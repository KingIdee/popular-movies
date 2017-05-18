package com.idee.android.popularmovies.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.idee.popularmovies.R;
import com.idee.android.popularmovies.data.model.ReviewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by idee on 5/15/17.
 */

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {

    private ArrayList<ReviewModel> reviewArrayList = new ArrayList<>();
    private Context mContext;

    public void setReviewList(ArrayList<ReviewModel> reviewArrayList){
        this.reviewArrayList = reviewArrayList;
        this.notifyDataSetChanged();
    }

    public ReviewListAdapter(Context mContext) {
        this.mContext = mContext;

    }

    @Override
    public ReviewListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.review_item_list,parent,false);
        return new ReviewListAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ReviewListAdapter.ViewHolder holder, int position) {
        holder.author.setText(reviewArrayList.get(position).getAuthor());
        holder.content.setText(reviewArrayList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder  {

        @BindView(R.id.tv_author)
        TextView author;

        @BindView(R.id.tv_content)
        TextView content;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }

}