package gonzalvo.dpsm.cas.upm.edu.ph.mem2speech.recognizer;

import android.content.res.AssetManager;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.util.ArrayList;
import java.util.List;

class InferenceInterface {

    private TensorFlowInferenceInterface inferenceInterface;

    InferenceInterface(AssetManager assetManager, String modelFilename){
        this.inferenceInterface = new TensorFlowInferenceInterface(assetManager, modelFilename);
    }

    List<int[]> runInference(InputNode[] inputNodes, OutputNodes outputNodes){
        List<int[]> outputs = new ArrayList<>();
        for (InputNode inputNode : inputNodes){
            inferenceInterface.feed(inputNode.getInputName(), inputNode.getInputValue(), inputNode.getInputShape());
        }
        inferenceInterface.run(outputNodes.getOutputNodeNames());
        for (String outputNodeName : outputNodes.getOutputNodeNames()){
            int[] output = new int[outputNodes.getShape(outputNodeName)];
            inferenceInterface.fetch(outputNodeName, output);
            outputs.add(output);
        }
        return outputs;
    }
}
