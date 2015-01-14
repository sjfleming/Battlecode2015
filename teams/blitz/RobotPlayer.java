package blitz;

import bassplayer.RobotPlayer.SquadState;
import battlecode.common.*;

import java.util.*;

public class RobotPlayer {
	
	static RobotController rc;
	
	// Game-given values
	static Team myTeam;
	static Team enemyTeam;
	static RobotType myType;
	static int myRange;
	
	// Our assigned values n stuff
	static int mySquad;
	static MapLocation squadTarget;
	
	static MapLocation myLocation;
	static Direction facing;
	
	// standard defines
	static MapLocation center;
	
	static Random rand;
	static Direction[] directions = {Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST, Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};
	static int myRounds = 0;
	
	
	
	static final int MAX_SQUADS = 16;
	
	/* Squad task defines */
	static final int squadTaskBase = 0;
	
	/* Squad target defines */
	static final int squadTargetBase = squadTaskBase + MAX_SQUADS;
	
	/* Squad unit counts */
	static final int squadUnitsBase = squadTargetBase + MAX_SQUADS;
	
	/* Next squad channel */
	static final int nextSquadChan = squadUnitsBase + MAX_SQUADS;
	
	
	static int bestMineScoreChan = nextSquadChan + 1;
	static int bestMineXChan = bestMineScoreChan + 1;
	static int bestMineYChan = bestMineXChan + 1;

	// Adjustable parameters
	static int numBeavers = 8;
	static int numMinerFactories = 3;
	static int numMiners = 80;
	static int numBarracks = 1;
	static int numSoldiers = 0;
	static int numHelipads = 10;
	static int numSupplyDepots = 3;
	static int numTankFactories = 1;
	static int numTanks = 14;
	
	
	/* Sensing location defines etc */
	static int senseLocsShort = 69;
	static int senseLocsLong = 109;
	static int[] senseLocsX = {0,-1,0,0,1,-1,-1,1,1,-2,0,0,2,-2,-2,-1,-1,1,1,2,2,-2,-2,2,2,-3,0,0,3,-3,-3,-1,-1,1,1,3,3,-3,-3,-2,-2,2,2,3,3,-4,0,0,4,-4,-4,-1,-1,1,1,4,4,-3,-3,3,3,-4,-4,-2,-2,2,2,4,4,-5,-4,-4,-3,-3,0,0,3,3,4,4,5,-5,-5,-1,-1,1,1,5,5,-5,-5,-2,-2,2,2,5,5,-4,-4,4,4,-5,-5,-3,-3,3,3,5,5};
	static int[] senseLocsY = {0,0,-1,1,0,-1,1,-1,1,0,-2,2,0,-1,1,-2,2,-2,2,-1,1,-2,2,-2,2,0,-3,3,0,-1,1,-3,3,-3,3,-1,1,-2,2,-3,3,-3,3,-2,2,0,-4,4,0,-1,1,-4,4,-4,4,-1,1,-3,3,-3,3,-2,2,-4,4,-4,4,-2,2,0,-3,3,-4,4,-5,5,-4,4,-3,3,0,-1,1,-5,5,-5,5,-1,1,-2,2,-5,5,-5,5,-2,2,-4,4,-4,4,-3,3,-5,5,-5,5,-3,3};
	static int[] senseLocsR = {0,10,10,10,10,14,14,14,14,20,20,20,20,22,22,22,22,22,22,22,22,28,28,28,28,30,30,30,30,31,31,31,31,31,31,31,31,36,36,36,36,36,36,36,36,40,40,40,40,41,41,41,41,41,41,41,41,42,42,42,42,44,44,44,44,44,44,44,44,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,53,53,53,53,53,53,53,53,56,56,56,56,58,58,58,58,58,58,58,58};
	static float[] sqrt = {0.000000f,1.000000f,1.414214f,1.732051f,2.000000f,2.236068f,2.449490f,2.645751f,2.828427f,3.000000f,3.162278f,3.316625f,3.464102f,3.605551f,3.741657f,3.872983f,4.000000f,4.123106f,4.242641f,4.358899f,4.472136f,4.582576f,4.690416f,4.795832f,4.898979f,5.000000f,5.099020f,5.196152f,5.291503f,5.385165f,5.477226f,5.567764f,5.656854f,5.744563f,5.830952f,5.916080f,6.000000f,6.082763f,6.164414f,6.244998f,6.324555f,6.403124f,6.480741f,6.557439f,6.633250f,6.708204f,6.782330f,6.855655f,6.928203f,7.000000f,7.071068f,7.141428f,7.211103f,7.280110f,7.348469f,7.416198f,7.483315f,7.549834f,7.615773f,7.681146f,7.745967f,7.810250f,7.874008f,7.937254f,8.000000f,8.062258f,8.124038f,8.185353f,8.246211f,8.306624f,8.366600f,8.426150f,8.485281f,8.544004f,8.602325f,8.660254f,8.717798f,8.774964f,8.831761f,8.888194f,8.944272f,9.000000f};
	static float[] invSqrt = {0.000000f,1.000000f,0.707107f,0.577350f,0.500000f,0.447214f,0.408248f,0.377964f,0.353553f,0.333333f,0.316228f,0.301511f,0.288675f,0.277350f,0.267261f,0.258199f,0.250000f,0.242536f,0.235702f,0.229416f,0.223607f,0.218218f,0.213201f,0.208514f,0.204124f,0.200000f,0.196116f,0.192450f,0.188982f,0.185695f,0.182574f,0.179605f,0.176777f,0.174078f,0.171499f,0.169031f,0.166667f,0.164399f,0.162221f,0.160128f,0.158114f,0.156174f,0.154303f,0.152499f,0.150756f,0.149071f,0.147442f,0.145865f,0.144338f,0.142857f,0.141421f,0.140028f,0.138675f,0.137361f,0.136083f,0.134840f,0.133631f,0.132453f,0.131306f,0.130189f,0.129099f,0.128037f,0.127000f,0.125988f,0.125000f,0.124035f,0.123091f,0.122169f,0.121268f,0.120386f,0.119523f,0.118678f,0.117851f,0.117041f,0.116248f,0.115470f,0.114708f,0.113961f,0.113228f,0.112509f,0.111803f,0.111111f};
	
	
	// HQ-specific
	static enum SquadState
	{
		RALLY,		// fewer than SQUAD_NUM units, defensive
		ATTACK,		// target a thing and kill it
		HARASS		// move around the back of the map and pick off targets
	}
	static MapLocation[] squadTargets = new MapLocation[MAX_SQUADS];
	static SquadState[] squadStates = new SquadState[MAX_SQUADS];
	static int[] squadCounts = new int[MAX_SQUADS];
	static int lastTowers = 0;
	
