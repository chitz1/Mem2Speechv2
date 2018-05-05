package gonzalvo.dpsm.cas.upm.edu.ph.mem2speech.recognizer;

public class RecognizerConfig {
    private final String[] charset;
    private final String servingModelConfigFile;
    private final int imageWidth;
    private final int imageHeight;

    public RecognizerConfig(String[] charset, int imageWidth, int imageHeight, String servingModelConfigFile) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.charset = charset;
        this.servingModelConfigFile = servingModelConfigFile;
    }

    String[] getCharset() {
        return charset;
    }

    int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    String getServingModelConfigFilename() {
        return servingModelConfigFile;
    }
}
