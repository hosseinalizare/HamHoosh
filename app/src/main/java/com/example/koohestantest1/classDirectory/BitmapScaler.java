package com.example.koohestantest1.classDirectory;

import android.graphics.Bitmap;

public class BitmapScaler {
    public int maxWidth = 1080;
    public int maxHeight = 1080;
    Bitmap tempb;
    public Bitmap ResizToStandard(Bitmap b)
    {

        if (b.getWidth()> maxWidth)
        {
            tempb = scaleToFitWidth( b, maxWidth);
        }

        if(tempb.getHeight() > maxHeight)
        {
            tempb = scaleToFitWidth(tempb, maxHeight);
        }
        return tempb;
    }

    // Scale and maintain aspect ratio given a desired width

    // BitmapScaler.scaleToFitWidth(bitmap, 100);

    private static Bitmap scaleToFitWidth(Bitmap b, int width)

    {

        float factor = width / (float)b.getWidth();

        return Bitmap.createScaledBitmap(b, width, (int)(b.getHeight() * factor), true);

    }


    // Scale and maintain aspect ratio given a desired height

    // BitmapScaler.scaleToFitHeight(bitmap, 100);

    private static Bitmap scaleToFitHeight(Bitmap b, int height)

    {

        float factor = height / (float)b.getHeight();

        return Bitmap.createScaledBitmap(b, (int)(b.getWidth() * factor), height, true);

    }

    // ...
}
