package com.bluealien99.knotsandcrosses;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView playBtn = (TextView) findViewById(R.id.playBtn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent play = new Intent(getApplicationContext(), PlayPVP.class);
                startActivity(play);
            }
        });

        TextView playvCBtn = (TextView) findViewById(R.id.playvCBtn);
        playvCBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent play = new Intent(getApplicationContext(), PlayPVP.class);
                play.putExtra("AI", true);
                startActivity(play);
            }
        });

    }

}
