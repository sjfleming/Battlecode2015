package grid;

import battlecode.common.*;

import java.util.*;
import java.lang.*;

/* Sensing location defines etc */
class RobotConsts
{
	int senseLocsShort = 69;
	int senseLocsLong = 109;
	int[] senseLocsX = {0,-1,0,0,1,-1,-1,1,1,-2,0,0,2,-2,-2,-1,-1,1,1,2,2,-2,-2,2,2,-3,0,0,3,-3,-3,-1,-1,1,1,3,3,-3,-3,-2,-2,2,2,3,3,-4,0,0,4,-4,-4,-1,-1,1,1,4,4,-3,-3,3,3,-4,-4,-2,-2,2,2,4,4,-5,-4,-4,-3,-3,0,0,3,3,4,4,5,-5,-5,-1,-1,1,1,5,5,-5,-5,-2,-2,2,2,5,5,-4,-4,4,4,-5,-5,-3,-3,3,3,5,5};
	int[] senseLocsY = {0,0,-1,1,0,-1,1,-1,1,0,-2,2,0,-1,1,-2,2,-2,2,-1,1,-2,2,-2,2,0,-3,3,0,-1,1,-3,3,-3,3,-1,1,-2,2,-3,3,-3,3,-2,2,0,-4,4,0,-1,1,-4,4,-4,4,-1,1,-3,3,-3,3,-2,2,-4,4,-4,4,-2,2,0,-3,3,-4,4,-5,5,-4,4,-3,3,0,-1,1,-5,5,-5,5,-1,1,-2,2,-5,5,-5,5,-2,2,-4,4,-4,4,-3,3,-5,5,-5,5,-3,3};
	int[] senseLocsR = {0,10,10,10,10,14,14,14,14,20,20,20,20,22,22,22,22,22,22,22,22,28,28,28,28,30,30,30,30,31,31,31,31,31,31,31,31,36,36,36,36,36,36,36,36,40,40,40,40,41,41,41,41,41,41,41,41,42,42,42,42,44,44,44,44,44,44,44,44,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,53,53,53,53,53,53,53,53,56,56,56,56,58,58,58,58,58,58,58,58};
	float[] sqrt = {0.000000f,1.000000f,1.414214f,1.732051f,2.000000f,2.236068f,2.449490f,2.645751f,2.828427f,3.000000f,3.162278f,3.316625f,3.464102f,3.605551f,3.741657f,3.872983f,4.000000f,4.123106f,4.242641f,4.358899f,4.472136f,4.582576f,4.690416f,4.795832f,4.898979f,5.000000f,5.099020f,5.196152f,5.291503f,5.385165f,5.477226f,5.567764f,5.656854f,5.744563f,5.830952f,5.916080f,6.000000f,6.082763f,6.164414f,6.244998f,6.324555f,6.403124f,6.480741f,6.557439f,6.633250f,6.708204f,6.782330f,6.855655f,6.928203f,7.000000f,7.071068f,7.141428f,7.211103f,7.280110f,7.348469f,7.416198f,7.483315f,7.549834f,7.615773f,7.681146f,7.745967f,7.810250f,7.874008f,7.937254f,8.000000f,8.062258f,8.124038f,8.185353f,8.246211f,8.306624f,8.366600f,8.426150f,8.485281f,8.544004f,8.602325f,8.660254f,8.717798f,8.774964f,8.831761f,8.888194f,8.944272f,9.000000f};
	float[] invSqrt = {0.000000f,1.000000f,0.707107f,0.577350f,0.500000f,0.447214f,0.408248f,0.377964f,0.353553f,0.333333f,0.316228f,0.301511f,0.288675f,0.277350f,0.267261f,0.258199f,0.250000f,0.242536f,0.235702f,0.229416f,0.223607f,0.218218f,0.213201f,0.208514f,0.204124f,0.200000f,0.196116f,0.192450f,0.188982f,0.185695f,0.182574f,0.179605f,0.176777f,0.174078f,0.171499f,0.169031f,0.166667f,0.164399f,0.162221f,0.160128f,0.158114f,0.156174f,0.154303f,0.152499f,0.150756f,0.149071f,0.147442f,0.145865f,0.144338f,0.142857f,0.141421f,0.140028f,0.138675f,0.137361f,0.136083f,0.134840f,0.133631f,0.132453f,0.131306f,0.130189f,0.129099f,0.128037f,0.127000f,0.125988f,0.125000f,0.124035f,0.123091f,0.122169f,0.121268f,0.120386f,0.119523f,0.118678f,0.117851f,0.117041f,0.116248f,0.115470f,0.114708f,0.113961f,0.113228f,0.112509f,0.111803f,0.111111f};
	
	/* Grid weight values */
	int WEIGHT_UNKNOWN_EDGE = -10;
	int WEIGHT_ENEMY_HQ = -200;
	int WEIGHT_ENEMY_TOWER = -50;
	int ENEMY_DECAY_RATE = 10;
	
	/* Ordinal unit weight array */
	int[] friendWeights = {
			100, // HQ 
			-50, // TOWER
			0, // SUPPLYDEPOT
			0, // TECHNOLOGYINSTITUTE
			-10, // BARRACKS
			0, // HELIPAD
			0, // TRAININGFIELD
			0, // TANKFACTORY
			-10, // MINERFACTORY
			0, // HANDWASHSTATION
			0, // AEROSPACELAB
			-5, // BEAVER
			0, // COMPUTER
			30, // SOLDIER
			30, // BASHER
			-5, // MINER
			0, // DRONE
			60, // TANK
			0, // COMMANDER
			0, // LAUNCHER
			0  // MISSILE
	};
	int[] enemyWeights = {
			0, // HQ 
			0, // TOWER
			-500, // SUPPLYDEPOT
			0, // TECHNOLOGYINSTITUTE
			-500, // BARRACKS
			-500, // HELIPAD
			0, // TRAININGFIELD
			0, // TANKFACTORY
			-1000, // MINERFACTORY
			0, // HANDWASHSTATION
			0, // AEROSPACELAB
			-500, // BEAVER
			0, // COMPUTER
			0, // SOLDIER
			0, // BASHER
			-500, // MINER
			100, // DRONE
			200, // TANK
			0, // COMMANDER
			0, // LAUNCHER
			0  // MISSILE
	};
}

public class RobotPlayer {
	
	static RobotController rc;
	
	// Game-given values
	static Team myTeam;
	static Team enemyTeam;
	static RobotType myType;
	
	// Our assigned values n stuff
	static int myCID;
	static int myGameID;
	static int mySquad;
	
	// did we move since last turn?
	static boolean justMoved;
	
	static MapLocation myTarget;
	
	static MapLocation myLocation;
	static Direction facing;
	static int myGridX;
	static int myGridY;
	static int myGridInd;
	static int lastGridInd=-1;

	// my grid location values
	static MapLocation myGridUL;
	static MapLocation myGridBR;
	static MapLocation myGridCenter;
		
	// standard defines
	static MapLocation center;
	
	static MapLocation myHQ;
	static MapLocation enemyHQ;
	
	// map symmetry
	// 0 - rotation/diag
	// 1 - rotation/horiz flip
	// 2 - rotation/vert flip
	// 3 - rotation
	// 4 - h flip
	// 5 - v flip
	// 6 - diag x = y
	// 7 - diag x = -y
	static int mapSymmetry;
	static int mapMinX;
	static int mapMinY;
	static int mapMaxX;
	static int mapMaxY;
	// minimum and maximum x/y coords of grid elements
	static int gridMinX;
	static int gridMinY;
	static int gridMaxX;
	static int gridMaxY;
	// and the minimum number of grid cells we may potentially have
	static int gridMinNum;
	
	static Random rand;
	static Direction[] directions = {Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST, Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};
	static int myRounds = 0;
	
	
	// sizes of all of our arrays
	static final int MAX_SQUADS = 16;
	
	static final int CID_NUM = 4000;
	
	// grid, not map
	// how many map elements per grid element?
	static final int GRID_SPC = 5;
	// and what is required to sense the whole grid?
	static final int GRID_SENSE = 2*(GRID_SPC/2)*(GRID_SPC/2);
	// max possible extents in one direction
	// needs to be at least 240/GRID_SPC
	// (make sure we've very nice and padded)
	static final int GRID_DIM = (240/GRID_SPC)+4;
	// and how many elements in base grid. 784 total, of which we will use at most 196
	static final int GRID_NUM = GRID_DIM*GRID_DIM;
	// offset to get to upper-right corner of grid
	static final int GRID_OFFSET = GRID_DIM*GRID_SPC/2;
	// mask for first 25 bits
	static final int GRID_MASK = (1<<25)-1;
	static final int EDGE_MASK = (1<<5)-1;
	