	static MapLocation[] enemyBuildings;
	static double[] enemyHP;
	
	static final int SQUAD_UNITS = 16;
	static final int HARASS_UNITS = 4;
	static final int HARASS_SQUADS = 2;

	static double lastOre = 0;
	static double curOre = 0;
	
	
	static class MapValue implements Comparable<MapValue>
	{
		int x;
		int y;
		double value;
		
		public MapValue(int lx, int ly, double val)
		{
			this.x = lx;
			this.y = ly;
			this.value = val;
		}
		
		public int compareTo(MapValue mv)
		{
			return Double.compare(this.value, mv.value);
		}
		
		public MapLocation offsetFrom(MapLocation ml)
		{
			return new MapLocation(x+ml.x,y+ml.y);
		}
	}
	

	public static void run(RobotController robotc)
	{
		rc = robotc;
		rand = new Random();
		myRange = rc.getType().attackRadiusSquared;
		myTeam = rc.getTeam();
		enemyTeam = myTeam.opponent();
		myType = rc.getType();
		
		// calculate center, assuming rotational symmetry for now
		MapLocation myBase = rc.senseHQLocation();
		MapLocation enBase = rc.senseEnemyHQLocation();
		
		center = new MapLocation((myBase.x+enBase.x)/2,(myBase.y+enBase.y)/2);
		
		facing = getRandomDirection();
		
		lastOre = rc.getTeamOre();
		curOre = lastOre;
		
		
		// Initialization code
		try {
			switch (myType)
			{
			case HQ:
				break;
			case BEAVER:
				facing = rc.getLocation().directionTo(myBase).opposite();
				break;
			case MINER:
				break;
			case DRONE:
				// get the next squad, as assigned by the HQ
				int nextSquad = rc.readBroadcast(nextSquadChan);
				// save the lowest byte
				mySquad = nextSquad & 255;
				// and write it back
				//rc.broadcast(nextSquadChan, nextSquad >> 8);
				break;
			}
		} catch (Exception e) {
			System.out.println("Initialization exception");
			e.printStackTrace();
		}
		
		while(true) {
			curOre = rc.getTeamOre();
			myLocation = rc.getLocation();
			
			switch (myType)
			{
			case HQ:
				doHQ();
				break;
			case TOWER:
				doTower();
				break;
			case HELIPAD:
				doHelipad();
				break;
			case BARRACKS:
				doBarracks();
				break;
			case MINERFACTORY:
				doMinerFactory();
				break;
			case MINER:
				doMiner();
				break;
			case DRONE:
				doDrone();
				break;
			case BASHER:
				doBasher();
				break;
			case SOLDIER:
				doSoldier();
				break;
			case BEAVER:
				doBeaver();
				break;
			case TANKFACTORY:
				doTankFactory();
				break;
			case TANK:
				doTank();
				break;
			}
			
			lastOre = curOre;
			
			if (Clock.getBytecodesLeft() < 600)
				rc.yield();
			
			if(myType!=RobotType.MINERFACTORY && myType!=RobotType.TANKFACTORY){
				try {
					transferSupplies();
				} catch (Exception e) {
					System.out.println("Supply exception");
				}
			}
			rc.yield();
		}
	}
	
	
	
