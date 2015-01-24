package com.beathub.kamenov;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;

import java.lang.ref.WeakReference;

public class ArtCoverContentFragment extends Fragment {

    private ImageView artcover;
    private final static int ART_COVER_PADDING = 20;
	
	public static Fragment newInstance(MainArtCoverFragment context, int pos, float scale,boolean IsBlured)
	{
		
		Bundle b = new Bundle();
		b.putInt("pos", pos);
		b.putFloat("scale", scale);
		b.putBoolean("IsBlured", IsBlured);
		return Fragment.instantiate(context.getActivity(), ArtCoverContentFragment.class.getName(), b);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (container == null) {
			return null;
		}

		LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.artcover_inner_view_fragment, container, false);

        artcover = (ImageView) layout.findViewById(R.id.artwork_viewpager_imageview);

        loadBitmap(((MainActivity)getActivity()).getCurrentPlaylingSong().getPath(), artcover);

		MyLinearLayout root = (MyLinearLayout) layout.findViewById(R.id.animated_view_pager);
		float scale = this.getArguments().getFloat("scale");
		root.setScaleBoth(scale);
		boolean isBlured=this.getArguments().getBoolean("IsBlured");
		if(isBlured)
		{
			ViewHelper.setAlpha(root, ArtcoverViewPagerAdapter.getMinAlpha());
			ViewHelper.setRotationY(root, ArtcoverViewPagerAdapter.getMinDegree());
		}
		return layout;
	}

    public void loadBitmap(String songPath, ImageView imageView) {
        BitmapWorkerAsyncTask task = new BitmapWorkerAsyncTask(imageView);
        task.execute(songPath);
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
            return BitmapFactory.decodeResource(getResources(), R.drawable.asd);
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(art, 0, art.length, options);

        options.inSampleSize = calculateInSampleSize(options, 200, 200);

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

    public class BitmapWorkerAsyncTask extends AsyncTask<String, Void, Bitmap> {
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


    }
}
