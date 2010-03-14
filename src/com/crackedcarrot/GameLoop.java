package com.crackedcarrot;

import android.os.SystemClock;
import android.util.Log;
import com.crackedcarrot.fileloader.Level;
import com.crackedcarrot.fileloader.Map;
import com.crackedcarrot.fileloader.TowerGrid;

/**
 * A runnable that updates the position of each creature and projectile
 * every frame by applying a simple movement simulation. Also keeps
 * track of player life, level count etc.
 */
public class GameLoop implements Runnable {
    private Player player;
    private Map mGameMap;

    private Creature[] mCreatures;
    private int remainingCreatures;

    private Level[] mLvl;
    private int lvlNbr;
    private Coords[] wayP;
    
    private Tower[] mTower;
    private TowerGrid[][] mTowerGrid;
    private Tower[] mTTypes;
    private int totalNumberOfTowers = 0;
    private Shot[] mShots;
    
    private long mLastTime;
    public volatile boolean run = true;

    private long gameSpeed;
    
    private SoundManager soundManager;
    private Scaler mScaler;
    private NativeRender renderHandle;
    
    public GameLoop(NativeRender renderHandle){
    	this.renderHandle = renderHandle;
    }
    
    public void run() { 
    	lvlNbr = 0;
	    gameSpeed = 1;
		Log.d("GAMELOOP","INIT GAMELOOP");

    	while(run){
    		initializeLvl();
    		
			// The LEVEL loop. Will run until all creatures are dead or done or player are dead.
        	while(remainingCreatures > 0 && run){

    			//Systemclock. Used to help determine speed of the game. 
				final long time = SystemClock.uptimeMillis();
				
	            // Used to calculate creature movement.
				final long timeDelta = time - mLastTime;
	            final float timeDeltaSeconds = 
	                mLastTime > 0.0f ? timeDelta / 1000.0f : 0.0f;
	            mLastTime = time;
	            
	            //Calls the method that moves the creature.
	            moveCreature(timeDeltaSeconds,time);
	            //Calls the method that handles the monsterkilling.
	            killCreature(timeDeltaSeconds);
	            
	            // Check if the GameLoop are to run the level loop one more time.
	            if (player.health < 1) {
            		//If you have lost all your lives then the game ends.
	            	run = false;
            	}
	        }
    		player.calculateInterest();
    		// Allocates MEMORY, BIG NO
    		//Log.d("GAMELOOP", "Money: " + player.money);


    		// Check if the GameLoop are to run the level loop one more time.
            if (player.health < 1) {
        		//If you have lost all your lives then the game ends.
            	Log.d("GAMETHREAD", "You are dead");
            	run = false;
        	} 
        	else if (remainingCreatures < 1) {
        		//If you have survied the entire wave without dying. Proceed to next next level.
            	Log.d("GAMETHREAD", "Wave complete");
        		lvlNbr++;
        		if (lvlNbr > mLvl.length) {
        			// You have completed this map
                	Log.d("GAMETHREAD", "You have completed this map");
        			run = false;
        		}
        	}
	    }
    	Log.d("GAMETHREAD", "dead thread");
    }

    
	private void initializeLvl() {
    	final long starttime = SystemClock.uptimeMillis();
		
		//The following line contains the code for initiating every level
		/////////////////////////////////////////////////////////////////
		renderHandle.freeSprites();
		//renderHandle.freeAllTextures();

    	remainingCreatures = mLvl[lvlNbr].nbrCreatures;
    	mCreatures = new Creature[remainingCreatures];
    	int reverse = remainingCreatures; 
		
		for (int z = 0; z < mCreatures.length; z++) {
			reverse--;
			// The following line is used to add the following wave of creatures to the list of creatures.
    		mCreatures[z] = new Creature(mLvl[lvlNbr].mResourceId);
			// In some way we have to determine when to spawn the creature. Since we dont want to spawn them all at once.
    		mCreatures[z].x = wayP[0].x;
    		mCreatures[z].y = wayP[0].y;
    		
    		mCreatures[z].health = mLvl[lvlNbr].health;
    		mCreatures[z].nextWayPoint = mLvl[lvlNbr].nextWayPoint;
    		mCreatures[z].velocity = mLvl[lvlNbr].velocity;
    		mCreatures[z].width = mLvl[lvlNbr].width;
    		mCreatures[z].height = mLvl[lvlNbr].height;
    		mCreatures[z].goldValue = mLvl[lvlNbr].goldValue;
    		mCreatures[z].specialAbility = mLvl[lvlNbr].goldValue;
    		
    		mCreatures[z].draw = false;
    		mCreatures[z].opacity = 1;
    		mCreatures[z].spawndelay = (long)(starttime + (reverse * mCreatures[z].velocity * gameSpeed * mCreatures[z].height/4));
		}

		// Sends an array with sprites to the renderer
		renderHandle.setSprites(mGameMap.getBackground(), NativeRender.BACKGROUND);
		renderHandle.setSprites(mCreatures, NativeRender.CREATURE);
		//renderHandle.setSprites(mTower, NativeRender.TOWER);
		//renderHandle.setSprites(mShots, NativeRender.SHOT);
		
		renderHandle.finalizeSprites();

        // Now's a good time to run the GC.  Since we won't do any explicit
        // allocation during the test, the GC should stay dormant and not
        // influence our results.
        Runtime r = Runtime.getRuntime();
        r.gc();
	}