	static void doHQ()
	{
		try 
		{
			rc.setIndicatorString(1,"Current ore: " + curOre);
			if (rc.isWeaponReady())
			{
				attackSomething();
			}

			int nextSquad = -1;
			// the number we fill squads to
			int squadMax = 12;
			for (int i=0; i<MAX_SQUADS; i++)
			{
				int squadcount = rc.readBroadcast(squadUnitsBase + i);
				if (squadcount >> 16 == Clock.getRoundNum() - 1)
				{
					squadcount = squadcount & 255;
					squadCounts[i] = squadcount;
					// refill squad with fewest nonzero units
					if (squadcount < squadMax && nextSquad == -1)
						nextSquad = i;
				}
				else
				{
					// no units reporting, all are dead
					squadCounts[i] = 0;
					if (nextSquad == -1)
						nextSquad = i;
				}
				//System.out.println("SquadCounts[" + i + "]:" + squadCounts[i]);
			}
			//System.out.println("NextSquad:" + nextSquad);
			if (nextSquad == -1)
				nextSquad = 0;
			rc.broadcast(nextSquadChan, nextSquad);
			
			// set the first squad's target to enemy towers
			MapLocation[] towers = rc.senseEnemyTowerLocations();
			MapLocation[] mytowers = rc.senseTowerLocations();
			MapLocation hq = rc.senseEnemyHQLocation();
			MapLocation myhq = rc.senseHQLocation();
			
			if (towers.length != lastTowers)
			{
				MapValue[] mvs = new MapValue[towers.length+mytowers.length];
				
				for (int i=0; i<towers.length; i++)
				{
					mvs[i] = new MapValue(towers[i].x-center.x,towers[i].y-center.y,towers[i].distanceSquaredTo(myhq));
				}
				for (int i=0; i<mytowers.length; i++)
				{
					mvs[i+towers.length] = new MapValue(mytowers[i].x-center.x,mytowers[i].y-center.y,mytowers[i].distanceSquaredTo(hq));
				}
				
				Arrays.sort(mvs);
				
				for (int i=0; i<towers.length+mytowers.length; i++)
				{
					int loc = (mvs[i].y << 16) + (mvs[i].x & 65535);
					rc.broadcast(squadTargetBase + i, loc);
				}				
				for (int i=towers.length+mytowers.length; i<MAX_SQUADS; i++)
				{					
					int loc = ((hq.y - center.y) << 16) + ((hq.x - center.x) & 65535);
					rc.broadcast(squadTargetBase + i, loc);
				}
			}
			
			int st = rc.readBroadcast(squadTargetBase);
			
			RobotInfo[] ourTeam = rc.senseNearbyRobots(1000, rc.getTeam());
			int n = 0; // current number of beavers
			for(RobotInfo ri: ourTeam){ // count up beavers
				if(ri.type==RobotType.BEAVER){
					n++;
				}
			}
			// beaver early game: spawn three before round 30 then wait until round 30 to make more
			if(n<numBeavers && (n!=4 && Clock.getRoundNum()<30) || n<numBeavers && Clock.getRoundNum()>=30)
			{
				if(rc.isCoreReady()) {
					trySpawn(myLocation.directionTo(rc.senseEnemyHQLocation()), RobotType.BEAVER);
				}
			}
			
		} catch (Exception e) {
			System.out.println("HQ Exception");
			e.printStackTrace();
		}
	}
	
	static void doTower()
	{
		try {
			if (rc.isWeaponReady())
			{
				attackSomething();
			}
		} catch (Exception e) {
			System.out.println("Tower Exception");
			e.printStackTrace();
		}
	}
	
	static void doHelipad()
	{
		try {
			if (rand.nextInt(100) < 20)
				spawnUnit(RobotType.DRONE);
		} catch (Exception e) {
			System.out.println("Helipad Exception");
			e.printStackTrace();
		}
	}
	
	static void doMinerFactory()
	{
		try {
			RobotInfo[] bots = rc.senseNearbyRobots(99999, myTeam);
			int nmine = 0;
			for (RobotInfo b: bots)
				if (b.type == RobotType.MINER)
					nmine++;
			
			if (curOre - lastOre < 25 && nmine < numMiners)
			{
				spawnUnit(RobotType.MINER);
			}
		} catch (Exception e) {
			System.out.println("Miner Factory Exception");
			e.printStackTrace();
		}
	}
	
	static void doTankFactory()
	{
		try {
			RobotInfo[] bots = rc.senseNearbyRobots(99999, myTeam);
			int tanks = 0;
			for (RobotInfo b: bots)
			{
				if (b.type == RobotType.TANK)
				{
					tanks++;
				}
			}
			if (tanks < numTanks)
			{
				trySpawn(facing,RobotType.TANK);
			}
		} catch (Exception e) {
			System.out.println("Tank Factory Exception");
			e.printStackTrace();
		}
	}

	
	static void doBarracks()
	{
		try {
			
		} catch (Exception e) {
			System.out.println("Barracks Exception");
			e.printStackTrace();
		}
	}

	
	static void doDrone()
	{
		try {
			updateSquadInfo();
            /*Direction toDest = rc.getLocation().directionTo(squadTarget);
        	Direction[] dirs = {toDest,
		    		toDest.rotateLeft(), toDest.rotateRight(),toDest.rotateLeft().rotateLeft(), toDest.rotateRight().rotateRight()};
        	for (Direction d : dirs) {
                if (rc.canMove(d) && rc.isCoreReady()) {
                    rc.move(d);
                }
            }*/
			attackSomething();
	        
			//potentialAct(squadTarget,RobotType.DRONE);
			//attackSomething();
			calcPotential();
			rc.setIndicatorString(0, "Drone: squad " + mySquad + ", target " + squadTarget);
		} catch (Exception e) {
			System.out.println("Drone Exception");
			e.printStackTrace();
		}
	}

