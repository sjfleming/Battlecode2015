package bassplayer;

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
	static final int nextSquadChan = squadUnitsBase + 1;
	
	
	static int bestMineScoreChan = nextSquadChan + 1;
	static int bestMineXChan = bestMineScoreChan + 1;
	static int bestMineYChan = bestMineXChan + 1;

	// Adjustable parameters
	static int numBeavers = 16;
	static int numMinerFactories = 4;
	static int numMiners = 40;
	static int numBarracks = 0;
	static int numSoldiers = 0;

	
	
	
	/* Sensing location defines etc */
	static int senseLocsShort = 69;
	static int senseLocsLong = 109;
	static int[] senseLocsX = {0,-1,0,0,1,-1,-1,1,1,-2,0,0,2,-2,-2,-1,-1,1,1,2,2,-2,-2,2,2,-3,0,0,3,-3,-3,-1,-1,1,1,3,3,-3,-3,-2,-2,2,2,3,3,-4,0,0,4,-4,-4,-1,-1,1,1,4,4,-3,-3,3,3,-4,-4,-2,-2,2,2,4,4,-5,-4,-4,-3,-3,0,0,3,3,4,4,5,-5,-5,-1,-1,1,1,5,5,-5,-5,-2,-2,2,2,5,5,-4,-4,4,4,-5,-5,-3,-3,3,3,5,5};
	static int[] senseLocsY = {0,0,-1,1,0,-1,1,-1,1,0,-2,2,0,-1,1,-2,2,-2,2,-1,1,-2,2,-2,2,0,-3,3,0,-1,1,-3,3,-3,3,-1,1,-2,2,-3,3,-3,3,-2,2,0,-4,4,0,-1,1,-4,4,-4,4,-1,1,-3,3,-3,3,-2,2,-4,4,-4,4,-2,2,0,-3,3,-4,4,-5,5,-4,4,-3,3,0,-1,1,-5,5,-5,5,-1,1,-2,2,-5,5,-5,5,-2,2,-4,4,-4,4,-3,3,-5,5,-5,5,-3,3};
	static int[] senseLocsR = {0,10,10,10,10,14,14,14,14,20,20,20,20,22,22,22,22,22,22,22,22,28,28,28,28,30,30,30,30,31,31,31,31,31,31,31,31,36,36,36,36,36,36,36,36,40,40,40,40,41,41,41,41,41,41,41,41,42,42,42,42,44,44,44,44,44,44,44,44,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,53,53,53,53,53,53,53,53,56,56,56,56,58,58,58,58,58,58,58,58};
	
	
	// HQ-specific
	static int[] squadCounts = new int[MAX_SQUADS];

	static double lastOre = 0;
	static double curOre = 0;
	

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
				rc.broadcast(nextSquadChan, nextSquad >> 8);
				break;
			}
		} catch (Exception e) {
			System.out.println("Initialization exception");
			e.printStackTrace();
		}
		
		while(true) {
			try {
				rc.setIndicatorString(0, "This is an indicator string.");
				rc.setIndicatorString(1, "I am a " + rc.getType() + " SQUAD: " + mySquad + " TARGET: " + squadTarget);
			} catch (Exception e) {
				System.out.println("Unexpected exception");
				e.printStackTrace();
			}
			
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
			}
			
			lastOre = curOre;
			try {
				transferSupplies();
			} catch (Exception e) {
				
			}
			rc.yield();
		}
	}
	
	
	
	static void doHQ()
	{
		try 
		{
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
					if (nextSquad < 0)
						nextSquad = i;
				}
			}
			rc.broadcast(nextSquadChan, nextSquad);
			
			// set the first squad's target to enemy base
			MapLocation[] towers = rc.senseEnemyTowerLocations();
			for (int i=0; i<towers.length; i++)
			{
				int loc = towers[i].y - center.y;
				loc = (loc << 16) + ((towers[i].x - center.x) & 65535);
				rc.broadcast(squadTargetBase + i, loc);
			}
			MapLocation hq = rc.senseEnemyHQLocation();
			for (int i=towers.length; i<MAX_SQUADS; i++)
			{
				int loc = hq.y - center.y;
				loc = (loc << 16) + ((hq.x - center.x) & 65535);
				rc.broadcast(squadTargetBase + i, loc);
			}
					
			
			RobotInfo[] ourTeam = rc.senseNearbyRobots(1000, rc.getTeam());
			int n = 0; // current number of beavers
			for(RobotInfo ri: ourTeam){ // count up beavers
				if(ri.type==RobotType.BEAVER){
					n++;
				}
			}
			if(Clock.getRoundNum()<300 && n<numBeavers){ // in the beginning, spawn 'numBeavers' beavers and send them out in all directions
				Direction dir = getRandomDirection();
				if(rc.isCoreReady() && rc.canSpawn(dir, RobotType.BEAVER)) {
					rc.spawn(dir, RobotType.BEAVER);
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

	
	static void doBarracks()
	{
		try {
			spawnUnit(RobotType.SOLDIER);
		} catch (Exception e) {
			System.out.println("Barracks Exception");
			e.printStackTrace();
		}
	}

	
	static void doDrone()
	{
		try {
			updateSquadInfo();
            Direction toDest = rc.getLocation().directionTo(squadTarget);
        	Direction[] dirs = {toDest,
		    		toDest.rotateLeft(), toDest.rotateRight(),toDest.rotateLeft().rotateLeft(), toDest.rotateRight().rotateRight()};
        	for (Direction d : dirs) {
                if (rc.canMove(d) && rc.isCoreReady()) {
                    rc.move(d);
                }
            }
			attackSomething();
	        
			//potentialAct(squadTarget,RobotType.DRONE);
			//attackSomething();
			//calcPotential();
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

	static void doBeaver()
	{
		try {
			if(Clock.getRoundNum() < 50)
			{
				moveStraight();
			}
			else
			{
				RobotInfo[] ourTeam = rc.senseNearbyRobots(100000, rc.getTeam());
				int n = 0; // current number of miner factories
				int m = 0; // current number of barracks
				for(RobotInfo ri: ourTeam){ // count up miner factories
					if(ri.type==RobotType.MINERFACTORY){
						n++;
					}else if(ri.type==RobotType.BARRACKS){
						m++;
					}
				}
				if(n<numMinerFactories) 
				{
					buildUnit(RobotType.MINERFACTORY);
				} 
				else if(m<numBarracks)
				{
					buildUnit(RobotType.BARRACKS);
				}
				mineAndMove();
			}
			// build miner factories if income < 25
			if (curOre - lastOre < 25 && rand.nextInt(500) > Clock.getRoundNum()) {
				buildUnit(RobotType.MINERFACTORY);
			}
			else {
				buildUnit(RobotType.HELIPAD);
			}
			attackSomething();
			mineAndMove();
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
		squadTarget = new MapLocation((st & 65535) + center.x, (st >> 16) + center.y);
		//System.out.println(mySquad + ", " + squadTarget + " " + center + " " + (int)(st & 65535) + " " + ((st >>> 16) - MAP_OFFSET));
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
		double forceX = 0.0;
		double forceY = 0.0;
		
		// get dem robots
		RobotInfo[] nearbyRobots = rc.senseNearbyRobots();
		
		// attracted to squad target, far away
		forceX = (squadTarget.x - myLocation.x);
		forceY = (squadTarget.y - myLocation.y);
		double f = Math.sqrt(forceX*forceX + forceY*forceY)/0.2;
		forceX /= f;
		forceY /= f;
		
		// calculate potential field from nearby robots
		for (RobotInfo bot : nearbyRobots)
		{
			int vecx = bot.location.x - myLocation.x;
			int vecy = bot.location.y - myLocation.y;
			int d2 = bot.location.distanceSquaredTo(myLocation);
			double d = Math.sqrt(d2);
			double falloff = 1.0/d2;
			double fx = 0.0;
			double fy = 0.0;
			
			final double kRepel = 1.0;
			
			if (bot.team == myTeam)
			{
				// just repel based on distance, at close range
				fx += -kRepel*falloff*falloff*vecx;
				fy += -kRepel*falloff*falloff*vecy;
			}
			else
			{
				// within attack range, repel
				double dattack = Math.sqrt(bot.type.attackRadiusSquared) - d;
				if (dattack > 0)
				{
					fx += kRepel*falloff*vecx;
					fy += kRepel*falloff*vecy;
				}
			}
			forceX += fx;
			forceY += fy;
		}
		// get direction of force
		/*f = Math.sqrt(forceX*forceX + forceY*forceY);
		forceX /= f;
		forceY /= f;*/
		double[] dots = new double[9];
		double maxDot = -1e6;
		int maxDir = 0;
		for (int i=0; i<9; i++)
		{
			dots[i] = forceX*senseLocsX[i] + forceY*senseLocsY[i];
			if (dots[i] > maxDot)
			{
				maxDot = dots[i];
				maxDir = i;
			}
		}
		MapLocation newloc = new MapLocation(myLocation.x + senseLocsX[maxDir],myLocation.y + senseLocsY[maxDir]);
		Direction newdir = myLocation.directionTo(newloc);
		if(rc.isCoreReady()&&rc.canMove(newdir)) {
			rc.move(newdir);
		}
	}
	
	// need to avoid towers bc sight range is same as firing range
	public static void potentialAct(MapLocation dest, RobotType type) throws GameActionException{
		double coreDelay = rc.getCoreDelay();
		int agg = 0;
		int allyRange = RobotType.DRONE.sensorRadiusSquared;
		
		// attack first
		attackSomething();
	
		// sense robots within vision radius

		RobotInfo[] nearbyEnemies = rc.senseNearbyRobots(rc.getLocation(),type.sensorRadiusSquared,rc.getTeam().opponent());
		RobotInfo[] nearbyAllies = rc.senseNearbyRobots(rc.getLocation(),allyRange,rc.getTeam());
		int numEnemies = nearbyEnemies.length;
		int numAllies = nearbyAllies.length;
		
		// potential field
		MapLocation myLoc = rc.getLocation();

		// value for each adjacent tile
		int field[] = {0,0,0,0,0,0,0,0,0}; // tile, NORTH, NORTHWEST...etc
		float pField[] = {0,0,0,0,0,0,0,0,0};
		
		Direction testDir = Direction.NORTH; 

		float enemyDelta = 0;
		float friendDelta = 0;
		float totalpField = 0;
		String fieldReport;

		// calc field for this tile first
		
		field[0] = calcField(myLoc, myLoc, dest, nearbyEnemies, nearbyAllies, coreDelay, numAllies);
		if (field[0]>1){
			pField[0] = field[0];
		}else{
			pField[0] = -1/(field[0]-2);
		}
		
		totalpField = pField[0];
		fieldReport = " " + field[0];
		
		
		// calc field for adjacent tiles
		testDir = Direction.NORTH;
		for (int i = 1; i < 9; i++){
			MapLocation testLoc = rc.getLocation().add(testDir);
			field[i] = calcField(myLoc,testLoc, dest, nearbyEnemies, nearbyAllies, coreDelay, numAllies);
			
			if (field[i]>1){
				pField[i] = field[i];
			}else{
				pField[i] = -1/(field[i]-2);
			}
			
			testDir = testDir.rotateLeft();
			totalpField = totalpField + pField[i];
			fieldReport = fieldReport + " " + field[i];
		}
		
		/*
		rc.setIndicatorString(0, "Field =  " + fieldReport);
		rc.setIndicatorString(1, "total p Field =  " + totalpField);
		rc.setIndicatorString(2, "core delay =  " + coreDelay);
		*/
		
		testDir = Direction.NORTH; 
		double diceRoll = totalpField*rand.nextDouble();
		
		
		float fieldCount = pField[0];
	
		for (int i = 1; i < 9; i=i+1){
			if (diceRoll>fieldCount && diceRoll <fieldCount + pField[i]){
				if(rc.isCoreReady()&&rc.canMove(testDir)){
					rc.move(testDir);
					break;
				}
			}
			testDir = testDir.rotateLeft();
			fieldCount = fieldCount + pField[i];
		}

		/*
		attackSomething();
		
		

		// for now, report field again for reporting
		// calc field for this tile first
		
		field[0] = calcField(myLoc, myLoc, dest, nearbyEnemies, nearbyAllies, coreDelay, agg);
		if (field[0]>1){
			pField[0] = field[0];
		}else{
			pField[0] = -1/(field[0]-2);
		}
		
		totalpField = pField[0];
		fieldReport = " " + field[0];
		
		
		// calc field for adjacent tiles
		testDir = Direction.NORTH;
		for (int i = 1; i < 9; i = i+1){
			MapLocation testLoc = rc.getLocation().add(testDir);
			field[i] = calcField(myLoc,testLoc, dest, nearbyEnemies, nearbyAllies, coreDelay, agg);
			
			if (field[i]>1){
				pField[i] = field[i];
			}else{
				pField[i] = -1/(field[i]-2);
			}
			
			testDir = testDir.rotateLeft();
			totalpField = totalpField + pField[i];
			fieldReport = fieldReport + " " + field[i];
		}
		
		rc.setIndicatorString(0, "Field =  " + fieldReport);
		rc.setIndicatorString(1, "total p Field =  " + totalpField);
		rc.setIndicatorString(2, "core delay =  " + coreDelay);
		
		
		rc.setIndicatorString(2, "numAllies" + allyRange + " " + numAllies +"numEnemies" + numEnemies );
		*/
	}


	private static int calcField(MapLocation myLoc, MapLocation testLoc, MapLocation dest, RobotInfo[] nearbyEnemies, RobotInfo[] nearbyAllies, double coreDelay, int numAllies) {
		int numEnemies = nearbyEnemies.length;


		
		final int fieldBaseline = 10;		
		final int enemyRangePenalty = -200;
		final int enemyTowerRangePenalty = -200;
		final int crashingPenalty = -1000;
		final int bunchingPenalty = -10;
		final int diagWhenCoreDelayPenalty = -40;
		final int restWhenCoreDelayBonus = 100;
		final int destBonus = 90;
		final int numAlliesBonus = 10;
		final int destroyTowerBonus = 340;
		
		int cumulativeField = fieldBaseline;
		Direction testDir = myLoc.directionTo(testLoc);
		
		// avoid enemy attack range and enemy units
		for (int j = 0; j < numEnemies; j=j+1){
			int enemyDelta = 0;
			if (nearbyEnemies[j].location.distanceSquaredTo(testLoc)<=nearbyEnemies[j].type.attackRadiusSquared){
				enemyDelta = enemyRangePenalty;
			}
			if (nearbyEnemies[j].location.distanceSquaredTo(testLoc)==0){
				enemyDelta = crashingPenalty;
			}
			cumulativeField = cumulativeField + enemyDelta;
		}		

		// repel allies and avoid crashing into my own units
		for (int j = 0; j < numAllies; j=j+1){
			int friendlyDelta = 0;
			if (nearbyAllies[j].location.distanceSquaredTo(testLoc)==0 && testLoc != myLoc){
				friendlyDelta = crashingPenalty;
			}else if (nearbyAllies[j].location.distanceSquaredTo(testLoc)<3){
				friendlyDelta = bunchingPenalty;
			}
			cumulativeField = cumulativeField + friendlyDelta;
		}	
		

		//discourage diagonal moves and encourage waiting if too much coreDelay 
		if (rc.getCoreDelay() > 0.6){
			if (testDir.isDiagonal()){
			cumulativeField = cumulativeField + diagWhenCoreDelayPenalty;
			}
			if (myLoc.distanceSquaredTo(testLoc) == 0){
				cumulativeField = cumulativeField + restWhenCoreDelayBonus;
			}
		}		
		

		// Move generally towards dest

			if (testLoc.distanceSquaredTo(dest)<myLoc.distanceSquaredTo(dest)){
				cumulativeField = cumulativeField + destBonus;
				//+ (numAllies-numEnemies)*numAlliesBonus;
			}
		
		
		// Avoid towers, which have same attack range as our visions
		// Unless you have numbers, then go towards it
		MapLocation[] enemyTowers = rc.senseEnemyTowerLocations();

		
		for(MapLocation m: enemyTowers){
			if(m.distanceSquaredTo(testLoc)<=RobotType.TOWER.attackRadiusSquared){
				cumulativeField = cumulativeField + enemyTowerRangePenalty;
			}
			if(m.distanceSquaredTo(testLoc)<m.distanceSquaredTo(myLoc) && numAllies-numEnemies > 5){
				cumulativeField = cumulativeField + destroyTowerBonus;
			}
		}
						
		return cumulativeField;	
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
		if (offsetIndex < 5) {
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
		if (offsetIndex < 8) {
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
		if (offsetIndex < 8) {
			rc.build(directions[(dirint+offsets[offsetIndex]+8)%8], type);
		}
	}
	
	
	private static void buildUnit(RobotType type) throws GameActionException {
		if(rc.getTeamOre()>type.oreCost){
			Direction buildDir = getRandomDirection();

			if(rc.isCoreReady()&&rc.canBuild(buildDir, type))
			{
				rc.build(buildDir, type);
			}
		}
	}

	
	private static void spawnUnit(RobotType type) throws GameActionException {
		Direction randomDir = getRandomDirection();

		if(rc.isCoreReady() && rc.canSpawn(randomDir, type))
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
	
	
	
	
	
}
