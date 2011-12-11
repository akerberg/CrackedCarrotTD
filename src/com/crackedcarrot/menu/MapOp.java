package com.crackedcarrot.menu;

import java.io.IOException;
import java.io.InputStream;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView.ScaleType;
import android.widget.ViewSwitcher.ViewFactory;

import com.crackedcarrot.GameInit;

/**
 * This class/activity consists of three clickable objects, two buttons that
 * switches between the existing maps with belonging description, and the map image
 * that starts the game loop when clicked on. It sends a level variable to the game
 * loop the game loads the data depending on which level the user has chosen.
 */
public class MapOp extends Activity implements ViewFactory {
	
	int mapcompleted = 0;
	int difficultymap1 = 0;
	int difficultymap2 = 0;
	int difficultymap3 = 0;
	int difficultymap4 = 0;
	int difficultymap5 = 0;
	int difficultymap6 = 0;
	int scoremap1 = 0;	
	int scoremap2 = 0;	
	int scoremap3 = 0;	
	int scoremap4 = 0;	
	int scoremap5 = 0;	
	int scoremap6 = 0;	
	
	
    /** The index for our "maps" array */
    private int difficulty = 1;
    private int mapSelected = 1;
    private int gameMode = 0;
        
    private TextView    tv;
    
    private ImageView mBackground;
    
    private ImageView easy;
    private ImageView hard;
    private ImageView normal;
    
    private RadioButton radioEasy;
    private RadioButton radioNormal;
    private RadioButton radioHard;
    private RadioButton radioSurvivalGame;
    private RadioButton radioNormalGame;
    
    private Button StartGameButton;
    
    private Gallery gallery;
    
    private Bitmap bitmap1;
    private Bitmap bitmap2;
    private Bitmap bitmap3;
    private Bitmap bitmap4;
    private Bitmap bitmap5;
    private Bitmap bitmap6;
    