	// update frequencies
	static final int GRID_DIFFUSE_FREQ = 2;
	static final int GRID_CONNECTIVITY_FREQ = 3;
	static final int GRID_UPDATE_FREQ = 1;
	
	// is this in the grid list?
	static final int STATUS_USING = (1<<0);
	// have we been there?
	static final int STATUS_SEEN = (1<<9);
	// is there an enemy HQ/tower there?
	static final int STATUS_HQ = (1<<10);
	static final int STATUS_TOWER = (1<<11);
	
	// which directions is this cell connected to others?
	static final int STATUS_CONN_NORTH = (1<<1);
	static final int STATUS_CONN_SOUTH = (1<<4);
	static final int STATUS_CONN_EAST = (1<<2);
	static final int STATUS_CONN_WEST = (1<<3);
	static final int STATUS_KNOW_NORTH = (1<<5);
	static final int STATUS_KNOW_SOUTH = (1<<8);
	static final int STATUS_KNOW_EAST = (1<<6);
	static final int STATUS_KNOW_WEST = (1<<7);
	static final int STATUS_KNOW_ALL = STATUS_KNOW_NORTH|STATUS_KNOW_SOUTH|STATUS_KNOW_EAST|STATUS_KNOW_WEST;
	
	static final int STATUS_NORTH=0;
	static final int STATUS_SOUTH=3;
	static final int STATUS_EAST=1;
	static final int STATUS_WEST=2;
	
	static final int PATHABLE_DONE=(1<<26);
	static final int UNKNOWN_NORTH=(1<<21);
	
	static int curChan = 0;
	
	/* Contig ID counting, to use for enemies and friends */
	static final int nextCIDChan = curChan++;
	static final int cidBase = curChan; static {curChan += CID_NUM;}
		
	/* Map info */
	static final int mapSymmetryChan = curChan++;
	static final int mapExtentsChan = curChan++;
	
	/* Grid info */
	// times that things were last fully computed
	static final int gridLastPotentialChan = curChan++;
	static final int gridLastDiffusionChan = curChan++;
	static final int gridLastConnectivityChan = curChan++;

	// pointer to which grid cells we're calculating the above
	static final int gridUpdatePtrChan = curChan++;
	static final int gridPotentialPtrChan = curChan++;
	static final int gridConnectivityPtrChan = curChan++;
	
	// potential field totals, for averaging
	static final int gridPotentialTotalChan = curChan++;
	static final int gridPotentialOffsetChan = curChan++;

	// list of grid indices that we have explored
	static final int gridListCountChan = curChan++;
	static final int gridListBase = curChan; static {curChan+=GRID_NUM/4;}
	// map list of basic grid square status
	static final int gridStatusBase = curChan; static {curChan+=GRID_NUM;}
	// map list of "seed positions" in grid squares
	static final int gridPositionBase = curChan; static {curChan+=GRID_NUM;}
	
	// these are calculated in a distributed fashion 
	static final int gridFriendBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridEnemyBase = curChan; static {curChan+=GRID_NUM;}
	// diffusion values for frienemy potential
	// upper bits are "distance from friendly unit"
	static final int gridPotentialBase = curChan; static {curChan+=GRID_NUM;}
	
	
	// connectivity values
	static final int gridConnectivityNormalBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridConnectivityKnownBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridConnectivityVoidBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridConnectivityPathableBase = curChan; static {curChan+=GRID_NUM;}
	static final int gridConnectivityEdgesBase = curChan; static {curChan+=GRID_NUM;}
	
	
	//===============================================================================================================
	
	// Keep track of best mine, score and location
	static int bestMineScoreChan = curChan++;
	static int bestMineXChan = curChan++;
	static int bestMineYChan = curChan++;
	
	// Keep track of number of each type of miner
	static int minersSupplying = curChan++;
	static int minersSearching = curChan++;
	static int minersLeading = curChan++;
	
	// Allow HQ to allocate mining duties
	static int minerShuffle = curChan++;
	
	static final int TARGET_SUPPLYING_MINERS = 10;
	
	// Keep track of miner IDs : using channel 499 and onward
	static int minerContiguousID = 499;
	static int myMinerID;
	
	// Supply transfer parameter
	static double supplyTransferFraction = 0.5;

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
//	public RobotPlayer(MinerState state){ // each robot has a MinerState field accessed by rc.minerState
//		this.minerState = state;
//	}
	static double lastLocationStartingOre;
	static double currentLocationStartingOre;
	static boolean bestMine;
	static double oreHere;
	static int[] minersPerState = {0,0,0}; // initially no miners: minersPerState[0]=SUPPLYING, minersPerState[1]=SEARCHING, minersPerState[2]=LEADING
	
	//===========================================================================================

	
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

	//===========================================================================================	
	
	// Adjustable parameters
    static int numBeavers = 8;
    static int numMinerFactories = 20;
    static int numMiners = 100;
    static int numBarracks = 1;
    static int numSoldiers = 20;
    static int numHelipads = 0;
	static int numSupplyDepots = 3;
	static int numTankFactories = 5;
	static int numTanks = 50;

	
	
	static RobotConsts Consts = new RobotConsts();
	
	static MapLocation[] enemyBuildings;
	
	static double lastOre = 0;
	static double curOre = 0;
	
	
	static final int[] gridOffX = {-2,-1,0,1,2,-2,-1,0,1,2,-2,-1,0,1,2,-2,-1,0,1,2,-2,-1,0,1,2};
	static final int[] gridOffY = {-2,-2,-2,-2,-2,-1,-1,-1,-1,-1,0,0,0,0,0,1,1,1,1,1,2,2,2,2,2};
	static int[] bitAdjacency = {99,231,462,924,792,3171,7399,14798,29596,25368,101472,236768,473536,947072,811776,3247104,7576576,15153152,30306304,25976832,3244032,7569408,15138816,30277632,25952256};
	static int[] bitEdge = {31,17318416,1082401,32505856};
	
	
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
		
		public boolean equals(MapLocation ml)
		{
			return (ml.x == this.x) && (ml.y == this.y);
		}
		
