package com.example.fast_dailyquotes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager2 = (ViewPager2) findViewById(R.id.viewPager);
        initData();
    }

    // Page Transformer 효과 적용 (선택적) -> ViewPager.setPageTransformer
    // Infinite View Pager 적용 -> getItemCount = Int.MAX_VALUE, bindView 의 position % quotes.size
    // position 0 에서 왼쪽으로 이동하려면 ViewPager.setCurrentItem(adapter.itemCount / 2, false)
    private void initView() {

    }

    // Firebase 서비스 연동한 개발 시 처음에는 에러가 날 수 있음. 앱을 삭제한 뒤 재설치 하면 동작한다
    private void initData() {
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(10)
                .build();
        remoteConfig.setConfigSettingsAsync(configSettings);

        remoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                                try {
                                    List<Quote> quotes = parseJson(remoteConfig.getString("quotes"));
                                    Boolean isNameRevealed = remoteConfig.getBoolean("is_name_revealed");

                                    QuoteViewPagerAdapter adapter = new QuoteViewPagerAdapter(quotes, isNameRevealed);
                                    viewPager2.setAdapter(adapter);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                        }
                    }
                });
    }

    private List<Quote> parseJson(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        List<Quote> quoteList = new ArrayList();

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            if (object != null) {
                Quote quote = new Quote(object.getString("quote"), object.getString("name"));
                quoteList.add(quote);
            }
        }

        return quoteList;
    }
}