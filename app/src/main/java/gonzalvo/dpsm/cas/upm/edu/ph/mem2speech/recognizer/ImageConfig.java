package gonzalvo.dpsm.cas.upm.edu.ph.mem2speech.recognizer;

import org.json.JSONException;
import org.json.JSONObject;

public class ImageConfig {
    private int imageWidth;
    private int imageHeight;

    ImageConfig(String imageConfigJsonString) {
        try {
            JSONObject obj = new JSONObject(imageConfigJsonString);
            this.imageHeight = obj.getInt("image_height");
            this.imageWidth = obj.getInt("image_width");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }
}
