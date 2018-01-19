package gonzalvo.dpsm.cas.upm.edu.ph.mem2speech;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import gonzalvo.dpsm.cas.upm.edu.ph.mem2speech.recognizer.OfflineRecognizer;
import gonzalvo.dpsm.cas.upm.edu.ph.mem2speech.recognizer.Recognizer;
import gonzalvo.dpsm.cas.upm.edu.ph.mem2speech.recognizer.RecognizerConfigBuilder;

public class ToText extends AsyncTask<Object, Void, Void> {

    public static final String EXTRA_RECOGNIZED_TEXT = "ph.edu.upm.cas.dpsm.gonzalvo.mem2speech.recognizedtext";

    private Context context;
    private ProgressDialog progressDialog;
    private String convertedText;

    public ToText(Context context) {
        this.context = context;
        this.convertedText = "";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        createDialog();
    }

    private void createDialog() {
        this.progressDialog = new ProgressDialog(this.context);
        this.progressDialog.setCancelable(false);
        this.progressDialog.setCanceledOnTouchOutside(false);
        this.progressDialog.setMessage("Converting to text...");
        this.progressDialog.show();
    }

    @Override
    protected Void doInBackground(Object... params) {
        Bitmap bitmap = (Bitmap) params[0];
        RecognizerConfigBuilder recognizerConfigBuilder = new RecognizerConfigBuilder();
        Recognizer recognizer = new OfflineRecognizer(recognizerConfigBuilder.setAssetManager(context.getAssets())
                .setImageHeight(128)
                .setImageWidth(128)
                .setModelFilename("frozen_bi_lstm_ctc_ocr.pb")
                .setCharsetFromFile("chars.txt")
                .build());
        convertedText = recognizer.recognizeHandwritingFrom(bitmap);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        this.progressDialog.cancel();
        Intent intent = new Intent(context, ConvertedTextActivity.class);
        intent.putExtra(EXTRA_RECOGNIZED_TEXT, convertedText);
        context.startActivity(intent);
    }
}