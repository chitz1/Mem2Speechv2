package gonzalvo.dpsm.cas.upm.edu.ph.mem2speech.recognizer;

import java.util.HashMap;
import java.util.Map;

class OutputNodes {
    private final Map<String, Integer> outputNodes;

    OutputNodes() {
        this.outputNodes = new HashMap<>();
    }

    void addOutputNode(String outputNodeName, int shape) {
        outputNodes.put(outputNodeName, shape);
    }

    String[] getOutputNodeNames(){
        return outputNodes.keySet().toArray(new String[outputNodes.size()]);
    }

    int getShape(String outputNodeName) {
        return outputNodes.get(outputNodeName);
    }
}
