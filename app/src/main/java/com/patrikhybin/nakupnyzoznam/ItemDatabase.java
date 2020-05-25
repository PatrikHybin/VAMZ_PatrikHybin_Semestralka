package com.patrikhybin.nakupnyzoznam;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * Abstraktná trieda, ktorá kontroluje integritu už pri kompilácii programu a uchováva dáta počas zmeny konfigurácie
 */
@Database(entities = Item.class, version = 6)
public abstract class ItemDatabase extends RoomDatabase {

    private static  ItemDatabase instance;

    public abstract ItemDao itemDao();

    /**
     * metóda vracajúca databazú, ak databáza neexistuje vytvorí novú
     * @param context
     * @return
     */
    public static synchronized ItemDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), ItemDatabase.class, "item_database.db").fallbackToDestructiveMigration().addCallback(roomCallBack).build();
            //instance = Room.databaseBuilder(context.getApplicationContext(), ItemDatabase.class, "item_database").fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    /**
     * metóda, ktorá pri vytvorení databázy zavolá triedu PopulateDBAAsyncTask
     */
    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    /**
     * trieda, ktorá nám naplní tabuľku pri vytvorení databázy
     */
    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private ItemDao itemDao;

        private PopulateDBAsyncTask(ItemDatabase db) {
            itemDao = db.itemDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            itemDao.insert(new Item("Rožok", "Billa"));
            itemDao.insert(new Item("Jablko", "Lidl"));
            itemDao.insert(new Item("Cukor", ""));
            return null;
        }
    }
}
