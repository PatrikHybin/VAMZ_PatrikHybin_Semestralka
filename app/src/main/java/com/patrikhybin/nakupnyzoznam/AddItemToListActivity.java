package com.patrikhybin.nakupnyzoznam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

/**
 * aktivita, ktorá dovoľuje vytvorenie novej položky
 */
public class AddItemToListActivity extends AppCompatActivity {

    private EditText itemNameText;
    private EditText itemCountText;
    private EditText itemShopText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_to_list);

        itemNameText = findViewById(R.id.add_item_name_id);
        itemCountText = findViewById(R.id.add_item_count_id);
        itemShopText = findViewById(R.id.add_shop_name_id);
        Button addButton = findViewById(R.id.add_item_add_button_id);
        addButton.setOnClickListener(new View.OnClickListener() {

            /**
             * metóda, ktorá skonroluje typ zadanej kvantity a či sme zadali meno, ak nie tak sa spustí snack bar, ktorý nás informuje o tom aby sme zadali názov položky
             * @param v
             */
            @Override
            public void onClick(View v) {
                String itemName = itemNameText.getText().toString();
                String shopName = itemShopText.getText().toString();
                String itemCount = itemCountText.getText().toString();
                int itemCountInt = 0;

                if (!itemCount.equals("")) {
                    itemCountInt = Integer.parseInt(itemCount);
                }

                if (itemName.equals("")) {
                    Snackbar.make(v, "Insert item name please", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Intent resultIntent = new Intent();

                    resultIntent.putExtra("Name", itemName);
                    resultIntent.putExtra("Count", itemCountInt);
                    resultIntent.putExtra("Shop", shopName);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }

            }
        });

    }
}