	/**
	 * Will go through all of the creatures from this level and
	 * calculate the movement. 
	 * <p>
	 * This method runs every loop the gameLoop takes. 
	 *
	 * @param  timeDeltaSeconds  	Time since last GameLoop lap 
	 * @param  time	 				Time since this level started
	 * @return      				void
	 */
    public void moveCreature(float timeDeltaSeconds, long time) {
    	// If the list of creatures is empty we will end this method
    	if (mCreatures == null) {
    		return;
    	}
    	for (int x = 0; x < mLvl[lvlNbr].nbrCreatures; x++) {
    		Creature object = mCreatures[x];
    		// Check to see if a not existing creature is supposed to spawn on to the map
			if (time > object.spawndelay && wayP[0].x == object.x && wayP[0].y == object.y) {
				object.draw = true;
			}	            	
			// If the creature is living start movement calculations.
			if (object.draw && object.opacity == 1.0f) {
	    		Coords co = wayP[object.nextWayPoint];
	    		// Creature is moving left.
				if(object.x > co.x){
		    		object.direction = Creature.LEFT;
		    		object.x = object.x - (object.velocity * timeDeltaSeconds * gameSpeed);
		    		if(!(object.x > co.x)){
		    			object.x = co.x;
		    		}
		    	}
	    		// Creature is moving right.
				else if (object.x < co.x) {
		    		object.direction = Creature.RIGHT;
		    		object.x = object.x + (object.velocity * timeDeltaSeconds * gameSpeed);
		    		if(!(object.x < co.x)){
		    			object.x = co.x;
		    		}
		    	}
	    		// Creature is moving down.
				else if(object.y > co.y){
		    		object.direction = Creature.DOWN;
		    		object.y = object.y - (object.velocity * timeDeltaSeconds * gameSpeed);
		    		if(!(object.y > co.y)){
		    			object.y = co.y;
		    		}
		    	}
	    		// Creature is moving up.
		    	else if (object.y < co.y) {
		    		object.direction = Creature.UP;
		    		object.y = object.y + (object.velocity * timeDeltaSeconds * gameSpeed);
		    		if(!(object.y < co.y)){
		    			object.y = co.y;
		    		}
		    	}
				// Creature has reached a WayPoint. Update
		    	if (object.y == co.y && object.x == co.x){
		    		object.updateWayPoint();
		    	}
		    	// Creature has reached is destination without being killed
		    	if (object.nextWayPoint >= wayP.length){
		    		object.draw = false;
		    		player.health --;
		    		remainingCreatures --;
		    	}
		    	
		    	// Creature is dead and fading...
			} else if (object.draw && object.opacity > 0.0f) {
					// If we divide by 10 the creature stays on the screen a while longer...
				object.opacity = object.opacity - (timeDeltaSeconds/10 * gameSpeed);
				if (object.opacity <= 0.0f) {
					object.draw = false;
	    			remainingCreatures --;
				}
			}
    	}
    }

    
	/**
	 * Will go through all of the towews and try to find targets
	 * <p>
	 * This method runs every loop the gameLoop takes. 
	 *
	 * @param  timeDeltaSeconds  	Time since last GameLoop lap 
	 * @return      				void
	 */
    public void killCreature(float timeDeltaSeconds) {
    	// If the list of shots is empty we will end this method
    	if (mTower == null) {
    		return;
    	}
    	//TODO: If we would use mTower.length.. The game will try to check all
    	//towers but since we only use one and don't have enabled buying
    	//we will wait with this loop.
    	for (int x = 0; x < mTower.length; x++) {
    		
    		Tower towerObject = mTower[x];
    		// Decrease the coolDown variable and check if it has reached zero
    		towerObject.tmpCoolDown = towerObject.tmpCoolDown - (timeDeltaSeconds * gameSpeed);
    		if (!towerObject.relatedShot.draw && (towerObject.tmpCoolDown <= 0)) {
    			// If the tower/shot is existing start calculations.
    			towerObject.trackEnemy(mCreatures,mLvl[lvlNbr].nbrCreatures);
    			if (towerObject.targetCreature != null) {
    					// play shot1.mp3
    					soundManager.playSound(0);
    					towerObject.tmpCoolDown = towerObject.coolDown;
    					towerObject.relatedShot.draw = true;
    			}
    		}
    		// if the creature is still alive or have not reached the goal
    		if (towerObject.relatedShot.draw && towerObject.targetCreature.draw && towerObject.targetCreature.opacity == 1.0) {
    			Creature targetCreature = towerObject.targetCreature;

    			float yDistance = (targetCreature.y+(targetCreature.height/2)) - towerObject.relatedShot.y;
    			float xDistance = (targetCreature.x+(targetCreature.width/2)) - towerObject.relatedShot.x;
    			double xyMovement = (towerObject.velocity * timeDeltaSeconds * gameSpeed);
    			
    			if ((Math.abs(yDistance) <= xyMovement) && (Math.abs(xDistance) <= xyMovement)) {
		    		towerObject.relatedShot.draw = false;
		    		towerObject.resetShotCordinates();
		    		//Basic way of implementing damage
		    		targetCreature.health = targetCreature.health - towerObject.createDamage();
		    		if (targetCreature.health <= 0) {
		    			//object.cre.draw = false;
		    			targetCreature.opacity = targetCreature.opacity - 0.1f;
		    			player.money = player.money + targetCreature.goldValue;
		    			Log.d("LOOP","Creature killed");
		    			// play died1.mp3
		    			soundManager.playSound(10);
		    		}
    			}
    			else {
        			double radian = Math.atan2(yDistance, xDistance);
        			towerObject.relatedShot.x += Math.cos(radian) * xyMovement;
        			towerObject.relatedShot.y += Math.sin(radian) * xyMovement;
    			}
			}
    		else {
    			towerObject.relatedShot.draw = false;
	    		towerObject.resetShotCordinates();
    		}
    	}
	}
    
