package com.crackedcarrot;

import java.util.Random;

import android.util.Log;

import com.crackedcarrot.menu.R;

/**
* Class defining a tower in the game
*/
public class Tower extends Sprite {
	private SoundManager soundManager;
	private Creature[] mCreatures;
	//different towertypes
	public final static int CANNON = 1;
	public final static int AOE = 2;
	public final static int BUNKER = 3;
	public final static int TELSA = 4;
	
	public enum UpgradeOption{upgrade_a, upgrade_b, upgrade_c};
	
	//towertype
	public int towerType;
	// The current range of a tower
	private float range;
	// The current AOE range of a tower
	private float rangeAOE;
	// Price for the tower
	private int price;
	// Resell value if the tower is sold
	private int resellPrice;
	// Minimum damage that this tower can inflict
	private int minDamage;
	// Maximum damage that this tower can inflict
	private int maxDamage;
	// AOE damage
	private int aoeDamage;
	// Speed of the shots
	private int velocity;
	// If the tower have frost damage
	private boolean hasFrostDamage;
	// If the tower have frost damage. How long?
	private int frostTime;
	// If the tower have frost damage. How much will it affect?
	private float frostAmount;
	// If the tower have fire damage
	private boolean hasFireDamage;
	// If the tower have firedamage how mutch
	private float fireFactor;
	// If the tower have poison damage
	private boolean hasPoisonDamage;
	// Factor of posiondamage.
	private int poisonFactor;
	// The linked update for this tower
	private int upgrade1;
	// The first special ability for this tower 
	private int upgrade2;
	// The second special abilty for this tower
	private int upgrade3;
	// The type of shot related to this tower
	public Shot relatedShot;
    // The time existing between each fired shot
    private float coolDown;
    // The temporary variable representing the time existing between each fired shot
    private float tmpCoolDown;
    // The current target creature
    private Creature targetCreature;
	// Random used to calculate damage
    private Random rand;
   // used by resume to uniquely identify this tower-type.
    private int towerTypeId;
    // Used to determine if the tower should animate impact
    private boolean ImpactdAnimate = false;
    //To determine when a creature has been teleported to spawnpoint we will check his nbr of laps
	private int targetCreatureMapLap;
	// All creatures avaible for this tower
	private TrackerList startTrackerList;
	//Sound. leaves
	private int sound_l;
	//Sound. impact
	private int sound_i;
    
    /**
     * Constructor used when defining a new tower in the game. Needs the texture resource,
     * subtype, list of creatures and soundmanager.
     * @param resourceId
     * @param type
     * @param mCreatures
     * @param soundManager
     */
	public Tower(int resourceId, int type, Creature[] mCreatures, SoundManager soundManager){
		super(resourceId, TOWER, type);
		this.soundManager = soundManager;
		this.mCreatures = mCreatures;
		rand = new Random();
	}
	
	public void initTower(int resourceId, int type, Creature[] mCreatures, SoundManager soundManager){
		this.setResourceId(resourceId);
		this.setType(TOWER, type);
		this.soundManager = soundManager;
		this.mCreatures = mCreatures;
		rand = new Random();
	}
	
	/**
	 * Calculates special damage. Used by all towers that have frost,posion or fire damage
     * @param tmpCreature
     * @param aoeTower
 	 * @return damage factor
	 */
	private float specialDamage(Creature tmpCreature, boolean aoeTower) {
		//If tower has frostdamage
		if (this.hasFrostDamage) {
			if (aoeTower)
				tmpCreature.affectWithFrost(this.frostTime/2,this.frostAmount);
			else
				tmpCreature.affectWithFrost(this.frostTime,this.frostAmount);
		}		
		// If tower has poison damage
		if (this.hasPoisonDamage) {
			if (this.isPoisonTower())
				tmpCreature.affectWithPoison(4,this.minDamage);
			if (this.isPoisonTower())
				tmpCreature.affectWithPoison(4,this.minDamage*this.poisonFactor);
		}				
		// If tower has firedamage
		if (this.hasFireDamage && !tmpCreature.creatureFireResistant)
			return this.fireFactor;
		else return 1;
	}
	
