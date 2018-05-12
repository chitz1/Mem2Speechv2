package gonzalvo.dpsm.cas.upm.edu.ph.mem2speech.recognizer;

public class RecognizerConfig {
    private final String[] charset;
    private final String servingModelConfigFile;
    private final String imageConfigFile;

    public RecognizerConfig(String[] charset, String imageConfigFile, String servingModelConfigFile) {
        this.imageConfigFile = imageConfigFile;
        this.charset = charset;
        this.servingModelConfigFile = servingModelConfigFile;
    }

    String[] getCharset() {
        return charset;
    }

    String getImageConfigFile() {
        return imageConfigFile;
    }

    String getServingModelConfigFilename() {
        return servingModelConfigFile;
    }
}