	/**
	 * Will give level information to the GameLoop.
	 * calculate the movement. 
	 * <p>
	 * This method is called before GameLoop is started. 
	 *
	 * @param  lvl			List of type Levels
	 * @return      		void
	 */    
    public void setLevels(Level[] lvl) {
        this.mLvl = lvl;
    }
    
    public void setSoundManager(SoundManager sm) {
    	this.soundManager = sm;
    }
    
    public void setPlayer(Player p) {
    	this.player = p;
    }

    public boolean createTower(Coords TowerPos, int towerType) {
		if (mTTypes.length > towerType && totalNumberOfTowers < mTower.length) {
			if (!mScaler.insideGrid(TowerPos.x,TowerPos.y)) {
				//You are trying to place a tower on a spot outside the grid
				return false;
			}
			
			Coords tmpC = mScaler.getGridXandY(TowerPos.x,TowerPos.y);
			int tmpx = tmpC.x;
			int tmpy = tmpC.y;
			Log.d("Towercreate status:",""+tmpx+","+tmpy);
			Log.d("Towercreate status:",""+TowerPos.x+","+TowerPos.y);
			
			if (mTowerGrid[tmpx][tmpy].empty) {
				mTower[totalNumberOfTowers].cloneTower(mTTypes[towerType]);
				mTower[totalNumberOfTowers].draw = true; //Tower drawable
				Coords tmp = mScaler.getGridPos(TowerPos.x,TowerPos.y);//Tower location
				mTower[totalNumberOfTowers].x = tmp.x;
				mTower[totalNumberOfTowers].y = tmp.y;
				mTower[totalNumberOfTowers].resetShotCordinates();//Same location of Shot as midpoint of Tower
				totalNumberOfTowers++;
				mTowerGrid[tmpx][tmpy].empty = false;
				mTowerGrid[tmpx][tmpy].tower = totalNumberOfTowers;
	    		return true;
			}
		}
		return false;
    }

	public void setTowerTypes(Tower[] tTypes) {
		this.mTTypes = tTypes;
	}

	public void setMap(Map gameMap) {
		this.mGameMap = gameMap;
		this.wayP = gameMap.getWaypoints().getCoords();
   		this.mTowerGrid = gameMap.getTowerGrid();
   		this.mScaler = gameMap.getScaler();
	}
}