package com.crackedcarrot.menu;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.crackedcarrot.GameInit;
import com.crackedcarrot.multiplayer.*;


public class MainMenu extends Activity {
	
	/*
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            // When the user center presses, let them pick a contact.
    		Intent Credits = new Intent(MainMenu.this,Credits.class);
			Log.d("MAINMENU", "User pressed a button.");
            return true;
        }
        return false;
    }
    */
    

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /** Ensures that the activity is displayed only in the portrait orientation */
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        Button StartGameButton = (Button)findViewById(R.id.StartGame);
        StartGameButton.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		//Intent StartGame = new Intent(MainMenu.this,StartGame.class);
        		//startActivity(StartGame);
        		Intent StartGame = new Intent(v.getContext(),MapOp.class);
        		startActivity(StartGame);
        	}
        });
        
        Button ResumeButton = (Button)findViewById(R.id.Resume);
        ResumeButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		//Send the level variable to the game loop and start it
        		Intent StartGame2 = new Intent(v.getContext(),GameInit.class);
        		StartGame2.putExtra("com.crackedcarrot.menu.map", 0);
        		StartGame2.putExtra("com.crackedcarrot.menu.difficulty", 0);
        		startActivity(StartGame2);
        	}
        });

        Button OptionsButton = (Button)findViewById(R.id.Options);
        OptionsButton.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		Intent Options = new Intent(MainMenu.this,Options.class);
        		startActivity(Options);
        	}
        });
        
        Button HelpButton = (Button)findViewById(R.id.Help);
        HelpButton.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		Intent Help = new Intent(MainMenu.this,InstructionWebView.class);
        		startActivity(Help);
        	}
        });
        
        Button MultiPlayerButton = (Button)findViewById(R.id.Multiplayer);
        MultiPlayerButton.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		Intent Multiplayer = new Intent(MainMenu.this,MultiplayerOp.class);
        		startActivity(Multiplayer);
        		finish();
        	}
        });
        
    }
}