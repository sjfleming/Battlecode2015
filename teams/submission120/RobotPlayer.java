package submission120;

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
	
	//**************************************************************************************************************

	// Unit Power Rankings (unitless) 
	// HQ, TOWER, SD, TECH, RACKS, HELI, FIELD, TFACT, MFACT, WASH, SPACE, BEAV, COMP, SOLDIER, BASH, MINE, DRONE, TANK, COMM, LAUNCH, MISSILE
	//static float unitVal[] = {100.0f, 100.0f, 0.0f, 0.0f, 0.0f, 0.0f,0.0f, 0.0f,0.0f, 0.0f,0.0f, 0.6f, 0.0f, 1.0f, 2.3f, 0.6f, 1.2f, 3.8f, 8.0f, 10.0f, 0.2f};
	static int unitVal[] = {1000, 1000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 1, 1, 8, 8, 10, 0};

	// Unit Attack Affinities (unitless)
	//static float unitAtt[] = {0.0f, 0.0f, 1.0f, 1.0f,4.0f, 4.0f,1.0f, 4.0f,5.0f, 1.0f,1.0f, 3.0f, 1.0f, 2.0f, 2.0f, 3.0f, 1.0f, 1.0f, 1.0f, 1.0f,1.0f};
	static int unitAtt[] = {0, 0, 0, 1, 4, 4, 1, 5, 6, 1, 4, 3, 1, 2, 2, 6, 2, 1, 1, 1, 4};

	// agg coefficient
	//static float aggCoef = 0.8f;
	
	//**************************************************************************************************************

	//===============================================================================================================
	
	// Keep track of best mine, score and location
	static int bestMineScoreChan = 1;
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
	
	// Supply transfer parameter
	static double supplyTransferFraction = 0.5;
	
	// Local enemy targets
	static int[] targetChannels = {200,201,202,203,204,205}; // channels to keep their robotIDs
	
	//===============================================================================================================

	// Adjustable parameters
	static int numBeavers = 4;
	static int numSoldiers = 4;
	static int numMinerFactories = 1;
	static int numMiners = 40;
	static int numBarracks = 1;
	static int numHelipads = 0;
	static int numSupplyDepots = 3;
	static int numTankFactories = 6;
	static int numTanks = 999;
	static int ATTACK_ROUND = 1700;
	
	
	/* Sensing location defines etc */
	static int senseLocsShort = 69;
	static int senseLocsLong = 109;
	static int[] senseLocsX = {0,-1,0,0,1,-1,-1,1,1,-2,0,0,2,-2,-2,-1,-1,1,1,2,2,-2,-2,2,2,-3,0,0,3,-3,-3,-1,-1,1,1,3,3,-3,-3,-2,-2,2,2,3,3,-4,0,0,4,-4,-4,-1,-1,1,1,4,4,-3,-3,3,3,-4,-4,-2,-2,2,2,4,4,-5,-4,-4,-3,-3,0,0,3,3,4,4,5,-5,-5,-1,-1,1,1,5,5,-5,-5,-2,-2,2,2,5,5,-4,-4,4,4,-5,-5,-3,-3,3,3,5,5};
	static int[] senseLocsY = {0,0,-1,1,0,-1,1,-1,1,0,-2,2,0,-1,1,-2,2,-2,2,-1,1,-2,2,-2,2,0,-3,3,0,-1,1,-3,3,-3,3,-1,1,-2,2,-3,3,-3,3,-2,2,0,-4,4,0,-1,1,-4,4,-4,4,-1,1,-3,3,-3,3,-2,2,-4,4,-4,4,-2,2,0,-3,3,-4,4,-5,5,-4,4,-3,3,0,-1,1,-5,5,-5,5,-1,1,-2,2,-5,5,-5,5,-2,2,-4,4,-4,4,-3,3,-5,5,-5,5,-3,3};
	static int[] senseLocsR = {0,10,10,10,10,14,14,14,14,20,20,20,20,22,22,22,22,22,22,22,22,28,28,28,28,30,30,30,30,31,31,31,31,31,31,31,31,36,36,36,36,36,36,36,36,40,40,40,40,41,41,41,41,41,41,41,41,42,42,42,42,44,44,44,44,44,44,44,44,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,53,53,53,53,53,53,53,53,56,56,56,56,58,58,58,58,58,58,58,58};
	static float[] sqrt = {0.000000f,1.000000f,1.414214f,1.732051f,2.000000f,2.236068f,2.449490f,2.645751f,2.828427f,3.000000f,3.162278f,3.316625f,3.464102f,3.605551f,3.741657f,3.872983f,4.000000f,4.123106f,4.242641f,4.358899f,4.472136f,4.582576f,4.690416f,4.795832f,4.898979f,5.000000f,5.099020f,5.196152f,5.291503f,5.385165f,5.477226f,5.567764f,5.656854f,5.744563f,5.830952f,5.916080f,6.000000f,6.082763f,6.164414f,6.244998f,6.324555f,6.403124f,6.480741f,6.557439f,6.633250f,6.708204f,6.782330f,6.855655f,6.928203f,7.000000f,7.071068f,7.141428f,7.211103f,7.280110f,7.348469f,7.416198f,7.483315f,7.549834f,7.615773f,7.681146f,7.745967f,7.810250f,7.874008f,7.937254f,8.000000f,8.062258f,8.124038f,8.185353f,8.246211f,8.306624f,8.366600f,8.426150f,8.485281f,8.544004f,8.602325f,8.660254f,8.717798f,8.774964f,8.831761f,8.888194f,8.944272f,9.000000f};
	static float[] invSqrt = {0.000000f,1.000000f,0.707107f,0.577350f,0.500000f,0.447214f,0.408248f,0.377964f,0.353553f,0.333333f,0.316228f,0.301511f,0.288675f,0.277350f,0.267261f,0.258199f,0.250000f,0.242536f,0.235702f,0.229416f,0.223607f,0.218218f,0.213201f,0.208514f,0.204124f,0.200000f,0.196116f,0.192450f,0.188982f,0.185695f,0.182574f,0.179605f,0.176777f,0.174078f,0.171499f,0.169031f,0.166667f,0.164399f,0.162221f,0.160128f,0.158114f,0.156174f,0.154303f,0.152499f,0.150756f,0.149071f,0.147442f,0.145865f,0.144338f,0.142857f,0.141421f,0.140028f,0.138675f,0.137361f,0.136083f,0.134840f,0.133631f,0.132453f,0.131306f,0.130189f,0.129099f,0.128037f,0.127000f,0.125988f,0.125000f,0.124035f,0.123091f,0.122169f,0.121268f,0.120386f,0.119523f,0.118678f,0.117851f,0.117041f,0.116248f,0.115470f,0.114708f,0.113961f,0.113228f,0.112509f,0.111803f,0.111111f};

	static int[] towerMask = {0,0,128,129,131,131,131,3,2,0,0,0,128,193,195,199,199,199,135,7,2,0,128,193,227,247,255,255,255,223,143,7,2,192,225,247,255,255,255,255,255,223,15,6,224,241,255,255,255,255,255,255,255,31,14,224,241,255,255,255,255,255,255,255,31,14,224,241,255,255,255,255,255,255,255,31,14,96,240,253,255,255,255,255,255,127,30,12,32,112,248,253,255,255,255,127,62,28,8,0,32,112,120,124,124,124,60,28,8,0,0,0,32,48,56,56,56,24,8,0,0};
	static int[] hqMask = {0,0,128,129,131,131,131,131,131,3,2,0,0,0,128,193,195,199,199,199,199,199,135,7,2,0,128,193,227,247,255,255,255,255,255,223,143,7,2,192,225,247,255,255,255,255,255,255,255,223,15,6,224,241,255,255,255,255,255,255,255,255,255,31,14,224,241,255,255,255,255,255,255,255,255,255,31,14,224,241,255,255,255,255,255,255,255,255,255,31,14,224,241,255,255,255,255,255,255,255,255,255,31,14,224,241,255,255,255,255,255,255,255,255,255,31,14,96,240,253,255,255,255,255,255,255,255,127,30,12,32,112,248,253,255,255,255,255,255,127,62,28,8,0,32,112,120,124,124,124,124,124,60,28,8,0,0,0,32,48,56,56,56,56,56,24,8,0,0};
	static int[] dirOffsets = {0,7,1,6,2,5,3,4};

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
				for(int i: targetChannels) // nullify targets
				{
					rc.broadcast(i,0);
				}
				break;
			case BEAVER:
				//facing = myLocation.directionTo(rc.senseEnemyHQLocation());
				break;
			case MINER:
				minerState = MinerState.SUPPLYING; // initially miners are in supply chain
				myMinerID = rc.readBroadcast(minerContiguousID); // obtain a minerID
				rc.broadcast(minerContiguousID, myMinerID + 1); // update next minerID
				rc.broadcast(myMinerID, rc.getID()); // save my robot ID on the message board
				break;
			case DRONE:
				break;
			default:
				break;
			}
		} catch (Exception e) {
			System.out.println("Initialization exception");
			//e.printStackTrace();
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
			case LAUNCHER:
				doLauncher();
				break;
			default:
				break;
			}
			
			// IMPORTANT SUPPLY TRANSFER CHANGE =======================================================================================================
			
			lastOre = curOre;
			int bytecodesLeft = Clock.getBytecodesLeft();
			if(bytecodesLeft>1200) // if you try to transfer supply and are out of bytecodes, you will queue a transfer to a location that might not be valid next round.  things move.
			{
				try {
					transferSupplies(supplyTransferFraction);
				} catch (Exception e) {
					System.out.println("Supply exception: " + toString(myType) + ".  " + bytecodesLeft + " bytecodes before transferSupplies() call.");
					//e.printStackTrace();
				}
			}
			
			//==========================================================================================================================================
			
			//if (Clock.getBytecodesLeft() < 600)
				rc.yield();
			
		}
	}
	
	
	
	private static String toString(RobotType type) {
		switch (type)
		{
		case HQ:
			return "HQ";
		case TOWER:
			return "tower";
		case HELIPAD:
			return "helipad";
		case BARRACKS:
			return "barracks";
		case MINERFACTORY:
			return "miner factory";
		case MINER:
			return "miner";
		case DRONE:
			return "drone";
		case BASHER:
			return "basher";
		case SOLDIER:
			return "soldier";
		case BEAVER:
			return "beaver";
		case TANKFACTORY:
			return "tank factory";
		case TANK:
			return "tank";
		case LAUNCHER:
			return "launcher";
		default:
			return null;
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
			
			supplyTransferFraction = 0.5;
			
		} catch (Exception e) {
			System.out.println("HQ Exception");
			//e.printStackTrace();
		}
	}
	
	static void doTower()
	{
		try {
			if (rc.isWeaponReady())
			{
				attackSomething();
			}
			
			supplyTransferFraction = 0.5;
			
		} catch (Exception e) {
			System.out.println("Tower Exception");
			//e.printStackTrace();
		}
	}
	
	static void doHelipad()
	{
		try {
			if (rand.nextInt(100) < 20)
				trySpawn(facing,RobotType.DRONE);
		} catch (Exception e) {
			System.out.println("Helipad Exception");
			//e.printStackTrace();
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
			
			supplyTransferFraction = 0.5;
			
		} catch (Exception e) {
			System.out.println("Miner Factory Exception");
			//e.printStackTrace();
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
			//e.printStackTrace();
		}
	}

	
	static void doBarracks()
	{
		try {
			RobotInfo[] bots = rc.senseNearbyRobots(99999, myTeam);
			int nSol = 0;
			for (RobotInfo b: bots)
				if (b.type == RobotType.SOLDIER)
					nSol++;
			
			if (curOre - lastOre < RobotType.SOLDIER.oreCost && nSol < numSoldiers)
			{
				trySpawn(facing,RobotType.SOLDIER);
			}
			
			supplyTransferFraction = 0.5;
			
		} catch (Exception e) {
			System.out.println("Miner Factory Exception");
			//e.printStackTrace();
		}
	}
	
	static void doDrone()
	{
		try {
			boolean attacking = attackSomething();
			myLocation = rc.getLocation();
			//if(!attacking) // chase a target on the list and go for it
			//{
				MapLocation targetLoc = chooseEnemyTarget();
				if(targetLoc!=null)
					tryMove(myLocation.directionTo(targetLoc));
			//}
		} catch (Exception e) {
			System.out.println("Drone Exception");
			//e.printStackTrace();
		}
	}

	static void doBasher()
	{
		try {
			
			boolean attacking = attackSomething();
			myLocation = rc.getLocation();
			if(!attacking) // chase a target on the list and go for it
			{
				MapLocation targetLoc = chooseEnemyTarget();
				if(targetLoc!=null)
					tryMove(myLocation.directionTo(targetLoc));
			}
			int bytecodesLeft = Clock.getBytecodesLeft();
			if(rc.isCoreReady() && bytecodesLeft>1200)
			{
				if (Clock.getRoundNum()<ATTACK_ROUND) {
					//aggMove();
					moveAround(true);
				}else{
					MapLocation towers[] = rc.senseEnemyTowerLocations();
					MapLocation closest = rc.senseEnemyHQLocation();
					for(MapLocation loc: towers){
						if(myLocation.distanceSquaredTo(loc)<myLocation.distanceSquaredTo(closest))
							closest = loc;
					}
					tryAggressiveMove(myLocation.directionTo(closest)); // aggressive
				}
			}
			supplyTransferFraction = 0.5;
			
		} catch (Exception e) {
			System.out.println("Basher Exception");
			//e.printStackTrace();
		}
	}

	static void doSoldier()
	{
		try {
			MapLocation enHQ = rc.senseEnemyHQLocation();
			
			//attackSomething();
			myLocation = rc.getLocation();
			squadTarget = enHQ;
			int bytecodesLeft = Clock.getBytecodesLeft();
			if(rc.isCoreReady() && bytecodesLeft>1200)
			{
				if(Clock.getRoundNum()<ATTACK_ROUND)
				{
					MapLocation targetLoc = chooseEnemyTarget();
					if(targetLoc!=null)
						squadTarget = targetLoc;
				}
				else // go berserk
				{
					MapLocation towers[] = rc.senseEnemyTowerLocations();
					MapLocation closest = enHQ;
					for(MapLocation loc: towers){
						if(myLocation.distanceSquaredTo(loc)<myLocation.distanceSquaredTo(closest))
							closest = loc;
					}
					squadTarget = closest;
				}
				movePotential();
				// attack is in movePotential
			}
			supplyTransferFraction = 0.5;
		} catch (Exception e) {
			int b = Clock.getBytecodeNum();
			System.out.println("Soldier Exception, " + b + " bytecodes.");
			e.printStackTrace();
		}
	}
	

	static void doTank()
	{
		try {
			MapLocation enHQ = rc.senseEnemyHQLocation();
			attackSomething();
			myLocation = rc.getLocation();
			squadTarget = enHQ;
			int bytecodesLeft = Clock.getBytecodesLeft();
			if(rc.isCoreReady() && bytecodesLeft>1200)
			{
				if(Clock.getRoundNum()<ATTACK_ROUND)
				{
					MapLocation targetLoc = chooseEnemyTarget();
					if(targetLoc!=null)
						squadTarget = targetLoc;
				}
				else // go berserk
				{
					MapLocation towers[] = rc.senseEnemyTowerLocations();
					MapLocation closest = enHQ;
					for(MapLocation loc: towers){
						if(myLocation.distanceSquaredTo(loc)<myLocation.distanceSquaredTo(closest))
							closest = loc;
					}
					squadTarget = closest;
				}
				movePotential();
				// attack is in movePotential
			}
			supplyTransferFraction = 0.5;
		} catch (Exception e) {
			int b = Clock.getBytecodeNum();
			System.out.println("Tank Exception, " + b + " bytecodes.");
			e.printStackTrace();
		}
	}
	
	static void doLauncher()
	{
		try {
			if(rc.getMissileCount()>0)
				missileAttack();
			else
				moveAround(true);
		} catch (Exception e) {
			System.out.println("Launcher Exception");
			//e.printStackTrace();
		}
	}
	
	static void doMissile()
	{
		try {
			RobotInfo enemies[] = rc.senseNearbyRobots(1, myTeam);
			
		} catch (Exception e) {
			System.out.println("Missile exception");
			//e.printStackTrace();
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
			}else if (m<1)
			{
				tryBuild(facing.opposite(),RobotType.BARRACKS);
				if(rc.isCoreReady()&&rc.canMine()){
					rc.mine();
				}
			}else if (tf<1)
			{
				tryBuild(facing.opposite(),RobotType.TANKFACTORY);
				if(rc.isCoreReady()&&rc.canMine()){
					rc.mine();
				}
			}
			else
			{
				if(curOre>1000) // if we aren't using up our ore, make more tank factories
				{
					numTankFactories = 12;
					numSupplyDepots = 6;
				}
				
				if(n < numMinerFactories) 
				{
					tryBuild(facing,RobotType.MINERFACTORY);
				} 
				else if(s<numSupplyDepots && tf > 2*s)
				{
					tryBuild(facing.opposite(),RobotType.SUPPLYDEPOT);
				}
				else if(m<numBarracks)
				{
					tryBuild(facing.opposite(),RobotType.BARRACKS);
				}
				else if(tf<numTankFactories)
				{
					tryBuild(facing.opposite(),RobotType.TANKFACTORY);
				}
				attackSomething();
				minerOperation(true, false); // (searching?, supplying?)
			}
			
			supplyTransferFraction = 0.5;
			
		} catch (Exception e) {
			System.out.println("Beaver Exception");
			//e.printStackTrace();
		}
	}
	