	//Old tracker
	/*
	private Creature trackNearestEnemy(int nbrCreatures) {
		Creature targetCreature = null;
		double lastCreatureDistance = Double.MAX_VALUE;
		
		for(int i = 0;i < nbrCreatures; i++ ){
			if(mCreatures[i].draw == true && mCreatures[i].health > 0){ // Is the creature still alive?
				double distance = Math.sqrt(
									Math.pow((this.relatedShot.x - (mCreatures[i].getScaledX())) , 2) + 
									Math.pow((this.relatedShot.y - (mCreatures[i].getScaledY())) , 2)  );
				if(distance < range){ // Is the creature within tower range?
					if (targetCreature == null) 
						targetCreature = mCreatures[i];
					else if (lastCreatureDistance > distance) {
						targetCreature = mCreatures[i];
						lastCreatureDistance = distance;
					}
				}
			}
		}
		return targetCreature;
	}
	*/
	/**
	 * Method that tracks a creature. It iterates over a list of creatures and picks
	 * the first creature in the list that is within the range of the tower 
	 * @param nbrCreatures
	 * @return nearest creature
	 */
	private Creature trackNearestEnemy() {
		Creature targetCreature = null;
		double lastCreatureDistance = Double.MAX_VALUE;
		
		TrackerList tmpList = startTrackerList;
		while(tmpList != null) {
			TrackerData tmpData = tmpList.data;
			if (tmpData != null) {
				Creature tmpCreature = tmpData.first;
				while ((tmpCreature != tmpData.last) && tmpCreature != null) {
			
					if(tmpCreature.draw == true && tmpCreature.health > 0){ // Is the creature still alive?
						double distance = Math.sqrt(
									Math.pow((this.relatedShot.x - (tmpCreature.getScaledX())) , 2) + 
									Math.pow((this.relatedShot.y - (tmpCreature.getScaledY())) , 2)  );
						if(distance < range){ // Is the creature within tower range?
							if (targetCreature == null) 
								targetCreature = tmpCreature;
							else if (lastCreatureDistance > distance) {
								targetCreature = tmpCreature;
								lastCreatureDistance = distance;
							}
						}
					}
					tmpCreature = tmpCreature.nextCreature;
				}
			}
			tmpList = tmpList.next;
		}
		return targetCreature;
	}
	
	/**
	 * Method that tracks all creatures that are in range of tower. It iterates over a list of creatures and 
	 * picks all creatures in range.
	 * @param nbrCreatures
	 * @param doFullDamage
	 */
	private int trackAllNearbyEnemies(boolean doFullDamage) {
		int nbrOfHits = 0;
		float range;
		if (doFullDamage)
			range = this.range;
		else 
			range = this.rangeAOE;

		TrackerList tmpList = startTrackerList;
		while(tmpList != null) {
			TrackerData tmpData = tmpList.data;
			if (tmpData != null) {
				Creature tmpCreature = tmpData.first;
				while ((tmpCreature != tmpData.last) && tmpCreature != null) {
					if(tmpCreature.draw == true && tmpCreature.health > 0){ // Is the creature still alive?
						double distance = Math.sqrt(
								Math.pow((this.relatedShot.x - (tmpCreature.getScaledX())) , 2) + 
								Math.pow((this.relatedShot.y - (tmpCreature.getScaledY())) , 2)  );
						if(distance <= range){ 
							float randomInt;
							if (doFullDamage) {
								float damageFactor = specialDamage(tmpCreature,false);
								randomInt = (rand.nextInt(this.maxDamage-this.minDamage) + this.minDamage) * damageFactor;
							} else {
								float damageFactor = specialDamage(tmpCreature,true);
								randomInt = this.aoeDamage * damageFactor;
							}
						tmpCreature.damage(randomInt,-1);
						nbrOfHits++;
						}
					}
					tmpCreature = tmpCreature.nextCreature;
				}
			}
			tmpList = tmpList.next;
		}
		return nbrOfHits;
	}
	
