package com.crackedcarrot;

import com.crackedcarrot.menu.R;
import com.scoreninja.adapter.ScoreNinjaAdapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GameFinished extends Activity {
	
	public ScoreNinjaAdapter scoreNinjaAdapter;
	
	private int score;
	private int mapChoice;
	private boolean multiplayer;
	private boolean survivalgame;
	private int difficulty;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamefinished);

        /** Ensures that the activity is displayed only in the portrait orientation */
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	
        Typeface typefaceSniglet = Typeface.createFromAsset(getAssets(), "fonts/Sniglet.ttf");
        
        Bundle extras  = getIntent().getExtras();
        score          = extras.getInt("score");
        mapChoice  = extras.getInt("map");
        boolean win    = extras.getBoolean("win");
        multiplayer = extras.getBoolean("multiplayer",true);
        survivalgame = extras.getBoolean("survival");
        difficulty = extras.getInt("difficulty");
	    
	    ImageView imageTitle = (ImageView) findViewById(R.id.GameFinishedImageViewTitle);
	    if (win)
	    	imageTitle.setImageResource(R.drawable.victory);
	    else
	    	imageTitle.setImageResource(R.drawable.defeat);

	    
	    ImageView image = (ImageView) findViewById(R.id.GameFinishedImageViewImage);
	    if (win)
	    	image.setImageResource(R.drawable.win);
	    else
	    	image.setImageResource(R.drawable.loose);

	    
        TextView tvTitle = (TextView) findViewById(R.id.GameFinishedTextViewTitle);
        tvTitle.setTypeface(typefaceSniglet);

        TextView tvText = (TextView) findViewById(R.id.GameFinishedTextViewText);
        tvText.setTag(typefaceSniglet);

        
	    ImageView trophyg = (ImageView) findViewById(R.id.trophyg);
	    ImageView trophys = (ImageView) findViewById(R.id.trophys);
	    ImageView trophyb = (ImageView) findViewById(R.id.trophyb);
        
        if (win && !survivalgame && !multiplayer) {
        	if (difficulty == 2)
        		trophyg.setVisibility(0); 
        	else if (difficulty == 1)
        		trophys.setVisibility(0); 
        	else
        		trophyb.setVisibility(0); 
        }
        
        
        if (win) {
        	tvTitle.setText("Congratulations!");
        	tvText.setText("You've slain all the vile rabbits and their evil companions and saved your precious carrots!");
        }
        else {
        	tvTitle.setText("You lost...");
        	tvText.setText("The vile rabbits have conquered your pitiful backgarden, and worse, the world!");
        }
        
        if (multiplayer && win) {
        	tvText.setText("You've defeated your opponent and watched him fall in the hands of the vile rabbits and their evil companions!");
        }
        else if (multiplayer && !win){
        	tvText.setText("Your opponent have used his vile rabbits to conquerer your pitiful backgarden!");
        	
        }
        
        TextView tvScore = (TextView) findViewById(R.id.GameFinishedTextViewScore);
        tvScore.setTypeface(typefaceSniglet);

        if (survivalgame) {
        	if (!multiplayer) {
            	tvTitle.setText("In survival game there is no winner...");
            	
            	tvText.setText("Training makes perfect. Try again!");
           		if (score >= 300)
            		tvText.setText("Good game but you can do better!");
           		if (score >= 500)
            		tvText.setText("Great work. But can you beat 700?");
            	if (score >= 700 && difficulty == 0)
            		tvText.setText("Nice! Time to kick ass on normal...");
            	if (score >= 700 && difficulty == 1)
            		tvText.setText("Excellent work. You are one of the best rabbit slayers in the world! Maybe it is time to try hard?");
               	if (score >= 700 && difficulty == 2)
            		tvText.setText("Amazing! You've proved yourself to be among the ranks of the best rabbit slayers in the world!");
        	}
        	tvScore.setText("Kills: " + extras.getInt("score"));
        }
        else tvScore.setText("Final score: " + extras.getInt("score"));
        
        
        //SAve level progress..
        if (win && !multiplayer && !survivalgame) {
	        SharedPreferences settings = getSharedPreferences("progress", 0);
	        SharedPreferences.Editor editor = settings.edit();
	        
	        int mapCompleted = settings.getInt("mapcompleted", 0);
	        
	        if (mapCompleted < mapChoice) {
		        editor.putInt("mapcompleted", mapChoice);
		        editor.commit();        
	        }
	        
	        if (mapChoice == 1) {
	        	int completedDifficultyMap1 = settings.getInt("difficultymap1", 0);
	        	if (completedDifficultyMap1 < difficulty+1) {
			        editor.putInt("difficultymap1", difficulty+1);
			        editor.commit();        	        		
	        	}	        
	        	int scoreMap1 = settings.getInt("scoremap1", 0);
	        	if (scoreMap1 < score) {
			        editor.putInt("scoremap1", score);
			        editor.commit();        	        		
	        	}	        
	        }
	        
	        if (mapChoice == 2) {
	        	int completedDifficultyMap2 = settings.getInt("difficultymap2", 0);
	        	if (completedDifficultyMap2 < difficulty+1) {
			        editor.putInt("difficultymap2", difficulty+1);
			        editor.commit();        	        		
	        	}	        
	        	int scoreMap2 = settings.getInt("scoremap2", 0);
	        	if (scoreMap2 < score) {
			        editor.putInt("scoremap2", score);
			        editor.commit();        	        		
	        	}	        
	        }

	        if (mapChoice == 3) {
	        	int completedDifficultyMap3 = settings.getInt("difficultymap3", 0);
	        	if (completedDifficultyMap3 < difficulty+1) {
			        editor.putInt("difficultymap3", difficulty+1);
			        editor.commit();        	        		
	        	}	        
	        	int scoreMap3 = settings.getInt("scoremap3", 0);
	        	if (scoreMap3 < score) {
			        editor.putInt("scoremap3", score);
			        editor.commit();        	        		
	        	}	        
	        }

	        if (mapChoice == 4) {
	        	int completedDifficultyMap4 = settings.getInt("difficultymap4", 0);
	        	if (completedDifficultyMap4 < difficulty+1) {
			        editor.putInt("difficultymap4", difficulty+1);
			        editor.commit();        	        		
	        	}	        
	        	int scoreMap4 = settings.getInt("scoremap4", 0);
	        	if (scoreMap4 < score) {
			        editor.putInt("scoremap4", score);
			        editor.commit();        	        		
	        	}	        
	        }
	        
	        if (mapChoice == 5) {
	        	int completedDifficultyMap5 = settings.getInt("difficultymap5", 0);
	        	if (completedDifficultyMap5 < difficulty+1) {
			        editor.putInt("difficultymap5", difficulty+1);
			        editor.commit();        	        		
	        	}	        
	        	int scoreMap5 = settings.getInt("scoremap5", 0);
	        	if (scoreMap5 < score) {
			        editor.putInt("scoremap5", score);
			        editor.commit();        	        		
	        	}	        
	        }

	        if (mapChoice == 6) {
	        	int completedDifficultyMap6 = settings.getInt("difficultymap6", 0);
	        	if (completedDifficultyMap6 < difficulty+1) {
			        editor.putInt("difficultymap6", difficulty+1);
			        editor.commit();        	        		
	        	}	        
	        	int scoreMap6 = settings.getInt("scoremap6", 0);
	        	if (scoreMap6 < score) {
			        editor.putInt("scoremap6", score);
			        editor.commit();        	        		
	        	}	        
	        }
             
        }

        //SAve survival score!s
        if (!multiplayer && survivalgame) {
	        SharedPreferences settings = getSharedPreferences("progress", 0);
	        SharedPreferences.Editor editor = settings.edit();
	        if (mapChoice == 1) {
	        	int killsMap1 = settings.getInt("killsmap1", 0);
	        	if (killsMap1 < score) {
			        editor.putInt("killsmap1", score);
			        editor.commit();        	        		
	        	}	        
	        }
	        if (mapChoice == 2) {
	        	int killsMap2 = settings.getInt("killsmap2", 0);
	        	if (killsMap2 < score) {
			        editor.putInt("killsmap2", score);
			        editor.commit();        	        		
	        	}	        
	        }
	        if (mapChoice == 3) {
	        	int killsMap3 = settings.getInt("killsmap3", 0);
	        	if (killsMap3 < score) {
			        editor.putInt("killsmap3", score);
			        editor.commit();        	        		
	        	}	        
	        }
	        if (mapChoice == 4) {
	        	int killsMap4 = settings.getInt("killsmap4", 0);
	        	if (killsMap4 < score) {
			        editor.putInt("killsmap4", score);
			        editor.commit();        	        		
	        	}	        
	        }
	        if (mapChoice == 5) {
	        	int killsMap5 = settings.getInt("killsmap5", 0);
	        	if (killsMap5 < score) {
			        editor.putInt("killsmap5", score);
			        editor.commit();        	        		
	        	}	        
	        }
	        if (mapChoice == 6) {
	        	int killsMap6 = settings.getInt("killsmap6", 0);
	        	if (killsMap6 < score) {
			        editor.putInt("killsmap6", score);
			        editor.commit();        	        		
	        	}	        
	        }
	        
        }
        
    	// Save everything and return to mainmenu.
        Button buttonBack = (Button) findViewById(R.id.GameFinished_Button_Ok);
        buttonBack.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		backButton();
        	}
        });
        
        // Load/prepare Scoreninja if it's active and installed.
        
        if (!this.multiplayer && !this.survivalgame && ScoreNinjaAdapter.isInstalled(this)) {
     	   
            if (mapChoice == 1) {
            	scoreNinjaAdapter = new ScoreNinjaAdapter(this, "mapzeroone", "E70411F009D4EDFBAD53DB7BE528BFE2");
            } else if (mapChoice == 2) {
            	scoreNinjaAdapter = new ScoreNinjaAdapter(this, "mapzerotwo", "26CCAFB5B609DEB078F18D52778FA70B");
            } else if (mapChoice == 3) {
            	scoreNinjaAdapter = new ScoreNinjaAdapter(this, "mapzerothree", "41F4C7AEF5A4DEF7BDC050AEB3EA37FC");
            } else if (mapChoice == 4) {
            	scoreNinjaAdapter = new ScoreNinjaAdapter(this, "mapzerofour", "EF3428A86CD2387E603C7CE41B9AAD34");
            } else if (mapChoice == 5) {
            	scoreNinjaAdapter = new ScoreNinjaAdapter(this, "mapzerofive", "FDF504FBDF1BF8E53968ED55CA591213");
            } else if (mapChoice == 6) {
            	scoreNinjaAdapter = new ScoreNinjaAdapter(this, "mapzerosix", "28E2D9AB8D002455400C1D93B09D9A64");
            }
     	   
     	   scoreNinjaAdapter.show(score);
        }
         
    }

    // Unfortunate API, but you must notify ScoreNinja onActivityResult.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      scoreNinjaAdapter.onActivityResult(
          requestCode, resultCode, data);
    }
    
    private void backButton() {
    	finish();
    }   
    
    @Override
    protected void onStop() {
       super.onStop();
    }
}
