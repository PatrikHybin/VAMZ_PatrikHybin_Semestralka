package com.patrikhybin.nakupnyzoznam;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Slúžy na prácu s databázov
 */
@Dao
public interface ItemDao {
    /**
     * metóda na vloženie jednej položky do tabuľky
     * @param item
     */
    @Insert
    void insert(Item item);
    /**
     * metóda na update jednej položky v tabuľke
     * @param item
     */
    @Update
    void update(Item item);
    /**
     * metóda na vymazanie jednej položky z tabuľky
     * @param item
     */
    @Delete
    void delete(Item item);
    /**
     * metóda na vymazanie všetkých položiek z tabuľky
     */
    @Query("DELETE FROM item_table")
    void deleteAllItems();

    /**
     * vráti všetky položky z tabuľky
     * @return
     */
    @Query("SELECT * FROM item_table ORDER BY shop")
    LiveData<List<Item>> getAllItems();

}