    /** References to our images */
    private Bitmap[] mmaps = {
    		bitmap1,
    		bitmap2,
    		bitmap3,
    		bitmap4,
    		bitmap5,
    		bitmap6,
    };
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)  {
    	
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu_startgame);    
        
        SharedPreferences settings = getSharedPreferences("progress", 0);
        mapcompleted = settings.getInt("mapcompleted", 0);
        difficultymap1 = settings.getInt("difficultymap1", 0);
        difficultymap2 = settings.getInt("difficultymap2", 0);
        difficultymap3 = settings.getInt("difficultymap3", 0);
        difficultymap4 = settings.getInt("difficultymap4", 0);
        difficultymap5 = settings.getInt("difficultymap5", 0);
        difficultymap6 = settings.getInt("difficultymap6", 0);
    	scoremap1 = settings.getInt("scoremap1", 0);
    	scoremap2 = settings.getInt("scoremap2", 0);
    	scoremap3 = settings.getInt("scoremap3", 0);
    	scoremap4 = settings.getInt("scoremap4", 0);
    	scoremap5 = settings.getInt("scoremap5", 0);
    	scoremap6 = settings.getInt("scoremap6", 0);

        
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize = 1;
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        InputStream is;
        
    	if (difficultymap1 == 3) {
        	is = this.getResources().openRawResource(R.drawable.trophymap1g);
    	}            
    	else if (difficultymap1 == 2) {
        	is = this.getResources().openRawResource(R.drawable.trophymap1s);
    	}            
    	else if (difficultymap1 == 1) {
        	is = this.getResources().openRawResource(R.drawable.trophymap1b);
    	}            
    	else {
        	is = this.getResources().openRawResource(R.drawable.previewmap1);            		
    	}
        
        
        try {
        	mmaps[0] = BitmapFactory.decodeStream(is, null, options);
            if (mapcompleted < 1) {
            	is = this.getResources().openRawResource(R.drawable.padlockmap2);
            }
            else {
            	if (difficultymap2 == 3) {
                	is = this.getResources().openRawResource(R.drawable.trophymap2g);
            	}            
            	else if (difficultymap2 == 2) {
                	is = this.getResources().openRawResource(R.drawable.trophymap2s);
            	}            
            	else if (difficultymap2 == 1) {
                	is = this.getResources().openRawResource(R.drawable.trophymap2b);
            	}            
            	else {
                	is = this.getResources().openRawResource(R.drawable.previewmap2);            		
            	}
            }
            mmaps[1] = BitmapFactory.decodeStream(is, null, options);

            
            if (mapcompleted < 2){
            	is = this.getResources().openRawResource(R.drawable.padlockmap3);
            }
            else {
            	if (difficultymap3 == 3) {
                	is = this.getResources().openRawResource(R.drawable.trophymap3g);
            	}            
            	else if (difficultymap3 == 2) {
                	is = this.getResources().openRawResource(R.drawable.trophymap3s);
            	}            
            	else if (difficultymap3 == 1) {
                	is = this.getResources().openRawResource(R.drawable.trophymap3b);
            	}            
            	else {
                	is = this.getResources().openRawResource(R.drawable.previewmap3);            		
            	}
            }
            mmaps[2] = BitmapFactory.decodeStream(is, null, options);

            
            if (mapcompleted < 3){
            	is = this.getResources().openRawResource(R.drawable.padlockmap4);
            }
            else {
            	if (difficultymap4 == 3) {
                	is = this.getResources().openRawResource(R.drawable.trophymap4g);
            	}            
            	else if (difficultymap4 == 2) {
                	is = this.getResources().openRawResource(R.drawable.trophymap4s);
            	}            
            	else if (difficultymap4 == 1) {
                	is = this.getResources().openRawResource(R.drawable.trophymap4b);
            	}            
            	else {
                	is = this.getResources().openRawResource(R.drawable.previewmap4);            		
            	}
            }
            mmaps[3] = BitmapFactory.decodeStream(is, null, options);

                        
            if (mapcompleted < 4){
            	is = this.getResources().openRawResource(R.drawable.padlockmap5);
            }
            else {
            	if (difficultymap5 == 3) {
                	is = this.getResources().openRawResource(R.drawable.trophymap5g);
            	}            
            	else if (difficultymap5 == 2) {
                	is = this.getResources().openRawResource(R.drawable.trophymap5s);
            	}            
            	else if (difficultymap5 == 1) {
                	is = this.getResources().openRawResource(R.drawable.trophymap5b);
            	}            
            	else {
                	is = this.getResources().openRawResource(R.drawable.previewmap5);            		
            	}
            }
            mmaps[4] = BitmapFactory.decodeStream(is, null, options);

            
            if (mapcompleted < 5){
            	is = this.getResources().openRawResource(R.drawable.padlockmap6);
            }
            else {
            	if (difficultymap6 == 3) {
                	is = this.getResources().openRawResource(R.drawable.trophymap6g);
            	}            
            	else if (difficultymap6 == 2) {
                	is = this.getResources().openRawResource(R.drawable.trophymap6s);
            	}            
            	else if (difficultymap6 == 1) {
                	is = this.getResources().openRawResource(R.drawable.trophymap6b);
            	}            
            	else {
                	is = this.getResources().openRawResource(R.drawable.previewmap6);            		
            	}
            }
            mmaps[5] = BitmapFactory.decodeStream(is, null, options);

        } finally {
            try {
                is.close();
            } catch (IOException e) {
            	//Skip
            }
        }
        
        /** Ensures that the activity is displayed only in the portrait orientation */
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	
        /** identifying the image views and text view, 
         *  these are the ones that will be set. */
    	mBackground = (ImageView) findViewById(R.id.mBackground);
    	
        tv = (TextView) this.findViewById(R.id.maptext);
    	Typeface face = Typeface.createFromAsset(this.getAssets(), "fonts/MuseoSans_500.otf");
    	tv.setTypeface(face);
    	
        gallery = (Gallery) findViewById(R.id.gallery1);
        gallery.setAdapter(new ImageAdapter(this));
        gallery.setOnItemSelectedListener(gItemSelectedHandler);
        
        //gallery.setSelection((gallery.getCount()/2)-2, true);
        gallery.setSelection(0, true);

        StartGameButton = (Button)findViewById(R.id.startmap);
        StartGameButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		//Send the level variable to the game loop and start it

            	if (mapcompleted+1 < mapSelected) {
	        		CharSequence text = "You need to complete previous map before starting this map.";
	        		int duration = Toast.LENGTH_SHORT;
	        		Toast toast = Toast.makeText(getBaseContext(), text, duration);
	        		toast.show();
	        		return;
            	}
        		
        		StartGameButton.setVisibility(View.INVISIBLE);
        		
        		tv.setVisibility(View.INVISIBLE);
        		
        		radioEasy.setVisibility(View.INVISIBLE);
        		radioNormal.setVisibility(View.INVISIBLE);
        		radioHard.setVisibility(View.INVISIBLE);
        		radioNormalGame.setVisibility(View.INVISIBLE);
        		radioSurvivalGame.setVisibility(View.INVISIBLE);
        		
        		easy.setVisibility(View.INVISIBLE);
        		normal.setVisibility(View.INVISIBLE);
        		hard.setVisibility(View.INVISIBLE);
        		
        		gallery.setVisibility(View.INVISIBLE);
        		
        		mBackground.setImageResource(R.drawable.loadimage);
        		mBackground.setScaleType(ScaleType.CENTER_INSIDE);
        		        		
        		Intent StartGame = new Intent(v.getContext(),GameInit.class);
       			StartGame.putExtra("com.crackedcarrot.menu.map", mapSelected);
        		StartGame.putExtra("com.crackedcarrot.menu.difficulty", difficulty);
        		StartGame.putExtra("com.crackedcarrot.menu.wave", gameMode);
        		startActivity(StartGame);
        		finish();
        	}
        });
        
        // Difficulty listeners.
    	face = Typeface.createFromAsset(this.getAssets(), "fonts/MuseoSans_500.otf");
        radioEasy = (RadioButton) findViewById(R.id.radioEasy);
       	radioEasy.setTypeface(face);
        radioNormal = (RadioButton) findViewById(R.id.radioNormal);
       	radioNormal.setTypeface(face);
        radioHard = (RadioButton) findViewById(R.id.radioHard);
       	radioHard.setTypeface(face);
        radioNormalGame = (RadioButton) findViewById(R.id.radioNormalGame);
        radioNormalGame.setTypeface(face);
       	radioSurvivalGame = (RadioButton) findViewById(R.id.radioSurvivalGame);
        radioSurvivalGame.setTypeface(face);
       	
       	radioEasy.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		difficulty = 0;
        		setRadioButtons(0);
            	CharSequence text = "For beginners. Gives the bronze trophy at completion.";
        		int duration = Toast.LENGTH_SHORT;
        		Toast toast = Toast.makeText(getBaseContext(), text, duration);
        		toast.show();
			}

        });
        
        radioNormal.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		difficulty = 1;
        		setRadioButtons(1);
            	CharSequence text = "For the average player. Gives the silver trophy at completion.";
        		int duration = Toast.LENGTH_SHORT;
        		Toast toast = Toast.makeText(getBaseContext(), text, duration);
        		toast.show();
			}
        });

        radioHard.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
    			difficulty = 2;
    			setRadioButtons(2);
            	CharSequence text = "For experienced players. Gives the gold trophy at completion.";
        		int duration = Toast.LENGTH_SHORT;
        		Toast toast = Toast.makeText(getBaseContext(), text, duration);
        		toast.show();
			}
        }); 

       	radioNormalGame.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		gameMode=0;
        		radioNormalGame.setChecked(true);
        		radioSurvivalGame.setChecked(false);
			}

        });        
        
       	radioSurvivalGame.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		/*if (fullversion == 0) {
                	CharSequence text = "Survival is not available in this version.";
            		int duration = Toast.LENGTH_SHORT;
            		Toast toast = Toast.makeText(getBaseContext(), text, duration);
            		toast.show();
        			gameMode = 0;
            		radioNormalGame.setChecked(true);
            		radioSurvivalGame.setChecked(false);
        		}*/
    			gameMode=3;
    			radioNormalGame.setChecked(false);
    			radioSurvivalGame.setChecked(true);
    			
    			if (mapcompleted+1 == mapSelected) {
                	CharSequence text = "Only normal mode unlocks the next map.";
            		int duration = Toast.LENGTH_SHORT;
            		Toast toast = Toast.makeText(getBaseContext(), text, duration);
            		toast.show();
    			} 
    			else {
                	//CharSequence text = ".";
            		//int duration = Toast.LENGTH_SHORT;
            		//Toast toast = Toast.makeText(getBaseContext(), text, duration);
            		//toast.show();
    				
    			}
        	}

        });        
        
        easy = (ImageView) findViewById(R.id.StartGameImageViewEasy);
        easy.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		difficulty = 0;
        		setRadioButtons(0);
        	}
        });
        
        normal = (ImageView) findViewById(R.id.StartGameImageViewNormal);
        normal.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		difficulty = 1;
        		setRadioButtons(1);
        	}
        });
        
        hard = (ImageView) findViewById(R.id.StartGameImageViewHard);
        hard.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        			difficulty = 2;
        			setRadioButtons(2);
        	}
        });

    }

    private void setRadioButtons(int i) {        		
    	radioEasy.setChecked(false);
    	radioNormal.setChecked(false);
    	radioHard.setChecked(false);
    	
    	switch(i) {
    	case 0:
        	radioEasy.setChecked(true);
    		break;
    	case 1:
        	radioNormal.setChecked(true);
    		break;
    	case 2:
        	radioHard.setChecked(true);
    		break;
    	}
    }

    
    
    	// Called when we get focus again (after a game has ended).
    @Override
    public void onRestart() {
        super.onRestart();

        		// TODO: Ta bort allt detta?
        	// Reset the selected map?
        // mapSelected = 1;
       	// tv.setText("Map 1: The field of grass.");
    }

	public View makeView() {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(R.drawable.xml_gallery);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setLayoutParams(new 
                ImageSwitcher.LayoutParams(
                        LayoutParams.FILL_PARENT,
                        LayoutParams.FILL_PARENT));
        return imageView;
	}
	
   public OnItemSelectedListener gItemSelectedHandler = new
   OnItemSelectedListener() {
      //@Override
       public void onItemSelected(AdapterView<?> parent, View v, int _position, long id) {
    	   //_position =  _position%6;
    	   switch(_position){
				case 0:			
					if (difficultymap1 == 1)
						tv.setText("Map 1: 30 waves. (Bronze)");
					else if (difficultymap1 == 2)
						tv.setText("Map 1: 30 waves. (Silver)");
					else if (difficultymap1 == 3)
						tv.setText("Map 1: 30 waves. (Gold)");
					else
						tv.setText("Map 1: 30 waves.");
					
					mapSelected = 1;
					break;
				case 1: 
			       	if (mapcompleted < 1) {
						mapSelected = 2;
						tv.setText("Map 2: Complete Map 1 to unlock.");
			       	}
			       	else {
			       		mapSelected = 2;

						if (difficultymap2 == 1)
							tv.setText("Map 2: 35 waves. (Bronze)");
						else if (difficultymap2 == 2)
							tv.setText("Map 2: 35 waves. (Silver)");
						else if (difficultymap2 == 3)
							tv.setText("Map 2: 35 waves. (Gold)");
						else
							tv.setText("Map 2: 35 waves.");			       		
			       	}
			       	break;	
				case 2: 
			       	if (mapcompleted < 2) {
						mapSelected = 3;
						tv.setText("Map 3: Complete Map 2 to unlock.");
			       	}
			       	else {
			       		mapSelected = 3;

						if (difficultymap3 == 1)
							tv.setText("Map 3: 40 waves. (Bronze)");
						else if (difficultymap3 == 2)
							tv.setText("Map 3: 40 waves. (Silver)");
						else if (difficultymap3 == 3)
							tv.setText("Map 3: 40 waves. (Gold)");
						else
							tv.setText("Map 3: 40 waves.");			    
					}
					break;
				case 3: 
			       	if (mapcompleted < 3) {
						mapSelected = 4;
						tv.setText("Map 4: Complete Map 3 to unlock.");
			       	}
			       	else {
			       		mapSelected = 4;

			       		if (difficultymap4 == 1)
							tv.setText("Map 4: 45 waves. (Bronze)");
						else if (difficultymap4 == 2)
							tv.setText("Map 4: 45 waves. (Silver)");
						else if (difficultymap4 == 3)
							tv.setText("Map 4: 45 waves. (Gold)");
						else
							tv.setText("Map 4: 45 waves.");			    
			       	}
					break;
				case 4: 
			       	if (mapcompleted < 4) {
						mapSelected = 5;
						tv.setText("Map 5: Complete Map 4 to unlock.");
			       	}
			       	else {
			       		mapSelected = 5;

			       		if (difficultymap5 == 1)
							tv.setText("Map 5: 50 waves. (Bronze)");
						else if (difficultymap5 == 2)
							tv.setText("Map 5: 50 waves. (Silver)");
						else if (difficultymap5 == 3)
							tv.setText("Map 5: 50 waves. (Gold)");
						else
							tv.setText("Map 5: 50 waves.");			    
			       	}
			       	break;	
				case 5: 
			       	if (mapcompleted < 2) {
						mapSelected = 6;
						tv.setText("Map 6: Complete Map 5 to unlock.");
			       	}
			       	else {
			       		mapSelected = 6;

			       		if (difficultymap6 == 1)
							tv.setText("Map 6: 55 waves. (Bronze)");
						else if (difficultymap6 == 2)
							tv.setText("Map 6: 55 waves. (Silver)");
						else if (difficultymap6 == 3)
							tv.setText("Map 6: 55 waves. (Gold)");
						else
							tv.setText("Map 6: 55 waves.");			    
			       	}
					break;
			}
			
       }
        //@Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }
    };

    private class ImageAdapter extends BaseAdapter 
    {
        private Context context;
        //private int itemBackground;
        public int position = 0;
        private int x;
        private int y;
        
        public ImageAdapter(Context c) {
            context = c;
            x = (int) (110 * getResources().getDisplayMetrics().density);
            y = (int) (165 * getResources().getDisplayMetrics().density);
        }
 
        //---returns the number of images---
        public int getCount() {
        	//return 1000;
        	return 6;
        }
 
        public Object getItem(int position) {
            //return position%6;
            return position;
        }
 
        public long getItemId(int position) {
            //return position%5;
            return position;
        }
 
        //---returns an ImageView view---
        public View getView(int position, View convertView, ViewGroup parent)
        {
        	this.position = position;
        	//this.position = position%6;
        	ImageView imageView = new ImageView(context);
            imageView.setImageBitmap(mmaps[this.position]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new Gallery.LayoutParams(x,y));
            imageView.setBackgroundResource(R.drawable.xml_gallery);
            return imageView;
        }

   }
    
}
