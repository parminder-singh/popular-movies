package com.example.parmindr.popularmovies.adapter;

import android.content.Context;
import android.net.Uri;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MovieListItem movieListItem = getItem(position);
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_movie, parent, false);

        ImageView moviePosterView = (ImageView) rootView.findViewById(R.id.movie_poster);
        fetchAndLoadMoviePoster(movieListItem.getPosterUri(), moviePosterView);

        return rootView;
    }

    private void fetchAndLoadMoviePoster(Uri moviePosterUri, ImageView moviePosterView) {
        Picasso.with(getContext()).load(moviePosterUri).into(moviePosterView);
    }

}
