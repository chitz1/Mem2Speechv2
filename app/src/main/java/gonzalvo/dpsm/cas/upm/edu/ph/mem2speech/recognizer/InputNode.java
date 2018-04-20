package gonzalvo.dpsm.cas.upm.edu.ph.mem2speech.recognizer;

class InputNode {
    private final String inputName;
    private final float[] inputValue;
    private final long[] inputShape;

    InputNode(String inputName, float[] inputValue, long[] inputShape) {
        this.inputName = inputName;
        this.inputValue = inputValue;
        this.inputShape = inputShape;
    }

    String getInputName() {
        return inputName;
    }

    float[] getInputValue() {
        return inputValue;
    }

    long[] getInputShape() {
        return inputShape;
    }
}
