package gonzalvo.dpsm.cas.upm.edu.ph.mem2speech.recognizer;

import android.graphics.Bitmap;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

public class OfflineRecognizer implements Recognizer {

    static {
        System.loadLibrary("tensorflow_inference");
    }

    private RecognizerConfig config;

    private TensorFlowInferenceInterface inferenceInterface;

    public OfflineRecognizer(RecognizerConfig config) {
        this.config = config;
        inferenceInterface = new TensorFlowInferenceInterface(this.config.getAssetManager(), this.config.getModelFilename());
    }

    @Override
    public String recognizeHandwritingFrom(Bitmap bitmap) {
        bitmap = Bitmap.createScaledBitmap(bitmap, config.getImageWidth(), config.getImageHeight(), true);
        int[] intValues = getPixelValuesFrom(bitmap);
        float[] floatValues = new float[bitmap.getWidth() * bitmap.getHeight()];
        for (int i = 0; i < intValues.length; ++i) {
            final int val = intValues[i];
            floatValues[i] = (((val >> 16) & 0xFF));
        }
        int[] result = new int[80];

        long[] INPUT_SIZE = new long[]{1, bitmap.getHeight(), bitmap.getWidth()};
        String[] inputs = new String[]{"input", "seq_len_input"};
        inferenceInterface.feed(inputs[0], floatValues, INPUT_SIZE);
        inferenceInterface.feed(inputs[1], new int[]{bitmap.getWidth()}, 1);

        String[] outputs = new String[]{"output"};
        inferenceInterface.run(outputs);
        inferenceInterface.fetch(outputs[0], result);

        return decode(result);
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
