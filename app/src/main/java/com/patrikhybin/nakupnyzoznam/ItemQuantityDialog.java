package com.patrikhybin.nakupnyzoznam;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

/**
 * dialog, ktorý sa pýta na počet jedenej položky, ktorú plánujeme zakúpiť
 */
public class ItemQuantityDialog extends AppCompatDialogFragment {

    private EditText itemQuantity;
    private TextView infoQuantity;
    private ItemQuantityDialogListener itemQuantityDialogListener;

    /**
     * vytvorí dialog a sledujuje, ktoré tlačidlo sme stačili ak ok tak nastavý náŠ input ako plánovaný počet nákupu danej položky
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_layout, null);

        itemQuantity = view.findViewById(R.id.dialog_edit_quantity_id);
        infoQuantity = view.findViewById(R.id.dialog_textview_id);

        builder.setView(view).setTitle("Quantity").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String quantity = itemQuantity.getText().toString();
                itemQuantityDialogListener.applyQuantity(quantity);
            }
        });


        return builder.create();
    }

    /**
     * nastavý itemQuantityDialogListener pre tento context
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            itemQuantityDialogListener = (ItemQuantityDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement dialog listener");
        }
    }

    /**
     * interface, ktorý aplikuje množstvo, ktoré sme zvoli už po to ako klikneme na ok tlačídlo
     */
    public interface ItemQuantityDialogListener{
        void applyQuantity(String quantity);
    }
}
