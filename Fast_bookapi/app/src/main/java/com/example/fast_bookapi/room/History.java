package com.example.fast_bookapi.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class History {

    public History(Integer uid, String keyword) {
        this.uid = uid;
        this.keyword = keyword;
    }

    @PrimaryKey
    private Integer uid;

    @ColumnInfo(name = "keyword")
    private String keyword;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
