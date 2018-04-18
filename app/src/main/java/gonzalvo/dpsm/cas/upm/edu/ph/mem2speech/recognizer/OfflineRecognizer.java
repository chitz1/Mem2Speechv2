package gonzalvo.dpsm.cas.upm.edu.ph.mem2speech.recognizer;

import android.content.res.AssetManager;
import android.graphics.Bitmap;

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
        bitmap = Bitmap.createScaledBitmap(
                bitmap,
                config.getImageSize(),
                config.getImageSize(),
                true
        );
        int[] intValues = getPixelValuesFrom(bitmap);
        float[] floatValues = new float[bitmap.getWidth() * bitmap.getHeight()];
        for (int i = 0; i < intValues.length; ++i) {
            final int val = intValues[i];
            floatValues[i] = (((val >> 16) & 0xFF));
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
