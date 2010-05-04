package com.crackedcarrot.menu;

import com.crackedcarrot.menu.R;
import com.scoreninja.adapter.ScoreNinjaAdapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class Options extends Activity {

	private boolean optionsHighscore;
	private boolean optionsNextLevel;
	private boolean optionsSound;
	
	private ImageButton imageButton1;
	private ImageButton imageButton2;
	private ImageButton imageButton3;

    private ScoreNinjaAdapter scoreNinjaAdapter;

	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu_options);

        /** Ensures that the activity is displayed only in the portrait orientation */
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    	// Register on ScoreNinja.
        scoreNinjaAdapter = new ScoreNinjaAdapter(this, "crackedcarrotd", "25912218B4FA767CCBE9F34735C93589");
    	
        // Restore preferences
        SharedPreferences settings = getSharedPreferences("Options", 0);
        optionsHighscore = settings.getBoolean("optionsHighscore", true);
        optionsNextLevel = settings.getBoolean("optionsNextLevel", true);
        optionsSound     = settings.getBoolean("optionsSound", true);
        
        
        imageButton1 = (ImageButton) findViewById(R.id.MainMenuOptionsImageButton1);
        imageButton1.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        			// Toggles sound on or off
        		if (optionsSound) {
        			setSound(false);
        		} else {
        			setSound(true);
        		}
        	}
        });
        	// Update the Sound on/off image.
        setSound(optionsSound);

        
        imageButton2 = (ImageButton) findViewById(R.id.MainMenuOptionsImageButton2);
        imageButton2.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        			// Toggles highscore on or off
        		if (optionsHighscore) {
        			setHighscore(false);
        		} else {
        			setHighscore(true);
        		}
        	}
        });
        	// Update highscore-image
        setHighscore(optionsHighscore);

        
        	// Enable/disable NextLevel-dialog.
        imageButton3 = (ImageButton) findViewById(R.id.MainMenuOptionsImageButton3);
        imageButton3.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        			// Toggles highscore on or off
        		if (optionsNextLevel) {
        			setNextLevel(false);
        		} else {
        			setNextLevel(true);
        		}
        	}
        });
        	// Update highscore-image
        setNextLevel(optionsNextLevel);
        
        
        ImageButton imageButtonSave = (ImageButton) findViewById(R.id.MainMenuOptionsImageButtonOk);
        imageButtonSave.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		setSave();
        	}
        });
    }

    // Unfortunate API, but you must notify ScoreNinja onActivityResult.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      scoreNinjaAdapter.onActivityResult(
          requestCode, resultCode, data);
    }

    
    @Override
    protected void onStop() {
       super.onStop();

      SharedPreferences settings = getSharedPreferences("Options", 0);
      SharedPreferences.Editor editor = settings.edit();
      editor.putBoolean("optionsHighscore", optionsHighscore);
      editor.putBoolean("optionsNextLevel", optionsNextLevel);
      editor.putBoolean("optionsSound", optionsSound);

      editor.commit();
    }

    
    public void setHighscore(boolean b) {
    	this.optionsHighscore = b;
    	
    	if (b) {
			imageButton2.setImageResource(R.drawable.button_yes);
    	} else {
			imageButton2.setImageResource(R.drawable.button_no);
    	}
    }
    
    public void setNextLevel(boolean b) {
    	this.optionsNextLevel = b;
    	
    	if (b) {
			imageButton3.setImageResource(R.drawable.button_yes);
    	} else {
			imageButton3.setImageResource(R.drawable.button_no);
    	}
    }
    
    public void setSave() {
    	this.finish();
    }
    
    public void setSound(boolean b) {
    	this.optionsSound = b;
    	
    	if (b) {
			imageButton1.setImageResource(R.drawable.button_yes);
    	} else {
			imageButton1.setImageResource(R.drawable.button_no);
    	}
    }

}
