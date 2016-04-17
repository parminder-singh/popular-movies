package com.example.parmindr.popularmovies.fragment;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.parmindr.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import popularmovies.view.MovieListItem;

public class MovieDetailsActivityFragment extends Fragment {

    public MovieDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        Intent intent = getActivity().getIntent();
        MovieListItem movieListItem = intent.getParcelableExtra(MoviesCollectionFragment.MOVIE_LIST_ITEM_INTENT_KEY);

        ImageView moviePosterView = (ImageView) rootView.findViewById(R.id.movie_poster);
        fetchAndLoadMoviePoster(movieListItem.getPosterUri(), moviePosterView);

        TextView movieNameView = (TextView) rootView.findViewById(R.id.movie_label);
        movieNameView.setText(movieListItem.getTitle());

        TextView movieViewerRatingView = (TextView) rootView.findViewById(R.id.movie_viewer_rating);
        movieViewerRatingView.setText(movieListItem.getViewerRating());

        TextView movieReleaseDateView = (TextView) rootView.findViewById(R.id.movie_release_date);
        movieReleaseDateView.setText(movieListItem.getRelaseDate());

        TextView moviePlotSynopsisView = (TextView) rootView.findViewById(R.id.movie_plot_synopsis);
        moviePlotSynopsisView.setText(movieListItem.getPlotSynopsis());

        return rootView;
    }

    private void fetchAndLoadMoviePoster(Uri moviePosterUri, ImageView moviePosterView) {
        Picasso.with(getActivity()).load(moviePosterUri).into(moviePosterView);
    }
}