//===========================================================================================
	
	static void doMiner()
	{
		try {
			rc.setIndicatorString(2, toString(minerState));
			attackSomething();
			// defensiveManeuvers();  ???
			if(rc.isCoreReady()) // otherwise don't waste the bytecode
				miningDuties();
			int message = rc.readBroadcast(myMinerID);
			rc.setIndicatorString(0, "Miner ID = " + myMinerID);
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
			mineScore = mineScore();
			if(mineScore > 60)
				minerState = MinerState.LEADING; // switch to a leader, this will be reported to HQ next round
			supplyTransferFraction = 0.5;
			break;
		case SUPPLYING:
			rc.setIndicatorDot(myLocation.add(0,-1), 255,0,0); // white dots on supply line
			if(myMinerID!=rc.readBroadcast(minerContiguousID)-1) // if we're the most recent miner, stay put
				minerOperation(false, true); // (searching?, supplying?)
			supplyTransferFraction = 0.9;
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
	
	// Supply Transfer Protocol
	static void transferSupplies(double fraction) throws GameActionException {
		RobotInfo[] nearbyAllies = rc.senseNearbyRobots(rc.getLocation(),GameConstants.SUPPLY_TRANSFER_RADIUS_SQUARED,myTeam);
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
	
	// Checks if a move in a given direction is safe from enemy towers and HQ
	static boolean isSafeDirection(Direction dir) throws GameActionException { //checks if the facing direction is safe from towers
		myLocation = rc.getLocation();
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
		if(tileInFront.distanceSquaredTo(rc.senseEnemyHQLocation())<=RobotType.HQ.attackRadiusSquared){
			goodSpace = false; //space in range of enemy towers
		}
		return goodSpace;
	}

	// This method will attack an enemy in sight, if there is one
	static boolean attackSomething() throws GameActionException
	{
		if(rc.isWeaponReady()) // otherwise don't waste the bytecode
		{
			RobotInfo[] enemies = rc.senseNearbyRobots(myRange, enemyTeam);
			double minhealth = 1000;
			if (enemies.length != 0) // immediate threats
			{
				MapLocation minloc = enemies[0].location;
				for (RobotInfo en: enemies)
				{
					addEnemyToTargets(en.ID); // if we have space, add it to the list of targets
					if (en.health < minhealth)
					{
						minhealth = en.health;
						minloc = en.location;
					}
				}
				if (rc.canAttackLocation(minloc))
				{
					rc.attackLocation(minloc);
					return true;
				}
			}
			else
				return false;
		}
		return false;
	}
	
	static void addEnemyToTargets(int ID) throws GameActionException {
		// adds an enemy ID to a list of targets, if that list is not full
		for(int i: targetChannels)
		{
			int enemyID = rc.readBroadcast(i);
			try{
				rc.senseRobot(enemyID); // throws exception for dead ones or 0
			}catch(Exception e){
				rc.broadcast(i,ID);
				//System.out.println("New target ID = " + ID);
				return;
			}
		}		
	}
	
	static MapLocation chooseEnemyTarget() throws GameActionException {
		MapLocation targetLoc = null;
		// choose a target from the hit list
		for(int i: targetChannels)
		{
			int targetID = rc.readBroadcast(i);
			if(targetID!=0)
			{
				try{
					targetLoc = rc.senseRobot(targetID).location;
					int dist = myLocation.distanceSquaredTo(targetLoc);
					if(dist<200)
						return targetLoc;
				}catch(Exception e){
				}
			}
		}
		/*
		// if there's nobody, pick an enemy tower or HQ
		if(targetLoc==null)
		{
			MapLocation[] targets = getBuildings(myTeam.opponent());
			targetLoc = targets[rand.nextInt(targets.length)]; // at random
		}
		*/
		return targetLoc;
	}


	// This method will attack an enemy in sight, if there is one
	static void missileAttack() throws GameActionException
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
		Direction dir = myLocation.directionTo(minloc);
		if (rc.isCoreReady() && rc.canLaunch(dir))
			rc.launchMissile(dir);
		System.out.println("Missile launched.");
	}


	static Direction getRandomDirection() {
		return Direction.values()[(int)(rand.nextDouble()*8)];
	}
	
	
	// Move Around: random moves; go left if hitting barrier; avoid towers
	static void moveAround(boolean defensive) throws GameActionException 
	{
		if(rc.isCoreReady()) // otherwise don't waste the bytecode
		{
			if(rand.nextDouble()<0.05){
				if(rand.nextDouble()<0.5){
					facing = facing.rotateLeft();
				}else{
					facing = facing.rotateRight();
				}
			}else{
				if(rc.getID()%10==0){ // every other robot, with even ID #, are defensive, the rest offensive
					MapLocation towers[] = rc.senseTowerLocations();
					int t = (int) rand.nextInt(towers.length); // defense picks one of our towers at random
					facing = myLocation.directionTo(towers[t]);
				}
				else{
					MapLocation towers[] = rc.senseEnemyTowerLocations();
					int t = (int) rand.nextInt(towers.length); // defense picks one of our towers at random
					facing = myLocation.directionTo(towers[t]);
				}
			}
			if(defensive)
				tryMove(facing);
			else
				tryAggressiveMove(facing);
		}
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


	// This method will attempt to move in Direction d (or as close to it as possible)
	static int getDangerMoves()
	{
		MapLocation[] towers = rc.senseEnemyTowerLocations();
		MapLocation hq = rc.senseEnemyHQLocation();
		MapLocation myloc = rc.getLocation();
		
		int bitmoves = 0;
		
		if (Math.abs(myloc.x-hq.x)<7 && Math.abs(myloc.y-hq.y)<7)
		{
			if (towers.length>1) // use range 35
			{
				int ind = (hq.x-myloc.x+6) + 13*(hq.y-myloc.y+6);
				bitmoves |= hqMask[ind];				
			}
			else // use 24
			{
				int ind = (hq.x-myloc.x+5) + 11*(hq.y-myloc.y+5);
				bitmoves |= towerMask[ind];
			}
		}
		
		for (MapLocation t : towers)
		{
			if (Math.abs(myloc.x-t.x)>=6 || Math.abs(myloc.y-t.y)>=6)
				continue;
				
			int ind = (t.x-myloc.x+5) + 11*(t.y-myloc.y+5);
			bitmoves |= towerMask[ind];
		}
		
		return bitmoves;
	}
	

	// This method will attempt to move in Direction d (or as close to it as possible)
	static boolean tryMove(Direction d) throws GameActionException {
	
		if(rc.isCoreReady())
		{
			int isDanger = getDangerMoves();
			
			int dirint = d.ordinal();
			
			/*
			for (int i=0; i<8; i++)
			{
				if (isSafe[i])
					rc.setIndicatorDot(rc.getLocation().add(Direction.values()[i]), 255,255,255);
				else
					rc.setIndicatorDot(rc.getLocation().add(Direction.values()[i]), 255,255,0);
			}
			*/
			for (int i=0; i<8; i++)
			{
				int dir = (dirint+dirOffsets[i])&7;
				if ((isDanger&(1<<dir))>0)
					continue;
				
				Direction dd = Direction.values()[dir];
				if (dd == facing.opposite())
					continue;
				if (!rc.canMove(dd))
					continue;
				
				rc.move(dd);
				return true;
			}
		}
		return false;
	}
	
	// This method will attempt to move in Direction d (or as close to it as possible)
	static boolean tryAggressiveMove(Direction d) throws GameActionException {
	
		if(rc.isCoreReady())
		{
			int dirint = d.ordinal();
			
			for (int i=0; i<8; i++)
			{
				int dir = (dirint+dirOffsets[i])&7;
				
				Direction dd = Direction.values()[dir];
				if (dd == facing.opposite())
					continue;
				if (!rc.canMove(dd))
					continue;
				
				rc.move(dd);
				return true;
			}
		}
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
	
	//**************************************************************************************************
	
	
	// Potential field move
	static void movePotential() throws GameActionException {
		
		float evadePref = 2.0f;
		myLocation = rc.getLocation();
		int strengthBal = unitVal[myType.ordinal()];
		int friendSeparation = 0;
		int inertia = 10;
		
		int forceX = 0;
		int forceY = 0;
		int evadeX = 0;
		int evadeY = 0;
		
		// get dem robots
		RobotInfo[] friendlyRobots = rc.senseNearbyRobots(myType.sensorRadiusSquared, myTeam);
		RobotInfo[] enemyRobots = rc.senseNearbyRobots(myType.sensorRadiusSquared, myTeam.opponent());
		
		// if no one is around, or if we're in berserker mode, just go to targets
		
		// switch on your targeting computer
		int dx = (squadTarget.x - myLocation.x);
		int dy = (squadTarget.y - myLocation.y);
		int d2dest = dx * dx + dy * dy;
		int targetForceX = 10 * dx * Math.abs(dx) / d2dest;
		int targetForceY = 10 * dy * Math.abs(dy) / d2dest;
		int targetForce = targetForceX*targetForceX + targetForceY*targetForceY;
		int friendForce = 0;
		double friendForceX = 0;
		double friendForceY = 0;
		int enemyForce = 0;
		double enemyForceX = 0;
		double enemyForceY = 0;
		
		// if there are enemies and we're not in berserker mode, worry about them
		if (enemyRobots.length != 0 && Clock.getRoundNum()<ATTACK_ROUND)
		{
			// attract enemies, and count up enemy firepower
			for (RobotInfo bot : enemyRobots) {
				// dangerous ones, dealt with below
				if (bot.type == RobotType.TOWER || bot.type == RobotType.HQ)
					continue;
				double vecx = bot.location.x - myLocation.x;
				double vecy = bot.location.y - myLocation.y;
				double d2 = bot.location.distanceSquaredTo(myLocation);
				
				// attraction to enemy units based on target value
				enemyForceX += unitAtt[bot.type.ordinal()] * vecx * Math.abs(vecx) / d2;
				enemyForceY += unitAtt[bot.type.ordinal()] * vecy * Math.abs(vecy) / d2;
				enemyForce += enemyForceX*enemyForceX + enemyForceY*enemyForceY;
				
				// repulsion from range of enemy weapons, proportional to attack power
				// subtracts a positive # if we're in range
				evadeX -= (bot.type.attackRadiusSquared - d2) * bot.type.attackPower * (bot.type.attackRadiusSquared - (vecy * vecy));
				evadeY -= (bot.type.attackRadiusSquared - d2) * bot.type.attackPower * (bot.type.attackRadiusSquared - (vecx * vecx));
				
				// enemy firepower
				strengthBal -= evadePref*unitVal[bot.type.ordinal()] * bot.type.attackPower;
				
				// enemy unit type check: space out if the enemies to splash damage
				if(bot.type==RobotType.MISSILE || bot.type==RobotType.LAUNCHER || bot.type==RobotType.BASHER)
				{
					friendSeparation = 1;
				}
			}
			
			// attract friends, and count up friendly firepower
			for (RobotInfo bot: friendlyRobots)
			{
				// doesn't apply to towers and HQ
				if (bot.type == RobotType.TOWER || bot.type == RobotType.HQ)
					continue;
				
				// weak attraction to friends, depending on friendSeparation
				double vecx = bot.location.x - myLocation.x;
				double vecy = bot.location.y - myLocation.y;
				double d2 = bot.location.distanceSquaredTo(myLocation);
				friendForceX += 10*(vecx * Math.abs(vecx) - friendSeparation - 1) / d2;
				friendForceY += 10*(vecx * Math.abs(vecx) - friendSeparation - 1) / d2;
				friendForce += friendForceX*friendForceX + friendForceY*friendForceY;
				
				// our firepower
				strengthBal += unitVal[bot.type.ordinal()] * bot.type.attackPower;
			}
		}
		

		
		rc.setIndicatorString(0, "enemies = " + enemyRobots.length + ", friends = " + friendlyRobots.length);
		//rc.setIndicatorString(1, "forceX = " + forceX + ", forceY = " + forceY);
		//rc.setIndicatorString(1, "dx = " + dx + ", dy = " + dy);
		//rc.setIndicatorString(1, "target = (" + squadTarget.x + ", " + squadTarget.y + ")");
		rc.setIndicatorString(1, "targetForce = " + targetForce + ", enemyForce = " + enemyForce + ", friendForce = " + friendForce);
		
		// figure out how to respond: offense or defense?
		forceX = (int) (targetForceX + enemyForceX + friendForceX);
		forceY = (int) (targetForceY + enemyForceY + friendForceY);
	
		int force = 0;
		MapLocation here = rc.getLocation();
		Direction dir = null;
		
		
		double attackDelay = rc.getWeaponDelay();
		
		if(strengthBal > 0) // we dominate, move in and attack
		{

			force = forceX*forceX + forceY*forceY;
			dir = here.directionTo(here.add((int)forceX,(int)forceY));
			rc.setIndicatorString(2,"Attack! (" + strengthBal + ").  Force = " + force + ", inertia = " + inertia);

			//attackSomething();
		}
		
		
		else // retreat out of range
		{
			force = evadeX*evadeX + evadeY*evadeY;
			dir = here.directionTo(here.add((int)evadeX,(int)evadeY));
			rc.setIndicatorString(2,"Evade! (" + strengthBal + ").  Force = " + force + ", inertia = " + inertia);
			attackDelay = 0; // so that you always move
			//attackSomething();
		}
		
		// only move if force exceeds a threshold = inertia
		// move in the direction closest to that direction, if it's out of range of enemy towers and HQ
		if(force > inertia && dir != null && attackDelay < 1){
			if(Clock.getRoundNum()<ATTACK_ROUND){ // only move if attack delay < 1
			tryMove(dir);
			}else{
			tryAggressiveMove(dir);
			}
		}
		return;
		

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

	//**************************************************************************************************

}



