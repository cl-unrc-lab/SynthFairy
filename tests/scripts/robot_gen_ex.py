#!/usr/bin/python

import sys, getopt
import random
import math

moves = []
rewards = []

moveSyntax = ["<-","<>","->"]

#   [0|<>] [0|->] [0|<>] [0|<>] 
#   [2|<-] [6|->] [1|->] [4|->] 
#   [0|<>] [1|->] [2|<>] [0|<>] 
#   [4|<>] [2|<-] [0|<-] [2|->]

def genRndBoardEx(seed) :

    maxReward = 7
    moves.append([1,2,1,1])
    moves.append([0,2,2,2])
    moves.append([1,2,1,1])
    moves.append([1,0,0,2])

    rewards.append([0,0,0,0])
    rewards.append([2,6,1,4])
    rewards.append([0,1,2,0])
    rewards.append([4,2,0,2])

def writePreamble(mFile) :

    # a depiction of the board as a comment
    mFile.write("// Board:\n//")
    for i in range(length) :
        mFile.write("\n//   ")
        for j in range(width) :
            mFile.write("[" + str(int(rewards[i][j])) + "|" + moveSyntax[moves[i][j]] + "] ")

    mFile.write("\n\n")


def writeRobotEx(fileName) :

    mFile = open(fileName,"w")

    writePreamble(mFile)

    mFile.write("Process Robot {\n")
    mFile.write("    col : INT ;    // col in [0,width-1] and row  in [0,length-1] indicate\n")
    mFile.write("    row : INT ;    // the robot's position in the board\n")
    mFile.write("\n")
    mFile.write("    light : INT ;  // current light color\n")
    mFile.write("                   // 0 : red (light's turn)\n")
    mFile.write("                   // 1 : yellow (robot moves sideways)\n")
    mFile.write("                   // 2 : green (robot moves forward)\n")
    mFile.write("\n")

    mFile.write("   Rewards :\n")
    for i in range(length) :
        for j in range(width) :
            mFile.write("       !(light == 0) && row == "+str(i)+" && col == "+str(j)+" : "+str(int(rewards[i][j]))+" ,\n")
    mFile.write("       !(light == 0) && row == "+str(length)+" : 0 ,\n")
    mFile.write("       light == 0 : 0 ;\n\n")
    mFile.write("   Controller : !(light == 0);\n")
    mFile.write("   Goal : row =="+str(length)+";\n")

    mFile.write("    // Initially the robot is positioned in (0,0) and the light is ready to move\n")
    mFile.write("    Initial : col==0 && row==0 && light==0 ;\n")
    mFile.write("\n")

    mFile.write("    // light moves\n")
    mFile.write("\n")
    mFile.write("    [l_y] light == 0 -> light = 1 ;\n")
    mFile.write("    [l_r] light == 0 -> light = 2 ;\n")
    mFile.write("\n")

    mFile.write("    // lightrobot moves\n")
    mFile.write("\n")

    for i in range(length) :
        for j in range(width) :
            if moves[i][j] <= 1 :
                mFile.write("    [r_l] (light == 1) && (row == "+str(i)+") && (col == "+str(j)+") -> "+str(1-probRobot)+" : light = 0, col = "+str((j-1)%width)+" ++ "+str(probRobot)+" : light = 0 ;\n")
            if moves[i][j] >= 1 :
                mFile.write("    [r_r] (light == 1) && (row == "+str(i)+") && (col == "+str(j)+") -> "+str(1-probRobot)+" : light = 0, col = "+str((j+1)%width)+" ++ "+str(probRobot)+" : light = 0 ;\n")
        mFile.write("    [r_f] (light == 2) && (row == "+str(i)+") -> "+str(1-probRobot)+" : light = 0, row = "+str(i+1)+" ++ "+str(probRobot)+" : light = 0 ;\n")

    mFile.write("    [r_f] (row == "+str(length)+") -> row = "+str(length)+" ;\n")

    mFile.write("}\n\n")

    mFile.write("Main() {\n")
    mFile.write("    robot : Robot ;\n")
    mFile.write("    run robot() ;\n")
    mFile.write("}\n")

    mFile.close()



def usage(exitVal) :

    print("\nusage robot_gen.py [-h] [-s <int> ] [-w <int>] [-l <int>] [-p <float>] [-q <float>]\n")
    print("-h :\n")
    print("  Print this help\n")
    print("-r :\n")
    print("  Exclude the rewards setting\n")
    print("-s num :\n")
    print("  Sets the seed for the pseudo-random number generator to 'num'. 'num' must be")
    print("  a non-negative integer (0 or higher) (default = 0)\n")
    print("-w num :\n")
    print("  Sets the width of the board to 'num'. 'num' must be a positive integer")
    print("  (greater than 0) (default = 5)\n")
    print("-l num :\n")
    print("  Sets the length of the board to 'num'. 'num' must be a positive integer")
    print("  (greater than 0) (default = 10)\n")
    print("-p num :\n")
    print("  Sets the failure probability of the robot to 'num'. 'num' must be a float")
    print("  in the interval (0,1) (default = 0.1)\n")
    print("-q num :\n")
    print("  Sets the failure probability of the light to 'num'. 'num' must be a float")
    print("  in the interval (0,1) (default = 0.05)\n")
    sys.exit(exitVal)



def main(argv):

    global width
    global length
    global probRobot
    global probLight
    global includeRewards

    width = 4
    length = 4
    probRobot = 0.1
    probLight = 0.05
    seed = 0
    includeRewards = True
    path = "tests/synthesis/"


    try:
        opts, args = getopt.getopt(argv,"hs:w:l:p:q:r",["seed=","width=","length=","prob_fail_robot","prob_fail_light"])
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
        elif opt in ("-w","width=") :
            try :
                width = int(arg)
            except ValueError :
                print("The width must be a positive integer")
                sys.exit(2)            
            if width <= 0 :
                print("The width must be a positive integer")
                sys.exit(2)
        elif opt in ("-l","length=") :
            try :
                length = int(arg)
            except ValueError :
                print("The length must be a positive integer")
                sys.exit(2)            
            if length <= 0 :
                print("The length must be a positive integer")
                sys.exit(2)
        elif opt in ("-p","prob_fail_robot=") :
            try :
                probRobot = float(arg)
            except ValueError :
                print("The failure probability of the robot must be a float in [0,1]")
                sys.exit(2)
            if probRobot < 0 or probRobot > 1 :
                print("The failure probability of the robot must be a float in [0,1]")
                sys.exit(2)
        elif opt in ("-q","prob_fail_light=") :
            try :
                probLight = float(arg)
            except ValueError :
                print("The failure probability of the light must be a float in [0,1]")
                sys.exit(2)
            if probLight < 0 or probLight > 1 :
                print("The failure probability of the light must be a float in [0,1]")
                sys.exit(2)
        elif opt == "-r" :
            includeRewards = False

    genRndBoardEx(seed)

    writeRobotEx(path+"robotExample["+str(probRobot)+("" if includeRewards else "-NR")+"].sgg")



main(sys.argv[1:])
