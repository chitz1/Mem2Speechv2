package gonzalvo.dpsm.cas.upm.edu.ph.mem2speech.recognizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class GraphComponents {
    private final OutputNodes outputNodes;
    private final InputNodes inputNodes;

    GraphComponents(String jsonString) {
        inputNodes = new InputNodes();
        outputNodes = new OutputNodes();
        try {
            JSONObject obj = new JSONObject(jsonString);
            JSONArray jsonInputNodes = obj.getJSONArray("input_nodes");
            for (int i = 0; i < jsonInputNodes.length(); i++){
                JSONObject jsonInputNode = jsonInputNodes.getJSONObject(i);
                JSONArray jsonInputShape = jsonInputNode.getJSONArray("input_shape");
                long[] inputShape = new long[jsonInputShape.length()];
                for (int j = 0; j < jsonInputShape.length(); j++)
                    inputShape[j] = jsonInputShape.getLong(j);
                inputNodes.addInputNode(jsonInputNode.getString("input_name"), inputShape);
            }
            JSONArray jsonOutputNames = obj.getJSONArray("output_names");
            for (int i = 0; i < jsonOutputNames.length(); i++)
                outputNodes.addOutputNode(jsonOutputNames.getString(i), 120);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    InputNodes getInputNodes() {
        return inputNodes;
    }

    OutputNodes getOutputNodes() {
        return outputNodes;
    }
}
