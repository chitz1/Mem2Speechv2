package gonzalvo.dpsm.cas.upm.edu.ph.mem2speech.recognizer;

class InputNode {
    private final String inputName;
    private final float[] inputValue;
    private final long[] inputShape;

    public InputNode(String inputName, float[] inputValue, long[] inputShape) {
        this.inputName = inputName;
        this.inputValue = inputValue;
        this.inputShape = inputShape;
    }

    public String getInputName() {
        return inputName;
    }

    public float[] getInputValue() {
        return inputValue;
    }

    public long[] getInputShape() {
        return inputShape;
    }
}
