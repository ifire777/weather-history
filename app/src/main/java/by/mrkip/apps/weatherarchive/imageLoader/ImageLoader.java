package by.mrkip.apps.weatherarchive.imageLoader;

import android.widget.ImageView;

public interface ImageLoader {

	void drawBitmap(final ImageView imageView, final String imageUrl);

	class Impl {

    		public static ImageLoader newInstance() {
			return new ImageLoaderImpl();

		}
	}

}
