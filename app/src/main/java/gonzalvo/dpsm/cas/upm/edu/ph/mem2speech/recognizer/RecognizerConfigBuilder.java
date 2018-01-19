package gonzalvo.dpsm.cas.upm.edu.ph.mem2speech.recognizer;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RecognizerConfigBuilder {
    private AssetManager assetManager;
    private String modelFilename;
    private String[] charset;
    private int imageWidth;
    private int imageHeight;

    public RecognizerConfigBuilder setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
        return this;
    }

    public RecognizerConfigBuilder setModelFilename(String modelFilename) {
        this.modelFilename = modelFilename;
        return this;
    }

    public RecognizerConfigBuilder setCharsetFromFile(String charsetFile) {
        this.charset = readCharsetFromFile(charsetFile);
        return this;
    }

    private String[] readCharsetFromFile(String charsetFile) {
        List<String> charset = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(assetManager.open(charsetFile)));
            String line;

            while((line = br.readLine()) != null){
                charset.add(line);
            }
            charset.add("");
            br.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        String[] charsetArr = new String[charset.size()];
        charsetArr = charset.toArray(charsetArr);
        return charsetArr;
    }

    public RecognizerConfigBuilder setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
        return this;
    }

    public RecognizerConfigBuilder setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
        return this;
    }

    public RecognizerConfig build() {
        return new RecognizerConfig(assetManager, modelFilename, charset, imageWidth, imageHeight);
    }
}
