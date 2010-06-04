package com.crackedcarrot;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crackedcarrot.UI.UIHandler;
import com.crackedcarrot.fileloader.Level;
import com.crackedcarrot.menu.InstructionWebView;
import com.crackedcarrot.menu.R;
import com.scoreninja.adapter.ScoreNinjaAdapter;

	/*
	 * 
	 * This class builds on the GameInit-class with the purpose of containing all the
	 * required GUI elements for the GameLoop-class, for cleanliness/readability.
	 * 
	 */

public class GameLoopGUI {
	
	private GameInit gameInit;

	private Dialog dialog = null;
	
	private Dialog dialogNextLevel = null;
	private Dialog dialogPause = null;
	private Dialog dialogQuit = null;
	private ProgressDialog dialogWait = null;
	private Dialog dialogTowerInfo = null;
	private Dialog dialogScore = null;
	private Dialog dialogMpWon = null;
	private Dialog dialogMpLost = null;
	private Dialog dialogCompare = null;
	
    private int          healthBarState = 3;
    private int          healthProgress = 100;
    //private int          resume;
    private int			 playerScore;
    private int			 opponentScore;
    public  int          towerInfo;
    
    private WebView mWebView; // used by TowerInfo-dialog to display html-pages.

    private Drawable     healthBarDrawable;
    private ImageView    enImView;
	private LinearLayout statusBar;
	private LinearLayout creatureBar;
	private LinearLayout counterBar;	
    private ProgressBar  healthProgressBar;
    private TextView     currencyView;
    private TextView     nrCreText;
    private TextView     counterText;
    private TextView     playerHealthView;
    private TextView     scoreCounter;
    private UIHandler	 hud;

    // Used when we ask for the instruction view
    private int 		currentSelectedTower;
	
    
    	// For readability-reasons.
    public final int DIALOG_NEXTLEVEL_ID = 1;
    public final int DIALOG_WON_ID       = 2;
    public final int DIALOG_LOST_ID      = 3;
    public final int DIALOG_HIGHSCORE_ID = 4;
           final int DIALOG_QUIT_ID	     = 5;
    public final int DIALOG_TOWERINFO_ID = 6;
           final int DIALOG_PAUSE_ID     = 7;
    public final int WAIT_OPPONENT_ID    = 8;
    public final int CLOSE_WAIT_OPPONENT = 9;
    public final int LEVEL_SCORE         = 10;
    public final int MULTIPLAYER_WON     = 11;
    public final int MULTIPLAYER_LOST    = 12;
    public final int COMPARE_PLAYERS     = 13;
    
    public final int GUI_PLAYERMONEY_ID     = 20;
    public final int GUI_PLAYERHEALTH_ID    = 21;
    public final int GUI_CREATUREVIEW_ID    = 22;
           final int GUI_CREATURELEFT_ID    = 23;
    public final int GUI_PROGRESSBAR_ID     = 24;
    public final int GUI_NEXTLEVELINTEXT_ID = 25;
    public final int GUI_SHOWSTATUSBAR_ID   = 26;
    public final int GUI_SHOWHEALTHBAR_ID   = 27;
    public final int GUI_HIDEHEALTHBAR_ID   = 28;

    
    final Button towerbutton1;
    final Button towerbutton2;
    final Button towerbutton3;
    final Button towerbutton4;
    final LinearLayout towertext;
    
    final LinearLayout towerUpgrade;
    final Button sellTower;
    final Button closeUpgrade;
    final Button upgradeLvl2;
    final Button upgradeLvl3;
    final Button upgradeFire1;
    final Button upgradeFire2;
    final Button upgradeFire3;
    final Button upgradeFrost1;
    final Button upgradeFrost2;
    final Button upgradeFrost3;
    final Button upgradePoison1;
    final Button upgradePoison2;
    final Button upgradePoison3;
    
