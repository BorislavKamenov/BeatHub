package AdaptersAndAbstractClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ObjectClasses.Album;
import com.beathub.kamenov.BitmapWorkerAsyncTask;
import com.beathub.kamenov.R;

import java.util.ArrayList;

import DataBases.BeatHubBaseHelper;

public class AlbumsGridAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Album> listOfAlbums;
    private BeatHubBaseHelper db;

    private ViewHolderGridAdapter holder;

    public AlbumsGridAdapter(Context context, ArrayList<Album> listOfAlbums){
        this.context = context;
        this.listOfAlbums = listOfAlbums;

        db = new BeatHubBaseHelper(context);
    }


    @Override
    public int getCount() {
        return listOfAlbums.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = convertView;

        if(view == null){

            holder = new ViewHolderGridAdapter();
            view = inflater.inflate(R.layout.grid_view_simple_row_item, parent, false);

            holder.albumImage = (ImageView) view.findViewById(R.id.album_image);

            holder.albumImage.setImageResource(R.drawable.default_album_image);
            holder.albumName = (TextView) view.findViewById(R.id.album_name);
            holder.artistName = (TextView) view.findViewById(R.id.artist_name);

            view.setTag(holder);
        }

        holder = (ViewHolderGridAdapter) view.getTag();

        Album album = listOfAlbums.get(position);

        holder.albumImage.setImageResource(R.drawable.default_album_image);

        //the image of the album is the artcover of the first song in this album
        String pathToOneSongFromAlbum = db.getPathOfOneSongInAlbum(album.getAlbumId());

        loadBitmap(pathToOneSongFromAlbum, holder.albumImage);
        holder.albumName.setText(album.getAlbumName());
        holder.artistName.setText(album.getArtistName());

        return view;
    }

    public void loadBitmap(String songPath, ImageView imageView) {
        BitmapWorkerAsyncTask task = new BitmapWorkerAsyncTask(imageView);
        task.setResolutionOfImages(100);
        task.execute(songPath);
    }

    class ViewHolderGridAdapter {
        ImageView albumImage;
        TextView albumName;
        TextView artistName;
    }


}
