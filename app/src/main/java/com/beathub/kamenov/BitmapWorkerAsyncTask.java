package com.beathub.kamenov;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

public class BitmapWorkerAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private final static int ART_COVER_PADDING = 20;

    private int resolutionOfImages;

    private final WeakReference<ImageView> imageViewReference;
    private String songPath = "";

    public BitmapWorkerAsyncTask(ImageView imageView) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<>(imageView);
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(String... params) {
        songPath = params[0];
        return getAlbumArtCover(songPath);
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {

        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {

                imageView.setImageBitmap(bitmap);
                imageView.setPadding(ART_COVER_PADDING, ART_COVER_PADDING, ART_COVER_PADDING, ART_COVER_PADDING);
            }
        }
    }

    /**
     * Get ArtCover image from the metadata of audio file
     *
     * @param filePath - absolute path of the current audio file
     * @return Bitmap
     */
    public Bitmap getAlbumArtCover(String filePath) {

        MediaMetadataRetriever metaRetriever;
        metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(filePath);

        byte[] art = metaRetriever.getEmbeddedPicture();

        if (art == null) {
            //return default artCover (app logo)
            return BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.default_artcover);
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(art, 0, art.length, options);

        options.inSampleSize = calculateInSampleSize(options, resolutionOfImages, resolutionOfImages);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(art, 0, art.length, options);
    }

    /**
     * Downsampling algorithm for an artcover images
     *
     * @param options
     * @param reqWidth  new width of the image
     * @param reqHeight new height of the image
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public void setResolutionOfImages(int resolutionOfImages) {
        this.resolutionOfImages = resolutionOfImages;
    }
}