	// Old tracker
	/*private int trackAllNearbyEnemies(int nbrCreatures, boolean doFullDamage) {
		int nbrOfHits = 0;
		float range;
		if (doFullDamage)
			range = this.range;
		else 
			range = this.rangeAOE;
		
		for(int i = 0;i < nbrCreatures; i++ ){
			if(mCreatures[i].draw == true && mCreatures[i].health > 0){ // Is the creature still alive?
				double distance = Math.sqrt(
						Math.pow((this.relatedShot.x - (mCreatures[i].getScaledX())) , 2) + 
						Math.pow((this.relatedShot.y - (mCreatures[i].getScaledY())) , 2)  );
				if(distance <= range){ 
					float randomInt;
					if (doFullDamage) {
						float damageFactor = specialDamage(mCreatures[i],false);
						randomInt = (rand.nextInt(this.maxDamage-this.minDamage) + this.minDamage) * damageFactor;
					} else {
						float damageFactor = specialDamage(mCreatures[i],true);
						randomInt = this.aoeDamage * damageFactor;
					}
					mCreatures[i].damage(randomInt,-1);
					nbrOfHits++;
				}
			}
		}
		return nbrOfHits;
	}*/
	
	
	/**
	 * Method that calculates the damage for a specific tower
	 * depending on the upgrade level and a random integer
	 * so the damage wont be predictable during game play
	 * @param timeDeltaSeconds
	 * @param nbrCreatures
	 */
	private void createProjectileDamage(float timeDeltaSeconds, int nbrCreatures){
		//First we have to check if the tower is ready to fire
		if (!this.relatedShot.draw && (this.tmpCoolDown <= 0)) {
			// This is happens when a tower with projectile damage is ready to fire.
			this.targetCreature = trackNearestEnemy();
			towerStartFireSequence(this.targetCreature);
		}
		//Creature has been teleported away. stop firing
		else if (this.relatedShot.draw && targetCreatureMapLap != targetCreature.mapLap) {
			this.tmpCoolDown = this.coolDown;			
			this.relatedShot.draw = false;
			this.relatedShot.resetShotCordinates();			
		}
		else if (this.relatedShot.draw && ImpactdAnimate) {
			float size = this.relatedShot.getWidth()/2;
			if (this.towerType == Tower.CANNON)
				size = this.rangeAOE;
			if (this.towerType == Tower.TELSA)
				size = this.targetCreature.getWidth()/2*this.targetCreature.scale;
			
			ImpactdAnimate = relatedShot.animateShot(timeDeltaSeconds, size, targetCreature);
		}
		// if the tower is currently in use:
		else if (this.relatedShot.draw) {
			updateProjectile(timeDeltaSeconds,nbrCreatures);
		}
	}

	/**
	 * Method that updates the shot for the current tower
	 * @param timeDeltaSeconds
	 * @param nbrCreatures
	 */
	private void updateProjectile(float timeDeltaSeconds, int nbrCreatures) {

		float yDistance = targetCreature.getScaledY() - this.relatedShot.y-(this.relatedShot.getHeight()/2);
		float xDistance = targetCreature.getScaledX() - this.relatedShot.x-(this.relatedShot.getWidth()/2);
		double xyMovement = (this.velocity * timeDeltaSeconds);
		
		if (this.towerType == Tower.TELSA || this.towerType == Tower.BUNKER) {
			relatedShot.animateMovingShot(timeDeltaSeconds);
		}
		
		// This will only happen if we have reached our destination creature
		if ((Math.abs(yDistance) <= xyMovement) && (Math.abs(xDistance) <= xyMovement)) 
			projectileHitsTarget(nbrCreatures);
		else {
			// Travel until we reach target
			double radian = Math.atan2(yDistance, xDistance);
			this.relatedShot.x += Math.cos(radian) * xyMovement;
			this.relatedShot.y += Math.sin(radian) * xyMovement;
		}
	}

	/**
	 * This runs when a tower hits a creature
	 * @param nbrCreatures
	 */
	private void projectileHitsTarget(int nbrCreatures) {
		this.tmpCoolDown = this.coolDown;
		float damageFactor = specialDamage(this.targetCreature,false);
		float randomInt = (rand.nextInt(this.maxDamage-this.minDamage) + this.minDamage) * damageFactor;
		targetCreature.damage(randomInt,sound_i);
		
		this.ImpactdAnimate = true;
		relatedShot.tmpAnimationTime = relatedShot.animationTime;
		
    	if (this.towerType == Tower.CANNON){
	    	this.trackAllNearbyEnemies(false);
		}
	}
	
	/**
	 * Method that calculates AOE damage for a specific tower
	 * depending on the upgrade level and a random integer
	 * so the damage wont be predictable during game play
	 * This method is only used by towers with direct aoe damage.
	 * Not to be confused with towers that have projectiledamage
	 */
	private void createPureAOEDamage(int nbrCreatures, float timeDeltaSeconds){
		if (ImpactdAnimate) {
			float size = this.range;
			ImpactdAnimate = relatedShot.animateShot(timeDeltaSeconds, size, null);
		}
		else if (this.tmpCoolDown <= 0) {
			if (trackAllNearbyEnemies(true) > 0) {
				soundManager.playSound(this.sound_l);
				this.tmpCoolDown = this.coolDown;
				this.relatedShot.draw = true;
				ImpactdAnimate = true;
			}
		}
	}	
	

