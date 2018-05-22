package gonzalvo.dpsm.cas.upm.edu.ph.mem2speech;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ConvertedTextActivity extends AppCompatActivity {

    private final int MAX_CHARS_TTS_CAN_READ = 3900;

    private TextView textView;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converted_text);

        Intent intent = getIntent();
        final String recognizedText = intent.getStringExtra(ToText.EXTRA_RECOGNIZED_TEXT);

        textView = findViewById(R.id.converted_text);
        textView.setText(recognizedText);

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR){
                    tts.setLanguage(Locale.US);
                }
            }
        });
    }

    public void toSpeech(View view){
        final String uId = this.hashCode() + "";
        String text = textView.getText().toString();
        if(text.length() >= MAX_CHARS_TTS_CAN_READ) {
            final List<String> splittedText = splitText(text);
            for(final String queueElement : splittedText) {
                tts.speak(queueElement, TextToSpeech.QUEUE_ADD, null, uId);
            }
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, uId);
        }
    }

    private List<String> splitText(String text) {
        final int textLength = text.length();
        final List<String> splittedText = new ArrayList<>();

        int index = 0;
        while (index < textLength) {
            splittedText.add(text.substring(index, Math.min(index + MAX_CHARS_TTS_CAN_READ, textLength)));
            index += MAX_CHARS_TTS_CAN_READ;
        }
        return splittedText;
    }

    @Override
    protected void onPause() {
        if(tts != null){
            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }
}
