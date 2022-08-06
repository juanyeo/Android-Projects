package com.example.fast_bookapi.api;

import com.example.fast_bookapi.model.Book;
import com.example.fast_bookapi.model.Books;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// Base URL: https://book.interpark.com
public interface BookAPIService {
    @GET("/api/search.api?output=json")
    Call<Books> getBooksByName(
            @Query("key") String apiKey,
            @Query("query") String keyword
    );

    @GET("/api/bestSeller.api?output=json&categoryId=100")
    Call<Books> getBestSellerBooks(
            @Query("key") String apiKey
    );
}
