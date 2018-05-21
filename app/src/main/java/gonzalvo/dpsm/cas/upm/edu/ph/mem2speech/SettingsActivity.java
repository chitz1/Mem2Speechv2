package gonzalvo.dpsm.cas.upm.edu.ph.mem2speech;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    public static final String KEY_PREF_MODEL_LIST = "model_preference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(
                android.R.id.content,
                new SettingsFragment())
                .commit();
    }
}
