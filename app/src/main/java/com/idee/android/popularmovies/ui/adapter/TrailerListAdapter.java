package com.idee.android.popularmovies.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.idee.popularmovies.R;
import com.idee.android.popularmovies.data.model.TrailerModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by idee on 5/15/17.
 */

public class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.ViewHolder> {

    private ArrayList<TrailerModel> trailerArrayList = new ArrayList<>();
    private Context mContext;
    private ItemClickListener mItemClickListener;

    public void setTrailerList(ArrayList<TrailerModel> movieArrayList){
        this.trailerArrayList = movieArrayList;
        this.notifyDataSetChanged();
    }

    public TrailerListAdapter(Context mContext, ItemClickListener mItemClickListener) {
        this.mContext = mContext;
        this.mItemClickListener = mItemClickListener;

    }

    @Override
    public TrailerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.trailer_item_list,parent,false);
        return new TrailerListAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(TrailerListAdapter.ViewHolder holder, int position) {
        holder.nameOfTrailer.setText(trailerArrayList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return trailerArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_name)
        TextView nameOfTrailer;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void onClick(View v) {
            TrailerModel model = trailerArrayList.get(getAdapterPosition());
            mItemClickListener.trailerOnClick(model);
        }
    }

    public interface ItemClickListener{
        void trailerOnClick(TrailerModel movieModel);
    }

}