	/**
	 * Main function of the tower class. Find and kill creatures
	 * @param timeDeltaSeconds
	 * @param nbrCreatures
	 */
	public void attackCreatures(float timeDeltaSeconds, int nbrCreatures) {
		// Decrease the coolDown variable
		this.tmpCoolDown = this.tmpCoolDown - timeDeltaSeconds;

		// This code is used by towers firing pure aoe damage.
		if (this.towerType == Tower.AOE) {
			createPureAOEDamage(nbrCreatures,timeDeltaSeconds);
		}
		// If bunker, cannon or tesla
		else {
			createProjectileDamage(timeDeltaSeconds, nbrCreatures);
		}
	}
	
	/**
	 * Init fire sequence. Show projectile and play sound
	 * @param targetCreature
	 */
	private void towerStartFireSequence(Creature targetCreature) {
		if (targetCreature != null) {
			if (sound_l != -1)
				soundManager.playSound(sound_l);
			this.relatedShot.draw = true;
			this.targetCreatureMapLap = targetCreature.mapLap;
		}
	}
	
	/**
	 * Given all variable this method will create a exaxt copy of
	 * another tower
	 */
	public void cloneTower(	
				int resourceId,
				int towerType,
				int towerTypeId,
				float range,
				float rangeAOE,
				int price,
				int resellPrice,
				int minDamage,
				int maxDamage,
				int aoeDamage,
				int velocity,
				int upgrade1,
				float coolDown,
				float width,
				float height,
				Shot copyShot,
				int sound_l,
				int sound_i
			){

			this.setResourceId(resourceId);
			this.towerType = towerType;
			this.towerTypeId = towerTypeId;
			this.range = range;
			this.rangeAOE = rangeAOE;
			this.price = price;
			this.resellPrice = resellPrice;
			this.minDamage = minDamage;
			this.maxDamage = maxDamage;
			this.aoeDamage = aoeDamage;
			this.velocity = velocity;
			this.upgrade1 = upgrade1;
			this.coolDown = coolDown;
			this.setWidth(width);
			this.setHeight(height);
			this.relatedShot.setWidth(copyShot.getWidth());
			this.relatedShot.setHeight(copyShot.getHeight());
			this.relatedShot.setResourceId(copyShot.getResourceId());
			this.relatedShot.setAnimationTime(copyShot.getAnimationTime());
			this.sound_l = sound_l;
			this.sound_i = sound_i;
	}
	
	/**
	 * Create new tower and place on map
	 * @param clone
	 * @param towerPlacement
	 * @param mScaler
	 */
	public void createTower(Tower clone, Coords towerPlacement, Scaler mScaler, Tracker tracker) {
		this.draw = false;

		//Use the textureNames that we preloaded into the towerTypes at startup
		//If this is a new created tower we have to manually reset the folowing values
		this.towerTypeId = clone.towerTypeId;
		
		if (this.towerTypeId <= 3)  {
			this.towerType = clone.towerType;
			this.relatedShot.setResourceId(clone.relatedShot.getResourceId());
			this.sound_l = clone.sound_l;
			this.sound_i = clone.sound_i;
			this.x = towerPlacement.x;
			this.y = towerPlacement.y;
		}
		this.setResourceId(clone.getResourceId());
		this.range = clone.range;
		this.rangeAOE = clone.rangeAOE;
		this.price = clone.price;
		this.resellPrice = clone.resellPrice;
		this.minDamage = clone.minDamage;
		this.maxDamage = clone.maxDamage;
		this.aoeDamage = clone.aoeDamage;
		this.velocity = clone.velocity;
		this.upgrade1 = clone.upgrade1;
		this.coolDown = clone.coolDown;
		this.relatedShot.setAnimationTime(clone.relatedShot.getAnimationTime());
		
		// Special abilities. If this is a new created tower we have to manually reset the folowing values
		if (this.towerTypeId <= 3)  {

			if (this.towerType != Tower.AOE) {
				this.hasPoisonDamage =false;
			}
			else {
				this.hasPoisonDamage =true;
			}
			this.hasFrostDamage = false;
			this.frostAmount = 0;
			this.frostTime = 0;
			this.hasFireDamage = false;
			this.fireFactor = 0;
			this.upgrade2 = 0;
			this.upgrade3 = 0;
			this.r = 1;
			this.g = 1;
			this.b = 1;
			this.relatedShot.r = 1;
			this.relatedShot.g = 1;
			this.relatedShot.b = 1;
		}
		
		// Tracker
		int rangeGrid = mScaler.rangeGrid((int)(this.range+this.rangeAOE));
		Coords tmp = mScaler.getGridXandY((int)x, (int)y);
		int right = tmp.x + rangeGrid;
		if (right >= (mScaler.getGridWidth() + 2))
			right = mScaler.getGridWidth()+1;
		int left = tmp.x - rangeGrid;
		if (left < 0)
			left = 0;
		int top = tmp.y + rangeGrid;
		if (top >= (mScaler.getGridHeight() + 2))
			top = mScaler.getGridHeight()+1;
		int bottom = tmp.y -rangeGrid;
		if (bottom < 0)
			bottom = 0;

		startTrackerList = new TrackerList();
		TrackerList currentTrackerList = new TrackerList();
		startTrackerList.next = currentTrackerList;

		for (int x = left; x <= right; x++) {
			for (int y = bottom; y <= top; y++) {
				currentTrackerList.data = (tracker.getTrackerData(x, y));
				currentTrackerList.next = new TrackerList();
				currentTrackerList = currentTrackerList.next;
			}
		}
		
		this.draw = true;
		this.relatedShot.resetShotCordinates();//Same location of Shot as midpoint of Tower
		this.relatedShot.draw = false;

	}
	
