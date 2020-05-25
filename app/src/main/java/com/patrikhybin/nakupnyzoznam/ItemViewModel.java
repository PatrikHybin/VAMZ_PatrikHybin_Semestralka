package com.patrikhybin.nakupnyzoznam;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * Pozoruje zmeny v iteme, uvedomuje si štádiumn life-cyclu a vďaka tomu sa ľahko implementuje landscape mode
 */
public class ItemViewModel extends AndroidViewModel {

    private ItemRepository repository;
    private LiveData<List<Item>> allItems;

    /**
     *  Konśtruktor ItemViewModelu, ktorý vytvorý nový repository a naplní ho
     * @param application
     */
    public ItemViewModel(@NonNull Application application) {
        super(application);
        repository = new ItemRepository(application);
        allItems = repository.getAllItems();
    }

    /**
     * metóda pre insert položky do databázy
     * @param item
     */
    public void insert(Item item) {
        repository.insert(item);
    }

    /**
     * metóda na update položky v databáze
     * @param item
     */
    public void update(Item item)  {
        repository.update(item);
    }

    /**
     * metóda na vymazanie položky z databázy
     * @param item
     */
    public void delete(Item item)  {
        repository.delete(item);
    }

    /**
     * metóda na vymazanie všetkých predmetov z databázy
     */
    public void deleteAllItems()  {
        repository.deleteAllItems();
    }

    /**
     * metóda na vrátenie všetkých itemov v databáze
     * @return
     */
    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }
}