	static void doBasher()
	{
		try {
			RobotInfo[] adjacentEnemies = rc.senseNearbyRobots(2, enemyTeam);

			// BASHERs attack automatically, so let's just move around mostly randomly
		if (rc.isCoreReady()) {
			int fate = rand.nextInt(1000);
			if (fate < 800) {
				tryMove(directions[rand.nextInt(8)]);
			} else {
				tryMove(rc.getLocation().directionTo(rc.senseEnemyHQLocation()));
			}
		}
		} catch (Exception e) {
			System.out.println("Basher Exception");
			e.printStackTrace();
		}
	}

	static void doSoldier()
	{
		try {
			attackSomething();
			if (rc.isCoreReady()) {
				aggMove();
			}
		} catch (Exception e) {
			System.out.println("Soldier Exception");
			e.printStackTrace();
		}
	}
	
	static void doTank()
	{
		try {
			attackSomething();
			tightDefense();
		} catch (Exception e) {
			System.out.println("Tank Exception");
			e.printStackTrace();
		}
	}


	static void doBeaver()
	{
		try {
			facing = myLocation.directionTo(rc.senseEnemyHQLocation());
			// beaver early game
			int time = Clock.getRoundNum();
			if(time < 30)
			{
				if(rc.isCoreReady()&&rc.canMine())
				{
					rc.mine();
				}
			}
			else if(time < 40)
			{
				tryBuild(facing.opposite(),RobotType.MINERFACTORY);
				mineAndMove();
			}
			else if(time < 200)
			{
				mineAndMove();
			}
			{
				RobotInfo[] ourTeam = rc.senseNearbyRobots(100000, rc.getTeam());
				int n = 0; // current number of miner factories
				int m = 0; // current number of barracks
				int o = 0; // current number of helipads
				int s = 0; // current number of supply depots
				int tf = 0;
				for(RobotInfo ri: ourTeam){ // count up miner factories
					if(ri.type==RobotType.MINERFACTORY){
						n++;
					}else if(ri.type==RobotType.BARRACKS){
						m++;
					}else if(ri.type==RobotType.TANKFACTORY){
						tf++;
					}else if (ri.type==RobotType.HELIPAD){
						o++;
					}else if (ri.type==RobotType.SUPPLYDEPOT){
						s++;
					}
				}
				// only build additional miner factories if we have more than 1
				if(s<2)
				{
					tryBuild(facing.opposite(),RobotType.SUPPLYDEPOT);
				}
				else if(n < numMinerFactories && (n == o)) 
				{
					tryBuild(facing,RobotType.MINERFACTORY);
				} 
				else if(m<numBarracks)
				{
					tryBuild(facing.opposite(),RobotType.BARRACKS);
				}
				else if(tf<1 && m>0){
					tryBuild(facing.opposite(),RobotType.TANKFACTORY);
				}
				else if(o<numHelipads)
				{
					tryBuild(facing,RobotType.HELIPAD);
				}
				else if(s<numSupplyDepots)
				{
					tryBuild(facing.opposite(),RobotType.SUPPLYDEPOT);
				}
				else if(tf<numTankFactories  && m>0)
				{
					tryBuild(facing.opposite(),RobotType.TANKFACTORY);
				}
				attackSomething();
				mineAndMove();
			}
		} catch (Exception e) {
			System.out.println("Beaver Exception");
			e.printStackTrace();
		}
	}
	
	static void doMiner()
	{
		try {
			attackSomething();
			mineAndMove();
		} catch (Exception e) {
			System.out.println("Miner Exception");
			e.printStackTrace();
		}
	}
	

