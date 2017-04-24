package com.example.cg.scaleimageview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.cg.scaleimageview.Customs.ScaleImageView;

public class MainActivity extends AppCompatActivity {

    private int[] imgSrc = new int[]{R.mipmap.mm1,R.mipmap.beauty_,R.mipmap.scenery3};

    private ScaleImageView sImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sImageView = (ScaleImageView)findViewById(R.id.sImageView);

        try
        {
            Thread.sleep(1000);
            sImageView.setImages(imgSrc);
        }catch (Exception ex)
        {

        }
    }
}
