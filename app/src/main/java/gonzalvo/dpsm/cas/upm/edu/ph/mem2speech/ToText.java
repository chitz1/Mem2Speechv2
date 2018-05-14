package gonzalvo.dpsm.cas.upm.edu.ph.mem2speech;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import gonzalvo.dpsm.cas.upm.edu.ph.mem2speech.recognizer.OfflineRecognizer;
import gonzalvo.dpsm.cas.upm.edu.ph.mem2speech.recognizer.Recognizer;
import gonzalvo.dpsm.cas.upm.edu.ph.mem2speech.recognizer.RecognizerConfig;

import static gonzalvo.dpsm.cas.upm.edu.ph.mem2speech.SettingsActivity.KEY_PREF_MODEL_LIST;

class ToText extends AsyncTask<Object, Void, Void> {

    static final String EXTRA_RECOGNIZED_TEXT = "ph.edu.upm.cas.dpsm.gonzalvo.mem2speech.recognizedtext";

    private final WeakReference<Context> contextReference;
    private ProgressDialog progressDialog;
    private String convertedText;

    ToText(WeakReferenceContextWrapper weakReferenceContextWrapper) {
        this.contextReference = weakReferenceContextWrapper.getContextReference();
        this.convertedText = "";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        createDialog();
    }

    private void createDialog() {
        this.progressDialog = new ProgressDialog(getContext());
        this.progressDialog.setCancelable(false);
        this.progressDialog.setCanceledOnTouchOutside(false);
        this.progressDialog.setMessage("Converting to text...");
        this.progressDialog.show();
    }

    @Override
    protected Void doInBackground(Object... params) {
        Bitmap bitmap = (Bitmap) params[0];
        String[] charset = readCharsetFromFile();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String modelPref = sharedPref.getString(KEY_PREF_MODEL_LIST, "");
        String modelPath = "models/" + modelPref;
        RecognizerConfig config = new RecognizerConfig(
                charset,
                modelPath + "/image_config.json",
                modelPath + "/serving_model_config.json"
        );
        Recognizer recognizer = new OfflineRecognizer(
                config,
                getAssetManager(),
                modelPath + "/graph.pb"
        );
        convertedText = recognizer.recognizeHandwritingFrom(bitmap);
        return null;
    }

    private AssetManager getAssetManager() {
        return getContext().getAssets();
    }

    private String[] readCharsetFromFile() {
        List<String> charset = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getAssetManager().open("charset.txt")));
            String line;

            while((line = br.readLine()) != null){
                charset.add(line);
            }
            charset.add("");
            br.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        String[] charsetArr = new String[charset.size()];
        charsetArr = charset.toArray(charsetArr);
        return charsetArr;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        this.progressDialog.cancel();
        Intent intent = new Intent(getContext(), ConvertedTextActivity.class);
        intent.putExtra(EXTRA_RECOGNIZED_TEXT, convertedText);
        getContext().startActivity(intent);
    }

    private Context getContext() {
        return contextReference.get();
    }
}