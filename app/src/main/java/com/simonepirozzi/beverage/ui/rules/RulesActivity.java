package com.simonepirozzi.beverage.ui.rules;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.simonepirozzi.beverage.R;


public class RulesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        TextView textView = findViewById(R.id.rulesText);

        StringBuilder stringBuilder = new StringBuilder();
        String[] array = getResources().getStringArray(R.array.rules_list);
        for (String rule : array) {
            stringBuilder.append(rule);
        }
        textView.setText(stringBuilder.toString());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
