package com.patrikhybin.nakupnyzoznam;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;


import java.util.List;

/**
 * trieda, ktorá zabezpečuje komunikáciu medzi databázov a viewModelom
 */
public class ItemRepository {
    private ItemDao itemDao;
    private LiveData<List<Item>> allItems;

    /**
     * vytvorí, inicializuje repositár a napní pole položiek
     * @param application
     */
    public ItemRepository(Application application) {
        ItemDatabase database = ItemDatabase.getInstance(application);
        itemDao = database.itemDao();
        allItems = itemDao.getAllItems();
    }

    /**
     * metóda vloží položku do databázy
     * @param item
     */
    public void insert(Item item) {
        new InsertItemAsyncTask(itemDao).execute(item);
    }

    /**
     * metóda aktualizuje položku v databáze
     * @param item
     */
    public void update(Item item) {
        new UpdateItemAsyncTask(itemDao).execute(item);
    }

    /**
     * metóda vymaže položku z databázy
     * @param item
     */
    public void delete(Item item) {
        new DeleteItemAsyncTask(itemDao).execute(item);
    }

    /**
     * metóda vymaže všetky položky v databáze
     */
    public void deleteAllItems() {
        new DeleteAllItemsAsyncTask(itemDao).execute();
    }

    /**
     * metóda vráti všetky položky z databázy
     * @return
     */
    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }

    private static class DeleteAllItemsAsyncTask extends AsyncTask<Void, Void, Void> {
        private ItemDao itemDao;

        private DeleteAllItemsAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            itemDao.deleteAllItems();
            return null;
        }
    }


    private static class DeleteItemAsyncTask extends AsyncTask<Item, Void, Void> {
        private ItemDao itemDao;

        private DeleteItemAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(Item... items) {
            itemDao.delete(items[0]);
            return null;
        }
    }



    private static class UpdateItemAsyncTask extends AsyncTask<Item, Void, Void> {
        private ItemDao itemDao;

        private UpdateItemAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(Item... items) {
            itemDao.update(items[0]);
            return null;
        }
    }


    private static class InsertItemAsyncTask extends AsyncTask<Item, Void, Void> {
        private ItemDao itemDao;

        private InsertItemAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(Item... items) {
            itemDao.insert(items[0]);
            return null;
        }
    }


}

