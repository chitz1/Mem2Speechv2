package gonzalvo.dpsm.cas.upm.edu.ph.mem2speech.recognizer;


import java.util.HashMap;
import java.util.Map;

class InputNodes {
    private final Map<String, long[]> inputNameShapeMap;

    InputNodes() {
        inputNameShapeMap = new HashMap<>();
    }

    public long[] getInputShape(String inputName){
        return inputNameShapeMap.get(inputName);
    }

    void addInputNode(String inputName, long[] inputShape){
        inputNameShapeMap.put(inputName, inputShape);
    }
}
