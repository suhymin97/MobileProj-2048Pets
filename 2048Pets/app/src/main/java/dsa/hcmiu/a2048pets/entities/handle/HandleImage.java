package dsa.hcmiu.a2048pets.entities.handle;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import dsa.hcmiu.a2048pets.MyApplication;

public class HandleImage {

    private String imageDir = "imageDir";
    private String imageName = "userAva.png";
    private static HandleImage instance;
    private Context context;

    private HandleImage() {
        context = MyApplication.getContext();
    }

    public static HandleImage get() {
        if (instance == null) {
            instance = new HandleImage();
        }
        return instance;
    }

    private void log(String logStr) {
        Log.d("Handle image", logStr);
    }

    public void loadImageFromUrl(String imageUrl, ImageView ivImage) {
        log("Load image from url");
        Picasso.get().load(imageUrl).into(ivImage);
    }

    public void downloadSaveImageFromUrl(String imageUrl) {
        log("Load image from url and save it to disk through Picasso");
        Picasso.get().load(imageUrl).into(picassoImageTarget(imageDir, imageName));
    }

    public boolean loadImageFromDisk(ImageView ivImage) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(imageDir, Context.MODE_APPEND);
        File myImageFile = new File(directory, imageName);

        if (myImageFile.exists()) {
            log("Load image from disk: " + myImageFile.getAbsolutePath());
            Picasso.get().load(myImageFile).into(ivImage);
            return true;
        } else {
            log("The image doesn't exist on disk!");
            return false;
        }
    }

    public void deleteImageFromDisk() {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(imageDir, Context.MODE_PRIVATE);
        File myImageFile = new File(directory, imageName);

        if (myImageFile.exists()) {
            if (myImageFile.delete()) log("The image on the disk deleted successfully!");
            else log("Failed to delete " + myImageFile.getAbsolutePath());
        } else {
            log("No such image on disk! No need to delete it!");
        }
    }

    public boolean checkIfImageExist() {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(imageDir, Context.MODE_PRIVATE);
        File myImageFile = new File(directory, imageName);

        return myImageFile.exists();
    }

    // Target class for saving image bitmap returned from Picasso
    private Target picassoImageTarget(String imageDir, final String imageName) {
        Log.d("picassoImageTarget", " picassoImageTarget");
        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE); // path to /data/data/yourapp/app_imageDir
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                final File myImageFile = new File(directory, imageName); // Create image file
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(myImageFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                log("image saved to >>>" + myImageFile.getAbsolutePath());
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if (placeHolderDrawable != null) {
                }
            }
        };
    }
}
