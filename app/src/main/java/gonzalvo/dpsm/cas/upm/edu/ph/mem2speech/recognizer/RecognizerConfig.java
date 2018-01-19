package gonzalvo.dpsm.cas.upm.edu.ph.mem2speech.recognizer;

import android.content.res.AssetManager;

class RecognizerConfig {
    private final AssetManager assetManager;
    private final String modelFilename;
    private final String[] charset;
    private final int imageWidth;
    private final int imageHeight;

    RecognizerConfig(AssetManager assetManager, String modelFilename, String[] charsetFile, int imageWidth, int imageHeight) {
        this.assetManager = assetManager;
        this.modelFilename = modelFilename;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.charset = charsetFile;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public String getModelFilename() {
        return modelFilename;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public String[] getCharset() {
        return charset;
    }
}