	public int upgradeSpecialAbility(UpgradeOption opt, int money) {
		int price = 0;
		
		if(opt == UpgradeOption.upgrade_c && this.upgrade3 == 0) {
			if (this.upgrade2 == 0 && money >= 30) {
				this.fireFactor = 3;
				this.hasFireDamage = true;
				this.r = 1;
				this.g = 0.7f;
				this.b = 0.7f;
				this.relatedShot.r = 1;
				this.relatedShot.g = 0.7f;
				this.relatedShot.b = 0.7f;
				this.upgrade2++;
				price = 20;
			}
			else if (this.upgrade2 == 1 && money >= 50) {
				this.fireFactor = 6;
				this.upgrade2++;
				price = 50;
			}
			else if (this.upgrade2 == 2 && money >= 80) {
				this.fireFactor = 9;
				this.upgrade2++;
				price = 100;
			}
		}
		else if(opt == UpgradeOption.upgrade_b && this.upgrade2 == 0) {
			if (this.towerType == Tower.CANNON) {
				if (this.upgrade3 == 0 && money >= 30) {
					this.relatedShot.setResourceId(R.drawable.cannonshot_ice);
					this.frostTime = 3;
					this.frostAmount = 0.6f;
					this.hasFrostDamage = true;
					this.r = 0.7f;
					this.g = 0.7f;
					this.b = 1;
					this.upgrade3++;
					price = 20;
				}
				else if (this.upgrade3 == 1 && money >= 50) {
					this.frostTime = 4;
					this.frostAmount = 0.4f;
					this.upgrade3++;
					price = 50;
				}
				else if (this.upgrade2 == 2 && money >= 80) {
					this.frostTime = 5;
					this.frostAmount = 0.2f;
					this.upgrade3++;
					price = 100;
				}
			}
			if (this.towerType == Tower.BUNKER) {
				if (this.upgrade3 == 0 && money >= 30) {
					this.hasPoisonDamage = true;
					this.poisonFactor = 1;
					this.r = 0.7f;
					this.g = 1;
					this.b = 0.7f;
					this.relatedShot.r = 0.7f;
					this.relatedShot.g = 1;
					this.relatedShot.b = 0.7f;
					this.upgrade3++;
					price = 20;
				}
				else if (this.upgrade3 == 1 && money >= 50) {
					this.poisonFactor = 2;
					this.upgrade3++;
					price = 50;
				}
				else if (this.upgrade3 == 2 && money >= 80) {
					this.poisonFactor = 3;
					this.upgrade3++;
					price = 100;
				}
			}
		}
		return price;
	}

	//////////////////////////////////////////////
	// Getter for tower
	//////////////////////////////////////////////
	
	/**
	 * Given a tower this method will create a new tower with the same
	 * variables as the given one
	 * @return
	 */
	public int getTowerTypeId() { return towerTypeId; }
	
	/**
	 * Return range of this tower
	 * @return
	 */
	public float getRange() { return this.range; }
	
	/**
	 * Return minimum damage
	 * @return
	 */
	public float getMinDamage() { return this.minDamage; }
	
	/**
	 * Return maximum damage
	 * @return
	 */
	public float getMaxDamage() { return this.maxDamage; }
	

	/**
	 * Returns the cost of this tower
	 * @return price
	 */	
	public int getPrice() { return price; }

	public int getResellPrice() {
		return this.resellPrice;
	}
	
	public int getUpgradeTypeIndex(UpgradeOption opt) {
		if(opt == UpgradeOption.upgrade_a)
			return upgrade1;
		else
			return upgrade2;
	}
	public boolean isPoisonTower(){
		return this.hasPoisonDamage;
	}
	public boolean isFrostTower(){
		return this.hasFrostDamage;
	}
	public boolean isFireTower(){
		return this.hasFireDamage;
	}

}