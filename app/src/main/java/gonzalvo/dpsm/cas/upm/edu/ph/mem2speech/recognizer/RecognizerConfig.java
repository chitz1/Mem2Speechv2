package gonzalvo.dpsm.cas.upm.edu.ph.mem2speech.recognizer;

public class RecognizerConfig {
    private final String[] charset;
    private final String servingModelConfigFile;
    private final int imageSize;

    public RecognizerConfig(String[] charset, int imageSize, String servingModelConfigFile) {
        this.imageSize = imageSize;
        this.charset = charset;
        this.servingModelConfigFile = servingModelConfigFile;
    }

    String[] getCharset() {
        return charset;
    }

    int getImageSize() {
        return imageSize;
    }

    String getServingModelConfigFilename() {
        return servingModelConfigFile;
    }
}
