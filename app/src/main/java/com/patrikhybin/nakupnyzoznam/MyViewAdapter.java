package com.patrikhybin.nakupnyzoznam;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

/**
 *  extenduje RecyclerView.Adapter a spája dáta s RecyclerView, na ktorom si overridem metódy podľa mojej potreby a ešte implementuje Filterable
 */
public class MyViewAdapter extends RecyclerView.Adapter<MyViewAdapter.MyViewHolder> implements Filterable {

    private List<Item> items = new ArrayList<>();
    private List<Item> itemsFull;
    private OnItemListener onItemListener;
    private ItemViewModel itemViewModel;

    /**
     * konštruktor pre MyViewAdapter, v ktorom priradím onItemListener(interface)
     * @param onItemListener
     */
    MyViewAdapter(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;


    }

    /**
     * metóda, ktorá nastaví itemViewModel
     * @param itemViewModel
     */
    public void setItemViewModel(ItemViewModel itemViewModel) {
        this.itemViewModel = itemViewModel;
    }

    /**
     * metóda, v ktorej sa vytvorí MyViewHolder a priradý sa mú onItemListener a view
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.cardview_item, parent ,false);
        return new MyViewHolder(onItemListener, view);
    }

    /**
     * metóda, ktorá podĺa pozície v poli pracuje s danou položkou
     * @param holder
     * @param position
     */
    @SuppressLint("SetTextI18n")
    @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
            holder.item_name.setText(items.get(position).getName());
            holder.shop_name.setText(items.get(position).getShop());
            holder.item_count.setText(items.get(position).getCount() + "");

            if (items.get(position).getBuying() != 0) {
                holder.item_buy_count.setText(items.get(position).getBuying() + "");
            } else {
                holder.item_buy_count.setText("");
            }

            checkBoxControl(holder,position);
            holder.check_box.setOnClickListener(new View.OnClickListener() {
                /**
                 * metóda, ktorá nastavuje pre položku či je check box zaškrtnutý alebo nie
                 * @param v
                 */
                @Override
                public void onClick(View v) {
                    if (!holder.item_buy_count.hasFocus()) {
                        holder.item_buy_count.requestFocus();
                        holder.item_buy_count.clearFocus();

                    }
                    if (items.get(position).getChecked()) {
                        items.get(position).setChecked(false);
                    } else {
                        items.get(position).setChecked(true);
                    }

                    itemViewModel.update(items.get(position));
                    notifyDataSetChanged();
                }
            });


    }

    /**
     * metóda vracajúca veľkosť pola položiek
     * @return
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    private void checkBoxControl(MyViewHolder holder, int position) {
        for (Item item : items) {
            if (items.get(position).getChecked()) {
                holder.check_box.setChecked(true);
            } else {
                holder.check_box.setChecked(false);
            }
            holder.card_View.setCardBackgroundColor(items.get(position).getColor());

        }
    }

    /**
     * metóda vracajúca položky v MyViewAdapter
     * @return
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * metóda vrajúca položku na základe jej pozície v MyViewAdapter
     * @param position
     * @return
     */
    public Item getItemAt(int position) {
        return items.get(position);
    }

    /**
     * metóda, ktorá nastavý položky
     * @param items
     */
    public void setItems(List<Item> items) {
        this.items = items;
        this.itemsFull = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    /**
     * metóda vracajúca vyfiltrované položky
     * @return
     */
    @Override
    public Filter getFilter() {
        return itemsFilter;
    }

    private Filter itemsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            notifyDataSetChanged();
            List<Item> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0 || constraint.equals("")) {

                filteredList.addAll(itemsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase();
                for (Item item : itemsFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            notifyDataSetChanged();
            return  results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            items.clear();
            try {
                items.addAll((List) results.values);
            } catch (Exception e) {}
            notifyDataSetChanged();
        }

    };

    /**
     * trieda, ktorá popisuje jednu položku v recyclerView
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView item_name;
        TextView item_count;
        TextView shop_name;
        TextView item_buy_count;
        EditText item_note;
        TextView date_note;
        CheckBox check_box;
        CardView card_View;
        OnItemListener onItemListener;

        /**
         * priradenie listeneru a view
         * @param onItemListener
         * @param itemView
         */
        MyViewHolder(final OnItemListener onItemListener, @NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.item_name_id);
            item_count =  itemView.findViewById(R.id.item_count_id);
            shop_name = itemView.findViewById(R.id.shop_name_id);
            item_buy_count = itemView.findViewById(R.id.item_count_buy_id);
            check_box = itemView.findViewById(R.id.item_buy_checkbox_id);
            card_View = itemView.findViewById(R.id.cardview_item);
            item_note = itemView.findViewById(R.id.edit_note_item_id);
            date_note = itemView.findViewById(R.id.edit_date_note_id);


            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
            item_buy_count.setOnClickListener(new View.OnClickListener() {
                @Override
                /**
                 * metóda, ktorá volá metódu z inteface na nastavenie možstva položiek pri kúpe
                 */
                public void onClick(View v) {
                    onItemListener.onItemQuantityClick(getAdapterPosition());
                }
            });

        }

        /**
         * metóda, ktorá volá metódu z inteface na vytvorenie aktivity pre danu položku
         * @param v
         */
        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }

    }

    /**
     *  interface, ktorý obsahuje metódu pre kliknutie na dané súčasti applikácie
     */
    public interface OnItemListener{
        void onItemClick(int position);
        void onItemQuantityClick(int position);
    }

}
