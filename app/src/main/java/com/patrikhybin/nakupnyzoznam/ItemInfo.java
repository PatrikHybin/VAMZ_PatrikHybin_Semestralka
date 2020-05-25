package com.patrikhybin.nakupnyzoznam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Aktivita, ktorá sa stará o podrobnejšie informácie o položke
 */
public class ItemInfo extends AppCompatActivity implements View.OnClickListener {

    private TextView dateInfo;
    private EditText itemInfo;
    private int year;
    private int month;
    private int day;
    private ImageView imageView;
    public static final int REQUEST_IMAGE = 99;
    private Item item;
    private int position;


    
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        TextView itemNameNote = findViewById(R.id.item_name_note_id);
        TextView shopNameNote = findViewById(R.id.shop_name_note_id);
        itemInfo = findViewById(R.id.edit_note_item_id);
        dateInfo = findViewById(R.id.edit_date_note_id);
        Button buttonTakePicture = findViewById(R.id.takepicturebutton_id);
        imageView = findViewById(R.id.image_note_id);

        Intent intent = getIntent();
        item = intent.getParcelableExtra("Item info");
        position = intent.getIntExtra("Position",0);
        itemNameNote.setText(String.format(" %s", item.getName()));
        shopNameNote.setText(String.format(" %s", item.getShop()));

        if (item.getDescription() == null) {
            itemInfo.setText(" ");
        } else {
            itemInfo.setText(String.format(" %s", item.getDescription()));
        }

        System.out.println(item.getDate());
        dateInfo.setText(item.getDate());


        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        loadImageFromStorage(directory.getPath());

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        dateInfo.setOnClickListener(this);

        buttonTakePicture.setOnClickListener(this);
        ImageButton imageButton = findViewById(R.id.button_item_info_go_back);
        imageButton.setOnClickListener(this);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(imageBitmap);
            saveToInternalStorage(imageBitmap);

        }

    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory,item.getId() + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private void loadImageFromStorage(String path)
    {
        try {
            File f = new File(path, item.getId() + ".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imageView = findViewById(R.id.image_note_id);
            imageView.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * metóda, v ktorej sa na základe id stlačenej časti aktivity udeje úloha
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_item_info_go_back :
                Intent resultIntent = new Intent();
                resultIntent.putExtra("Description", itemInfo.getText().toString());

                resultIntent.putExtra("Date", dateInfo.getText().toString());
                resultIntent.putExtra("Position", position);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                break;
            case R.id.takepicturebutton_id :
                Intent takeImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takeImageIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takeImageIntent, REQUEST_IMAGE);
                }
                break;
            case R.id.edit_date_note_id:
                DatePickerDialog datePickerDialog = new DatePickerDialog(ItemInfo.this, new DatePickerDialog.OnDateSetListener() {


                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        dateInfo.setText(date);

                    }
                },year , month, day);
                datePickerDialog.show();
            default:
                break;

        }
    }
}
