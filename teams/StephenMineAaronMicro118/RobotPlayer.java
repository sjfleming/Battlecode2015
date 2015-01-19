package StephenMineAaronMicro118;


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
	
	// Unit Power Rankings (unitless) 
	// HQ, TOWER, SD, TECH, RACKS, HELI, FIELD, TFACT, MFACT, WASH, SPACE, BEAV, COMP, SOLDIER, BASH, MINE, DRONE, TANK, COMM, LAUNCH, MISSILE
		static float unitVal[] = {30.0f, 20.0f, 0.0f, 0.0f, 0.0f, 0.0f,0.0f, 0.0f,0.0f, 0.0f,0.0f, 0.6f, 0.0f, 1.0f, 2.3f, 0.6f, 1.2f, 3.8f, 8.0f, 10.0f, 0.2f};
		
	// Unit Attack Affinities (unitless)
	static float unitAtt[] = {0.0f, 0.0f, 1.0f, 1.0f,4.0f, 4.0f,1.0f, 4.0f,5.0f, 1.0f,1.0f, 3.0f, 1.0f, 2.0f, 2.0f, 3.0f, 1.0f, 1.0f, 1.0f, 1.0f,1.0f};

	// Shoot Impatience - Sweeping 
	static int shootImpatience = 100; // in clicks
	static int lastShotCounter = 0;
	static int sweepCounter = 0;
	static int sweeping = 0;
	static int sweepBoredomLength = 50; // in clicks
	
	
	// Move Impatience Counter
	static int moveImpatience = 15; // in clicks
	static int moveRecSize = 2000;
	static MapLocation moveRec[] = new MapLocation[moveRecSize];
	static int moveIdx = 3; //start at 3 so it doesnt crash
	static int lastMoveCounter = 0;
	static int bored;
	static int boredCounter = 0;
	static int moveBoredomLength = 20;
	static int boredomLength = 20;
	static int boredDirIdx;
	
	// agg coefficient
	static float aggCoef = 0.8f;
	
	static final int MAX_SQUADS = 16;
	
	/* Squad task defines */
	static final int squadTaskBase = 0;
	
	/* Squad target defines */
	static final int squadTargetBase = squadTaskBase + MAX_SQUADS;
	
	/* Squad unit counts */
	static final int squadUnitsBase = squadTargetBase + MAX_SQUADS;
	
	/* Next squad channel */
	static final int nextSquadChan = squadUnitsBase + MAX_SQUADS;
	
	//===============================================================================================================
	
	// Keep track of best mine, score and location
	static int bestMineScoreChan = nextSquadChan + 1;
	static int bestMineXChan = bestMineScoreChan + 1;
	static int bestMineYChan = bestMineXChan + 1;
	
	// Keep track of number of each type of miner
	static int minersSupplying = bestMineYChan +1;
	static int minersSearching = minersSupplying + 1;
	static int minersLeading = minersSearching + 1;
	
	// Allow HQ to allocate mining duties
	static int minerShuffle = minersLeading +1;
	static final int TARGET_SUPPLYING_MINERS = 10;
	
	// Keep track of miner IDs : using channel 499 and onward
	static int minerContiguousID = 499;
	static int myMinerID;
	
	//===============================================================================================================

	// Adjustable parameters
	static int numBeavers = 4;
	static int numMinerFactories = 1;
	static int numMiners = 40;
	static int numBarracks = 12;
	static int numHelipads = 0;
	static int numSupplyDepots = 3;
	static int numTankFactories = 0;
	static int numTanks = 0;
	
	
	/* Sensing location defines etc */
	static int senseLocsShort = 69;
	static int senseLocsLong = 109;
	static int[] senseLocsX = {0,-1,0,0,1,-1,-1,1,1,-2,0,0,2,-2,-2,-1,-1,1,1,2,2,-2,-2,2,2,-3,0,0,3,-3,-3,-1,-1,1,1,3,3,-3,-3,-2,-2,2,2,3,3,-4,0,0,4,-4,-4,-1,-1,1,1,4,4,-3,-3,3,3,-4,-4,-2,-2,2,2,4,4,-5,-4,-4,-3,-3,0,0,3,3,4,4,5,-5,-5,-1,-1,1,1,5,5,-5,-5,-2,-2,2,2,5,5,-4,-4,4,4,-5,-5,-3,-3,3,3,5,5};
	static int[] senseLocsY = {0,0,-1,1,0,-1,1,-1,1,0,-2,2,0,-1,1,-2,2,-2,2,-1,1,-2,2,-2,2,0,-3,3,0,-1,1,-3,3,-3,3,-1,1,-2,2,-3,3,-3,3,-2,2,0,-4,4,0,-1,1,-4,4,-4,4,-1,1,-3,3,-3,3,-2,2,-4,4,-4,4,-2,2,0,-3,3,-4,4,-5,5,-4,4,-3,3,0,-1,1,-5,5,-5,5,-1,1,-2,2,-5,5,-5,5,-2,2,-4,4,-4,4,-3,3,-5,5,-5,5,-3,3};
	static int[] senseLocsR = {0,10,10,10,10,14,14,14,14,20,20,20,20,22,22,22,22,22,22,22,22,28,28,28,28,30,30,30,30,31,31,31,31,31,31,31,31,36,36,36,36,36,36,36,36,40,40,40,40,41,41,41,41,41,41,41,41,42,42,42,42,44,44,44,44,44,44,44,44,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,53,53,53,53,53,53,53,53,56,56,56,56,58,58,58,58,58,58,58,58};
	static float[] sqrt = {0.000000f,1.000000f,1.414214f,1.732051f,2.000000f,2.236068f,2.449490f,2.645751f,2.828427f,3.000000f,3.162278f,3.316625f,3.464102f,3.605551f,3.741657f,3.872983f,4.000000f,4.123106f,4.242641f,4.358899f,4.472136f,4.582576f,4.690416f,4.795832f,4.898979f,5.000000f,5.099020f,5.196152f,5.291503f,5.385165f,5.477226f,5.567764f,5.656854f,5.744563f,5.830952f,5.916080f,6.000000f,6.082763f,6.164414f,6.244998f,6.324555f,6.403124f,6.480741f,6.557439f,6.633250f,6.708204f,6.782330f,6.855655f,6.928203f,7.000000f,7.071068f,7.141428f,7.211103f,7.280110f,7.348469f,7.416198f,7.483315f,7.549834f,7.615773f,7.681146f,7.745967f,7.810250f,7.874008f,7.937254f,8.000000f,8.062258f,8.124038f,8.185353f,8.246211f,8.306624f,8.366600f,8.426150f,8.485281f,8.544004f,8.602325f,8.660254f,8.717798f,8.774964f,8.831761f,8.888194f,8.944272f,9.000000f};
	static float[] invSqrt = {0.000000f,1.000000f,0.707107f,0.577350f,0.500000f,0.447214f,0.408248f,0.377964f,0.353553f,0.333333f,0.316228f,0.301511f,0.288675f,0.277350f,0.267261f,0.258199f,0.250000f,0.242536f,0.235702f,0.229416f,0.223607f,0.218218f,0.213201f,0.208514f,0.204124f,0.200000f,0.196116f,0.192450f,0.188982f,0.185695f,0.182574f,0.179605f,0.176777f,0.174078f,0.171499f,0.169031f,0.166667f,0.164399f,0.162221f,0.160128f,0.158114f,0.156174f,0.154303f,0.152499f,0.150756f,0.149071f,0.147442f,0.145865f,0.144338f,0.142857f,0.141421f,0.140028f,0.138675f,0.137361f,0.136083f,0.134840f,0.133631f,0.132453f,0.131306f,0.130189f,0.129099f,0.128037f,0.127000f,0.125988f,0.125000f,0.124035f,0.123091f,0.122169f,0.121268f,0.120386f,0.119523f,0.118678f,0.117851f,0.117041f,0.116248f,0.115470f,0.114708f,0.113961f,0.113228f,0.112509f,0.111803f,0.111111f};
	
	//===========================================================================================
	// Miner-specific
	static enum MinerState
	{
		SUPPLYING,	// supply line, usually stationary
		SEARCHING,	// executing a local ore finding algorithm, and generally following leaders
		LEADING,	// mining and moving forward only
	}
	static String toString(MinerState state) {
		String ans = null;
		switch(state)
		{
		case SUPPLYING:
			ans = "SUPPLYING";
			break;
		case SEARCHING:
			ans =  "SEARCHING";
			break;
		case LEADING:
			ans =  "LEADING";
			break;
		}
		return ans;
	}
	static MinerState minerState;
	public RobotPlayer(MinerState state){ // each robot has a MinerState field accessed by rc.minerState
		this.minerState = state;
	}
	static boolean justMoved;
	static double lastLocationStartingOre;
	static double currentLocationStartingOre;
	static boolean bestMine;
	static double oreHere;
	static int[] minersPerState = {0,0,0}; // initially no miners: minersPerState[0]=SUPPLYING, minersPerState[1]=SEARCHING, minersPerState[2]=LEADING
	
	//===========================================================================================
	
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
		rand = new Random(rc.getID());
		myRange = rc.getType().attackRadiusSquared;
		myTeam = rc.getTeam();
		enemyTeam = myTeam.opponent();
		myType = rc.getType();
		
		// calculate center, assuming rotational symmetry for now
		MapLocation myBase = rc.senseHQLocation();
		MapLocation enBase = rc.senseEnemyHQLocation();
		
		center = new MapLocation((myBase.x+enBase.x)/2,(myBase.y+enBase.y)/2);
		
		facing = getRandomDirection();
		justMoved = false;
		
		lastOre = rc.getTeamOre();
		curOre = lastOre;
		
		
		// Initialization code
		try {
			switch (myType)
			{
			case HQ:
				rc.broadcast(minerContiguousID,500);
				break;
			case BEAVER:
				//facing = myLocation.directionTo(myBase).opposite();
				break;
			case MINER:
				minerState = MinerState.SUPPLYING; // initially miners are in supply chain
				myMinerID = rc.readBroadcast(minerContiguousID); // obtain a minerID
				rc.broadcast(minerContiguousID, myMinerID + 1); // update next minerID
				rc.broadcast(myMinerID, rc.getID()); // save my robot ID on the message board
				break;
			case DRONE:
				// get the next squad, as assigned by the HQ
				int nextSquad = rc.readBroadcast(nextSquadChan);
				// save the lowest byte
				mySquad = nextSquad & 255;
				// and write it back
				//rc.broadcast(nextSquadChan, nextSquad >> 8);
				break;
			default:
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
			default:
				break;
			}
			
			lastOre = curOre;
			
			if (Clock.getBytecodesLeft() < 600)
				rc.yield();
			
			if(myType!=RobotType.MINERFACTORY && myType!=RobotType.TANKFACTORY){
				try {
					transferSupplies(0.5);
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
			rc.setIndicatorString(1,"Current ore: " + (int)curOre);
			attackSomething();
			
			// figure out miner state updates ======================================================================================
			int roundNow = Clock.getRoundNum();
			
			// suppliers
			int supplierCount = rc.readBroadcast(minersSupplying);
			if(supplierCount >> 16 == roundNow - 1){ // if the most recent update was last round
				supplierCount = supplierCount & 255; // bit shifting nonsense
			}
			else
				supplierCount = 0; // no units reporting, all are dead
			minersPerState[0] = supplierCount;
			
			// searchers
			int searcherCount = rc.readBroadcast(minersSearching);
			if(searcherCount >> 16 == roundNow - 1){ // if the most recent update was last round
				searcherCount = searcherCount & 255; // bit shifting nonsense
			}
			else
				searcherCount = 0; // no units reporting, all are dead
			minersPerState[1] = searcherCount;
			
			// leaders
			int leaderCount = rc.readBroadcast(minersLeading);
			if(leaderCount >> 16 == roundNow - 1) // if the most recent update was last round
				leaderCount = leaderCount & 255; // bit shifting nonsense
			else
				leaderCount = 0; // no units reporting, all are dead
			minersPerState[2] = leaderCount;
			rc.setIndicatorString(0,"Miners supplying: " + supplierCount + ", searching: " + searcherCount + ", leading: " + leaderCount);
			int bestMineScore = rc.readBroadcast(bestMineScoreChan);
			int bestMineX = rc.readBroadcast(bestMineXChan);
			int bestMineY = rc.readBroadcast(bestMineYChan);
			rc.setIndicatorDot(new MapLocation(bestMineX,bestMineY), 0,255,0);
			rc.setIndicatorString(2,"Best mine = " + bestMineScore + " at (" + bestMineX + "," + bestMineY + ")");
			rc.broadcast(bestMineScoreChan, bestMineScore - 2); // slight decay ensures best mine won't last for too long
			
			// if we have too many suppliers, promote a supplier to searcher (numbers track until supplierCount reaches TARGET_SUPPLYING_MINERS)
			if(supplierCount<TARGET_SUPPLYING_MINERS)
				rc.broadcast(minerShuffle, supplierCount - searcherCount); // the miners only react to a positive number
			else
				rc.broadcast(minerShuffle, supplierCount - TARGET_SUPPLYING_MINERS); // the miners only react to a positive number
			
			//========================================================================================================================
			
			int nextSquad = -1;
			// figure out squad state updates
			for (int i=0; i<MAX_SQUADS; i++)
			{
				int squadcount = rc.readBroadcast(squadUnitsBase + i);

				if (squadcount >> 16 == Clock.getRoundNum() - 1)
				{
					// units alive, get number of units
					squadcount = squadcount & 255;
					squadCounts[i] = squadcount;
					// if we are rallying and full
					int targetNum = SQUAD_UNITS;
					if (i<HARASS_SQUADS) targetNum = HARASS_UNITS;

					if (squadStates[i] == SquadState.RALLY)
					{
						if (squadCounts[i] >= targetNum)
						{
							if (((rc.readBroadcast(squadTaskBase+i) >> 8) & 255) >= targetNum) {
								// this squad is now live, and is all near target, don't need more units
								squadStates[i] = (i<HARASS_SQUADS)?SquadState.HARASS:SquadState.ATTACK;
								//doSquadTarget(i);
							}
						}
						else
						{
							// it needs more units, target stays, but only if we're rallying
							if (nextSquad == -1) nextSquad = i;
						}
					}
					else // see if other types reached their target
					{
						if (rc.canSenseLocation(squadTargets[i]))
						{
							RobotInfo ri = rc.senseRobotAtLocation(squadTargets[i]);
							if (ri != null && ri.team == myTeam)
							{
								//doSquadTarget(i);
							}
						}
					}
				}
				else
				{
					// no units reporting, all are dead
					squadCounts[i] = 0;
					squadStates[i] = SquadState.RALLY;
					if (nextSquad == -1) nextSquad = i;
					//doSquadTarget(i);
				}
			}
			//System.out.println("NextSquad:" + nextSquad);
			if (nextSquad == -1)
				nextSquad = 0;
			rc.broadcast(nextSquadChan, nextSquad);
			
			// set the first squad's target to enemy towers
			MapLocation[] towers = rc.senseEnemyTowerLocations();
			//MapLocation hq = rc.senseEnemyHQLocation();
			MapLocation myhq = rc.senseHQLocation();
			
			if (towers.length != lastTowers)
			{
				MapValue[] mvs = new MapValue[towers.length];
				
				for (int i=0; i<towers.length; i++)
				{
					mvs[i] = new MapValue(towers[i].x-center.x,towers[i].y-center.y,towers[i].distanceSquaredTo(myhq));
				}
				
				Arrays.sort(mvs);
				
				for (int i=0; i<towers.length; i++)
				{
					int loc = (mvs[i].y << 16) + (mvs[i].x & 65535);
					rc.broadcast(squadTargetBase + i, loc);
				}
			}
			
			int st = rc.readBroadcast(squadTargetBase);
			
			
			RobotInfo[] ourTeam = rc.senseNearbyRobots(1000, rc.getTeam());
			int n = 0; // current number of beavers
			int m = 0; // current number of miner factories
			int h = 0; // current number of helipads
			for(RobotInfo ri: ourTeam){ // count up stuff
				if(ri.type==RobotType.BEAVER){
					n++;
				} else if(ri.type==RobotType.MINERFACTORY){
					m++;
				} else if(ri.type==RobotType.HELIPAD){
					h++;
				}
			}
			// make 1 beavers, then more after first miner factory and helipad
			if(n<1 || (n<numBeavers && m>0 && h>0) || (n<2 && m>0))
			{
				trySpawn(myLocation.directionTo(rc.senseEnemyHQLocation()), RobotType.BEAVER);
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
				trySpawn(facing,RobotType.DRONE);
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
				trySpawn(facing,RobotType.MINER);
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
			double r = rand.nextDouble();
			if(r < 0.25)
				trySpawn(facing,RobotType.SOLDIER);
			else if(r<0.75)
				trySpawn(facing,RobotType.BASHER);
		} catch (Exception e) {
			System.out.println("Barracks Exception");
			e.printStackTrace();
		}
	}

	
	static void doDrone()
	{
		try {
			updateSquadInfo();
            /*Direction toDest = myLocation.directionTo(squadTarget);
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
			movePotential();
			rc.setIndicatorString(0, "Drone: squad " + mySquad + ", target " + squadTarget);
		} catch (Exception e) {
			System.out.println("Drone Exception");
			e.printStackTrace();
		}
	}

	static void doBasher()
	{
		try {
			//updateSquadInfo();
			if (Clock.getRoundNum()<1800) {
			squadTarget = rc.senseEnemyHQLocation();
			}

			else{
				aggCoef = 1000;
				MapLocation towers[] = rc.senseEnemyTowerLocations();
				MapLocation closest = rc.senseEnemyHQLocation();
				for(MapLocation loc: towers){
					if(myLocation.distanceSquaredTo(loc)<myLocation.distanceSquaredTo(closest))
						closest = loc;
				squadTarget = closest;
				}
			}
			attackSomething();
			movePotential();

		} catch (Exception e) {
			System.out.println("Soldier Exception");
			e.printStackTrace();
		}
	}

	static void doSoldier()
	{
		try {
			//updateSquadInfo();
			if (Clock.getRoundNum()<1800) {
			squadTarget = rc.senseEnemyHQLocation();
			
			}

			else{
				aggCoef = 1000;
				MapLocation towers[] = rc.senseEnemyTowerLocations();
				MapLocation closest = rc.senseEnemyHQLocation();
				for(MapLocation loc: towers){
					if(myLocation.distanceSquaredTo(loc)<myLocation.distanceSquaredTo(closest))
						closest = loc;
				squadTarget = closest;
				}
			}
			attackSomething();
			movePotential();

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
			RobotInfo[] ourTeam = rc.senseNearbyRobots(100000, rc.getTeam());
			int n = 0; // current number of miner factories
			int m = 0; // current number of barracks
			int o = 0; // current number of helipads
			int s = 0; // current number of supply depots
			int mi = 0; // current number of miners
			int tf = 0; // tank factories
			for(RobotInfo ri: ourTeam)
			{ // count up stuff
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
				}else if(ri.type==RobotType.MINER){
					mi++;
				}
			}
			// sit still and mine until we have 
			if(n<1)
			{
				tryBuild(facing,RobotType.MINERFACTORY);
				if(rc.isCoreReady()&&rc.canMine()){
					rc.mine();
				}
			}
			/*
			else if (o<1)
			{
				tryBuild(facing.opposite(),RobotType.HELIPAD);
				if(rc.isCoreReady()&&rc.canMine()){
					rc.mine();
				}
			}
			
			else if (m<1)
			{
				tryBuild(facing.opposite(),RobotType.BARRACKS);
				if(rc.isCoreReady()&&rc.canMine()){
					rc.mine();
				}
			}
			*/
			else
			{
				if(n < numMinerFactories && (n == o)) 
				{
					tryBuild(facing,RobotType.MINERFACTORY);
				} 
				else if(s<numSupplyDepots && mi>1)// && m>1)
				{
					tryBuild(facing.opposite(),RobotType.SUPPLYDEPOT);
				}
				else if(o<numHelipads)
				{
					tryBuild(facing,RobotType.HELIPAD);
				}
				else if(m<numBarracks)
				{
					tryBuild(facing.opposite(),RobotType.BARRACKS);
				}
				else if(tf<numTankFactories  && m>0)
				{
					tryBuild(facing.opposite(),RobotType.TANKFACTORY);
				}
				attackSomething();
				minerOperation(true, false); // (searching?, supplying?)
				transferSupplies(0.5);
			}
			
		} catch (Exception e) {
			System.out.println("Beaver Exception");
			e.printStackTrace();
		}
	}
	
//===========================================================================================
	
	static void doMiner()
	{
		try {
			if (Clock.getRoundNum()<1800) {
				rc.setIndicatorString(2, toString(minerState));
				attackSomething();
				// defensiveManeuvers();  ???
				miningDuties();
				int message = rc.readBroadcast(myMinerID);
				rc.setIndicatorString(0, "Miner ID = " + myMinerID);
			}else{
				aggCoef = 1000;
				MapLocation towers[] = rc.senseEnemyTowerLocations();
				MapLocation closest = rc.senseEnemyHQLocation();
				for(MapLocation loc: towers){
					if(myLocation.distanceSquaredTo(loc)<myLocation.distanceSquaredTo(closest))
						closest = loc;
				squadTarget = closest;
				attackSomething();
				movePotential();
				}
			}
		} catch (Exception e) {
			System.out.println("Miner Exception");
			//e.printStackTrace();
		}
	}

	static void miningDuties() throws GameActionException
	{
		oreHere = rc.senseOre(myLocation);
		double mineScore;
		updateMinerInfo(); // tells HQ numbers of each type of miner and gets instructions about promoting suppliers to searchers
		// depending on what state the miner is in, execute functionality
		switch (minerState)
		{
		case LEADING:
			mineScore = mineScore();
			if( (oreHere<lastLocationStartingOre && mineScore < 60) || oreHere < 10) // less ore and can't max out
				minerState = MinerState.SEARCHING; // switch to searching, this will be reported to HQ next round
			else // we've been sitting still and mining
				mineAndMoveStraight();
			break;
		case SEARCHING:
			minerOperation(true, false); // (searching?, supplying?)
			transferSupplies(0.5);
			mineScore = mineScore();
			if(mineScore > 60)
				minerState = MinerState.LEADING; // switch to a leader, this will be reported to HQ next round
			break;
		case SUPPLYING:
			rc.setIndicatorDot(myLocation.add(0,-1), 255,0,0); // white dots on supply line
			if(myMinerID!=rc.readBroadcast(minerContiguousID)-1) // if we're the most recent miner, stay put
				minerOperation(false, true); // (searching?, supplying?)
			transferSupplies(0.9);
			break;
		}
	}

	static double mineScore() throws GameActionException {
		int[] x = {0,-1, 0, 0, 1,-1,-1, 1, 1,-2, 0, 0, 2,-2,-2, 2, 2};
		int[] y = {0, 0,-1, 1, 0,-1, 1,-1, 1, 0,-2, 2, 0,-2, 2,-2, 2};
		double[] thisMine = vectorSumOre(x,y);
		double thisMineScore = thisMine[2];
		return thisMineScore;
	}
	
	static void mineAndMoveStraight() throws GameActionException {
		if(rc.senseOre(myLocation)>=10){ //there is plenty of ore, so try to mine
			if(rand.nextDouble()<1){ // mine 100% of time we can collect max
				if(rc.isCoreReady()&&rc.canMine()){
					rc.mine();
					justMoved = false;
				}
			}else{
				justMoved = tryMove(facing);
			}
		}else{
			justMoved = tryMove(facing);
		}
		if(justMoved) // update the starting amount of ore on each space we move to
		{
			lastLocationStartingOre = currentLocationStartingOre; 
			currentLocationStartingOre = rc.senseOre(rc.getLocation());
		}
	}
	
	static void minerOperation(boolean searching, boolean supplying) throws GameActionException {
		
		if(rc.senseOre(myLocation)>=10){ //there is plenty of ore, so try to mine
			if(rand.nextDouble()<0.98){ // mine 98% of time we can collect max
				if(rc.isCoreReady()&&rc.canMine()){
					rc.mine();
				}
			}else{
				facing = minerPotential(searching,supplying);
				tryMove(facing);
			}
			
		}else if( rc.senseOre(myLocation)>0.8){ //there is a bit of ore, so maybe try to mine, maybe move on (suppliers don't mine)
			if(rand.nextDouble()<0.2){ // mine
				if(rc.isCoreReady()&&rc.canMine()){
					rc.mine();
				}
			}else{ // look for more ore
				facing = minerPotential(searching,supplying);
				tryMove(facing);
			}
		}else{ //no ore, so look for more
			facing = minerPotential(searching,supplying);
			tryMove(facing);
		}
	}
	
	
	// Miner's potential field calculation.  Yields an integer representing the movement direction 0-7.  A null value means not to move.
	static Direction minerPotential(boolean searching, boolean supplying) throws GameActionException {
		
		double mineScore = 0;
		int dx = 0;
		int dy = 0;
		int totalPotential[] = {0,0};
		
		if(searching)
		{
			// nearest-neighbor ore sensing
			int x1[] = {0,1,1,1,0,-1,-1,-1};
			int y1[] = {1,1,0,-1,-1,-1,0,1};
			double innerPotential[] = vectorSumOre(x1,y1);
			mineScore += innerPotential[2];
			totalPotential[0] += (int) (2*innerPotential[0]);
			totalPotential[1] += (int) (2*innerPotential[1]);
			
			// next-nearest-neighbor ore sensing
			int x2[] = {0,2,2,2,0,-2,-2,-2};
			int y2[] = {2,2,0,-2,-2,-2,0,2};
			double outerPotential[] = vectorSumOre(x2,y2);
			mineScore += outerPotential[2];
			totalPotential[0] += (int) (outerPotential[0]/10);
			totalPotential[1] += (int) (outerPotential[1]/10);
			
			// global target direction: WORK ON THIS!!!!
			// IDEA: save nearby locations within this robot's internal variables
			int globalBestMineScore = rc.readBroadcast(bestMineScoreChan);
			if(mineScore > globalBestMineScore){
				rc.broadcast(bestMineScoreChan, (int)mineScore);
				rc.broadcast(bestMineXChan, myLocation.x);
				rc.broadcast(bestMineYChan, myLocation.y);
				//if(rc.getType()==RobotType.BEAVER && mineScore > 140){ // have a beaver build a miner factory on the spot
				//	tryBuild(facing,RobotType.MINERFACTORY);
				//}
			}
			else{
				int targetX = rc.readBroadcast(bestMineXChan);
				int targetY = rc.readBroadcast(bestMineYChan);
				double globalPullFactor = Math.max(0,globalBestMineScore)/20; // pull towards a good mine is proportional to the value at that mine
				dx = (targetX - myLocation.x);
				dy = (targetY - myLocation.y);
				double dist = dx*dx + dy*dy;
				double px = 0;
				double py = 0;
				if(mineScore<40) // terrible spot, make the pull great, and distance-independent!
				{
					px = dx*globalPullFactor/20;
					py = dy*globalPullFactor/20;
				}
				else
				{
					px = dx*globalPullFactor/dist;
					py = dy*globalPullFactor/dist;
				}
				totalPotential[0] += (int) px;
				totalPotential[1] += (int) py;
				//System.out.println(myMinerID + " global pull = (" + (int)px + "," + (int)py + "), inner = (" + (int)(2*innerPotential[0]) + "," + (int)(2*innerPotential[1]) + "), outer = (" + (int)(outerPotential[0]/10) + "," + (int)(outerPotential[1]/10) + ")");
			}
		}
		if(supplying)
		{
			// attraction to chain of suppliers: attract twice as much to one in front (to within supply transfer radius) as to one behind
			// miners have "contiguous ID" starting at message board channel 500, each new miner assigned the next ID.  contiguous ID = channel.
			int message = rc.readBroadcast(myMinerID);
			
			// find guy in front
			if(myMinerID!=500) // unless i'm first
			{
				boolean found = false;
				boolean endOfLine = false;
				int nextMinerID = myMinerID-1;
				RobotInfo robo = null;
				while( !found && nextMinerID >= 500 && !endOfLine )
				{
					int nextRobotID = rc.readBroadcast(nextMinerID);
					if(nextRobotID==-1) // we've reached ones that have already been recruited to be searchers
					{
						endOfLine = true;
						continue;
					}
					try	{
						robo = rc.senseRobot(nextRobotID);
					}
					catch (Exception e)	{
						nextMinerID--;
					}
					if(robo!=null)
						found = true;
				}
				if(found) // here's the robot to follow
				{
					dx = (robo.location.x - myLocation.x);
					dy = (robo.location.y - myLocation.y);
				}
				else // no one ahead of me to follow
				{
					// go toward the best mine!
					int targetX = rc.readBroadcast(bestMineXChan);
					int targetY = rc.readBroadcast(bestMineYChan);
					int globalBestMineScore = rc.readBroadcast(bestMineScoreChan);
					double globalPullFactor = Math.max(0,globalBestMineScore)*10; // pull towards a good mine is proportional to the value at that mine
					dx = (targetX - myLocation.x);
					dy = (targetY - myLocation.y);
					double dist = dx*dx + dy*dy;
					double px = dx*globalPullFactor/dist;
					double py = dy*globalPullFactor/dist;
					totalPotential[0] += (int) px;
					totalPotential[1] += (int) py;
					//if(!endOfLine) // end of line should have no one to follow, but if i'm not the end of the line, then i have lost my way
					//	minerState = MinerState.SEARCHING; // by default i start searching
				}
			}
			int forwardPullFactor = 2*Math.max(dx*dx + dy*dy - 15, 0); // zero if we're in supply transfer radius, increasing as we're farther away
			totalPotential[0] += dx*forwardPullFactor;
			totalPotential[1] += dy*forwardPullFactor;
			
			// find guy behind
			int unbornMinerID = rc.readBroadcast(minerContiguousID);
			if(myMinerID != unbornMinerID-1) // as long as i am not the last miner created
			{
				boolean found = false;
				int prevMinerID = myMinerID+1;
				RobotInfo robo = null;
				while( !found && prevMinerID < unbornMinerID )
				{
					int prevRobotID = rc.readBroadcast(prevMinerID);
					try	{
						robo = rc.senseRobot(prevRobotID);
					}
					catch (Exception e)	{
						prevMinerID++;
					}
					if(robo!=null)
						found = true;
				}
				if(found) // here's the robot to follow
				{
					dx = (robo.location.x - myLocation.x);
					dy = (robo.location.y - myLocation.y);
				}
			}
			int backwardPullFactor = 10*Math.max(dx*dx + dy*dy - 15, 0); // zero if we're in supply transfer radius, increasing as we're farther away
			totalPotential[0] += dx*backwardPullFactor;
			totalPotential[1] += dy*backwardPullFactor;
			
		}
		
		// total direction
		Direction bestDirection = myLocation.directionTo( myLocation.add((int)totalPotential[0],(int)totalPotential[1]) ); // direction to move
		rc.setIndicatorString(1, "mining value =  " + (int) mineScore + "best direction =  " + bestDirection.toString());
		// don't go back to your last spot ever
		if(bestDirection==facing.opposite())
			bestDirection = getRandomDirection();
		return bestDirection;
	}

	static double[] vectorSumOre(int[] x, int[] y) throws GameActionException {
		MapLocation sensingRegion[] = new MapLocation[x.length];
		for(int a=0; a<x.length; a++){
			sensingRegion[a] = myLocation.add(x[a],y[a]);
		}
		double ore = 0;
		int i=0;
		double potentialX = 0;
		double potentialY = 0;
		double mineScore = -1*x.length*5; // makes it so that a flat region of 5 ore will have a score of 0
		for(MapLocation m: sensingRegion){
			ore = rc.senseOre(m);
			ore = (double)ore;
			RobotInfo robo = rc.senseRobotAtLocation(m);
			TerrainTile tile = rc.senseTerrainTile(m);
			mineScore += ore;
			//if(robo.type!=RobotType.MINER || robo.team!=rc.getTeam()){ // if there's a miner there, don't go toward it
			if( robo==null && tile.isTraversable() && isSafeDirection(myLocation.directionTo(m))){
				potentialX += ore*x[i];
				potentialY += ore*y[i];
			}else if(robo!=null){ // repulsion from other robots
				potentialX -= 10*x[i];
				potentialY -= 10*y[i];
			}else{ // try to make it so we won't try to go where we can't
				potentialX -= 1*x[i];
				potentialY -= 1*y[i];
			}
			i++;
		}
		double potential[] = {potentialX, potentialY, mineScore};
		return potential;
	}
	
	// This method updates miner-specific info on the message board
	static void updateMinerInfo() throws GameActionException
	{
		int roundNow = Clock.getRoundNum();
		
		// first check to see if HQ says a supplier should be promoted to a searcher
		if(minerState == MinerState.SUPPLYING){
			int orders = rc.readBroadcast(minerShuffle);
			boolean firstInLine = false;
			int nextRobotID = rc.readBroadcast(myMinerID-1);
			if(nextRobotID==-1 || myMinerID==500) // is the guy before me a searcher, or am i the very first?
				firstInLine = true;
			if(orders > 0 && firstInLine) // only promote the ones at the end of the supply line
			{
				minerState = MinerState.SEARCHING; // promotion
				rc.broadcast(minerShuffle, orders-1); // make it known
				rc.broadcast(myMinerID, -1); // ruin my robot ID
			}
		}
		
		// now update message board and signify my presence
		switch(minerState){
		
		case SUPPLYING:
			int numSupplying = rc.readBroadcast(minersSupplying);
			//System.out.println("read " + numSupplying + " supplying");
			if (roundNow != (numSupplying >> 16)) // if this value has not been updated this round already by another miner
			{
				numSupplying = roundNow << 16; // make it known that i updated it this round
				numSupplying++; // and add one for me to the accumulated number
			}
			else
			{
				numSupplying++; // and add one for me to the accumulated number
			}
			rc.broadcast(minersSupplying, numSupplying); // broadcast
			//System.out.println("wrote " + numSupplying + " supplying, and that is really " + (numSupplying & 255) );
			break;
			
		case SEARCHING:
			int numSearching = rc.readBroadcast(minersSearching);
			//System.out.println("read " + numSearching + " supplying");
			if (roundNow != (numSearching >> 16)) // if this value has not been updated this round already by another miner
			{
				numSearching = roundNow << 16; // make it known that i updated it this round
				numSearching++; // and add one for me to the accumulated number
			}
			else
			{
				numSearching++; // and add one for me to the accumulated number
			}
			rc.broadcast(minersSearching, numSearching); // broadcast
			//System.out.println("wrote " + numSearching + " supplying");
			break;
			
		case LEADING:
			int numLeading = rc.readBroadcast(minersLeading);
			//System.out.println("read " + numLeading + " supplying");
			if (roundNow != (numLeading >> 16)) // if this value has not been updated this round already by another miner
			{
				numLeading = roundNow << 16; // make it known that i updated it this round
				numLeading++; // and add one for me to the accumulated number
			}
			else
			{
				numLeading++; // and add one for me to the accumulated number
			}
			rc.broadcast(minersLeading, numLeading); // broadcast
			//System.out.println("wrote " + numLeading + " supplying");
			break;
		}
	}

//=============================================================================================

	



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
	static void transferSupplies(double fraction) throws GameActionException {
		RobotInfo[] nearbyAllies = rc.senseNearbyRobots(myLocation,GameConstants.SUPPLY_TRANSFER_RADIUS_SQUARED,myTeam);
		double mySupply = rc.getSupplyLevel();
		double lowestSupply = mySupply;
		double transferAmount = 0;
		MapLocation suppliesToThisLocation = null;
		for(RobotInfo ri:nearbyAllies){ // only transfer to bots with less supply
			if(ri.supplyLevel<lowestSupply){
				lowestSupply = ri.supplyLevel;
				transferAmount = mySupply*fraction;
				suppliesToThisLocation = ri.location;
			}
		}
		if(suppliesToThisLocation!=null && transferAmount>100){ // if we're doing a transfer
			MapLocation myHQ = rc.senseHQLocation();
			int dxOther = (suppliesToThisLocation.x - myHQ.x);
			int dyOther = (suppliesToThisLocation.y - myHQ.y);
			int dxMe = (myLocation.x - myHQ.x);
			int dyMe = (myLocation.y - myHQ.y);
			int distance = dxOther*dxOther + dyOther*dyOther - dxMe*dxMe - dyMe*dyMe;
			if(distance>0){ // make sure the other guy is farther from HQ
				rc.transferSupplies((int)transferAmount, suppliesToThisLocation);
			}
		}
	}
	
	static boolean isSafeDirection(Direction dir) throws GameActionException { //checks if the facing direction is safe from towers
		MapLocation tileInFront = myLocation.add(dir);
		boolean goodSpace = true;
		//check that the direction in front is not a tile that can be attacked by the enemy towers
		MapLocation[] enemyTowers = rc.senseEnemyTowerLocations();
		for(MapLocation m: enemyTowers){
			if(m.distanceSquaredTo(tileInFront)<=RobotType.TOWER.attackRadiusSquared){
				goodSpace = false; //space in range of enemy towers
				break;
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
	
		
	
	// Move Around: random moves; go left if hitting barrier; avoid towers
	static void moveAround(boolean defensive) throws GameActionException 
	{
		if(rand.nextDouble()<0.8){
			if(rand.nextDouble()<0.5){
				facing = facing.rotateLeft();
			}else{
				facing = facing.rotateRight();
			}
		}else{
			if(rc.getID()%2==0){ // every other robot, with even ID #, are offensive, rest defensive
				MapLocation towers[] = rc.senseEnemyTowerLocations();
				int t = (int) rand.nextInt(towers.length); // defense picks one of our towers at random
				facing = myLocation.directionTo(towers[t]);
			}
			else{
				MapLocation towers[] = rc.senseTowerLocations();
				int t = (int) rand.nextInt(towers.length); // defense picks one of our towers at random
				facing = myLocation.directionTo(towers[t]);
			}
		}
		if(defensive)
			tryMove(facing);
		else
			tryAggressiveMove(facing);
	}
	
	// Aggressive Move (does not avoid towers)
	static void aggMove() throws GameActionException {

        facing = myLocation.directionTo(squadTarget);
        
		if(rand.nextDouble()<0.15){
			if(rand.nextDouble()<0.5){
				facing = facing.rotateLeft();
			}else{
				facing = facing.rotateRight();
			}
		}
		
		MapLocation tileInFront = myLocation.add(facing);

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
		static void movePotential() throws GameActionException {

			float forceX = 0.0f;
			float forceY = 0.0f;

			float fDest = 3.0f;
			float fBored = 1.5f;

			float qFriendly = -2.0f; // repel, goes as 1/r

			float fSticky = 0.0f; // goes as 1/r
			float fBinding = 1.0f;
			float fNoiseMax = 1.0f;
			float fEnemySighted = 1.5f;
			float qEnemyInRange = -2.0f; //is multiplied by attack strength
			//float aggCoef = 0.8f;
			float fNoise = 0.0f;
			
			float strengthBal = unitVal[rc.getType().ordinal()];
			
			// get dem robots
			RobotInfo[] friendlyRobots = rc.senseNearbyRobots(
					myType.sensorRadiusSquared, myTeam);
			RobotInfo[] enemyRobots = rc.senseNearbyRobots(
					myType.sensorRadiusSquared, myTeam.opponent());
			for (RobotInfo bot : friendlyRobots) strengthBal += unitVal[bot.type.ordinal()];
			for (RobotInfo bot : enemyRobots) strengthBal -= unitVal[bot.type.ordinal()];

			// attracted to squad target, far away
			int destX = (squadTarget.x - myLocation.x);
			int destY = (squadTarget.y - myLocation.y);

			float d2dest = (float) Math.sqrt(destX * destX + destY * destY);

			forceX = (fDest + aggCoef)* destX / d2dest;
			forceY = (fDest + aggCoef) * destY / d2dest;
			// attraction to target goes as const qDest
			
			
			// ********** BOREDOM ENGINE  **************
			// Move impatience: if not moving or rocking, stop it and get attracted randomly for a while.
			//rc.setIndicatorString(0, "moves: " + myLocation + " " + moveRec[moveIdx] + " " + moveRec[moveIdx-1]+" " + moveRec[moveIdx-2]);
			lastMoveCounter += 1;
			lastShotCounter +=1;

			if (bored == 1){

				// if bored add a force in direction boredDir for boredLength ticks;
				forceX += fBored * senseLocsX[boredDirIdx];
				forceY += fBored * senseLocsY[boredDirIdx];		
				boredCounter += 1; 
				rc.setIndicatorString(1, "Boredom Counter = " + boredCounter);
				if (boredCounter > boredomLength){
					bored = 0;
					boredCounter = 0;
					lastMoveCounter = 0;
					lastShotCounter = 0;
					rc.setIndicatorString(0, "not bored");
				}

			}else if (lastMoveCounter > moveImpatience || myLocation.equals(moveRec[moveIdx - 2])){
				rc.setIndicatorString(0, "BORED!");
				bored = 1;
				boredomLength = moveBoredomLength;
				boredDirIdx = rand.nextInt(8);
				
			}else if (lastShotCounter > shootImpatience){
				rc.setIndicatorString(0, "BORED! Sweeping...");
				bored = 1;
				boredomLength = sweepBoredomLength;
				boredDirIdx = rand.nextInt(8);
				
			}else rc.setIndicatorString(0, "not bored");

			

			rc.setIndicatorString(2, "lastShot Counter = " + lastShotCounter);

		
			for (RobotInfo bot : friendlyRobots) {


				// doesn't apply to towers and HQ
				if (bot.type == RobotType.TOWER || bot.type == RobotType.HQ)
					continue;

				int vecx = bot.location.x - myLocation.x;
				int vecy = bot.location.y - myLocation.y;
				int d2 = bot.location.distanceSquaredTo(myLocation);
				float id = invSqrt[d2];

				// weakly attract, constant normalized to unitVal 
				forceX += unitVal[bot.type.ordinal()] * fSticky *id * vecx; // constant
				forceY += unitVal[bot.type.ordinal()] * fSticky *id* vecy;	
		
				// don't get too close, at close range, as -1/r
				forceX += qFriendly * id * id*vecx;
				forceY += qFriendly * id * id*vecy;
				
			}

			for (RobotInfo bot : enemyRobots) {
				// dangerous ones, dealt with below
				if (bot.type == RobotType.TOWER || bot.type == RobotType.HQ)
					continue;

				int vecx = bot.location.x - myLocation.x;
				int vecy = bot.location.y - myLocation.y;
				int d2 = bot.location.distanceSquaredTo(myLocation);


				float id = invSqrt[d2];

				// attract to enemy units, adjusted for strength balance, constant
				forceX += strengthBal * fEnemySighted * unitAtt[bot.type.ordinal()] * id  * vecx ; // constant 
				forceY += strengthBal * fEnemySighted * unitAtt[bot.type.ordinal()] * id  * vecy ;


				// enemies in range, 1/r
				float dattack = sqrt[bot.type.attackRadiusSquared] - sqrt[d2]
						+ 2.0f;
				if (dattack > 0) {
					forceX += unitVal[bot.type.ordinal()] * qEnemyInRange * id*id * vecx; // constant
					forceY += unitVal[bot.type.ordinal()] * qEnemyInRange * id *id * vecy;	
				}
			}

			boolean hasSquad = friendlyRobots.length > 8
					|| ((rc.readBroadcast(squadTaskBase + mySquad) >> 8) & 255) > 8;

					MapLocation[] towers = getBuildings(myTeam.opponent());
					
					MapLocation enHQ = towers[0];
					int d2 = enHQ.distanceSquaredTo(myLocation);
					// check if it's in range
					if (d2 <= RobotType.HQ.attackRadiusSquared + 20){

						float id = invSqrt[d2];

						int vecx = enHQ.x - myLocation.x;
						int vecy = enHQ.y - myLocation.y;


						float dattack = sqrt[RobotType.HQ.attackRadiusSquared] - sqrt[d2]
								+ 2.0f;		

						if (dattack > 0) {
							// repel with fEnemyInRange 
							forceX += unitVal[0] * qEnemyInRange * id * vecx ; // hard shell
							forceY += unitVal[0] * qEnemyInRange * id * vecy ;	
						}

					}
					
					for (MapLocation tower : towers) {
						d2 = tower.distanceSquaredTo(myLocation);
						// check if it's in range
						if (d2 > RobotType.TOWER.attackRadiusSquared + 20)
							continue;

						float id = invSqrt[d2];

						int vecx = tower.x - myLocation.x;
						int vecy = tower.y - myLocation.y;


						float dattack = sqrt[RobotType.TOWER.attackRadiusSquared] - sqrt[d2]
								+ 2.0f;		
						
						if (dattack > 0) {
							// repel with fEnemyInRange 
							forceX += unitVal[1] * qEnemyInRange * id * vecx ; // hard shell
							forceY += unitVal[1] * qEnemyInRange * id * vecy ;	
						}
					}
					
						
						// get direction of force
					MapValue[] mvs = new MapValue[9];

					for (int i = 0; i < 9; i++)
						mvs[i] = new MapValue(senseLocsX[i], senseLocsY[i], forceX
								* senseLocsX[i] + forceY * senseLocsY[i]);

					rc.setIndicatorLine(myLocation, new MapLocation(myLocation.x
							+ (int) (forceX * 10), myLocation.y + (int) (forceY * 10)), 0,
							255, 255);

					Arrays.sort(mvs);

					for (int i = 8; i > 0; i--) {
						// don't move unless you exceed "binding force"
						fNoise *= fNoiseMax * 2 * (rand.nextDouble() - 0.5);
						if (mvs[i].value < fBinding + fNoise)
							break;
						Direction newdir = myLocation.directionTo(mvs[i]
								.offsetFrom(myLocation));
						if (rc.isCoreReady() && rc.canMove(newdir)) {
							
							rc.move(newdir);					
							moveIdx += 1;
							moveRec[moveIdx] = myLocation.add(newdir);
							lastMoveCounter = 0;
							//rc.setIndicatorString(0, "moveIdx = " + moveIdx);

						}
					}
		}


	// This method will attempt to move in Direction d (or as close to it as possible)
	static boolean tryMove(Direction d) throws GameActionException {
		int dirint = directionToInt(d);
		Direction testDir = directions[(dirint+0+8)%8];
		if(rc.isCoreReady()){
			if(rc.canMove(testDir) && isSafeDirection(testDir) && testDir!=facing.opposite()){
				rc.move(testDir);
				return true;
			}else
				testDir = directions[(dirint+1+8)%8];
			if(rc.canMove(testDir) && isSafeDirection(testDir) && testDir!=facing.opposite()){
				rc.move(testDir);
				return true;
			}else
				testDir = directions[(dirint-1+8)%8];
			if(rc.canMove(testDir) && isSafeDirection(testDir) && testDir!=facing.opposite()){
				rc.move(testDir);
				return true;
			}else
				testDir = directions[(dirint+2+8)%8];
			if(rc.canMove(testDir) && isSafeDirection(testDir) && testDir!=facing.opposite()){
				rc.move(testDir);
				return true;
			}else
				testDir = directions[(dirint-2+8)%8];
			if(rc.canMove(testDir) && isSafeDirection(testDir) && testDir!=facing.opposite()){
				rc.move(testDir);
				return true;
			}else
				testDir = directions[(dirint+3+8)%8];
			if(rc.canMove(testDir) && isSafeDirection(testDir) && testDir!=facing.opposite()){
				rc.move(testDir);
				return true;
			}else
					testDir = directions[(dirint-3+8)%8];
				if(rc.canMove(testDir) && isSafeDirection(testDir) && testDir!=facing.opposite()){
					rc.move(testDir);
					return true;
			}else
				return false;
		}else
			return false;
	}
	
	// This method will attempt to move in Direction d (or as close to it as possible)
		static boolean tryAggressiveMove(Direction d) throws GameActionException {
			int dirint = directionToInt(d);
			Direction testDir = directions[(dirint+0+8)%8];
			if(rc.isCoreReady()){
				if(rc.canMove(testDir) && testDir!=facing.opposite()){
					rc.move(testDir);
					return true;
				}else
					testDir = directions[(dirint+1+8)%8];
				if(rc.canMove(testDir) && testDir!=facing.opposite()){
					rc.move(testDir);
					return true;
				}else
					testDir = directions[(dirint-1+8)%8];
				if(rc.canMove(testDir) && testDir!=facing.opposite()){
					rc.move(testDir);
					return true;
				}else
					testDir = directions[(dirint+2+8)%8];
				if(rc.canMove(testDir) && testDir!=facing.opposite()){
					rc.move(testDir);
					return true;
				}else
					testDir = directions[(dirint-2+8)%8];
				if(rc.canMove(testDir) && testDir!=facing.opposite()){
					rc.move(testDir);
					return true;
				}else
					testDir = directions[(dirint+3+8)%8];
				if(rc.canMove(testDir) && testDir!=facing.opposite()){
					rc.move(testDir);
					return true;
				}else
						testDir = directions[(dirint-3+8)%8];
					if(rc.canMove(testDir) && testDir!=facing.opposite()){
						rc.move(testDir);
						return true;
				}else
					return false;
			}else
				return false;
		}

	// This method will attempt to spawn in the given direction (or as close to it as possible)
	static void trySpawn(Direction d, RobotType type) throws GameActionException {
		int offsetIndex = 0;
		int[] offsets = {0,1,-1,2,-2,3,-3,4};
		int dirint = directionToInt(d);
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
		while (offsetIndex < 8 && !rc.canMove(directions[(dirint+offsets[offsetIndex]+8)%8])) {
			offsetIndex++;
		}
		if (offsetIndex < 8 && rc.getTeamOre()>type.oreCost && rc.isCoreReady()) {
			rc.build(directions[(dirint+offsets[offsetIndex]+8)%8], type);
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
		RobotInfo[] unitsAround = rc.senseNearbyRobots(5, myTeam); // whoever i can see on my team
		// potential field becomes the facing direction
		double px = 0;
		double py = 0;
		for(RobotInfo ri: unitsAround){
			if(ri.type==RobotType.HQ || ri.type==RobotType.TOWER){
				int dx = ri.location.x - myLocation.x;
				int dy = ri.location.y - myLocation.y;
				double d = 1 + dx*dx + dy*dy; // r^2
				px += tanksPerTarget * dx / d;
				py += tanksPerTarget * dy / d;
			}else if(ri.type==RobotType.TANK){
				int dx = ri.location.x - myLocation.x;
				int dy = ri.location.y - myLocation.y;
				double d = 1 + dx*dx + dy*dy; // r^2
				px -= 2 * dx / d;
				py -= 2 * dy / d;
			}
			px = 10*px;
			py = 10*py;
		}
		for(int i=0; i<8; i++){
			TerrainTile terrain = rc.senseTerrainTile(myLocation.add(senseLocsX[i],senseLocsY[i])); // get repelled by bad terrain
			if(terrain!=TerrainTile.NORMAL){
				px -= 100 * senseLocsX[i];
				py -= 100 * senseLocsY[i];
			}
		}
		facing = myLocation.directionTo(myLocation.add((int) px, (int) py));
		if(px==0 && py==0){ // nothing around to sense, move between towers and HQ
			facing = getRandomDirection();
		}
		tryMove(facing);
	}
	
	// get all buildings, including HQ, in a single uniform list
	static MapLocation[] getBuildings(Team team) {
		MapLocation[] buildings;
		if (team == myTeam) {
			MapLocation[] towers = rc.senseTowerLocations();
			MapLocation hq = rc.senseHQLocation();
			buildings = new MapLocation[towers.length + 1];
			System.arraycopy(towers, 0, buildings, 1, towers.length);
			buildings[0] = hq;
		} else {
			MapLocation[] towers = rc.senseEnemyTowerLocations();
			MapLocation hq = rc.senseEnemyHQLocation();
			buildings = new MapLocation[towers.length + 1];
			System.arraycopy(towers, 0, buildings, 1, towers.length);
			buildings[0] = hq;
		}
		return buildings;
	}
	
}
