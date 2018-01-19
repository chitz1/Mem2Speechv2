package gonzalvo.dpsm.cas.upm.edu.ph.mem2speech;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class ConvertedTextActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converted_text);

        Intent intent = getIntent();
        final String recognizedText = intent.getStringExtra(ToText.EXTRA_RECOGNIZED_TEXT);

        textView = findViewById(R.id.converted_text);
        textView.setText(recognizedText);
    }

    public void toSpeech(View view){
        new ToSpeech(ConvertedTextActivity.this).execute(textView.getText().toString());
    }
}
