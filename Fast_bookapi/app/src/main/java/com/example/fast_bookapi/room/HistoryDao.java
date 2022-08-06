package com.example.fast_bookapi.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HistoryDao {

    @Query("SELECT * FROM history")
    List<History> getAll();

    @Insert
    void insertHistory(History history);

    @Query("DELETE FROM history WHERE keyword == :keyword")
    void delete(String keyword);

}
