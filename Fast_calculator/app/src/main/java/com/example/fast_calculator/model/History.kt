package com.example.fast_calculator.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//Room : History 클래스 자체를 데이터베이스의 테이블로 사용하겠다
@Entity
data class History(
    @PrimaryKey val uid: Int?,
    @ColumnInfo(name = "expression") val expression: String?,
    @ColumnInfo(name = "result") val result: String
)