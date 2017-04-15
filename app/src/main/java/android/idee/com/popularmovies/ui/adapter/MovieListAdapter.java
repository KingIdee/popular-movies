package android.idee.com.popularmovies.ui.adapter;

import android.content.Context;
import android.idee.com.popularmovies.R;
import android.idee.com.popularmovies.data.model.MovieModel;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by idee on 4/12/17.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private ArrayList<MovieModel> movieArrayList = new ArrayList<>();
    private Context mContext;
    private ItemClickListener mItemClickListener;

    public void setMovieList(ArrayList<MovieModel> movieArrayList){
        this.movieArrayList = movieArrayList;
        notifyDataSetChanged();
    }

    public MovieListAdapter(Context mContext,  ItemClickListener mItemClickListener) {
        this.mContext = mContext;
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.movie_item_list,parent,false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg
        holder.movieTitle.setText(movieArrayList.get(position).getTitle());
        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/" +movieArrayList.get(position).getPosterPath()).into(holder.posterImage);
    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView posterImage;
        TextView movieTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            posterImage = (ImageView) itemView.findViewById(R.id.iv_movie_poster_image);
            movieTitle = (TextView) itemView.findViewById(R.id.tv_movie_title);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.recyclerViewOnClick(getAdapterPosition());
        }
    }

    public interface ItemClickListener{
        void recyclerViewOnClick(int position);
    }

}