	// This method updates all squad-specific info
	static void updateSquadInfo() throws GameActionException
	{
		// accumulate squad unit counts
		int squadUnits = rc.readBroadcast(squadUnitsBase + mySquad);
		if (Clock.getRoundNum() != (squadUnits >> 16))
			squadUnits = Clock.getRoundNum() << 16;
		squadUnits++;
		rc.broadcast(squadUnitsBase + mySquad, squadUnits);
		
		// and update squad targets
		int st = rc.readBroadcast(squadTargetBase + mySquad);
		squadTarget = new MapLocation(((int)(short)st) + center.x, (st >> 16) + center.y);
		//System.out.println(mySquad + ", " + squadTarget + " " + center + " " + (int)(st & 65535) + " " + ((st >>> 16) - MAP_OFFSET));
		
		int squadTask = rc.readBroadcast(squadTaskBase + mySquad);
		if (Clock.getRoundNum() != (squadTask >> 16))
			squadTask = (Clock.getRoundNum() << 16) + ((squadTask & 255) << 8);
		
		if (myLocation.distanceSquaredTo(squadTarget) < 81)
			squadTask++;
		
		rc.broadcast(squadTaskBase + mySquad, squadTask);
	}
	
	
	// Supply Transfer Protocol
	static void transferSupplies() throws GameActionException {
		RobotInfo[] nearbyAllies = rc.senseNearbyRobots(myLocation,GameConstants.SUPPLY_TRANSFER_RADIUS_SQUARED,myTeam);
		double mySupply = rc.getSupplyLevel();
		double lowestSupply = mySupply;
		double transferAmount = 0;
		MapLocation suppliesToThisLocation = null;
		for(RobotInfo ri:nearbyAllies){
			if(ri.supplyLevel<lowestSupply){
				lowestSupply = ri.supplyLevel;
				transferAmount = (mySupply-ri.supplyLevel)/2;
				suppliesToThisLocation = ri.location;
			}
		}
		if(suppliesToThisLocation!=null){
			rc.transferSupplies((int)transferAmount, suppliesToThisLocation);
		}
	}
	
	static void moveStraight() throws GameActionException {
		if(isGoodMovementDirection()){
			//try to move in the facing direction
			if(rc.isCoreReady()&&rc.canMove(facing)){
				rc.move(facing);
			}
		}else{
			facing = facing.rotateLeft();
		}
	}
	
	private static boolean isGoodMovementDirection() throws GameActionException { //checks if the facing direction is "good", meaning safe from towers and not a blockage or off-map or occupied
		MapLocation tileInFront = rc.getLocation().add(facing);
		boolean goodSpace = true;
		//check that we are not facing off the edge of the map or are blocked
		if(rc.senseTerrainTile(tileInFront)!=TerrainTile.NORMAL){
			goodSpace = false;
		}else{
			//check that the space is not occupied by a robot
			if(rc.isLocationOccupied(tileInFront)){
				goodSpace = false; //space occupied
			}else{
				//check that the direction in front is not a tile that can be attacked by the enemy towers
				MapLocation[] enemyTowers = rc.senseEnemyTowerLocations();
				for(MapLocation m: enemyTowers){
					if(m.distanceSquaredTo(tileInFront)<=RobotType.TOWER.attackRadiusSquared){
						goodSpace = false; //space in range of enemy towers
						break;
					}
				}
			}
		}
		return goodSpace;
	}

	// This method will attack an enemy in sight, if there is one
	static void attackSomething() throws GameActionException
	{
		RobotInfo[] enemies = rc.senseNearbyRobots(myRange, enemyTeam);
		double minhealth = 1000;
		
		if (enemies.length == 0)
			return;

		MapLocation minloc = enemies[0].location;
		for (RobotInfo en: enemies)
		{
			if (en.health < minhealth)
			{
				minhealth = en.health;
				minloc = en.location;
			}
		}
		if (rc.canAttackLocation(minloc) && rc.isWeaponReady())
			rc.attackLocation(minloc);
	}
	
	
	static Direction getRandomDirection() {
		return Direction.values()[(int)(rand.nextDouble()*8)];
	}

