package com.example.parmindr.popularmovies.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.parmindr.popularmovies.R;
import com.example.parmindr.popularmovies.data.MovieListItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by parmindr on 4/16/16.
 */
public class MoviesAdapter extends ArrayAdapter<MovieListItem> {

    public MoviesAdapter(Context context) {
        super(context, 0);
    }

    private static final String LOG_TAG = MoviesAdapter.class.getSimpleName();

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_movie, parent, false);
            ImageView moviePoster = (ImageView) convertView.findViewById(R.id.movie_poster);
            viewHolder = new ViewHolder();
            viewHolder.moviePoster = moviePoster;
            convertView.setTag(viewHolder);
            Log.d(LOG_TAG, "Creating new view");
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            Log.d(LOG_TAG, "Recycling view");
        }

        MovieListItem movieListItem = getItem(position);
        fetchAndLoadMoviePoster(movieListItem.getPosterUri(), viewHolder.moviePoster);

        return convertView;
    }

    private void fetchAndLoadMoviePoster(Uri moviePosterUri, ImageView moviePosterView) {
        Picasso.with(getContext()).load(moviePosterUri).into(moviePosterView);
    }

    static class ViewHolder{
        ImageView moviePoster;
    }

}
