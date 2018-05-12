package gonzalvo.dpsm.cas.upm.edu.ph.mem2speech.recognizer;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;

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
        String servingModelConfig = loadJSONFrom(config.getServingModelConfigFilename());
        GraphComponents graphComponents = new GraphComponents(servingModelConfig);
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

    private Bitmap resize(Bitmap image, int maxHeight, int maxWidth) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            if (finalWidth == maxWidth) {
                image = padBottom(image, maxHeight);
            } else {
                image = padRight(image, maxWidth);
            }
            return image;
        } else {
            return image;
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

    private float[] getFeaturesFrom(Bitmap bitmap) {
        String imageConfigJsonString = loadJSONFrom(config.getImageConfigFile());
        ImageConfig imageConfig = new ImageConfig(imageConfigJsonString);
        bitmap = resize(bitmap, imageConfig.getImageHeight(), imageConfig.getImageWidth());
        int[] pixels = getPixelValuesFrom(bitmap);
        float[] floatValues = new float[pixels.length];
        for (int i = 0; i < pixels.length - 1; ++i) {
            final int pixel = pixels[i];
            int b = pixel & 0xff;
            floatValues[i] = (float)((0xff - b)/255.0);
        }
        return floatValues;
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