	static void mineAndMove() throws GameActionException {
		if(rc.senseOre(rc.getLocation())>12){ //there is plenty of ore, so try to mine
			if(rand.nextDouble()<0.9){ // mine 90%
				if(rc.isCoreReady()&&rc.canMine()){
					rc.mine();
				}
			}else{
				facing = minerPotential();
				moveStraight();
			}
			
		}else if(rc.senseOre(rc.getLocation())>0.8){ //there is a bit of ore, so maybe try to mine, maybe move on
			if(rand.nextDouble()<0.2){ // mine
				if(rc.isCoreReady()&&rc.canMine()){
					rc.mine();
				}
			}else{ // look for more ore
				facing = minerPotential();
				moveStraight();
			}
		}else{ //no ore, so look for more
			facing = minerPotential();
			moveStraight();
		}
	}
	
	
	// Miner's potential field calculation.  Yields an integer representing the movement direction 0-7.  A null value means not to move.
	static Direction minerPotential() throws GameActionException {
		// just random
		//return (int)(rand.nextDouble()*8);
		
		float mineScore = 0;
		MapLocation here = rc.getLocation();
		
		// nearest-neighbor ore sensing
		int x1[] = {0,1,1,1,0,-1,-1,-1};
		int y1[] = {1,1,0,-1,-1,-1,0,1};
		//int x1[] = {0,1,0,-1,};
		//int y1[] = {1,0,-1,-0};
		float innerPotential[] = vectorSumOreAndMiners(x1,y1);
		mineScore += innerPotential[2];
		
		// next-nearest-neighbor ore sensing
		int x2[] = {0,2,2,2,0,-2,-2,-2};
		int y2[] = {2,2,0,-2,-2,-2,0,2};
		//int x2[] = {2,2,-2,-2};
		//int y2[] = {2,-2,-2,2};
		float outerPotential[] = vectorSumOreAndMiners(x2,y2);
		mineScore += outerPotential[2];

		// global target direction: WORK ON THIS!!!!
		rc.setIndicatorString(1, "mining value =  " + mineScore);
		if(mineScore > (float)rc.readBroadcast(bestMineScoreChan)){
			rc.broadcast(bestMineScoreChan, (int)mineScore);
			rc.broadcast(bestMineXChan, here.x);
			rc.broadcast(bestMineYChan, here.y);
			if(rc.getType()==RobotType.BEAVER){
				tryBuild(facing,RobotType.MINERFACTORY);
			}
		}
		int targetX = rc.readBroadcast(bestMineXChan);
		int targetY = rc.readBroadcast(bestMineYChan);
		float globalPullFactor = Math.max(0,mineScore)/20; // pull towards a good mine is proportional to the value at that mine
		float dx = (targetX - here.x);
		float dy = (targetY - here.y);
		float dist = dx*dx + dy*dy;
		float px = dx*globalPullFactor/dist;
		float py = dy*globalPullFactor/dist;
		float globalPotential[] = {px,py};

		// total direction
		float totalPotentialX = innerPotential[0]*10 + outerPotential[0] + globalPotential[0];
		float totalPotentialY = innerPotential[1]*10 + outerPotential[1] + globalPotential[1];
		Direction bestDirection = here.directionTo(here.add((int)totalPotentialX,(int)totalPotentialY)); // direction to move
		if(bestDirection==Direction.OMNI){ // can't decide where to go, don't let it get stuck
			bestDirection = getRandomDirection();
		}
		rc.setIndicatorString(0, "best direction =  " + bestDirection.toString());
		return bestDirection;
	}

	static float[] vectorSumOreAndMiners(int[] x, int[] y) throws GameActionException {
		MapLocation here = rc.getLocation();
		MapLocation sensingRegion[] = new MapLocation[x.length];
		for(int a=0; a<x.length; a++){
			sensingRegion[a] = here.add(x[a],y[a]);
		}
		double ore = 0;
		int i=0;
		float potentialX = 0;
		float potentialY = 0;
		float mineScore = -1*x.length*10; // makes it so that a flat region of 10 ore will have a score of 0
		for(MapLocation m: sensingRegion){
			ore = rc.senseOre(m);
			ore = (float)ore;
			RobotInfo robo = rc.senseRobotAtLocation(m);
			TerrainTile tile = rc.senseTerrainTile(m);
			mineScore += ore;
			//if(robo.type!=RobotType.MINER || robo.team!=rc.getTeam()){ // if there's a miner there, don't go toward it
			if(robo==null){
				potentialX += ore*x[i];
				potentialY += ore*y[i];
			}else{ // move away from others
				potentialX -= 5*x[i];
				potentialY -= 5*y[i];
			}
			i++;
		}
		float potential[] = {potentialX, potentialY, mineScore};
		return potential;
	}
		
	
	// Move Around: random moves; go left if hitting barrier; avoid towers
	static void moveAround() throws GameActionException 
	{
		if(rand.nextDouble()<0.05){
			if(rand.nextDouble()<0.5){
				facing = facing.rotateLeft();
			}else{
				facing = facing.rotateRight();
			}
		}
		MapLocation tileInFront = rc.getLocation().add(facing);
		
		//check that the direction in front is not a tile that can be attacked by the enemy towers
		MapLocation[] enemyTowers = rc.senseEnemyTowerLocations();
		boolean tileInFrontSafe = true;
		for(MapLocation m: enemyTowers){
			if(m.distanceSquaredTo(tileInFront)<=RobotType.TOWER.attackRadiusSquared){
				tileInFrontSafe = false;
				break;
			}
		}

		//check that we are not facing off the edge of the map
		if(rc.senseTerrainTile(tileInFront)!=TerrainTile.NORMAL||!tileInFrontSafe){
			facing = facing.rotateLeft();
		}else{
			//try to move in the facing direction
			if(rc.isCoreReady()&&rc.canMove(facing)){
				rc.move(facing);
			}
		}
	}
	
	// Aggressive Move (does not avoid towers)
	static void aggMove() throws GameActionException {

        facing = rc.getLocation().directionTo(squadTarget);
        
		if(rand.nextDouble()<0.15){
			if(rand.nextDouble()<0.5){
				facing = facing.rotateLeft();
			}else{
				facing = facing.rotateRight();
			}
		}
		
		MapLocation tileInFront = rc.getLocation().add(facing);

		//check that we are not facing off the edge of the map
		if(rc.senseTerrainTile(tileInFront)!=TerrainTile.NORMAL){
			facing = facing.rotateLeft();
		}else{
			//try to move in the facing direction
			if(rc.isCoreReady()&&rc.canMove(facing)){
				rc.move(facing);
			}
		}
    }

