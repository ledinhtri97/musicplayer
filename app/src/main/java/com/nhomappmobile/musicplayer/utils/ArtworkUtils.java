package com.nhomappmobile.musicplayer.utils;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.ImageView;

import com.nhomappmobile.musicplayer.R;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;

public class ArtworkUtils {
    private  static final Uri mArtworkUri;

    static {
        mArtworkUri=Uri.parse("content://media/external/audio/albumart");

    }

    //public static final Bitmap getArtworkFromFile(final Context context, final long albumId,int reqWidth,int reqHeight){
    public static final FileDescriptor getArtworkFromFile(final Context context,final long albumId ){
        if (albumId < 0){
            return null;
        }
        FileDescriptor fileDescriptor = null;
        try{
            final Uri uri = ContentUris.withAppendedId(mArtworkUri,albumId);
            final ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri,"r");
            if (parcelFileDescriptor != null){
                //fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                //artwork = decodeSampleBitmapFromDescriptor(fileDescriptor,reqWidth,reqHeight);
                fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            }
        } catch (final IllegalStateException e){

        } catch(final FileNotFoundException e){

        } catch (final OutOfMemoryError evict){
            Log.e("lol", "OutOfMemoryError - getArtworkFromFile - ", evict);
        }
        return fileDescriptor;
    }
    public static Uri getUri(final Context context,long albumId){
        Uri uri = ContentUris.withAppendedId(mArtworkUri, albumId);
        return uri;

    }
   /* public static final int calculateInSampleSize(final BitmapFactory.Options options, final int reqWidth, final int reqHeight){
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height>reqHeight || width > reqWidth) {
            if (height > reqHeight) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
            final float totalPixels = width * height;
*/
    public static class BitmapWorkerTask extends AsyncTask<BitmapTaskParams,Void,Bitmap>{
        private final WeakReference<ImageView> imageViewReference;
        private int data = 0;
        public BitmapWorkerTask(ImageView imageView){
            imageViewReference = new WeakReference<ImageView>(imageView);
        }
        @Override
        protected Bitmap doInBackGround(BitmapTaskParams... params){
            FileDescriptor fileDescriptor = params[0].fileDescriptor;
            int width = params[0].reqWidth;
            int height = params[0].reqHeight;
            return BitmapUtils.decodeSampledBitmapFromDescriptor(fileDescriptor,width,height);
        }
       @Override
       protected void onPostExecute(Bitmap bitmap) {
           if (isCancelled()) {
               bitmap = null;
           }
           if (imageViewReference != null && bitmap != null) {
               final ImageView imageView = imageViewReference.get();
               final BitmapWorkerTask bitmapWorkerTask =
                       getBitmapWorkerTask(imageView);
               if (this == bitmapWorkerTask && imageView != null) {
                   imageView.setImageBitmap(bitmap);
               }
           }
       }
    }
    private static class BitmapTaskParams {
        FileDescriptor fileDescriptor;
        int reqWidth,reqHeight;

        BitmapTaskParams(FileDescriptor filedesc,int width, int height) {
            this.fileDescriptor=filedesc;
            this.reqWidth = width;
            this.reqHeight = height;
        }
    }
   /* public static Bitmap decodeSampleBitmapFromDescriptor(FileDescriptor fileDescriptor,int reqWidth, int reqHeight){

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor,null,options);

        options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFileDescriptor(fileDescriptor,null,options);
    }*/
   public static void loadBitmap( Context context,ImageView imageView, long albumId,int reqWidth,int reqHeight) {
       FileDescriptor fileDescriptor=getArtworkFromFile(context,albumId);
       if (cancelPotentialWork(albumId, imageView)) {
           final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
           BitmapTaskParams params = new BitmapTaskParams(fileDescriptor, reqWidth, reqHeight);
           Bitmap mPlaceHolderBitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
           final AsyncDrawable asyncDrawable =
                   new AsyncDrawable(albumId, mPlaceHolderBitmap, task);
           imageView.setImageDrawable(asyncDrawable);
           if (fileDescriptor!=null) {
               task.execute(params);
           }
       }
   }
    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(long id, Bitmap bitmap,
                             BitmapWorkerTask bitmapWorkerTask) {
            // super(id, bitmap);
            bitmapWorkerTaskReference =
                    new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }
    public static boolean cancelPotentialWork(long data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final int bitmapData = bitmapWorkerTask.data;
            // If bitmapData is not yet set or it differs from the new data
            if (bitmapData == 0 || bitmapData != data) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }
    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

}
