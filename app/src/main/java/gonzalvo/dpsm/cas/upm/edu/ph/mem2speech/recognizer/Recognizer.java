package gonzalvo.dpsm.cas.upm.edu.ph.mem2speech.recognizer;


import android.graphics.Bitmap;

public interface Recognizer {
    String recognizeHandwritingFrom(Bitmap bitmap);
}
