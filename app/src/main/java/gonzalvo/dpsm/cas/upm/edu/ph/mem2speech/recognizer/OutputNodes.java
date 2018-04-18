package gonzalvo.dpsm.cas.upm.edu.ph.mem2speech.recognizer;

import java.util.HashMap;
import java.util.Map;

public class OutputNodes {
    private final Map<String, Integer> outputNodes;

    public OutputNodes() {
        this.outputNodes = new HashMap<>();
    }

    public void addOutputNode(String outputNodeName, int shape) {
        outputNodes.put(outputNodeName, shape);
    }

    public String[] getOutputNodeNames(){
        return outputNodes.keySet().toArray(new String[outputNodes.size()]);
    }

    public int getShape(String outputNodeName) {
        return outputNodes.get(outputNodeName);
    }
}
