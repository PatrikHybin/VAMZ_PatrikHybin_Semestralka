package com.patrikhybin.nakupnyzoznam;


import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;


import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "item_table")

/**
 * Trieda predstavujúca jednu položku na zakúpenie, a taktiež záznam v tabuĹke Item v databáze. Implementuje Parcelable pre možnost zaslania metódov putExtra do novej aktivity.
 */
public class Item implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String shop;
    private int count;
    private String description;
    private String date;
    private boolean checked;
    private boolean highlight;
    private int color;
    private int buying;

    /**
     * Konštruktor triedy Item, ktorej pri vytvorený prídu parametre názov predmetu na zakúpenie a obchod, v ktorom sa ma daný predmet kúpit.
     * @param name
     * @param shop
     */
    public Item(String name, String shop) {
        this.name = name;
        this.shop = shop;
        this.count = 0;
        this.color = Color.LTGRAY;
    }

    /**
     * metóda implementovaná z Parcelable
     * @param in
     */
    protected Item(Parcel in) {
        id = in.readInt();
        name = in.readString();
        shop = in.readString();
        count = in.readInt();
        checked = in.readByte() != 0;
        highlight = in.readByte() != 0;
        color = in.readInt();
        description = in.readString();
        buying = in.readInt();
        date = in.readString();
    }

    /**
     * metóda implementovańa z Parcelable
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(shop);
        dest.writeInt(count);
        dest.writeByte((byte) (checked ? 1 : 0));
        dest.writeByte((byte) (highlight ? 1 : 0));
        dest.writeInt(color);
        dest.writeString(description);
        dest.writeInt(buying);
        dest.writeString(date);

    }

    /**
     * metóda implementovańa z Parcelable
     */
    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * vráti nastavený dátum
     * @return
     */
    public String getDate() {
        return date;
    }

    /**
     * nastaví sa dátum
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }
    /**
     * vráti popis predmetu
     * @return
     */
    public String getDescription() {
        return description;
    }
    /**
     * nastaví sa popis
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * nastaví sa id predmetu
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * vráti id predmetu
     * @return
     */
    public int getId() {
        return id;
    }
    /**
     * nastaví sa či je predmet zvýraznený
     * @param bool
     */
    public void highlight(boolean bool) {
        highlight = bool;
    }

    /**
     * vráti či je predmet zvýraznený
     * @return
     */
    public boolean getHighlight() {
        return highlight;
    }
    /**
     * nastaví sa farba
     * @param color
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * vráti farbu predmetu
     * @return
     */
    public int getColor() {
        return color;
    }

    /**
     * vráti či je checkbox zaškrtnutý
     * @return
     */
    boolean getChecked() {
        return checked;
    }
    /**
     * nastaví sa či je checkbox zaškrnutý alebo nie
     * @param bool
     */
    void setChecked(boolean bool) {
        this.checked = bool;
    }

    /**
     * vráti meno predmetu
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * vráti počet, ktorý treba kúpiť
     * @return
     */
    public int getCount() {
        return count;
    }
    /**
     * vráti názov obchodu
     * @return
     */
    public String getShop() {
        return shop;
    }
    /**
     * nastaví meno obchodu
     * @param shop
     */
    public void setShop(String shop) {
        this.shop = shop;
    }
    /**
     * nastaví sa meno obchodu
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * nastaví sa počet na zakúpenie
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * vráti koľko kupujeme
     * @return
     */
    int getBuying() {
        return buying;
    }
    /**
     * nastaví sa kupujúci počet
     * @param buying
     */
    void setBuying(int buying) {
        this.buying = buying;
    }


}
