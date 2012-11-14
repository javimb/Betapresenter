package com.javimb.betapresenter.client;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener, OnSharedPreferenceChangeListener {
	private SharedPreferences prefs;
	private Button leftButton, rightButton, startButton, resetButton;
	private Chronometer chrono;
    private String ip;
    private int port;
    
    public static TextView logo;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
        
        leftButton = (Button) findViewById(R.buttons.left);
        rightButton = (Button) findViewById(R.buttons.right);
        startButton = (Button) findViewById(R.buttons.start);
        resetButton = (Button) findViewById(R.buttons.reset);
        chrono = (Chronometer) findViewById(R.id.chrono);
        
        logo = (TextView) findViewById(R.id.logo);
        
        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        startButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
        
        ip = prefs.getString("ip", getString(R.string.defaultIp));
        port = Integer.parseInt(prefs.getString("port", getString(R.string.defaultPort)));
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.buttons.left:
			new Sender(ip, port, "1");
			break;
		case R.buttons.right:
			new Sender(ip, port, "2");
			break;
		case R.buttons.start:
			chrono.setBase(SystemClock.elapsedRealtime());
			chrono.start();
			startButton.setEnabled(false);
			break;
		case R.buttons.reset:
			chrono.setBase(SystemClock.elapsedRealtime());
			chrono.stop();
			startButton.setEnabled(true);
			break;
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		ip = prefs.getString("ip", getString(R.string.defaultIp));
        port = Integer.parseInt(prefs.getString("port", getString(R.string.defaultPort)));		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itemPrefs:
			startActivity(new Intent(this, PrefsActivity.class));
			break;
		}
		return true;
	}
}