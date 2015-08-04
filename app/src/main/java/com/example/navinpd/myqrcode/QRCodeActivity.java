package com.example.navinpd.myqrcode;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.CaptureActivity;


public class QRCodeActivity extends CaptureActivity {
    ImageView qrGeneratedImage;
    Button qrGeneratorButton;
    EditText qrText;
    Button qrScanButton;
    TextView qrScannedText;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        qrGeneratorButton = (Button) findViewById(R.id.qr_scan_button);
        qrGeneratedImage = (ImageView) findViewById(R.id.qr_image);
        qrText = (EditText) findViewById(R.id.qr_text);
        qrScanButton =(Button) findViewById(R.id.qr_read_button);
        qrScannedText = (TextView) findViewById(R.id.scanned_text);

        qrGeneratorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String secret = qrText.getText().toString();
                if (secret.length() == 0) {
                    Toast.makeText(QRCodeActivity.this, "Empty text ", Toast.LENGTH_LONG).show();
                    return;
                }

                QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(secret,
                        null,
                        Contents.Type.TEXT,
                        BarcodeFormat.QR_CODE.toString(),
                        250);
                try {
                    Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
                    qrGeneratedImage.setImageBitmap(bitmap);

                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

        qrScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //start the scanning activity from the com.google.zxing.client.android.SCAN intent
                    Intent intent = new Intent(ACTION_SCAN);
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    startActivityForResult(intent, 10);
                } catch (ActivityNotFoundException anfe) {
                    Toast.makeText(QRCodeActivity.this, anfe.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10 && resultCode == RESULT_OK) {
            String contents = data.getStringExtra("SCAN_RESULT");
            qrScannedText.setText(contents);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_qrcode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