	// Potential field move
	static void calcPotential() throws GameActionException
	{
		float forceX = 0.0f;
		float forceY = 0.0f;
		
		// get dem robots
		RobotInfo[] friendlyRobots = rc.senseNearbyRobots(myType.sensorRadiusSquared,myTeam);
		RobotInfo[] enemyRobots = rc.senseNearbyRobots(myType.sensorRadiusSquared,myTeam.opponent());
		
		// attracted to squad target, far away
		forceX = (squadTarget.x - myLocation.x);
		forceY = (squadTarget.y - myLocation.y);
		
		float f = (float)Math.sqrt(forceX*forceX + forceY*forceY);
		forceX /= f;
		forceY /= f;
		
		// forceXY is now normalized distance to squadTarget
		
		double friendlyHP = 0;
		
		// don't get too close to friendly things
		for (RobotInfo bot : friendlyRobots)
		{
			friendlyHP += bot.health;
			//if (bot.type.attackRadiusSquared > 0)
			//	continue;
			
			int vecx = bot.location.x - myLocation.x;
			int vecy = bot.location.y - myLocation.y;
			int d2 = bot.location.distanceSquaredTo(myLocation);
			float id = invSqrt[d2];
			
			float kRepel = 0.1f;
			
			// just repel based on distance, at close range
			forceX += -kRepel*id*id*vecx;
			forceY += -kRepel*id*id*vecy;
		}
				
		for (RobotInfo bot : enemyRobots)
		{
			// dangerous ones, dealt with below
			if (bot.type == RobotType.TOWER || bot.type == RobotType.HQ)
				continue;
			
			int vecx = bot.location.x - myLocation.x;
			int vecy = bot.location.y - myLocation.y;
			int d2 = bot.location.distanceSquaredTo(myLocation);
			
			float id = invSqrt[d2];
			
			float kRepel = -8.0f;
			
			// attract if it can't fight back
			if (bot.type.attackRadiusSquared == 0)
				kRepel = 5.0f;
			
			// within attack range, repel
			// (difference in distances)
			if(bot.type!=RobotType.MINER){
				float dattack = sqrt[bot.type.attackRadiusSquared] - sqrt[d2] + 1.0f;
				if (dattack > 0)
				{
					kRepel /= (dattack*dattack);
				}
			}

			forceX += kRepel*id*vecx;
			forceY += kRepel*id*vecy;
		}
		
		boolean hasSquad = friendlyRobots.length > 5 || ((rc.readBroadcast(squadTaskBase+mySquad) >> 8) & 255) > 5;
		
		MapLocation[] towers = rc.senseEnemyTowerLocations();
		
		for (MapLocation tower : towers)
		{
			int d2 = tower.distanceSquaredTo(myLocation);
			// check if it's in range
			if (d2 > RobotType.TOWER.attackRadiusSquared + 20)
				continue;
			
			float id = invSqrt[d2];

			int vecx = tower.x - myLocation.x;
			int vecy = tower.y - myLocation.y;
			
			float kRepel = -2.0f;

			// if we can kill it easily, we attract
			if (rc.canSenseLocation(tower) && tower.equals(squadTarget))
			{
				RobotInfo ti = rc.senseRobotAtLocation(tower);
				if (ti.health < friendlyHP*10)
					kRepel = 50;
			}
			
			// otherwise, only get attracted to target tower, remain repelled from others
			if (hasSquad && tower.equals(squadTarget))
				kRepel = 50;
			
			// within attack range, repel
			// (difference in distances)
			float dattack = sqrt[RobotType.TOWER.attackRadiusSquared] - sqrt[d2] + 1.5f;
			if (kRepel > 0) // this is attractive, actually
				dattack = kRepel*id;
			else // and this means we are being repelled
				dattack = kRepel*id/Math.max(dattack,1);
			forceX += dattack*vecx;
			forceY += dattack*vecy;
		}
		// get direction of force
		MapValue[] mvs = new MapValue[9];

		for (int i=0; i<9; i++)
			mvs[i] = new MapValue(senseLocsX[i],senseLocsY[i],forceX*senseLocsX[i] + forceY*senseLocsY[i]);
		
		rc.setIndicatorLine(myLocation, new MapLocation(myLocation.x + (int)(forceX*10), myLocation.y + (int)(forceY*10)), 0, 255, 255);

		Arrays.sort(mvs);
		
		for (int i=8; i>0; i--)
		{
			// facing the wrong way, don't move at all
			if (mvs[i].value < 0)
				break;
			Direction newdir = myLocation.directionTo(mvs[i].offsetFrom(myLocation));
			if(rc.isCoreReady()&&rc.canMove(newdir))
			{
				rc.move(newdir);
			}
		}
	}
	

	// This method will attempt to move in Direction d (or as close to it as possible)
	static void tryMove(Direction d) throws GameActionException {
		int offsetIndex = 0;
		int[] offsets = {0,1,-1,2,-2};
		int dirint = directionToInt(d);
		boolean blocked = false;
		while (offsetIndex < 5 && !rc.canMove(directions[(dirint+offsets[offsetIndex]+8)%8])) {
			offsetIndex++;
		}
		if (offsetIndex < 5 && rc.isCoreReady()) {
			rc.move(directions[(dirint+offsets[offsetIndex]+8)%8]);
		}
	}