    final Button tower1Information;
    final Button tower2Information;
    final Button tower3Information;
    final Button tower4Information;

    
   	// Constructor. A good place to initiate all our different GUI-components.
    public GameLoopGUI(GameInit gi, final UIHandler hud) {
    	gameInit = gi;
    	this.hud = hud;
    	
    	towerUpgrade = (LinearLayout) gameInit.findViewById(R.id.upgrade_layout);
    	upgradeLvl2 = (Button) gameInit.findViewById(R.id.upgrade_lvl2);
    	upgradeLvl3 = (Button) gameInit.findViewById(R.id.upgrade_lvl3);
    	upgradeFire1 = (Button) gameInit.findViewById(R.id.upgrade_fire1);
    	upgradeFire2 = (Button) gameInit.findViewById(R.id.upgrade_fire2);
    	upgradeFire3 = (Button) gameInit.findViewById(R.id.upgrade_fire3);
    	upgradeFrost1 = (Button) gameInit.findViewById(R.id.upgrade_frost1);
    	upgradeFrost2 = (Button) gameInit.findViewById(R.id.upgrade_frost2);
    	upgradeFrost3 = (Button) gameInit.findViewById(R.id.upgrade_frost3);
    	upgradePoison1 = (Button) gameInit.findViewById(R.id.upgrade_poison1);
    	upgradePoison2 = (Button) gameInit.findViewById(R.id.upgrade_poison2);
    	upgradePoison3 = (Button) gameInit.findViewById(R.id.upgrade_poison3);
    	sellTower = (Button) gameInit.findViewById(R.id.sell);
    	closeUpgrade = (Button) gameInit.findViewById(R.id.close_upgrade);

        towertext = (LinearLayout) gameInit.findViewById(R.id.ttext);
        towerbutton1 = (Button) gameInit.findViewById(R.id.t1);
        towerbutton2 = (Button) gameInit.findViewById(R.id.t2);
        towerbutton3 = (Button) gameInit.findViewById(R.id.t3);
        towerbutton4 = (Button) gameInit.findViewById(R.id.t4);
        
        // Tower information. Clicking this will open information about this tower
        tower1Information = (Button) gameInit.findViewById(R.id.t1info);
        tower1Information.setOnClickListener(new InfoListener());
        // Tower information. Clicking this will open information about this tower
        tower2Information = (Button) gameInit.findViewById(R.id.t2info);
        tower2Information.setOnClickListener(new InfoListener());
        // Tower information. Clicking this will open information about this tower
        tower3Information = (Button) gameInit.findViewById(R.id.t3info);
        tower3Information.setOnClickListener(new InfoListener());
        // Tower information. Clicking this will open information about this tower
        tower4Information = (Button) gameInit.findViewById(R.id.t4info);
        tower4Information.setOnClickListener(new InfoListener());
    	
        // Create an pointer to the statusbar
        statusBar = (LinearLayout) gameInit.findViewById(R.id.status_menu);
        // Create an pointer to creatureBar
        creatureBar = (LinearLayout) gameInit.findViewById(R.id.creature_part);        
        // Create an pointer to counterBar
        counterBar = (LinearLayout) gameInit.findViewById(R.id.counter);
        
		// Create the TextView showing number of enemies left
        nrCreText = (TextView) gameInit.findViewById(R.id.nrEnemyLeft);
    	Typeface MuseoSans = Typeface.createFromAsset(gameInit.getAssets(), "fonts/MuseoSans_500.otf");
    	nrCreText.setTypeface(MuseoSans);
    	
		// Create the TextView showing counter
    	counterText = (TextView) gameInit.findViewById(R.id.countertext);
    	counterText.setTypeface(MuseoSans);
    	
    		// And the score Counter.
    	scoreCounter = (TextView) gameInit.findViewById(R.id.scoreCounter);
    	scoreCounter.setTypeface(MuseoSans);
    	
        // Create the progress bar, showing the enemies total health
        healthProgressBar = (ProgressBar) gameInit.findViewById(R.id.health_progress);
        healthProgressBar.setMax(healthProgress);
        healthProgressBar.setProgress(healthProgress);
        healthBarDrawable = healthProgressBar.getProgressDrawable();
		healthBarDrawable.setColorFilter(Color.parseColor("#339900"),PorterDuff.Mode.MULTIPLY);

        // Create the ImageView showing current creature
        enImView = (ImageView) gameInit.findViewById(R.id.enemyImVi);
        
        // Create the text view showing the amount of currency
        currencyView = (TextView)gameInit.findViewById(R.id.currency);
        currencyView.setTypeface(MuseoSans);       
        
    	// Create the text view showing a players health
        playerHealthView = (TextView) gameInit.findViewById(R.id.playerHealth);
        playerHealthView.setTypeface(MuseoSans); 
        
        /** Listeners for the five icons in the in-game menu.
         *  When clicked on, it's possible to place a tower
         *  on an empty space on the map. The first button
         *  is the normal/fast switcher. */
        final Button forward = (Button) gameInit.findViewById(R.id.forward);
        final Button play = (Button) gameInit.findViewById(R.id.play);

        forward.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		forward.setVisibility(View.GONE);
        		gameInit.gameLoop.setGameSpeed(3);
        		play.setVisibility(View.VISIBLE);
        	}
        });

        play.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		play.setVisibility(View.GONE);
        		gameInit.gameLoop.setGameSpeed(1);
        		forward.setVisibility(View.VISIBLE);
        	}
        });
        
        closeUpgrade.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		Log.d("GUI", "Close Upgrade clicked!");
        		
        		gameInit.hudHandler.hideRangeIndicator();

        		towerUpgrade.setVisibility(View.GONE);
           		towerbutton1.setVisibility(View.VISIBLE);
        		towerbutton2.setVisibility(View.VISIBLE);
        		towerbutton3.setVisibility(View.VISIBLE);
        		towerbutton4.setVisibility(View.VISIBLE);
        	}
        });
        
        towerbutton1.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		// A tower of type 1 has been chosen, where to put it?
        		gameInit.mGLSurfaceView.setTowerType(0);
        		openTowerBuildMenu(0);
        		hud.showGrid();
        	}
        });
        towerbutton2.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		// A tower of type 2 has been chosen, where to put it?
        		gameInit.mGLSurfaceView.setTowerType(1);
        		openTowerBuildMenu(1);
        		hud.showGrid();
        	}
        });
        towerbutton3.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		// A tower of type 3 has been chosen, where to put it?
        		gameInit.mGLSurfaceView.setTowerType(2);
        		openTowerBuildMenu(2);
        		hud.showGrid();
        	}
        });
        towerbutton4.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		// A tower of type 4 has been chosen, where to put it?
        		gameInit.mGLSurfaceView.setTowerType(3);
        		openTowerBuildMenu(3);
        		hud.showGrid();
        	}
        });

        // Button that removes towerInformation
        final Button inMenu6 = (Button) gameInit.findViewById(R.id.inmenu6);
        inMenu6.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		
        		gameInit.hudHandler.hideRangeIndicator();
        		
        		gameInit.mGLSurfaceView.setTowerType(-1);
        		towertext.setVisibility(View.GONE);
           		towerbutton1.setVisibility(View.VISIBLE);
        		towerbutton2.setVisibility(View.VISIBLE);
        		towerbutton3.setVisibility(View.VISIBLE);
        		towerbutton4.setVisibility(View.VISIBLE);
        		hud.hideGrid();
        	}
        });

    }
    
    
	/*
	 *  Creates all of our dialogs.
	 *  
	 *  Note: This functions is only called ONCE for each dialog.
	 *  If you need a dynamic dialog this code does NOT go here!
	 *  
	 */
	protected Dialog onCreateDialog(int id) {
		
		WindowManager.LayoutParams lp;
		
	    switch(id) {
	    
	    case DIALOG_NEXTLEVEL_ID:
	    	dialogNextLevel = new Dialog(gameInit,R.style.NextlevelTheme);
	    	dialogNextLevel.setContentView(R.layout.nextlevel);
	    	dialogNextLevel.setCancelable(true);
	    	// Info button
	    	Button infoButton = (Button) dialogNextLevel.findViewById(R.id.infobutton2);
	        infoButton.setOnClickListener(new OnClickListener() {
	        	
	        	public void onClick(View v) {
	        		Intent ShowInstr = new Intent(v.getContext(),InstructionWebView.class);
	        		gameInit.startActivity(ShowInstr);
	        	}
	        });
	    	
	    	// A button	    	
	    	Button butt = (Button) dialogNextLevel.findViewById(R.id.NextLevelButton);
	    	Typeface face = Typeface.createFromAsset(gameInit.getAssets(), "fonts/Sniglet.ttf");
	    	butt.setTypeface(face);
	    	butt.setOnClickListener(
	    			new View.OnClickListener() {
	    				public void onClick(View v) {
	    					dialogNextLevel.dismiss();

				    }
				});
	    	
	    	// Title of each level:
	    	TextView title = (TextView) dialogNextLevel.findViewById(R.id.NextLevelTitle);
	    	title.setTypeface(face);
	
    	
	    	// Text for next level will be placed here.
	    	TextView text = (TextView) dialogNextLevel.findViewById(R.id.NextLevelText);
	    	face = Typeface.createFromAsset(gameInit.getAssets(), "fonts/MuseoSans_500.otf");
	    	text.setTypeface(face);
	    	
	    	dialogNextLevel.setOnDismissListener(
	    			new DialogInterface.OnDismissListener() {
						public void onDismiss(DialogInterface dialog) {
							gameInit.gameLoop.dialogClick();
						}
	    			});
	    	return dialogNextLevel;
	    	//break;
	    	
	    	
	    case DIALOG_TOWERINFO_ID:
	    	dialogTowerInfo = new Dialog(gameInit, R.style.NextlevelTheme);
	    	dialogTowerInfo.setContentView(R.layout.towerinfo);
	    	dialogTowerInfo.setCancelable(true);
	    	
	    	Button close = (Button) dialogTowerInfo.findViewById(R.id.closewebdialog);
	    	close.setOnClickListener(
	    			new View.OnClickListener() {
	    				public void onClick(View v) {
	    					// nothing else. handled by onDismissListener instead, it's better.
	    					dialogTowerInfo.dismiss();
	    				}
	    			});

	    	final Button back = (Button) dialogTowerInfo.findViewById(R.id.backwebdialog);
	    	back.setOnClickListener(
	    			new View.OnClickListener() {
	    				public void onClick(View v) {
	    					// We'll never have a Back button in this dialog.
	    					//mWebView.goBack();
	    				}
	    			});
	    	
	    	dialogTowerInfo.setOnDismissListener(
	    			new DialogInterface.OnDismissListener() {
						public void onDismiss(DialogInterface dialog) {
								// Done with this window, unpause stuff.
							mWebView.clearView();
							GameLoop.unPause();
						}
	    			});
	    	
	        mWebView = (WebView) dialogTowerInfo.findViewById(R.id.webview);
	        mWebView.setBackgroundColor(0);

	        WebSettings webSettings = mWebView.getSettings();
	        webSettings.setSavePassword(false);
	        webSettings.setSaveFormData(false);
	        webSettings.setJavaScriptEnabled(false);
	        webSettings.setSupportZoom(false);
	        
	        return dialogTowerInfo;
	    	
	    case DIALOG_WON_ID:
	    	dialog = new Dialog(gameInit,R.style.NextlevelTheme);
	        dialog.setContentView(R.layout.levelwon);
	    	dialog.setCancelable(false);
	    	// First button
	    	Button buttonWon = (Button) dialog.findViewById(R.id.LevelWon_OK);
	        buttonWon.setOnClickListener(new OnClickListener() {
	        	public void onClick(View v) {
	        		gameInit.gameLoop.dialogClick();
	        	}
	        });
	    	break;
	    	
	    case DIALOG_LOST_ID:
	    	dialog = new Dialog(gameInit,R.style.NextlevelTheme);
	        dialog.setContentView(R.layout.levellost);
	    	dialog.setCancelable(false);
	    	// First button
	    	Button buttonLost = (Button) dialog.findViewById(R.id.LevelLost_OK);
	        buttonLost.setOnClickListener(new OnClickListener() {
	        	public void onClick(View v) {
	        		gameInit.gameLoop.dialogClick();
	        	}
	        });
	    	break;
	    	
	    case DIALOG_QUIT_ID:
	    	dialogQuit = new Dialog(gameInit,R.style.NextlevelTheme);
	    	dialogQuit.setContentView(R.layout.levelquit);
	    	dialogQuit.setCancelable(true);
	    	// First button
	    	Button quitYes = (Button) dialogQuit.findViewById(R.id.LevelQuit_Yes);
	        quitYes.setOnClickListener(new OnClickListener() {
	        	public void onClick(View v) {
	        		gameInit.finish();
	        	}
	        });
	    	
	    	// Second button
	    	Button quitNo = (Button) dialogQuit.findViewById(R.id.LevelQuit_No);
	    	quitNo.setOnClickListener(
	    			new View.OnClickListener() {
	    				public void onClick(View v) {
	    					dialogQuit.dismiss();
				    }
				});
	    	
	    	// Dismiss-listener
	    	dialogQuit.setOnDismissListener(
	    			new DialogInterface.OnDismissListener() {
						public void onDismiss(DialogInterface dialog) {
							// do nothing.
						}
	    			});
	    	
	        lp = dialogQuit.getWindow().getAttributes();
	        dialogQuit.getWindow().setAttributes(lp);
	        dialogQuit.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	    	
	    	return dialogQuit;
	    	//break;
	    	
	    case DIALOG_PAUSE_ID:
	    	dialogPause = new Dialog(gameInit, R.style.InGameMenu);
	        dialogPause.setContentView(R.layout.levelpause);
	    	dialogPause.setCancelable(true);
	    	
	    	// Continue button
	    	Button buttonPauseContinue = (Button) dialogPause.findViewById(R.id.LevelPause_Continue);
	    	face = Typeface.createFromAsset(gameInit.getAssets(), "fonts/Sniglet.ttf");
	    	buttonPauseContinue.setTypeface(face);

	    	buttonPauseContinue.setOnClickListener(
	    		new OnClickListener() {
	    			public void onClick(View v) {
	    				dialogPause.dismiss();
	    			}
	    		});
	    	
	    	// Continue button
	    	final Button buttonPauseSound = (Button) dialogPause.findViewById(R.id.LevelPause_Sound);
	    	face = Typeface.createFromAsset(gameInit.getAssets(), "fonts/Sniglet.ttf");
	    	buttonPauseSound.setTypeface(face);
	    	buttonPauseSound.setOnClickListener(
	    		new OnClickListener() {
	    			public void onClick(View v) {
	    				String pausetext = "Sound: ";
	    				if (gameInit.gameLoop.soundManager.playSound) {
	    	    			gameInit.gameLoop.soundManager.playSound = false;
	    		    		pausetext += "<font color=red>off</font>";
	    	    		} else {
	    	    			gameInit.gameLoop.soundManager.playSound = true;
	    		    		pausetext += "<font color=green>on</font>";
	    	    		}
	    		    	CharSequence chS = Html.fromHtml(pausetext);
	    				buttonPauseSound.setText(chS);

	    			}
	    		});
	    		// And update the text to match the current setting.
			String pausetext = "Sound: ";
			if (gameInit.gameLoop.soundManager.playSound)
	    		pausetext += "<font color=green>on</font>";
			else
	    		pausetext += "<font color=red>off</font>";
	    	CharSequence chS = Html.fromHtml(pausetext);
			buttonPauseSound.setText(chS);
	    	
	    	// Help button
	    	Button buttonPauseHelp = (Button) dialogPause.findViewById(R.id.LevelPause_Help);
	    	face = Typeface.createFromAsset(gameInit.getAssets(), "fonts/Sniglet.ttf");
	    	buttonPauseHelp.setTypeface(face);
	    	buttonPauseHelp.setOnClickListener(
	    		new OnClickListener() {
	    			public void onClick(View v) {
	    	       		Intent ShowInstr = new Intent(v.getContext(),InstructionWebView.class);
	            		gameInit.startActivity(ShowInstr);
	    			}
	    		});
	    	
	    	// Quit button
	    	Button buttonPauseQuit = (Button) dialogPause.findViewById(R.id.LevelPause_Quit);
	    	face = Typeface.createFromAsset(gameInit.getAssets(), "fonts/Sniglet.ttf");
	    	buttonPauseQuit.setTypeface(face);
	    	buttonPauseQuit.setOnClickListener(
	    		new OnClickListener() {
	    			public void onClick(View v) {
	    	       		gameInit.showDialog(DIALOG_QUIT_ID);
	    			}
	    		});
	        
	    	// Dismiss-listener
	    	dialogPause.setOnDismissListener(
	    		new DialogInterface.OnDismissListener() {
					public void onDismiss(DialogInterface dialog) {
						GameLoop.unPause();
					}
	    		});
	    	
	    		// Makes the background of the dialog blurred.
	        lp = dialogPause.getWindow().getAttributes();
	        dialogPause.getWindow().setAttributes(lp);
	        dialogPause.getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
	            WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

	    	return dialogPause;
	    	
	    case WAIT_OPPONENT_ID:
	    	dialogWait = new ProgressDialog(gameInit);
	    	dialogWait.setMessage("Waiting for opponent...");
	    	dialogWait.setIndeterminate(true);
	    	dialogWait.setCancelable(false);
	    	return dialogWait;
	    	
	    case LEVEL_SCORE:
	    	dialogScore = new Dialog(gameInit,R.style.NextlevelTheme);
	        dialogScore.setContentView(R.layout.multiplayer_score);
	    	dialogScore.setCancelable(false);
	    	
	    	Button closeScore = (Button) dialogScore.findViewById(R.id.scoreOK);
	        closeScore.setOnClickListener(new OnClickListener() {
	        	public void onClick(View v) {
	        		dialogScore.dismiss();
	        	}
	        });
	        dialogScore.setOnDismissListener(
	    			new DialogInterface.OnDismissListener() {
						public void onDismiss(DialogInterface dialog) {
							gameInit.gameLoop.dialogClick();
						}
	    			});
	        return dialogScore;
	        
	    case MULTIPLAYER_WON:
	    	dialogMpWon = new Dialog(gameInit,R.style.NextlevelTheme);
	    	dialogMpWon.setContentView(R.layout.multiplayer_won);
	    	dialogMpWon.setCancelable(false);
	    	
	    	TextView tv = (TextView) dialogMpWon.findViewById(R.id.wonText);
	    	String wonText = "The opponent is dead! You have won the battle!" + "<br><br>";
	    	wonText += 		"<b>Your score:<b> " + playerScore;
	    	CharSequence stText = Html.fromHtml(wonText);
		    tv.setText(stText);
	    	
	    	Button buttonMpWon = (Button) dialogMpWon.findViewById(R.id.MpWon_OK);
	        buttonMpWon.setOnClickListener(new OnClickListener() {
	        	public void onClick(View v) {
	        		dialogMpWon.dismiss();
	        	}
	        });
	        dialogMpWon.setOnDismissListener(
	    			new DialogInterface.OnDismissListener() {
						public void onDismiss(DialogInterface dialog) {
							gameInit.gameLoop.dialogClick();
						}
	    			});
	    	return dialogMpWon;
	    	
	    case MULTIPLAYER_LOST:
	    	dialogMpLost = new Dialog(gameInit,R.style.NextlevelTheme);
	    	dialogMpLost.setContentView(R.layout.multiplayer_lost);
	    	dialogMpLost.setCancelable(false);
	    	// First button
	    	Button buttonMpLost = (Button) dialogMpLost.findViewById(R.id.mpLost_OK);
	        buttonMpLost.setOnClickListener(new OnClickListener() {
	        	public void onClick(View v) {
	        		dialogMpLost.dismiss();
	        	}
	        });
	        dialogMpLost.setOnDismissListener(
	    			new DialogInterface.OnDismissListener() {
						public void onDismiss(DialogInterface dialog) {
							gameInit.gameLoop.dialogClick();
						}
	    			});
	    	return dialogMpLost;
	    case COMPARE_PLAYERS:
	    	dialogCompare = new Dialog(gameInit,R.style.NextlevelTheme);
	    	dialogCompare.setContentView(R.layout.multiplayer_compare);
	    	dialogCompare.setCancelable(false);
	    	// First button
	    	Button buttonCompare = (Button) dialogCompare.findViewById(R.id.mpCompare_OK);
	        buttonCompare.setOnClickListener(new OnClickListener() {
	        	public void onClick(View v) {
	        		dialogCompare.dismiss();
	        	}
	        });
	        dialogCompare.setOnDismissListener(
	    			new DialogInterface.OnDismissListener() {
						public void onDismiss(DialogInterface dialog) {
							gameInit.gameLoop.dialogClick();
						}
	    			});
	    	return dialogCompare;
	    	
	    default:
	    	Log.d("GAMEINIT", "onCreateDialog got unknown dialog id: " + id);
	        dialog = null;
	    }
	    return dialog;
	}

	
	/*
	 *  Creates our NextLevel-dialog.
	 *  
	 *  This is called every time a dialog is presented.
	 *  If you want dynamic dialogs, put your code here.
	 *  
	 */
	protected void onPrepareDialog(int id, Dialog dialog) {
	    switch(id) {
	    case DIALOG_NEXTLEVEL_ID:

	    	int currLvlnbr = gameInit.gameLoop.getLevelNumber() + 1;

	    	Level currLvl = gameInit.gameLoop.getLevelData();

	    	// Title:
	    	TextView title = (TextView) dialog.findViewById(R.id.NextLevelTitle);
	    	String titleText ="Level " + currLvlnbr + "<br>" + currLvl.creepTitle +"<br>";
		    CharSequence styledText = Html.fromHtml(titleText);
	    	title.setText(styledText);
	    	
    		// And an icon.
	    	ImageView image = (ImageView) dialog.findViewById(R.id.NextLevelImage);
	    	image.setImageResource(currLvl.getDisplayResourceId());
	    	
	    	// Text for next level goes here.
	    	TextView text = (TextView) dialog.findViewById(R.id.NextLevelText);
	    	
	    	Player currPlayer = gameInit.gameLoop.getPlayerData();
	    	String lvlText ="Number of creeps: " + currLvl.nbrCreatures +"<br>";
	    	lvlText += 		"Bounty: " + currLvl.goldValue + "g/creep<br>";
	    	lvlText += 		"Health: " + (int)currLvl.getHealth() + "hp/creep<br>";
	    	lvlText += 		"<br>";
	    	lvlText += 		"Special abilitys:<br>";
	    	int tmpAbil = 0;
	    	if (currLvl.creatureFast) {
		    	lvlText += 		"<font color=0xFF00FF>Fast level</font><br>";
		    	tmpAbil++;
	    	}
		    if (currLvl.creatureFireResistant) {
		    	lvlText += 		"<font color=red>Fire resistant</font><br>";
		    	tmpAbil++;
		    }
		    if (currLvl.creatureFrostResistant) {
		    	lvlText += 		"<font color=blue>Frost resistant</font><br>";
		    	tmpAbil++;
		    }
		    if (currLvl.creaturePoisonResistant) {
		    	lvlText += 		"<font color=green>Posion resistant</font><br>";
		    	tmpAbil++;
		    }
		    if (tmpAbil == 0)
		    	lvlText += 		"No special ability<br>";
		    
		    if (currLvlnbr > 1) {
		    	lvlText += 		"<br>Previous level:<br>";
		    	lvlText += 		"Interest gained:" + currPlayer.getInterestGainedThisLvl() + "<br>";
		    	lvlText += 		"Health lost:" + currPlayer.getHealthLostThisLvl();		    	
		    }
		    else {
		    	lvlText += 		"<br>Tip:<br>";
		    	lvlText += 		"If you have trouble <br>understanding this game.<br> Use the information<br> button below or ingame";
		    }
		    styledText = Html.fromHtml(lvlText);
		    text.setText(styledText);

		    if (currLvl.creaturePoisonResistant && !currLvl.creatureFireResistant && !currLvl.creatureFrostResistant) {
		    	image.setColorFilter(Color.rgb(178, 255, 178),PorterDuff.Mode.MULTIPLY);
		    }
		    else if (!currLvl.creaturePoisonResistant && currLvl.creatureFireResistant && !currLvl.creatureFrostResistant) {
		    	image.setColorFilter(Color.rgb(255, 178, 178),PorterDuff.Mode.MULTIPLY);
		    }
		    else if (!currLvl.creaturePoisonResistant && !currLvl.creatureFireResistant && currLvl.creatureFrostResistant) {
		    	image.setColorFilter(Color.rgb(178, 178, 255),PorterDuff.Mode.MULTIPLY);
		    }
		    else if (currLvl.creaturePoisonResistant && currLvl.creatureFrostResistant && !currLvl.creatureFireResistant) {
		    	image.setColorFilter(Color.rgb(178, 255, 255),PorterDuff.Mode.MULTIPLY);
		    }
		    else if (currLvl.creaturePoisonResistant && !currLvl.creatureFrostResistant && currLvl.creatureFireResistant) {
		    	image.setColorFilter(Color.rgb(255, 255, 178),PorterDuff.Mode.MULTIPLY);
		    }
		    else if (!currLvl.creaturePoisonResistant && currLvl.creatureFrostResistant && currLvl.creatureFireResistant) {
		    	image.setColorFilter(Color.rgb(255, 178, 255),PorterDuff.Mode.MULTIPLY);
		    }
		    else 
		    	image.setColorFilter(Color.rgb(255, 255, 255),PorterDuff.Mode.MULTIPLY);
		    break;
	    case LEVEL_SCORE:
	    	TextView tv = (TextView) dialogScore.findViewById(R.id.scoreText);
	    	String scoreText = "<b>Score so far:</b> " + "<br>";
	    	scoreText += 		"You: " + playerScore + "<br>";
	    	scoreText += 		"Opponent: " + opponentScore + "<br>";
	    	CharSequence sText = Html.fromHtml(scoreText);
		    tv.setText(sText);
	        break;
	    case COMPARE_PLAYERS:
	    	TextView wL = (TextView) dialogCompare.findViewById(R.id.compareWinLoose);
	    	TextView cS = (TextView) dialogCompare.findViewById(R.id.compareScores);
	    	String winLoose;
	    	String compareScores;
	    	//Is player score better than opponents, if so player is the winner
	    	if(playerScore > opponentScore){
	    		winLoose = "<b>You win!</b>";
	    	}
	    	else if (playerScore < opponentScore){
	    		winLoose = "<b>You Loose!</b>";
	    	} 
	    	else {
	    		winLoose = "<b>It's a tie!</b>";
	    	}
	    	compareScores = "Your score: " + playerScore + "<br>";
	    	compareScores += "Opponent's score: " + opponentScore;
	    	CharSequence chS = Html.fromHtml(winLoose);
		    wL.setText(chS);
		    CharSequence chS2 = Html.fromHtml(compareScores);
		    cS.setText(chS2);
	    	break;		    
	    case DIALOG_PAUSE_ID:
	    	final Button buttonPauseSound = (Button) dialogPause.findViewById(R.id.LevelPause_Sound);
    		// And update the image to match the current setting.
			String pausetext = "Sound: ";
	    	if (gameInit.gameLoop.soundManager.playSound)
	    		pausetext += "<font color=green>on</font>";
			else
	    		pausetext += "<font color=red>off</font>";
	    	
			chS = Html.fromHtml(pausetext);
			buttonPauseSound.setText(chS);
			break;
			
	    case DIALOG_TOWERINFO_ID:
	        // Fetch information from previous intent. The information will contain the
	        // tower decided by the player.
	        String url = null;
        	int tower = towerInfo;
        	if (tower == 0) 
                url = "file:///android_asset/t1.html";
        	if (tower == 1) 
                url = "file:///android_asset/t2.html";
        	if (tower == 2) 
                url = "file:///android_asset/t3.html";
        	if (tower == 3) 
                url = "file:///android_asset/t4.html";
	        mWebView.loadUrl(url);
	        break;
			
	    default:
	    	Log.d("GAMEINIT", "onPrepareDialog got unknown dialog id: " + id);
	        dialog = null;
	    }
	}

	
		// This is used to handle calls from the GameLoop to show
		// our dialogs.
	public Handler guiHandler = new Handler() {
	
	    @Override
	    public void handleMessage(Message msg) {

	        switch (msg.what) {
	        	 case DIALOG_NEXTLEVEL_ID:
	        		 SharedPreferences settings1 = gameInit.getSharedPreferences("Options", 0);
	        	     if (settings1.getBoolean("optionsNextLevel", true)
	        	    		 && !GameInit.multiplayerMode()) {
	        	    	 Log.d("GAMELOOPGUI", "Start next level dialog");
	        	    	 gameInit.showDialog(DIALOG_NEXTLEVEL_ID);
	        	     } else {
	        	    	 	// Simulate clicking the dialog.
	        	    	 Log.d("GAMELOOPGUI", "Simulate next level dialog");
	        	    	 gameInit.gameLoop.dialogClick();
	        	     }
	        		 break;
	        	 case DIALOG_WON_ID:
	        		 gameInit.showDialog(DIALOG_WON_ID);
	        		 break;
	        	 case DIALOG_LOST_ID:
	        		 gameInit.showDialog(DIALOG_LOST_ID);
	        		 break;
	        	 case DIALOG_HIGHSCORE_ID:
	        		 SharedPreferences settings2 = gameInit.getSharedPreferences("Options", 0);
	        	     if (settings2.getBoolean("optionsHighscore", false) && ScoreNinjaAdapter.isInstalled(gameInit)) {
	        	    	 	// If ScoreNinja is enabled and installed we show it to the player: 
	        	    	 gameInit.scoreNinjaAdapter.show(msg.arg1);
	        	     }
	        		 break;
	        		 
	        	 case GUI_PLAYERMONEY_ID:
	        		 // Update currencyView (MONEY) and score.
	        		 
	        		 scoreCounter.setText("" + String.format("%08d", gameInit.gameLoop.player.getScore()) );
	        		 
	        		 currencyView.setText("" + msg.arg1);
	        		 break;
	        	 case GUI_PLAYERHEALTH_ID:
	        		 // Update player-health. and score.
	        		 
	        		 scoreCounter.setText("" + String.format("%08d", gameInit.gameLoop.player.getScore()) );
	        		 
	        		 playerHealthView.setText("" + msg.arg1);
	        		 break;
	        	 case GUI_CREATUREVIEW_ID:
	        		 // Update Enemy-ImageView
	        		 enImView.setImageResource(msg.arg1);
	        		 break;
	        	 case GUI_CREATURELEFT_ID:
	        		 // update number of creatures still alive on GUI.
	        		 String tt = String.valueOf(msg.arg1);
	        		 nrCreText.setText("" + tt);
	        		 break;
	        	 case GUI_PROGRESSBAR_ID: // update progressbar with creatures health.
	        		 // The code below is used to change color of healthbar when health drops
	        		 if (msg.arg1 >= 66 && healthBarState == 1) {
	       				 healthBarDrawable.setColorFilter(Color.parseColor("#339900"),PorterDuff.Mode.MULTIPLY);
	        			 healthBarState = 3;
	        		 }
	        		 if (msg.arg1 <= 66 && healthBarState == 3) {
	        			 healthBarDrawable.setColorFilter(Color.parseColor("#FFBB00"),PorterDuff.Mode.MULTIPLY);
	        			 healthBarState = 2;
	        		 }
	        		 if (msg.arg1 <= 33 && healthBarState == 2) {
	        			 healthBarDrawable.setColorFilter(Color.parseColor("#CC0000"),PorterDuff.Mode.MULTIPLY);
	        			 healthBarState = 1;
	        		 }
	        		 healthProgressBar.setProgress(msg.arg1);
	        		 break;
	        		 
	        	 case GUI_NEXTLEVELINTEXT_ID: // This is used to show how long time until next lvl.
	        		 tt = String.valueOf(msg.arg1);
	        		 counterText.setText("Next level in: " + tt);
	        		 break;
	        		 
	        	 case GUI_SHOWSTATUSBAR_ID:
	        		 //Show statusbar
		    			statusBar.setVisibility(View.VISIBLE);
		    			break;
	        	 case GUI_SHOWHEALTHBAR_ID:
	        		 //If we want to switch back to healthbar
	        		 counterBar.setVisibility(View.GONE);
	        		 creatureBar.setVisibility(View.VISIBLE);
	        		 break;
	        	 case GUI_HIDEHEALTHBAR_ID:
	        		 //If we want to use space in statusbar to show time to next level counter
	        		 creatureBar.setVisibility(View.GONE);
	        		 counterBar.setVisibility(View.VISIBLE);
	        		 break;
	        	 case WAIT_OPPONENT_ID:
	        		 gameInit.showDialog(WAIT_OPPONENT_ID);
	        		 break;
	        	 case CLOSE_WAIT_OPPONENT:
	        		 dialogWait.hide();
	        		 break;
	        	 case LEVEL_SCORE:
	        		 playerScore = msg.arg1;
	        		 gameInit.showDialog(LEVEL_SCORE);
	        		 break;
	        	 case MULTIPLAYER_WON:
	        		 playerScore = msg.arg1;
	        		 gameInit.showDialog(MULTIPLAYER_WON);
	        		 break;
	        	 case MULTIPLAYER_LOST:
	        		 gameInit.showDialog(MULTIPLAYER_LOST);
	    			 break;
	        	 case COMPARE_PLAYERS:
	        		 playerScore = msg.arg1;
	        		 gameInit.showDialog(COMPARE_PLAYERS);
	        		 break;
	        		 
	        	 case -1: // GAME IS DONE, CLOSE ACTIVITY.
	        		 gameInit.finish();
	        		 break;
	        	 case -2: // SAVE THE GAME.
	        		 	// arg 1 = save game, 2 = remove saved game.
	        		 gameInit.saveGame(msg.arg1);
	        		 break;
	        		 
	        	 default:
	                 Log.e("GAMELOOPGUI", "guiHandler error! msg.what: " + msg.what);
	                 break;
	        }
	    }
	};
	
	public void sendMessage(int i, int j, int k) {

		// TODO: remove this when done debugging msgs.
		//Log.d("GAMELOOPGUI", "sendMessage: " + i);
		
		Message msg = Message.obtain();
		msg.what = i;
		msg.arg1 = j;
		msg.arg2 = k;
		guiHandler.sendMessage(msg);
	}

	private void openTowerBuildMenu(int towerId) {
	    if (towerId == 0) {
		    tower1Information.setVisibility(View.VISIBLE);
		    tower2Information.setVisibility(View.GONE);
		    tower3Information.setVisibility(View.GONE);
		    tower4Information.setVisibility(View.GONE);
	    }
	    else if (towerId == 1) {
		    tower1Information.setVisibility(View.GONE);
		    tower2Information.setVisibility(View.VISIBLE);
		    tower3Information.setVisibility(View.GONE);
		    tower4Information.setVisibility(View.GONE);
	    }
	    else if (towerId == 2) {
		    tower1Information.setVisibility(View.GONE);
		    tower2Information.setVisibility(View.GONE);
		    tower3Information.setVisibility(View.VISIBLE);
		    tower4Information.setVisibility(View.GONE);
	    }
	    else if (towerId == 3) {
		    tower1Information.setVisibility(View.GONE);
		    tower2Information.setVisibility(View.GONE);
		    tower3Information.setVisibility(View.GONE);
		    tower4Information.setVisibility(View.VISIBLE);
	    }
		this.currentSelectedTower = towerId;
   		towerbutton1.setVisibility(View.GONE);
		towerbutton2.setVisibility(View.GONE);
		towerbutton3.setVisibility(View.GONE);
		towerbutton4.setVisibility(View.GONE);
		towertext.setVisibility(View.VISIBLE);
	}
	
	public void setOpponentScore(int score){
		this.opponentScore = score;
	}
	
	/** Method used to get the GameInit object from the multiplayer handler */
	public GameInit getGameInit(){
		return this.gameInit;
	}
	
	public void showTowerUpgrade(int showLevelUpgrade, int LevelPrice,
								int showFireUpgrade, int FirePrice, 
								int showFrostUpgrade, int FrostPrice, 
								int showPoisonUpgrade, int PoisonPrice,
								int recellValue) {
		
		this.upgradeFire1.setVisibility(View.GONE);
		this.upgradeFire2.setVisibility(View.GONE);
		this.upgradeFire3.setVisibility(View.GONE);
		this.upgradeFrost1.setVisibility(View.GONE);
		this.upgradeFrost2.setVisibility(View.GONE);
		this.upgradeFrost3.setVisibility(View.GONE);
		this.upgradePoison1.setVisibility(View.GONE);
		this.upgradePoison2.setVisibility(View.GONE);
		this.upgradePoison3.setVisibility(View.GONE);
		this.upgradeLvl2.setVisibility(View.GONE);
		this.upgradeLvl3.setVisibility(View.GONE);
		
    	this.sellTower.setText("+"+recellValue);
		
		switch(showLevelUpgrade) {
			case(1):
				this.upgradeLvl2.setText("-"+LevelPrice);
				this.upgradeLvl2.setVisibility(View.VISIBLE);

				break;
			case(2):
				this.upgradeLvl3.setText("-"+LevelPrice);
				this.upgradeLvl3.setVisibility(View.VISIBLE);
				break;
		}
		switch(showFireUpgrade) {
		case(0):
			this.upgradeFire1.setText("-"+FirePrice);
			this.upgradeFire1.setVisibility(View.VISIBLE);
    		break;
		case(1):
			this.upgradeFire2.setText("-"+FirePrice);
			this.upgradeFire2.setVisibility(View.VISIBLE);
			break;
		case(2):
			this.upgradeFire3.setText("-"+FirePrice);
			this.upgradeFire3.setVisibility(View.VISIBLE);
			break;
		}
		switch(showFrostUpgrade) {
		case(0):
			this.upgradeFrost1.setText("-"+FrostPrice);
			this.upgradeFrost1.setVisibility(View.VISIBLE);
    		break;
		case(1):
			this.upgradeFrost2.setText("-"+FrostPrice);
			this.upgradeFrost2.setVisibility(View.VISIBLE);
			break;
		case(2):
			this.upgradeFrost3.setText("-"+FrostPrice);
			this.upgradeFrost3.setVisibility(View.VISIBLE);
			break;
		}
		switch(showPoisonUpgrade) {
		case(0):
			this.upgradePoison1.setText("-"+PoisonPrice);
			this.upgradePoison1.setVisibility(View.VISIBLE);
			break;
		case(1):
			this.upgradePoison2.setText("-"+PoisonPrice);
			this.upgradePoison2.setVisibility(View.VISIBLE);
		break;
		case(2):
			this.upgradePoison3.setText("-"+PoisonPrice);
			this.upgradePoison3.setVisibility(View.VISIBLE);
		break;
		}

		gameInit.mGLSurfaceView.setTowerType(-1);
		hud.hideGrid();
		this.towertext.setVisibility(View.GONE);
		towerbutton1.setVisibility(View.GONE);
		towerbutton2.setVisibility(View.GONE);
		towerbutton3.setVisibility(View.GONE);
		towerbutton4.setVisibility(View.GONE);
		towerUpgrade.setVisibility(View.VISIBLE);

	}
	
	public void hideTowerUpgrade() {
		towerUpgrade.setVisibility(View.GONE);
		towerbutton1.setVisibility(View.VISIBLE);
		towerbutton2.setVisibility(View.VISIBLE);
		towerbutton3.setVisibility(View.VISIBLE);
		towerbutton4.setVisibility(View.VISIBLE);
		
	}
	public void setUpgradeListeners(OnClickListener upgradeTowerLvlListener,
									OnClickListener upgradeFireListener,
									OnClickListener upgradeFrostListener,
									OnClickListener upgradePoisonListener,
									OnClickListener sellListener){
		
		this.upgradeLvl2.setOnClickListener(upgradeTowerLvlListener);
		this.upgradeLvl3.setOnClickListener(upgradeTowerLvlListener);
		this.upgradeFire1.setOnClickListener(upgradeFireListener);
		this.upgradeFire2.setOnClickListener(upgradeFireListener);
		this.upgradeFire3.setOnClickListener(upgradeFireListener);
		this.upgradeFrost1.setOnClickListener(upgradeFrostListener);
		this.upgradeFrost2.setOnClickListener(upgradeFrostListener);
		this.upgradeFrost3.setOnClickListener(upgradeFrostListener);
		this.upgradePoison1.setOnClickListener(upgradePoisonListener);
		this.upgradePoison2.setOnClickListener(upgradePoisonListener);
		this.upgradePoison3.setOnClickListener(upgradePoisonListener);
		this.sellTower.setOnClickListener(sellListener);
		
	}


	public void NotEnougMoney() {
		CharSequence text = "Not enough money";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(this.getGameInit(), text, duration);
		toast.show();
		hud.blinkRedRange();
	}

	private class InfoListener implements OnClickListener{
    	public void onClick(View v){
    		if (GameLoop.pause == false) {
    			GameLoop.pause();
    			gameInit.gameLoop.gui.towerInfo = currentSelectedTower;
    			gameInit.showDialog(DIALOG_TOWERINFO_ID);
    		}
    	}
    }

}