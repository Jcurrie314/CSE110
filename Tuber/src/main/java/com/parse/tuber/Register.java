package com.parse.tuber;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.NumberFormat;
import java.util.Locale;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class Register extends AppCompatActivity implements View.OnClickListener {
    EditText etFirstName, etEmail, etUsername, etPassword, etPhone, etPrice;
    Spinner sMajor;
    Button bRegister, bUpload;
    TextView loginLink;
    CheckBox cbStudent, cbTutor;
    byte[] image;
    private static final int SELECT_PHOTO = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etPrice = (EditText) findViewById(R.id.etPrice);

        sMajor = (Spinner) findViewById(R.id.sMajor);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bRegister = (Button) findViewById(R.id.bRegister);
        bUpload = (Button) findViewById(R.id.bUpload);

        loginLink = (TextView) findViewById(R.id.tvLoginLink);

        cbStudent = (CheckBox) findViewById(R.id.cbStudent);
        cbTutor = (CheckBox) findViewById(R.id.cbTutor);

        bRegister.setOnClickListener(this);
        bUpload.setOnClickListener(this);
        loginLink.setOnClickListener(this);
        etPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bRegister:
                String email = etEmail.getText().toString().trim();
                if (email.equals("")) {
                    etEmail.setError("Email is required!");
                } else if (!email.substring(Math.max(0, email.length() - 8)).equals("ucsd.edu")) {
                    etEmail.setError("UCSD email is required");
                } else if (etUsername.getText().toString().trim().equals("")) {
                    etUsername.setError("Username is required!");
                } else if (etPassword.getText().toString().trim().equals("")) {
                    etPassword.setError("Password is required!");
                } else {
                    registerUser();
                }
                break;
            case R.id.tvLoginLink:
                Intent loginIntent = new Intent(Register.this, Login.class);
                startActivity(loginIntent);
                break;
            case R.id.bUpload:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);



        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();

                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(
                                selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    Bitmap bmp = BitmapFactory.decodeStream(imageStream);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    getResizedBitmap(bmp, 500).compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    image = stream.toByteArray();
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    stream = null;

                }
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void registerUser() {


        ParseUser user = new ParseUser();

        user.setUsername(etUsername.getText().toString().trim().toLowerCase());
        user.setPassword(etPassword.getText().toString().trim());
        user.setEmail(etEmail.getText().toString().trim());
        user.put("phone", etPhone.getText().toString().trim());

        user.put("fee", Integer.parseInt(etPrice.getText().toString()));
        user.put("major", sMajor.getSelectedItem().toString());
        user.put("name", etFirstName.getText().toString().trim());
        user.put("student", cbStudent.isChecked());
        user.put("tutor", cbTutor.isChecked());
        user.put("rating", 0);

        ParseFile file = new ParseFile("profilePic.png", image);
        // Upload the image into Parse Cloud
        try {
            file.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        user.put("profilePic", file);


        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(com.parse.ParseException e) {

                if (e == null) {
                    Intent registerIntent = new Intent(Register.this, VerifyEmail.class);
                    startActivity(registerIntent);
                } else {
                    Toast.makeText(getApplicationContext(), e.toString(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