	// This method will attempt to spawn in the given direction (or as close to it as possible)
	static void trySpawn(Direction d, RobotType type) throws GameActionException {
		int offsetIndex = 0;
		int[] offsets = {0,1,-1,2,-2,3,-3,4};
		int dirint = directionToInt(d);
		boolean blocked = false;
		while (offsetIndex < 8 && !rc.canSpawn(directions[(dirint+offsets[offsetIndex]+8)%8], type)) {
			offsetIndex++;
		}
		if (offsetIndex < 8 && rc.getTeamOre()>type.oreCost && rc.isCoreReady()) {
			rc.spawn(directions[(dirint+offsets[offsetIndex]+8)%8], type);
		}
	}

	// This method will attempt to build in the given direction (or as close to it as possible)
	static void tryBuild(Direction d, RobotType type) throws GameActionException {
		int offsetIndex = 0;
		int[] offsets = {0,1,-1,2,-2,3,-3,4};
		int dirint = directionToInt(d);
		boolean blocked = false;
		while (offsetIndex < 8 && !rc.canMove(directions[(dirint+offsets[offsetIndex]+8)%8])) {
			offsetIndex++;
		}
		if (offsetIndex < 8 && rc.getTeamOre()>type.oreCost && rc.isCoreReady()) {
			rc.build(directions[(dirint+offsets[offsetIndex]+8)%8], type);
		}
	}
	
	
	static void buildUnit(RobotType type) throws GameActionException {
		if(rc.getTeamOre()>type.oreCost && rc.isCoreReady()) {
			
			Direction buildDir = getRandomDirection();
			for (int i=0; i<8; i++)
			{
				if(rc.canBuild(buildDir, type))
				{
					rc.build(buildDir, type);
					return;
				}
				buildDir = buildDir.rotateLeft();
			}
		}
	}

	
	private static void spawnUnit(RobotType type) throws GameActionException {
		Direction randomDir = getRandomDirection();

		if(rc.isCoreReady() && rc.canSpawn(randomDir, type) && rc.getTeamOre()>type.oreCost)
		{
			rc.spawn(randomDir, type);
		}
	}

	static int directionToInt(Direction d) {
		switch(d) {
			case NORTH:
				return 0;
			case NORTH_EAST:
				return 1;
			case EAST:
				return 2;
			case SOUTH_EAST:
				return 3;
			case SOUTH:
				return 4;
			case SOUTH_WEST:
				return 5;
			case WEST:
				return 6;
			case NORTH_WEST:
				return 7;
			default:
				return -1;
		}
	}
	
	// potential field thing for tank defense (idea is 2 per tower/HQ)
	static void tightDefense() throws GameActionException {
		int tanksPerTarget = 2;
		MapLocation here = rc.getLocation();
		RobotInfo[] unitsAround = rc.senseNearbyRobots(5, myTeam); // whoever i can see on my team
		// potential field becomes the facing direction
		double px = 0;
		double py = 0;
		for(RobotInfo ri: unitsAround){
			if(ri.type==RobotType.HQ || ri.type==RobotType.TOWER){
				int dx = ri.location.x - here.x;
				int dy = ri.location.y - here.y;
				double d = 1 + dx*dx + dy*dy; // r^2
				px += tanksPerTarget * dx / d;
				py += tanksPerTarget * dy / d;
			}else if(ri.type==RobotType.TANK){
				int dx = ri.location.x - here.x;
				int dy = ri.location.y - here.y;
				double d = 1 + dx*dx + dy*dy; // r^2
				px -= 2 * dx / d;
				py -= 2 * dy / d;
			}
			px = 10*px;
			py = 10*py;
		}
		for(int i=0; i<8; i++){
			TerrainTile terrain = rc.senseTerrainTile(here.add(senseLocsX[i],senseLocsY[i])); // get repelled by bad terrain
			if(terrain!=TerrainTile.NORMAL){
				px -= 100 * senseLocsX[i];
				py -= 100 * senseLocsY[i];
			}
		}
		facing = here.directionTo(here.add((int) px, (int) py));
		if(px==0 && py==0){ // nothing around to sense, move between towers and HQ
			/*
			MapLocation towerLocations[] = rc.senseTowerLocations();
			int sumx = 0;
			int sumy = 0;
			int n = 1;
			for(MapLocation locs: towerLocations){
				sumx += locs.x;
				sumy += locs.y;
				n += 1;
			}
			MapLocation myHQ = rc.senseHQLocation();
			sumx += myHQ.x;
			sumy += myHQ.y;
			int x = sumx / n;
			int y = sumy / n;
			MapLocation generalArea = new MapLocation(x,y);
			facing = here.directionTo(generalArea);
			*/
			facing = getRandomDirection();
		}
		tryMove(facing);
	}
	
	
	
}
