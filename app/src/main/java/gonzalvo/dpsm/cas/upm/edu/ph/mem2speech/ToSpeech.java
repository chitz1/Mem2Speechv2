package gonzalvo.dpsm.cas.upm.edu.ph.mem2speech;

import android.content.Context;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class ToSpeech extends AsyncTask<String, Void, Void> implements TextToSpeech.OnInitListener{

    private final int MAX_CHARS_TTS_CAN_READ = 3900;
    private String text;
    private TextToSpeech tts;

    ToSpeech(Context context) {
        this.tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                tts.setLanguage(Locale.US);
            }
        });
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... params) {
        text = params[0];
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        speak(text);
    }

    private void speak(String text) {
        final String uId = this.hashCode() + "";
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
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String s) {

                }

                @Override
                public void onDone(String s) {

                }

                @Override
                public void onError(String s) {

                }
            });
        }
    }
}
