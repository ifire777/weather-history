package by.mrkip.apps.weatherarchive.imageLoader;

import android.content.Context;
import android.widget.ImageView;

public interface SimpleImageLoader {

	void drawBitmap(final ImageView imageView, final String imageUrl);

	class Impl {

		public static SimpleImageLoader getInstance(Context context) {
//			context.getSystemService()
		}

		public static SimpleImageLoader newInstance() {
			return new SimpleImageLoaderImpl();

		}
	}

}
