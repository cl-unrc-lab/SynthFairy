#!/usr/bin/python

import sys, getopt
import random
import math

roads = []
checkpoints = []
rewards = []


def genBoardEx() :

    global checkpoints
    global rewards

    roads.append([0,1,0,0,0,1])
    roads.append([1,0,1,0,1,2])
    roads.append([0,1,0,1,0,0])
    roads.append([0,0,1,0,1,0])
    roads.append([0,1,0,1,0,1])
    roads.append([1,2,0,0,1,0])

    checkpoints = [0,1,0,0,1,1]
    rewards = [1,1,1,1,1,1]

def writePreamble(mFile) :

    # a depiction of the board as a comment
    mFile.write("// Map:\n//")
    for i in range(maxWaypoint) :
        mFile.write("\n// ")
        for j in range(maxWaypoint-i) :
            if (roads[i][j+i] > 0):
                mFile.write("[w" + str(i) + "-w" + str(j+i) + " danger?:"+str(roads[i][j+i] == 2) + "] ")
    mFile.write("// Checkpoints:\n//")
    for i in range(maxWaypoint) :
        if (checkpoints[i] > 0):
            mFile.write("[w" + str(i) +"]")
    mFile.write("// Rewards:\n//")
    for i in range(maxWaypoint) :
        mFile.write("[w" + str(i) +"]("+str(rewards[i])+")")
            
    mFile.write("\n\n")

def writeUAV_1(fileName) :

    mFile = open(fileName,"w")

    writePreamble(mFile)

    mFile.write("Process UAVOperator {\n")
    mFile.write("    wc : INT ;    // waypoint current, -1 : null\n")
    mFile.write("\n")
    for i in range(maxWaypoint) :
        mFile.write("    w"+(str(i))+" : BOOL ;  // visited w"+(str(i))+"?\n")
    mFile.write("    state: INT ; // 0:init,1:okPic,2:del,3:ndel,4:finish\n")
    mFile.write("    ctrl : BOOL ;  // player turn\n")
    mFile.write("\n")

    mFile.write("   Rewards :\n")
    cpCounter = 0
    for i in range(maxWaypoint) :
        if (i==maxWaypoint-1):
            mFile.write("       state==1 && !w"+str(i)+" && wc=="+str(i)+" : "+str(rewards[i])+";\n\n")
        else:
            mFile.write("       state==1 && !w"+str(i)+" && wc=="+str(i)+" : "+str(rewards[i])+",\n")

    mFile.write("   Controller : ctrl;\n")
    mFile.write("   Goal : state==4;\n")
   
    mFile.write("    // Initial State\n")
    mFile.write("   Initial : wc==0 && !ctrl && state==0 &&")
    for i in range(maxWaypoint) :
        if (i==maxWaypoint-1):
            mFile.write(" !w"+str(i)+";\n\n")
        else:
            mFile.write(" !w"+str(i)+" &&")

    mFile.write("    // environment moves\n")
    mFile.write("\n")
    mFile.write("    [loiter] !ctrl && !(wc==-1) && state == 0 -> state = 0;\n")
    mFile.write("    [okPic] !ctrl && !(wc==-1) && state == 0 -> state = 1;\n")
    for i in range(maxWaypoint) :
        if checkpoints[i]==1:
            mFile.write("    [delegate] !ctrl && state == 1 && wc == "+str(i)+" -> "+str(probDel)+": state = 3,ctrl = true, w"+str(i)+" = true ++ "+str(1-probDel)+": state = 2, w"+str(i)+" = true;\n")
        else:
            mFile.write("    [delegate] !ctrl && state == 1 && wc == "+str(i)+" -> state = 3, ctrl = true, w"+str(i)+" = true;\n")
     
    for i in range(maxWaypoint) :
        for j in range(maxWaypoint) :
            if roads[i][j] == 1:
                mFile.write("    [fly_"+str(i)+"_"+str(j)+"] !ctrl && state == 2 && wc == "+str(i)+" -> "+str(1-probAcc)+": state = 0, wc = "+str(j)+" ++ "+str(probAcc)+": state = 4;\n")
            if roads[i][j] == 2:
                mFile.write("    [fly_"+str(i)+"_"+str(j)+"] !ctrl && state == 2 && wc == "+str(i)+" -> state = 4;\n")
    mFile.write("\n")

    mFile.write("    // controller moves\n")
    mFile.write("\n")
    for i in range(maxWaypoint) :
        for j in range(maxWaypoint) :
            if roads[i][j] == 1:
                mFile.write("    [fly_"+str(i)+"_"+str(j)+"] ctrl && state == 3 && wc == "+str(i)+" -> "+str(1-probAcc)+": ctrl = false, state = 0, wc = "+str(j)+" ++ "+str(probAcc)+": ctrl = false, state = 4;\n")
            if roads[i][j] == 2:
                mFile.write("    [fly_"+str(i)+"_"+str(j)+"] ctrl && state == 3 && wc == "+str(i)+" -> state = 4;\n")
    mFile.write("    [finish] ")
    for i in range(maxWaypoint) :
        if (i==maxWaypoint-1):
            mFile.write("w"+str(i) + " -> state = 4;\n")
        else:
            mFile.write("w"+str(i)+" && ")
    mFile.write("    [finish] state == 4 -> state = 4;\n")
    mFile.write("}\n\n")

    mFile.write("Main() {\n")
    mFile.write("    uav : UAVOperator ;\n")
    mFile.write("    run uav() ;\n")
    mFile.write("}\n")

    mFile.close()

