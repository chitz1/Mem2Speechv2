package gonzalvo.dpsm.cas.upm.edu.ph.mem2speech.recognizer;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class OfflineRecognizer implements Recognizer {
    private final RecognizerConfig config;

    private final InferenceInterface inferenceInterface;
    private final AssetManager assetManager;

    public OfflineRecognizer(RecognizerConfig config,
                             AssetManager assetManager,
                             String modelFilename) {
        this.config = config;
        this.assetManager = assetManager;
        inferenceInterface = new InferenceInterface(this.assetManager, modelFilename);
    }

    @Override
    public String recognizeHandwritingFrom(Bitmap bitmap) {
        float[] features = getFeaturesFrom(bitmap);
        String jsonString = loadJSONFrom(config.getServingModelConfigFilename());
        GraphComponents graphComponents = new GraphComponents(jsonString);
        InputNodes inputNodes = graphComponents.getInputNodes();
        InputNode inputNode = new InputNode(
                "features",
                features,
                inputNodes.getInputShape("features")
        );
        OutputNodes outputNodes = graphComponents.getOutputNodes();
        List<int[]> outputs = inferenceInterface.runInference(new InputNode[]{inputNode}, outputNodes);
        return decode(outputs.get(0));
    }

    private String loadJSONFrom(String jsonFilename) {
        String json;
        try {
            InputStream is = assetManager.open(jsonFilename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private float[] getFeaturesFrom(Bitmap bitmap) {
        bitmap = resizeBitmap(bitmap);
        bitmap = invertColors(bitmap);
        int[] intValues = getPixelValuesFrom(bitmap);
        float[] floatValues = new float[bitmap.getWidth() * bitmap.getHeight()];
        for (int i = 0; i < intValues.length; ++i) {
            final int val = intValues[i];
            floatValues[i] = (((val >> 16) & 0xFF));
        }
        return floatValues;
    }

    private Bitmap invertColors(Bitmap bitmap) {
        Bitmap outputImage = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outputImage);
        Paint invertedPaint = new Paint();
        invertedPaint.setColorFilter(new ColorMatrixColorFilter(NEGATIVE));
        canvas.drawBitmap(bitmap, 0, 0, invertedPaint);
        return outputImage;
    }

    private static final float[] NEGATIVE = {
            -1.0f,     0,     0,    0, 255, // red
            0, -1.0f,     0,    0, 255, // green
            0,     0, -1.0f,    0, 255, // blue
            0,     0,     0, 1.0f,   0  // alpha
    };

    private Bitmap resizeBitmap(Bitmap bitmap) {
        int maxWidth = config.getImageWidth();
        int maxHeight = config.getImageHeight();
        if (maxHeight > 0 && maxWidth > 0) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            bitmap = Bitmap.createScaledBitmap(bitmap, finalWidth, finalHeight, true);
            bitmap = finalWidth == maxWidth ? padBottom(bitmap, maxHeight) : padRight(bitmap, maxWidth);
            return bitmap;
        } else {
            return bitmap;
        }
    }

    private Bitmap padRight(Bitmap bitmap, int maxWidth) {
        Bitmap outputImage = Bitmap.createBitmap(maxWidth, bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outputImage);
        canvas.drawARGB(255, 255, 255, 255);
        canvas.drawBitmap(bitmap, 0, 0, null);
        return outputImage;
    }

    private Bitmap padBottom(Bitmap bitmap, int maxHeight) {
        Bitmap outputImage = Bitmap.createBitmap(bitmap.getWidth(), maxHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outputImage);
        canvas.drawARGB(255, 255, 255, 255);
        canvas.drawBitmap(bitmap, 0, 0, null);
        return outputImage;
    }

    private int[] getPixelValuesFrom(Bitmap bitmap) {
        int[] intValues = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        return intValues;
    }

    private String decode(int[] result) {
        String[] charset = config.getCharset();
        StringBuilder sb = new StringBuilder();
        for(int encodedCharacter : result){
            sb.append(charset[encodedCharacter]);
        }
        return sb.toString();
    }

}
