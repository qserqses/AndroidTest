package com.example.skra.filebrowser2;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ShowSVG extends AppCompatActivity {

    ImageView image;
    Intent intent;
    Intent intent2;
    Button convert;
    Button Return;
    InputStream in;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_svg);
        image = (ImageView) findViewById(R.id.Image);
        convert = (Button) findViewById(R.id.ConvertButton);
        Return = (Button) findViewById(R.id.ReturnButton);
        image.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        //Lapiemy intent i generujemy obrazek
        intent = getIntent();
        String path = intent.getStringExtra(MainActivity.pass);
        //File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/android.svg");
        file = new File(path);
        if (file.exists()) {
            try {
                in = new BufferedInputStream(new FileInputStream(file));
                final SVG svg = SVGParser.getSVGFromInputStream(in);
                image.setImageDrawable(svg.createPictureDrawable());
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        } else
            Toast.makeText(getApplicationContext(), "Jason, we have to cook!", Toast.LENGTH_SHORT).show();
    }

    public void ConvertSVG(View v) {

        intent2 = new Intent(getApplicationContext(), MainActivity.class);
        setResult(RESULT_OK, intent2);
        finish();
    }

    public void Previous(View v) {
        intent2 = new Intent(getApplicationContext(), MainActivity.class);
        setResult(RESULT_CANCELED, intent2);
        finish();
    }
}
