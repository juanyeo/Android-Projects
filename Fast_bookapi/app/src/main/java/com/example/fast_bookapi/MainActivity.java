package com.example.fast_bookapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fast_bookapi.adapter.BookListAdapter;
import com.example.fast_bookapi.adapter.DiffUtilClass;
import com.example.fast_bookapi.api.BookAPIService;
import com.example.fast_bookapi.databinding.ActivityMainBinding;
import com.example.fast_bookapi.model.Books;
import com.example.fast_bookapi.room.AppDatabase;
import com.example.fast_bookapi.room.History;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private BookAPIService bookApiService;
    private Call<Books> booksCall;

    private ActivityMainBinding binding;
    private RecyclerView recyclerView;
    private BookListAdapter adapter;

    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
        initRetrofit();
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "book-database").build();

        getBestSellerRetrofit();

        SelectAsyncTask async = new SelectAsyncTask();
        async.execute();
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://book.interpark.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        bookApiService = retrofit.create(BookAPIService.class);
    }

    private void initViews() {
        adapter = new BookListAdapter(new DiffUtilClass());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        binding.searchEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // 1. 엔터키 를 2. 누르는 동작을 수행했을 때
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == MotionEvent.ACTION_DOWN) {
                    getBooksRetrofit(binding.searchEditText.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    private void getBestSellerRetrofit() {
        booksCall = bookApiService.getBestSellerBooks("1394B778128EA14D5B6386368431E0F65D0027117763D05BBBD781E5B69834EF");
        booksCall.enqueue(retrofitCallback);
    }

    private void getBooksRetrofit(String query) {
        InsertAsyncTask async = new InsertAsyncTask();
        async.execute(query);

        booksCall = bookApiService.getBooksByName("1394B778128EA14D5B6386368431E0F65D0027117763D05BBBD781E5B69834EF",
                query);
        booksCall.enqueue(retrofitCallback);
    }

    class SelectAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            List<History> histories = database.historyDao().getAll();

            for (History history: histories) {
                Log.d("rooom", history.getKeyword());
            }

            return null;
        }
    }

    class InsertAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String query = strings[0];
            History history = new History(null, query);
            database.historyDao().insertHistory(history);

            return null;
        }
    }

    private Callback<Books> retrofitCallback = new Callback<Books>() {

        @Override
        public void onResponse(Call<Books> call, Response<Books> response) {

            if (!response.isSuccessful()) {
                Log.d("bookapi", "Response is not Successful");
                return;
            }

            Books result = response.body();

            Log.d("bookapi", result.booksString());
            adapter.submitList(result.getItem());
        }

        @Override
        public void onFailure(Call<Books> call, Throwable t) {
            t.printStackTrace();
        }
    };
}