def usage(exitVal) :

    print("\nusage uavOperator_gen.py [-h] [-s <int> ] [-w <int>] [-d <int>] [-r <int>] [-p <float>] [-q <float>]\n")
    print("-h :\n")
    print("  Print this help\n")
    print("-s num :\n")
    print("  Sets the seed for the pseudo-random number generator to 'num'. 'num' must be")
    print("  a non-negative integer (0 or higher) (default = 0)\n")
    print("-w num :\n")
    print("  Sets the number of waypoints of the map to 'num'. 'num' must be a positive integer")
    print("  (greater than 0) (default = 5)\n")
    print("-d num :\n")
    print("  Sets the maximum distance of the roads to 'num'. 'num' must be a positive integer")
    print("  (greater than 0) (default = 10)\n")
    print("-p num :\n")
    print("  Sets the probability of the operator to delegate a task to the UAV to 'num'. 'num' must be a float")
    print("  in the interval (0,1) (default = 0.1)\n")
    print("-q num :\n")
    print("  Sets the probability of the UAV taking a bad quality picture to 'num'. 'num' must be a float")
    print("  in the interval (0,1) (default = 0.05)\n")
    sys.exit(exitVal)



def main(argv):

    global maxWaypoint
    global probDel
    global probAcc

    maxWaypoint = 6
    probDel = 0.5
    probAcc = 0.3
    seed = 0
    path = "../tests/synthesis/uavOperator/"


    try:
        opts, args = getopt.getopt(argv,"hs:w:p:q:",["seed=","waypoints=","prob_delegate","prob_accident"])
    except getopt.GetoptError:
        usage(2)
    for opt, arg in opts:
        if opt == '-h':
            usage(0)
        elif opt in ("-s","seed=") :
            try :
                seed = int(arg)
            except ValueError :
                print("The width must be a nonnegative integer")
                sys.exit(2)            
            if seed < 0 :
                print("The width must be a nonnegative integer")
                sys.exit(2)
        elif opt in ("-w","waypoints=") :
            try :
                maxWaypoint = int(arg)
            except ValueError :
                print("The number of waypoints must be a positive integer")
                sys.exit(2)            
            if maxWaypoint <= 0 :
                print("The number of waypoints must be a positive integer")
                sys.exit(2)
        elif opt in ("-p","prob_delegate=") :
            try :
                probDel = float(arg)
            except ValueError :
                print("The delegate probability must be a float in (0,1)")
                sys.exit(2)
            if probDel <= 0 or probDel >= 1 :
                print("The delegate probability must be a float in (0,1)")
                sys.exit(2)
        elif opt in ("-q","prob_accident=") :
            try :
                probAcc = float(arg)
            except ValueError :
                print("The accident probability must be a float in (0,1)")
                sys.exit(2)
            if probAcc <= 0 or probAcc >= 1 :
                print("The accident probability must be a float in (0,1)")
                sys.exit(2)

    genBoardEx()
    writeUAV_1(path+"uavEX.sgg")
    rewards = [1,3,1,1,1,4]
    writeUAV_1(path+"uavEX2.sgg")
    rewards = [1,3,1,1,2,4]
    writeUAV_1(path+"uavEX3.sgg")

main(sys.argv[1:])
