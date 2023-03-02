package com.omkokate.proj;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CROP_IMAGE_REQUEST = 2;

    private Uri mImageUri;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_main);

        mImageView = findViewById(R.id.image_view);

        Button btnSelectImage = findViewById(R.id.btn_select_image);
        btnSelectImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_select_image) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            startCropActivity(mImageUri);
        } else if (requestCode == CROP_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri resultUri = UCrop.getOutput(data);
            Bitmap bitmap = BitmapFactory.decodeFile(resultUri.getPath());
            mImageView.setImageBitmap(bitmap);
        }
    }

    private void startCropActivity(Uri uri) {
        UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), "cropped")))
                .withAspectRatio(1, 1)
                .start(this, CROP_IMAGE_REQUEST);
    }
}
