package gonzalvo.dpsm.cas.upm.edu.ph.mem2speech;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import gonzalvo.dpsm.cas.upm.edu.ph.mem2speech.canvas.DrawingView;

public class MainActivity extends AppCompatActivity {

    private DrawingView drawingView;
    private boolean isPen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawingView = findViewById(R.id.drawing);
    }

    public void switchTool(View view){
        ImageView penButton = view.findViewById(R.id.write);
        drawingView.switchTool();
        if(isPen){
            penButton.setRotation(180);
            isPen = false;
        } else {
            penButton.setRotation(360);
            isPen = true;
        }
    }

    public void clear(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage("Erase canvas?");
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                switch(which){
                    case DialogInterface.BUTTON_POSITIVE:
                        drawingView.clearCanvas();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        builder.setPositiveButton("Erase", dialogClickListener);
        builder.setNegativeButton("Cancel", dialogClickListener);
        builder.show();
    }

    public void recognize(View view){
        Bitmap bitmap = drawingView.getDrawingCache();
        new ToText(new WeakReferenceContextWrapper(new WeakReference<Context>(MainActivity.this))).execute(bitmap);
    }
}