		public String toString()
		{
			return new MapLocation(this.x,this.y).toString();
		}
	}
	

	public static void run(RobotController robotc)
	{
		rc = robotc;
		
		//while (lastTowers == 0)
		//	rc.yield();
		//System.out.println(java.util.Arrays.asList(RobotType.values()));
		// Initialization code
		try {
			
			init();
			
			switch (myType)
			{
			case HQ:
				System.out.println(curChan + " channels in use");
				
				rc.broadcast(minerContiguousID,500);
				
				analyzeMap();
				// initialize to empty, so we fill it and do stuff
				enemyBuildings = new MapLocation[0];
				break;
			case BEAVER:
				facing = rc.getLocation().directionTo(myHQ).opposite();
				break;
			case MINER:
				minerState = MinerState.SUPPLYING; // initially miners are in supply chain
				myMinerID = rc.readBroadcast(minerContiguousID); // obtain a minerID
				rc.broadcast(minerContiguousID, myMinerID + 1); // update next minerID
				rc.broadcast(myMinerID, rc.getID()); // save my robot ID on the message board
				break;
			case DRONE:
				break;
			}
		} catch (Exception e) {
			System.out.println("Initialization exception");
			e.printStackTrace();
		}
		
		while(true) {
			
			int curround = Clock.getRoundNum();
			
			try {
				update();
			} catch (Exception e) {
				System.out.println("General update exception");
				e.printStackTrace();
			}
			
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
			case TANK:
				doTank();
				break;
			case TANKFACTORY:
				doTankFactory();
				break;
			}
			
			lastOre = curOre;

			// IMPORTANT SUPPLY TRANSFER CHANGE =======================================================================================================
			
			lastOre = curOre;
			int bytecodesLeft = Clock.getBytecodesLeft();
			if(bytecodesLeft>1200) // if you try to transfer supply and are out of bytecodes, you will queue a transfer to a location that might not be valid next round.  things move.
			{
				try {
					transferSupplies(supplyTransferFraction);
				} catch (Exception e) {
					System.out.println("Supply exception: " + myType.toString() + ".  " + bytecodesLeft + " bytecodes before transferSupplies() call.");
					//e.printStackTrace();
				}
			}
			
			//==========================================================================================================================================
			
			try {
				rc.setIndicatorString(0,"Gradient: " + gridGradient(myGridInd) + ", " + rc.readBroadcast(gridPotentialBase+myGridInd) + "," + "Offset: " + rc.readBroadcast(gridPotentialOffsetChan)
						+ ", Friend: " + rc.readBroadcast(gridFriendBase+myGridInd) + ", Enemy:" + rc.readBroadcast(gridEnemyBase+myGridInd));
				spare();
			} catch (Exception e) {
				System.out.println("Grid exception");
				e.printStackTrace();
			}

			if (Clock.getRoundNum() == curround)
				rc.yield();
		}
	}
	
	// what to do in extra cycles
	static void spare() throws GameActionException
	{
		while (Clock.getBytecodesLeft() > 1500 && gridUpdate());
		while (Clock.getBytecodesLeft() > 1000 && gridConnectivity());
		while (Clock.getBytecodesLeft() > 400 && gridDiffuse());
	}
	
	// general initialization, for everyone
	static void init() throws GameActionException
	{
		rand = new Random();
		myTeam = rc.getTeam();
		enemyTeam = myTeam.opponent();
		myType = rc.getType();
		
		// calculate center of map, as defined for everyone
		myHQ = rc.senseHQLocation();
		enemyHQ = rc.senseEnemyHQLocation();
		
		myTarget = enemyHQ;
		
		center = new MapLocation((myHQ.x+enemyHQ.x)/2,(myHQ.y+enemyHQ.y)/2);

		facing = getRandomDirection();
		
		// initialize things
		lastOre = rc.getTeamOre();
		curOre = lastOre;
		
		// do contiguous ID calculation
		myGameID = rc.getID();
		myCID = rc.readBroadcast(nextCIDChan);
		rc.broadcast(nextCIDChan,myCID+1);
		rc.broadcast(cidBase+myCID, myGameID);
		
		// load map extents
		getExtents();
	}
	
	static void update() throws GameActionException
	{
		MapLocation newloc = rc.getLocation();
		if (!newloc.equals(myLocation))
		{
			myLocation = newloc;
			justMoved = true;
		}
		else
		{
			justMoved = false;
		}

		curOre = rc.getTeamOre();
		
		if (justMoved)
		{			
			// update internal grid coordinate values
			myGridX = (GRID_OFFSET+myLocation.x-center.x+GRID_SPC/2)/GRID_SPC;
			myGridY = (GRID_OFFSET+myLocation.y-center.y+GRID_SPC/2)/GRID_SPC;
			myGridInd = myGridY*GRID_DIM + myGridX;
			
			if (myGridInd != lastGridInd)
			{
				myGridCenter = gridCenter(myGridInd);
				// both coords are inclusive
				myGridUL = myGridCenter.add(-GRID_SPC/2,-GRID_SPC/2);
				myGridBR = myGridCenter.add(GRID_SPC/2,GRID_SPC/2);

				int gridstatus = rc.readBroadcast(gridStatusBase+myGridInd);
				// has this grid cell been added yet?
				if ((gridstatus&STATUS_USING) == 0)
				{
					// only add it if array has been initialized
					// no double adding...
					int gridcount = rc.readBroadcast(gridListCountChan);
					if (gridcount >= gridMinNum)
					{
						gridstatus |= STATUS_USING|STATUS_SEEN;
						// write that we have entered it
						rc.broadcast(gridStatusBase+myGridInd, gridstatus);
						// and add it to global grid list
						rc.broadcast(gridListBase+gridcount,myGridInd);
						rc.broadcast(gridListCountChan,gridcount+1);
						int pathind = (myLocation.x-myGridCenter.x+2)+5*(myLocation.y-myGridCenter.y+2);
						rc.broadcast(gridConnectivityPathableBase+myGridInd, (1<<pathind));
					}
				}
				
				// or we just haven't been here yet
				if ((gridstatus&STATUS_SEEN) == 0 && myType != RobotType.TOWER && myType != RobotType.HQ)
				{
					rc.broadcast(gridStatusBase+myGridInd, gridstatus|STATUS_SEEN);
					// and add our pathable index
					int pathind = rc.readBroadcast(gridConnectivityPathableBase+myGridInd);
					if (pathind == 0)
					{
						pathind = (myLocation.x-myGridCenter.x+2)+5*(myLocation.y-myGridCenter.y+2);
						rc.broadcast(gridConnectivityPathableBase+myGridInd, (1<<pathind));
					}
				}

				
				lastGridInd = myGridInd;
			}
			// draw grid boundaries?
			/*
			rc.setIndicatorLine(myGridUL, myGridUL.add(GRID_SPC-1,0), 0, 255, 255);
			rc.setIndicatorLine(myGridUL, myGridUL.add(0,GRID_SPC-1), 0, 255, 255);
			rc.setIndicatorLine(myGridBR, myGridBR.add(0,-GRID_SPC+1), 0, 255, 255);
			rc.setIndicatorLine(myGridBR, myGridBR.add(-GRID_SPC+1,0), 0, 255, 255);
			*/
		}
	}
	
	// calculate symmetry of map, etc, for HQ only

	// 0 - rotation/diag
	// 1 - rotation/horiz flip
	// 2 - rotation/vert flip
	// 3 - rotation
	// 4 - h flip
	// 5 - v flip
	// 6 - diag x = y
	// 7 - diag x = -y
	static void analyzeMap() throws GameActionException
	{
		// get each set of buildings
		MapLocation[] myblds = getBuildings(myTeam);
		MapLocation[] enblds = getBuildings(myTeam.opponent());
		// 0 is HQ location
		// check horizontal/vertical first, easy
		if (myblds[0].x == enblds[0].x)
		{
			mapSymmetry = 1;
		}
		else if (myblds[0].y == enblds[0].y)
		{
			mapSymmetry = 2;
		}
		else // diag or rotation
		{
			if (Math.abs(enblds[0].x - myblds[0].x) != Math.abs(enblds[0].y - myblds[0].y))
				mapSymmetry = 3; // has to be rotational
			else
				mapSymmetry = 0; // diagonal/rotational still
			
		}
		
		// and save it, why not
		rc.broadcast(mapSymmetryChan, mapSymmetry);
		
		// get min and max bounds of visible buildings
		Arrays.sort(myblds, new Comparator<MapLocation>() {
		    public int compare(MapLocation ml1, MapLocation ml2) {
	 	      return Integer.compare(ml1.x,ml2.x);}});
		Arrays.sort(enblds, new Comparator<MapLocation>() {
		    public int compare(MapLocation ml1, MapLocation ml2) {
	 	      return Integer.compare(ml1.x,ml2.x);}});
		
		mapMinX = Math.min(myblds[0].x,enblds[0].x);
		mapMaxX = Math.max(myblds[myblds.length-1].x,enblds[enblds.length-1].x);
		
		Arrays.sort(myblds, new Comparator<MapLocation>() {
		    public int compare(MapLocation ml1, MapLocation ml2) {
	 	      return Integer.compare(ml1.y,ml2.y);}});
		Arrays.sort(enblds, new Comparator<MapLocation>() {
		    public int compare(MapLocation ml1, MapLocation ml2) {
	 	      return Integer.compare(ml1.y,ml2.y);}});

		mapMinY = Math.min(myblds[0].y,enblds[0].y);
		mapMaxY = Math.max(myblds[myblds.length-1].y,enblds[enblds.length-1].y);

		// and pack it into map extents broadcast
		mapMinX -= center.x;
		mapMaxX -= center.x;
		mapMinY -= center.y;
		mapMaxY -= center.y;
		int mapextents = (mapMinX&255) | ((mapMaxX&255)<<8) | ((mapMinY&255)<<16) | ((mapMaxY&255)<<24);
		rc.broadcast(mapExtentsChan, mapextents);
		
		getExtents();
		
		
		// now check each tower on a case-by-case basis
		/*
		switch (mapSymmetry)
		{
		case 0:
			// any not diagonally symmetric?
			// (aka x-coord diff != y-coord diff)
			// so sort by diff of x and y coords
			// (+1, +1) diagonal reflection test
			if ((myblds[0].x - enblds[0].x) == (myblds[0].y - enblds[0].y))
			{
				Arrays.sort(myblds, new Comparator<MapLocation>() {
				    public int compare(MapLocation ml1, MapLocation ml2) {
			 	      return Integer.compare(ml1.x+ml1.y,ml2.x+ml2.y);}});
				Arrays.sort(enblds, new Comparator<MapLocation>() {
				    public int compare(MapLocation ml1, MapLocation ml2) {
			 	      return Integer.compare(ml1.x+ml1.y,ml2.x+ml2.y);}});
				for (int i=1; i<myblds.length; i++)
				{
					// rotation test
					if (myblds[i].x != enblds[i].x)
					{
						mapSymmetry = 3;
						break;
					}
				}
			}
			else
			{
				Arrays.sort(myblds, new Comparator<MapLocation>() {
				    public int compare(MapLocation ml1, MapLocation ml2) {
			 	      return Integer.compare(ml1.x+ml1.y,ml2.x+ml2.y);}});
				Arrays.sort(enblds, new Comparator<MapLocation>() {
				    public int compare(MapLocation ml1, MapLocation ml2) {
			 	      return Integer.compare(ml1.x+ml1.y,ml2.x+ml2.y);}});
				for (int i=1; i<myblds.length; i++)
				{
					// rotation test
					if (myblds[i].x != enblds[i].x)
					{
						mapSymmetry = 3;
						break;
					}
				}
			}

			break;
		case 1:
			// any not horizontally symmetric?
			// (aka any x-coords not the same?)
			// so sort by x coords and compare
			Arrays.sort(myblds, new Comparator<MapLocation>() {
			    public int compare(MapLocation ml1, MapLocation ml2) {
		 	      return Integer.compare(ml1.x,ml2.x);}});
			Arrays.sort(enblds, new Comparator<MapLocation>() {
			    public int compare(MapLocation ml1, MapLocation ml2) {
		 	      return Integer.compare(ml1.x,ml2.x);}});

			
			for (int i=1; i<myblds.length; i++)
			{
				// rotation test
				if (myblds[i].x != enblds[i].x)
				{
					mapSymmetry = 3;
					break;
				}
			}
			break;
		case 2:
			// any not vertically symmetric?
			// (aka any y-coords not the same?)
			// so sort by y coords and compare
			Arrays.sort(myblds, new Comparator<MapLocation>() {
			    public int compare(MapLocation ml1, MapLocation ml2) {
		 	      return Integer.compare(ml1.y,ml2.y);}});
			Arrays.sort(enblds, new Comparator<MapLocation>() {
			    public int compare(MapLocation ml1, MapLocation ml2) {
		 	      return Integer.compare(ml1.y,ml2.y);}});

			
			for (int i=1; i<myblds.length; i++)
			{
				// rotation test
				if (myblds[i].y != enblds[i].y)
				{
					mapSymmetry = 3;
					break;
				}
			}
			break;
		}
		*/
	}
	
	static void getExtents() throws GameActionException
	{
		// unpack map extents
		int mapextents = rc.readBroadcast(mapExtentsChan);
		mapMinX = (int)(byte)((mapextents)&255);
		mapMaxX = (int)(byte)((mapextents>>8)&255);
		mapMinY = (int)(byte)((mapextents>>16)&255);
		mapMaxY = (int)(byte)((mapextents>>24)&255);
		
		// grid extents x/y:
		gridMinX = (mapMinX+GRID_OFFSET+GRID_SPC/2)/GRID_SPC;
		gridMinY = (mapMinY+GRID_OFFSET+GRID_SPC/2)/GRID_SPC;
		gridMaxX = (mapMaxX+GRID_OFFSET+GRID_SPC/2)/GRID_SPC;
		gridMaxY = (mapMaxY+GRID_OFFSET+GRID_SPC/2)/GRID_SPC;
		// and minimum number in order to populate whole map
		gridMinNum = (gridMaxX-gridMinX+1)*(gridMaxY-gridMinY+1);
		System.out.println("Minimum grid size: " + gridMinNum);
		//System.out.println("Map extents: "+mapMinX+","+mapMaxX+","+mapMinY+","+mapMaxY);
		
		// and symmetry
		mapSymmetry = rc.readBroadcast(mapSymmetryChan);
	}
	
	// get all buildings, including HQ, in a single uniform list
	static MapLocation[] getBuildings(Team team)
	{
		MapLocation[] buildings;
		if (team == myTeam)
		{
			MapLocation[] towers = rc.senseTowerLocations();
			MapLocation hq = rc.senseHQLocation();
			buildings = new MapLocation[towers.length+1];
			System.arraycopy(towers,0,buildings,1,towers.length);
			buildings[0] = hq;
		}
		else
		{
			MapLocation[] towers = rc.senseEnemyTowerLocations();
			MapLocation hq = rc.senseEnemyHQLocation();
			buildings = new MapLocation[towers.length+1];
			System.arraycopy(towers,0,buildings,1,towers.length);
			buildings[0] = hq;
		}
		return buildings;
	}

	
	
	
	static void doHQ()
	{
		try
		{
			rc.setIndicatorString(1,"Current ore: " + curOre);
			if (rc.isWeaponReady())
				attackSomething();
			
			// update enemy buildings every once in a while
			if (Clock.getRoundNum()%10 == 0)
			{
				MapLocation[] buildings = getBuildings(myTeam.opponent());
				if (buildings.length != enemyBuildings.length)
				{
					// something changed. unset old buildings, set new buildings
					for (int i=0; i<enemyBuildings.length; i++)
					{
						int gridind = gridIndex(enemyBuildings[i]);
						int gridstatus = rc.readBroadcast(gridStatusBase+gridind);
						gridstatus &= ~(i==0?STATUS_HQ:STATUS_TOWER);
						rc.broadcast(gridStatusBase+gridind,gridstatus);
					}
					for (int i=0; i<buildings.length; i++)
					{
						int gridind = gridIndex(buildings[i]);
						int gridstatus = rc.readBroadcast(gridStatusBase+gridind);
						gridstatus |= (i==0?STATUS_HQ:STATUS_TOWER);
						rc.broadcast(gridStatusBase+gridind,gridstatus);
					}
					enemyBuildings = buildings;
				}
			}
			
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
			debug_drawGridVals();

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
			double r = rand.nextDouble();
			if(r < 0.75)
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
			attackSomething();
			movePotential();
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
			myTarget = rc.senseEnemyHQLocation();
			}

			else{
				aggCoef = 1000;
				MapLocation towers[] = rc.senseEnemyTowerLocations();
				MapLocation closest = rc.senseEnemyHQLocation();
				for(MapLocation loc: towers) {
					if(myLocation.distanceSquaredTo(loc)<myLocation.distanceSquaredTo(closest))
						closest = loc;
				myTarget = closest;
				}
			}
			attackSomething();
			movePotential();
			supplyTransferFraction = 0.5;

		} catch (Exception e) {
			System.out.println("Basher Exception");
			e.printStackTrace();
		}
	}

	/*static void doSoldier()
	{
		try {
			attackSomething();
			if (rc.isCoreReady()) {
				MapValue dest = gridGradient(myGridInd);
				MapLocation m = new MapLocation(dest.x+myGridCenter.x,dest.y+myGridCenter.y);
		        facing = rc.getLocation().directionTo(m);
		        
		        if (facing == Direction.OMNI)
		        	facing = Direction.values()[rand.nextInt(8)];
		        
		        while (!rc.canMove(facing))
		        {
		        	if(rand.nextDouble()<0.5)
			        	facing = facing.rotateLeft();
		        	else
		        		facing = facing.rotateRight();
		        }
		        rc.move(facing);
			}
		} catch (Exception e) {
			System.out.println("Soldier Exception");
			e.printStackTrace();
		}
	}*/
	
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
	
	static void doTank()
	{
		try {
			if (Clock.getRoundNum()<1800) {
				myTarget = rc.senseEnemyHQLocation();
			}

			else{
				aggCoef = 1000;
				MapLocation towers[] = rc.senseEnemyTowerLocations();
				MapLocation closest = rc.senseEnemyHQLocation();
				for(MapLocation loc: towers){
					if(myLocation.distanceSquaredTo(loc)<myLocation.distanceSquaredTo(closest))
						closest = loc;
					myTarget = closest;
				}
			}
			attackSomething();
			movePotential();
			supplyTransferFraction = 0.5;
		} catch (Exception e) {
			System.out.println("Tank Exception");
			e.printStackTrace();
		}
	}
	
	static void doSoldier()
	{
		try {
			//updateSquadInfo();
			if (Clock.getRoundNum()<1800) {
				myTarget = rc.senseEnemyHQLocation();
			}

			else{
				aggCoef = 1000;
				MapLocation towers[] = rc.senseEnemyTowerLocations();
				MapLocation closest = rc.senseEnemyHQLocation();
				for(MapLocation loc: towers){
					if(myLocation.distanceSquaredTo(loc)<myLocation.distanceSquaredTo(closest))
						closest = loc;
				myTarget = closest;
				}
			}
			attackSomething();
			movePotential();
			supplyTransferFraction = 0.5;

		} catch (Exception e) {
			System.out.println("Soldier Exception");
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
				buildUnit(RobotType.MINERFACTORY);
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
					buildUnit(RobotType.MINERFACTORY);
				} 
				else if(s<numSupplyDepots && mi>1)// && m>1)
				{
					buildUnit(RobotType.SUPPLYDEPOT);
				}
				else if(o<numHelipads)
				{
					buildUnit(RobotType.HELIPAD);
				}
				else if(m<numBarracks)
				{
					buildUnit(RobotType.BARRACKS);
				}
				else if(tf<numTankFactories  && m>0)
				{
					buildUnit(RobotType.TANKFACTORY);
				}
				attackSomething();
				minerOperation(true, false); // (searching?, supplying?)
			}
			
			supplyTransferFraction = 0.5;
			
		} catch (Exception e) {
			System.out.println("Beaver Exception");
			e.printStackTrace();
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
			e.printStackTrace();
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
				}
			}else{
				tryMove(facing);
			}
		}else{
			tryMove(facing);
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
	
	static MapLocation gridCenter(int gridX, int gridY)
	{
		return new MapLocation(gridX*GRID_SPC+center.x-GRID_OFFSET,gridY*GRID_SPC+center.y-GRID_OFFSET);
	}
	
	static MapLocation gridCenter(int gridInd)
	{
		return new MapLocation((gridInd%GRID_DIM)*GRID_SPC+center.x-GRID_OFFSET,(gridInd/GRID_DIM)*GRID_SPC+center.y-GRID_OFFSET);
	}
	
	static int gridIndex(MapLocation loc)
	{
		return GRID_DIM*((GRID_OFFSET+loc.y-center.y+GRID_SPC/2)/GRID_SPC)
				+ ((GRID_OFFSET+loc.x-center.x+GRID_SPC/2)/GRID_SPC);
	}
	
	static MapValue gridGradient(int gridind) throws GameActionException
	{
		int bc = Clock.getBytecodeNum();
		
		int gridX = gridind%GRID_DIM;
		int gridY = gridind/GRID_DIM;
		// contains connectivity information
		int gridstatus = rc.readBroadcast(gridStatusBase+gridind);
		// my current value
		int gridval = rc.readBroadcast(gridPotentialBase+gridind);
		
		MapValue gradient = new MapValue(0,0,0);
		
		if (gridX>0)
		{
			int val = rc.readBroadcast(gridPotentialBase+gridind-1);
			if ((gridstatus&STATUS_CONN_WEST)>0)
				gradient.x += (val-gridval);
		}
		
		if (gridX<GRID_DIM-1)
		{
			int val = rc.readBroadcast(gridPotentialBase+gridind+1);
			if ((gridstatus&STATUS_CONN_EAST)>0)
				gradient.x -= (val-gridval);
		}
		
		if (gridY>0)
		{
			int val = rc.readBroadcast(gridPotentialBase+gridind-GRID_DIM);
			if ((gridstatus&STATUS_CONN_NORTH)>0)
				gradient.y += (val-gridval);
		}
		
		if (gridY<GRID_DIM-1)
		{
			int val = rc.readBroadcast(gridPotentialBase+gridind+GRID_DIM);
			if ((gridstatus&STATUS_CONN_SOUTH)>0)
				gradient.y -= (val-gridval);
		}
		
		gradient.value = Math.sqrt(gradient.x*gradient.x+gradient.y*gradient.y);
		
		//System.out.println("Gradient time: " + (Clock.getBytecodeNum()-bc));
		return gradient;
	}
	
	static boolean gridUpdate() throws GameActionException
	{
		// 100 bytecodes for preamble
		// 100 bytecodes for senseRobots
		// ~100 bytecodes/robot
		// 100 bytecodes for broadcasts
		
		int bc = Clock.getBytecodeNum();

		int ptr = rc.readBroadcast(gridUpdatePtrChan);
		int gridn = rc.readBroadcast(gridListCountChan);
		int gridind = rc.readBroadcast(gridListBase+ptr);
		
		// do we start a new cycle?
		if (ptr == 0)
		{
			int curround = Clock.getRoundNum();
			int lastup = rc.readBroadcast(gridLastPotentialChan);
			// recently updated, don't run again
			if (curround - lastup < GRID_UPDATE_FREQ)
				return false;

			rc.broadcast(gridLastPotentialChan,curround);
			System.out.println("Grid update @ " + curround + ", Grid Size: " + gridn);
		}
		
		// check if we've added all of the map-required grid elements yet or not
		// if not, only do that, and nothing else
		if (gridn < gridMinNum)
		{
			// just add one, then carry on
			// horizontal dimension of minimum grid
			int gridd = (gridMaxX-gridMinX+1);
			// get actual x,y coords
			int gridy = (gridn/gridd)+gridMinY;
			int gridx = (gridn%gridd)+gridMinX;
			// and add them
			gridind = gridx+gridy*GRID_DIM;
			
			// write that we are using it
			int gridstatus = rc.readBroadcast(gridStatusBase+gridind); 
			// and add proper connectivity values
			if (gridx>gridMinX)
				gridstatus |= STATUS_CONN_WEST;
			if (gridx<gridMaxX)
				gridstatus |= STATUS_CONN_EAST;
			if (gridy>gridMinY)
				gridstatus |= STATUS_CONN_NORTH;
			if (gridy<gridMaxY)
				gridstatus |= STATUS_CONN_SOUTH;
			
			rc.broadcast(gridStatusBase+gridind, gridstatus|STATUS_USING);
			// and add it to global grid list
			int gridcount = rc.readBroadcast(gridListCountChan);
			rc.broadcast(gridListBase+gridcount,gridind);
			rc.broadcast(gridListCountChan,gridcount+1);
			rc.broadcast(gridUpdatePtrChan,gridcount);
			return true;
		}
		
		MapLocation loc = gridCenter(gridind);
		MapLocation ul = loc.add(-GRID_SPC/2,-GRID_SPC/2);
		MapLocation br = loc.add(GRID_SPC/2,GRID_SPC/2);
		
		// now sense frienemies in all grid cells
		RobotInfo[] bots = rc.senseNearbyRobots(loc,GRID_SENSE,null);
		
		int prevFriend = rc.readBroadcast(gridFriendBase+gridind);
		int prevEnemy = rc.readBroadcast(gridEnemyBase+gridind);
		int prevPotential = rc.readBroadcast(gridPotentialBase+gridind);
		
		int gridFriend = 0;
		int gridEnemy = 0;
		
		for (RobotInfo ri : bots)
		{
			// not in this grid cell?
			if ((ri.location.x < ul.x) || (ri.location.x > br.x) ||
					(ri.location.y < ul.y) || (ri.location.y > br.y))
				continue;
			
			//int strength = getRobotStrength(ri.health,ri.supplyLevel,ri.type);
			if (ri.team == myTeam)
				gridFriend += Consts.friendWeights[ri.type.ordinal()];
			else
				gridEnemy += (prevPotential>0?-2:1)*Consts.enemyWeights[ri.type.ordinal()];
		}
		
		// relax enemy value to 0
		if (Math.abs(gridEnemy) < Math.abs(prevEnemy))
			gridEnemy = prevEnemy+Consts.ENEMY_DECAY_RATE*(prevEnemy>0?-1:1);
		
		if (gridFriend != prevFriend)
			rc.broadcast(gridFriendBase+gridind,gridFriend);
		if (gridEnemy != prevEnemy)
			rc.broadcast(gridEnemyBase+gridind,gridEnemy);
		
		rc.broadcast(gridUpdatePtrChan,(ptr+1)%gridn);
		
		return true;
		
		//System.out.println("Update time (" + bots.length + "): " + (Clock.getBytecodeNum()-bc));
	}
	
	static boolean gridDiffuse() throws GameActionException
	{
		int bc = Clock.getBytecodeNum();
		int ptr = rc.readBroadcast(gridPotentialPtrChan);
		int gridn = rc.readBroadcast(gridListCountChan);
		int gridind = rc.readBroadcast(gridListBase+ptr);
		
		if (gridn == 0)
			return false;
		
		if (ptr == 0)
		{
			int curround = Clock.getRoundNum();
			int lastup = rc.readBroadcast(gridLastDiffusionChan);
			// recently updated, don't run again
			if (curround - lastup < GRID_DIFFUSE_FREQ)
				return false;
			
			// or we're starting to run again, set offset
			int total = rc.readBroadcast(gridPotentialTotalChan);
			total = total/gridn;
			rc.broadcast(gridPotentialOffsetChan, -total);
			rc.broadcast(gridPotentialTotalChan, 0);

			rc.broadcast(gridLastDiffusionChan,curround);
			System.out.println("Diffuse update @ " + curround);
		}
		
		// contains connectivity information
		int gridstatus = rc.readBroadcast(gridStatusBase+gridind);
		
		// # of grid squares to closest friendly unit
		
		// source term for potential is difference between friend and enemy squares
		int gridval = rc.readBroadcast(gridPotentialBase+gridind);
		int source = rc.readBroadcast(gridFriendBase+gridind);
		
		if (source > 0)
			source = source / 4;
		
		source = source + rc.readBroadcast(gridEnemyBase+gridind);
		
		/*if (gridval > 0)
		{
			// there are friendly units, we need lots of guys here
			source = source + -2*rc.readBroadcast(gridEnemyBase+gridind);
		}
		else
		{
			// no friendly units
			source = source + rc.readBroadcast(gridEnemyBase+gridind);
		}*/
		
		if ((gridstatus&STATUS_HQ)>0)
			source += Consts.WEIGHT_ENEMY_HQ;
		else if ((gridstatus&STATUS_TOWER)>0)
			source += Consts.WEIGHT_ENEMY_TOWER;

		
		
		// new value, computed from average of surrounding squares
		int newval = source;
		
		int val = rc.readBroadcast(gridPotentialBase+gridind-1);
		if ((gridstatus&STATUS_CONN_WEST)>0) // connected, diffuse
			newval += val;
		else if ((gridstatus&STATUS_KNOW_WEST)>0) // known unconnected, don't
			newval += gridval;
		else 	// unknown unconnected, weak attraction
			newval += (gridval+Consts.WEIGHT_UNKNOWN_EDGE);
			//newval += gridval/2;

		
		val = rc.readBroadcast(gridPotentialBase+gridind+1);
		if ((gridstatus&STATUS_CONN_EAST)>0)
			newval += val;
		else if ((gridstatus&STATUS_KNOW_EAST)>0)
			newval += gridval;
		else
			newval += (gridval+Consts.WEIGHT_UNKNOWN_EDGE);
			//newval += gridval/2;

		
		val = rc.readBroadcast(gridPotentialBase+gridind-GRID_DIM);
		if ((gridstatus&STATUS_CONN_NORTH)>0)
			newval += val;
		else if ((gridstatus&STATUS_KNOW_NORTH)>0)
			newval += gridval;
		else
			newval += (gridval+Consts.WEIGHT_UNKNOWN_EDGE);
			//newval += gridval/2;
		
		
		val = rc.readBroadcast(gridPotentialBase+gridind+GRID_DIM);
		if ((gridstatus&STATUS_CONN_SOUTH)>0)
			newval += val;
		else if ((gridstatus&STATUS_KNOW_SOUTH)>0)
			newval += gridval;
		else
			newval += (gridval+Consts.WEIGHT_UNKNOWN_EDGE);
			//newval += gridval/2;
		
		// and take the average
		newval = newval/4;
		
		// shift the potential by the offset
		newval += rc.readBroadcast(gridPotentialOffsetChan);
		
		// and add the value we're writing to the accumulating total
		rc.broadcast(gridPotentialTotalChan, newval+rc.readBroadcast(gridPotentialTotalChan));
		
		rc.broadcast(gridPotentialBase+gridind, newval);
		rc.broadcast(gridPotentialPtrChan,(ptr+1)%gridn);
		//System.out.println("Diffuse time: " + (Clock.getBytecodeNum()-bc));
		
		return true;
	}
	
	static boolean gridConnectivity() throws GameActionException
	{
		int bc = Clock.getBytecodeNum();
		
		int ptr = rc.readBroadcast(gridConnectivityPtrChan);
		int gridn = rc.readBroadcast(gridListCountChan);
		int gridind = rc.readBroadcast(gridListBase+ptr);
		int gridstatus = rc.readBroadcast(gridStatusBase+gridind);
		
		if (gridn == 0)
			return false;
		
		
		if (ptr == 0)
		{
			int curround = Clock.getRoundNum();
			int lastup = rc.readBroadcast(gridLastConnectivityChan);
			// recently updated, don't run again
			if (curround - lastup < GRID_CONNECTIVITY_FREQ)
				return false;

			rc.broadcast(gridLastConnectivityChan,curround);
			System.out.println("Connectivity update @ " + curround);
		}
		
		// have we visited? if not, don't bother
		if ((gridstatus&STATUS_SEEN) == 0 || (gridstatus&STATUS_KNOW_ALL) == STATUS_KNOW_ALL)
		{
			// move on to the next one
			rc.broadcast(gridConnectivityPtrChan,(ptr+1)%gridn);
			return true;
		}
		
		MapLocation loc = gridCenter(gridind);
		
		final int GRID_N = GRID_SPC*GRID_SPC;

		// seed all the values
		int norms = rc.readBroadcast(gridConnectivityNormalBase+gridind);
		int voids = rc.readBroadcast(gridConnectivityVoidBase+gridind);
		int known = rc.readBroadcast(gridConnectivityKnownBase+gridind);
		int oldknown = known;
		
		// do we know all the tiles?
		// if we don't, try finding some new ones
		
		int nadd=0;
		
		if (known != GRID_MASK)
		{
			// first, get the terrain tiles
			int unknown = GRID_MASK&(~known);
			
			// drop down the unknown bits one by one
			while (unknown>0)
			{
				int i = Integer.numberOfTrailingZeros(unknown);
				unknown &= (unknown-1);
				int z = (1<<i);
				switch (rc.senseTerrainTile(loc.add(gridOffX[i],gridOffY[i])))
				{
				case NORMAL:
					norms |= z;
					known |= z;
					break;
				case UNKNOWN:
					break;
				case VOID:
				case OFF_MAP:
					voids |= z;
					known |= z;
					break;
				}
				nadd++;
				// limit how many times we add things just to slow it down a bit to 1000-bc-size chunks
				if (nadd>10) break;
			}
			
			// then write them back if it changed
			if (known != oldknown)
			{
				rc.broadcast(gridConnectivityNormalBase+gridind,norms);
				rc.broadcast(gridConnectivityVoidBase+gridind,voids);
				rc.broadcast(gridConnectivityKnownBase+gridind,known);
			}
			
			// and if we added a lot, return without doing stuff
			if (nadd > 10)
			{
				//System.out.println("Connectivity add time: " + (Clock.getBytecodeNum()-bc));
				return true;
			}
		}
		
		
		// now, do the flood fill
		int pathable = rc.readBroadcast(gridConnectivityPathableBase+gridind);
		// if pathable is on a void, just start at first non-void
		if ((pathable&voids) > 0)
			pathable = Integer.lowestOneBit(norms);
		// but only if we have new information, or we haven't pathed at all yet
		if ((oldknown!=known) || (pathable&PATHABLE_DONE)==0)
		{
			pathable &= GRID_MASK;
			
			int newpath = pathable;
			
			for (int i=0; i<GRID_N; i++)
			{
				int path = relaxGrid(pathable,norms);
				// now set newpath to difference between old and new
				newpath = path & (~pathable);
				pathable = path;
				if (newpath == 0)
					break;
			}
			
			// save the path we found
			rc.broadcast(gridConnectivityPathableBase+gridind,pathable|PATHABLE_DONE);
			
			//System.out.println("Connectivity path time: " + (Clock.getBytecodeNum()-bc));
			
			// we did our day's work for now
			return true;
		}
		
		// if we got to here, it means we have an up-to-date path
		
		// first, try to connect unknown path with pathable
		int unknown = GRID_MASK&(~known);
		// unknown should need to be relaxed only once
		int unknownpath = relaxGrid(unknown,norms); 
		
		int maybe = pathable;
		// if unknowns are connected with main connected region
		if ((unknownpath&pathable)>0)
			maybe |= unknownpath;
		
		// how many did we add this loop cycle?
		int edgeadded = 0;
		
		// now, test each edge
		for (int dir=0; dir<4; dir++)
		{
			// if we know this one, keep going
			if ((gridstatus&(STATUS_KNOW_NORTH<<dir))>0)
				continue;
			
			int adjind = gridind+gridOffset(dir);
			
			// check out the adjacent grid square
			int adjpath = rc.readBroadcast(gridConnectivityPathableBase+adjind);
			
			// if it has pathable, but incomplete, return
			if (adjpath > 0 && ((adjpath&PATHABLE_DONE)==0))
				continue;
			
			// create grid for pathable to outside edge
			// NOTE: these are bitboards for the adjacent grid square!
			int adjmaybe=0;
			int adjnorm=0;

			// now, create an adjpath if neighbor was not pathable
			if (adjpath == 0)
			{
				// loop through adjacent squares
				switch (dir)
				{
				case STATUS_NORTH:
					for (int i=0; i<5; i++)
					{
						switch (rc.senseTerrainTile(loc.add(i-2,-(GRID_SPC+1)/2)))
						{
						case NORMAL:
							adjpath |= bitAdjacency[i+20];
							adjnorm |= 1<<(i+20);
							break;
						case UNKNOWN:
							adjmaybe |= bitAdjacency[i+20];
							break;
						case VOID:
						case OFF_MAP:
							break;
						}
					}
					break;
				case STATUS_SOUTH:
					for (int i=0; i<5; i++)
					{
						switch (rc.senseTerrainTile(loc.add(i-2,(GRID_SPC+1)/2)))
						{
						case NORMAL:
							adjpath |= bitAdjacency[i];
							adjnorm |= 1<<(i);
							break;
						case UNKNOWN:
							adjmaybe |= bitAdjacency[i];
							break;
						case VOID:
						case OFF_MAP:
							break;
						}
					}
					break;
				case STATUS_EAST:
					for (int i=0; i<5; i++)
					{
						switch (rc.senseTerrainTile(loc.add((GRID_SPC+1)/2,i-2)))
						{
						case NORMAL:
							adjpath |= bitAdjacency[i*5];
							adjnorm |= 1<<(i*5);
							break;
						case UNKNOWN:
							adjmaybe |= bitAdjacency[i*5];
							break;
						case VOID:
						case OFF_MAP:
							break;
						}
					}
					break;
				case STATUS_WEST:
					for (int i=0; i<5; i++)
					{
						switch (rc.senseTerrainTile(loc.add(-(GRID_SPC+1)/2,i-2)))
						{
						case NORMAL:
							adjpath |= bitAdjacency[i*5+4];
							adjnorm |= 1<<(i*5+4);
							break;
						case UNKNOWN:
							adjmaybe |= bitAdjacency[i*5+4];
							break;
						case VOID:
						case OFF_MAP:
							break;
						}
					}
					break;
				}
			}
			else
			{
				// we have already loaded adjpath from adjacency thing
				int adjnorms = rc.readBroadcast(gridConnectivityNormalBase+adjind);
				int adjunknowns = GRID_MASK&(~rc.readBroadcast(gridConnectivityKnownBase+adjind));
				
				// now relax unknowns by one
				adjmaybe = relaxGrid(adjunknowns,adjnorms);
			}
			
			adjmaybe |= adjpath;
			
			// so now adjpath is bit mask for our grid locations
			// and adjmaybe contains that and possible connections
			// and adjnorm is just actual normal spaces in adjacent grid
			
			
			// now we shift adjpath and adjmaybe to our grid edge
			switch (dir)
			{
			case STATUS_NORTH:
				// south to north
				adjpath >>= 20;
				adjmaybe >>= 20;
				break;
			case STATUS_SOUTH:
				adjpath <<= 20;
				adjmaybe <<= 20;
				break;
			case STATUS_EAST:
				// west to east
				adjpath <<= 4;
				adjmaybe <<= 4;
				break;
			case STATUS_WEST:
				adjpath >>= 4;
				adjmaybe >>= 4;
				break;
			}
			
			// and mask them off
			adjpath &= bitEdge[dir];
			adjmaybe &= bitEdge[dir];
			
			// check for definitely connected
			if ((adjpath&pathable)>0)
			{
				setConnectivities(gridind,dir,true);
				// adjacent's pathable was 0, and we found some to connect to
				if (adjnorm > 0)
					rc.broadcast(gridConnectivityPathableBase+adjind,adjnorm);
					
			}
			// or definitely unconnected
			// means, no connections, and no connections through unknowns either
			else if ((adjmaybe&maybe)==0)
			{
				setConnectivities(gridind,dir,false);
			}
			
			edgeadded++;
			
			if (edgeadded >= 2)
				break;
		}
		
		
		//System.out.println("Connectivity edge time: " + (Clock.getBytecodeNum()-bc));
		
		/*
		for (int i=0; i<GRID_N; i++)
			if ((pathable&(1<<i))>0)
				rc.setIndicatorDot(loc.add(gridOffX[i],gridOffY[i]), 255, 255, 255);
				*/
		
		rc.broadcast(gridConnectivityPtrChan,(ptr+1)%gridn);

		return true;
	}
	
	static int relaxGrid(int fill, int mask)
	{
		int path = fill;
		while (fill > 0)
		{
			int lbit = Integer.numberOfTrailingZeros(fill);
			// add the lowest bit to the growing path
			path |= (bitAdjacency[lbit] & mask);
			// and unset lowest bit
			fill &= (fill-1);
		}
		return path;
	}
	
	static int gridOffset(int dir)
	{
		switch (dir)
		{
		case STATUS_NORTH:
			return -GRID_DIM;
		case STATUS_SOUTH:
			return GRID_DIM;
		case STATUS_EAST:
			return +1;
		case STATUS_WEST:
			return -1;
		}
		return 0;
	}
	
	static void setConnectivities(int gridind, int dir, boolean connected) throws GameActionException
	{
		boolean couldset = setConnectivity(gridind+gridOffset(dir),3-dir,connected);		
		// so if we couldn't set the connectivity, then we still don't know
		if (couldset)
			setConnectivity(gridind,dir,connected);
	}

	// update connectivity of single square
	static boolean setConnectivity(int gridind, int dir, boolean connected) throws GameActionException
	{
		int gridstatus = rc.readBroadcast(gridStatusBase+gridind);
		
		// if we're trying to connect to a square that isn't being used, we don't do nuthin
		if (connected && !((gridstatus&STATUS_USING)>0))
			return false;
				
		if (connected)
			gridstatus = gridstatus | (STATUS_CONN_NORTH<<dir);
		else
			gridstatus = gridstatus & (~(STATUS_CONN_NORTH<<dir));
		
		gridstatus = gridstatus | (STATUS_KNOW_NORTH<<dir);
		
		rc.broadcast(gridStatusBase+gridind, gridstatus);
		
		return true;
	}
	
	static int getRobotStrength(double health, double supply, RobotType type)
	{
		return 10*(int)(type.attackPower + ((supply>type.supplyUpkeep*10)?5:0));
	}
	
	// Supply Transfer Protocol
	static void transferSupplies() throws GameActionException {
		
		if (myType == RobotType.MINERFACTORY || Clock.getBytecodesLeft() < 600)
			return;
		
		RobotInfo[] nearbyAllies = rc.senseNearbyRobots(rc.getLocation(),GameConstants.SUPPLY_TRANSFER_RADIUS_SQUARED,myTeam);
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
		// if we can attack squad target (eg. tower), so do
		if (!rc.isWeaponReady())
			return;
		if (myTarget != null && rc.canAttackLocation(myTarget))
		{
			RobotInfo ri = rc.senseRobotAtLocation(myTarget);
			if (ri != null && ri.team == myTeam.opponent())
			{
				rc.attackLocation(myTarget);
				return;
			}
		}
		
		RobotInfo[] enemies = rc.senseNearbyRobots(myType.attackRadiusSquared, enemyTeam);
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
		if (rc.canAttackLocation(minloc))
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
			//TerrainTile tile = rc.senseTerrainTile(m);
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

        facing = rc.getLocation().directionTo(myTarget);
        
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
		//int destX = (myTarget.x - myLocation.x);
		//int destY = (myTarget.y - myLocation.y);
		//float d2dest = (float) Math.sqrt(destX * destX + destY * destY);
		MapValue dest = gridGradient(myGridInd);
		int destX = dest.x;
		int destY = dest.y;
		float d2dest = (float)dest.value;


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
			forceX += fBored * Consts.senseLocsX[boredDirIdx];
			forceY += fBored * Consts.senseLocsY[boredDirIdx];		
			boredCounter += 1; 
			rc.setIndicatorString(1, "Boredom Counter = " + boredCounter);
			if (boredCounter > boredomLength){
				bored = 0;
				boredCounter = 0;
				lastMoveCounter = 0;
				lastShotCounter = 0;
				//rc.setIndicatorString(0, "not bored");
			}

		}else if (lastMoveCounter > moveImpatience || myLocation.equals(moveRec[moveIdx - 2])){
			//rc.setIndicatorString(0, "BORED!");
			bored = 1;
			boredomLength = moveBoredomLength;
			boredDirIdx = rand.nextInt(8);
			
		}else if (lastShotCounter > shootImpatience){
			//rc.setIndicatorString(0, "BORED! Sweeping...");
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
			float id = Consts.invSqrt[d2];

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


			float id = Consts.invSqrt[d2];

			// attract to enemy units, adjusted for strength balance, constant
			forceX += strengthBal * fEnemySighted * unitAtt[bot.type.ordinal()] * id  * vecx ; // constant 
			forceY += strengthBal * fEnemySighted * unitAtt[bot.type.ordinal()] * id  * vecy ;


			// enemies in range, 1/r
			float dattack = Consts.sqrt[bot.type.attackRadiusSquared] - Consts.sqrt[d2]
					+ 2.0f;
			if (dattack > 0) {
				forceX += unitVal[bot.type.ordinal()] * qEnemyInRange * id*id * vecx; // constant
				forceY += unitVal[bot.type.ordinal()] * qEnemyInRange * id *id * vecy;	
			}
		}

//		boolean hasSquad = friendlyRobots.length > 8
//				|| ((rc.readBroadcast(squadTaskBase + mySquad) >> 8) & 255) > 8;

			MapLocation[] towers = getBuildings(myTeam.opponent());
			
			MapLocation enHQ = towers[0];
			int d2 = enHQ.distanceSquaredTo(myLocation);
			// check if it's in range
			if (d2 <= RobotType.HQ.attackRadiusSquared + 20){

				float id = Consts.invSqrt[d2];

				int vecx = enHQ.x - myLocation.x;
				int vecy = enHQ.y - myLocation.y;


				float dattack = Consts.sqrt[RobotType.HQ.attackRadiusSquared] - Consts.sqrt[d2]
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

				float id = Consts.invSqrt[d2];

				int vecx = tower.x - myLocation.x;
				int vecy = tower.y - myLocation.y;


				float dattack = Consts.sqrt[RobotType.TOWER.attackRadiusSquared] - Consts.sqrt[d2]
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
				mvs[i] = new MapValue(Consts.senseLocsX[i], Consts.senseLocsY[i], forceX
						* Consts.senseLocsX[i] + forceY * Consts.senseLocsY[i]);

/*			rc.setIndicatorLine(myLocation, new MapLocation(myLocation.x
					+ (int) (forceX * 10), myLocation.y + (int) (forceY * 10)), 0,
					255, 255);
*/
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



	// Potential field move
	/*
	static void calcPotential() throws GameActionException
	{
		float forceX = 0.0f;
		float forceY = 0.0f;
		
		// get dem robots
		RobotInfo[] friendlyRobots = rc.senseNearbyRobots(myType.sensorRadiusSquared,myTeam);
		RobotInfo[] enemyRobots = rc.senseNearbyRobots(myType.sensorRadiusSquared,myTeam.opponent());
		
		// attracted to squad target, far away
		forceX = (myTarget.x - myLocation.x);
		forceY = (myTarget.y - myLocation.y);
		
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
			float id = Consts.invSqrt[d2];
			
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
			
			float id = Consts.invSqrt[d2];
			
			float kRepel = -8.0f;
			
			// attract if it can't fight back
			if (bot.type.attackRadiusSquared == 0)
				kRepel = 5.0f;
			
			// within attack range, repel
			// (difference in distances)
			float dattack = Consts.sqrt[bot.type.attackRadiusSquared] - Consts.sqrt[d2] + 2.0f;
			if (dattack > 0)
			{
				kRepel /= (dattack*dattack);
			}
			else if (mySquad < HARASS_SQUADS)
			{
				// outside of attack range, weakly attrack (if harassing)
				if (bot.type.attackRadiusSquared < myType.attackRadiusSquared + 5)
				{
					kRepel = 5.0f;
				}
			}

			forceX += kRepel*id*vecx;
			forceY += kRepel*id*vecy;
		}
		
		boolean hasSquad = friendlyRobots.length > 8 || ((rc.readBroadcast(squadTaskBase+mySquad) >> 8) & 255) > 8;
		
		MapLocation[] towers = getBuildings(myTeam.opponent());
		
		for (MapLocation tower : towers)
		{
			int d2 = tower.distanceSquaredTo(myLocation);
			// check if it's in range
			if (d2 > RobotType.TOWER.attackRadiusSquared + 20)
				continue;
			
			float id = Consts.invSqrt[d2];

			int vecx = tower.x - myLocation.x;
			int vecy = tower.y - myLocation.y;
			
			float kRepel = -20.0f;

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
			float offset = 1.5f;
			// steer very clear of towers
			if (mySquad < HARASS_SQUADS) offset = 5.5f;
			float dattack = Consts.sqrt[RobotType.TOWER.attackRadiusSquared] - Consts.sqrt[d2] + offset;
			if (kRepel > 0)
			{// this is attractive, actually
				dattack = kRepel*id;
				rc.setIndicatorString(1,"Tower attraction");
			}
			else // and this means we are being repelled
			{
				dattack = kRepel*id/Math.max(dattack,1);
				rc.setIndicatorString(1,"Tower repulsion: " + dattack);
			}
			forceX += dattack*vecx;
			forceY += dattack*vecy;
		}
		// get direction of force
		MapValue[] mvs = new MapValue[9];

		for (int i=0; i<9; i++)
			mvs[i] = new MapValue(Consts.senseLocsX[i],Consts.senseLocsY[i],forceX*Consts.senseLocsX[i] + forceY*Consts.senseLocsY[i]);
		
		//rc.setIndicatorLine(myLocation, new MapLocation(myLocation.x + (int)(forceX*10), myLocation.y + (int)(forceY*10)), 0, 255, 255);

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
	*/
	
	// This method will attempt to move in Direction d (or as close to it as possible)
	static void tryMove(Direction d) throws GameActionException {
		
		if (!rc.isCoreReady())
			return;
		
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
		
		if (!rc.isCoreReady())
			return;
		
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

	
	static void spawnUnit(RobotType type) throws GameActionException {
		if(rc.isCoreReady())
		{
			Direction spawnDir = getRandomDirection();
			for (int i=0; i<8; i++)
			{
				if(rc.canSpawn(spawnDir, type))
				{
					rc.spawn(spawnDir, type);
					return;
				}
				spawnDir = spawnDir.rotateLeft();
			}
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
	
	
	
	static void debug_drawGridVals() throws GameActionException
	{
		if (Clock.getRoundNum() % 5 > 5)
			return;
		
		for (int x=0; x<GRID_DIM; x++)
		{
			for (int y=0;y<GRID_DIM;y++)
			{
				int gridind = y*GRID_DIM+x;
				
				// not mapped yet
				int gridstatus = rc.readBroadcast(gridStatusBase+gridind); 
				if ((gridstatus&STATUS_USING)==0)
				//if ((gridstatus&STATUS_SEEN)==0)
					continue;
				
				MapLocation loc = gridCenter(gridind);
				int val = (rc.readBroadcast(gridPotentialBase+gridind));
				
				// display tower locations?
				/*if ((gridstatus&STATUS_HQ) > 0)
				{
					rc.setIndicatorDot(loc,255,255,255);
					continue;
				}
				if ((gridstatus&STATUS_TOWER) > 0)
				{
					rc.setIndicatorDot(loc,128,128,128);
					continue;
				}*/
				
				//int dist2friends = val >> 16;
				/*if (dist2friends > 2)
				{
					rc.setIndicatorDot(loc,255,255,0);
				}
				else*/
				if (val > 0)
				{
					// friendly-controlled, set blue
					if (val>5)
						val += 100;
					if (val>255) val = 255;
					rc.setIndicatorDot(loc,0,0,val);
				}
				else
				{
					// enemy-controlled, set red
					val = -val;
					if (val>5)
						val += 100;
					if (val>255) val = 255;
					rc.setIndicatorDot(loc,val,0,0);
				}
				
				// check connectivities
				
				if ((gridstatus&STATUS_CONN_NORTH)>0)
				{
					int l = 1;
					if ((gridstatus&STATUS_KNOW_NORTH)>0)
						l=GRID_SPC;
					rc.setIndicatorLine(loc,loc.add(0,-l),255,255,0);
				}
				else if ((gridstatus&STATUS_KNOW_NORTH)>0)
					rc.setIndicatorDot(loc.add(0,-1),255,255,0);

				if ((gridstatus&STATUS_CONN_SOUTH)>0)
				{
					int l = 1;
					if ((gridstatus&STATUS_KNOW_SOUTH)>0)
						l=GRID_SPC;
					rc.setIndicatorLine(loc,loc.add(0,l),255,255,0);
				}
				else if ((gridstatus&STATUS_KNOW_SOUTH)>0)
					rc.setIndicatorDot(loc.add(0,1),255,255,0);

				if ((gridstatus&STATUS_CONN_EAST)>0)
				{
					int l = 1;
					if ((gridstatus&STATUS_KNOW_EAST)>0)
						l=GRID_SPC;
					rc.setIndicatorLine(loc,loc.add(l,0),255,255,0);
				}
				else if ((gridstatus&STATUS_KNOW_EAST)>0)
					rc.setIndicatorDot(loc.add(1,0),255,255,0);
				
				if ((gridstatus&STATUS_CONN_WEST)>0)
				{
					int l = 1;
					if ((gridstatus&STATUS_KNOW_WEST)>0)
						l=GRID_SPC;
					rc.setIndicatorLine(loc,loc.add(-l,0),255,255,0);
				}
				else if ((gridstatus&STATUS_KNOW_WEST)>0)
					rc.setIndicatorDot(loc.add(-1,0),255,255,0);

				MapValue gradient = gridGradient(gridind);
				int len = (int)(gradient.value/4) + 1;
				//int len = 2;
				gradient.x /= len;
				gradient.y /= len;
				
				rc.setIndicatorLine(loc,loc.add(gradient.x,gradient.y),0,255,0);
				
				// draw a dot
				/*MapLocation loc = gridCenter(gridind);
				int val = (rc.readBroadcast(gridFriendBase+gridind));
				if (val > 255) val = 255;
				rc.setIndicatorDot(loc,0,0,val);
				
				val = (rc.readBroadcast(gridEnemyBase+gridind));
				if (val > 255) val = 255;
				rc.setIndicatorDot(loc.add(1,0),val,0,0);*/
			}
		}
	}
	
	
	
}
