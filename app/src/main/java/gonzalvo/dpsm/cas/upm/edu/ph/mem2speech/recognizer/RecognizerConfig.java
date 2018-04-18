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

    public String[] getCharset() {
        return charset;
    }

    public int getImageSize() {
        return imageSize;
    }

    public String getServingModelConfigFilename() {
        return servingModelConfigFile;
    }
}
