package popularmovies.view;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by parmindr on 4/10/16.
 */
public class MovieListItem implements Parcelable {

    private String title;
    private Uri posterUri;
    private String plotSynopsis;
    private String viewerRating;
    private String relaseDate;

    public MovieListItem(Parcel parcel) {
        this.title = parcel.readString();
        this.posterUri = (Uri) parcel.readParcelable(Uri.class.getClassLoader());
        this.plotSynopsis = parcel.readString();
        this.relaseDate = parcel.readString();
        this.viewerRating = parcel.readString();
    }

    public MovieListItem() {
        this.title = title;
        this.posterUri = posterUri;
        this.plotSynopsis = plotSynopsis;
        this.viewerRating = viewerRating;
        this.relaseDate = relaseDate;
    }

    public String getViewerRating() {
        return viewerRating;
    }

    public void setViewerRating(String viewerRating) {
        this.viewerRating = viewerRating;
    }

    public String getRelaseDate() {
        return relaseDate;
    }

    public void setRelaseDate(String relaseDate) {
        this.relaseDate = relaseDate;
    }

    public String getPlotSynopsis() {

        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Uri getPosterUri() {
        return posterUri;
    }

    public void setPosterUri(Uri posterUri) {
        this.posterUri = posterUri;
    }

    @Override
    public String toString() {
        return "MovieListItem{" +
                "title='" + title + '\'' +
                ", posterUri=" + posterUri +
                ", plotSynopsis='" + plotSynopsis + '\'' +
                ", viewerRating='" + viewerRating + '\'' +
                ", relaseDate='" + relaseDate + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeParcelable(posterUri, PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeString(plotSynopsis);
        dest.writeString(relaseDate);
        dest.writeString(viewerRating);
    }

    public static final Parcelable.Creator<MovieListItem> CREATOR = new Parcelable.Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new MovieListItem(source);
        }

        @Override
        public MovieListItem[] newArray(int size) {
            return new MovieListItem[size];
        }
    };
}