import os
from sys import exit
from optparse import OptionParser

name = "map"

lb=49.5051
le=49.7680
Lb=5.8873
Le=6.4744

num=9

parser = OptionParser()
parser.add_option('--name', help=("Base name of osm files."), type="string", dest="name")
parser.add_option('--left',
                  help=("Left Longitude"), type="float", dest='left')
parser.add_option('--right',
                  help=("Right Longitude"),  type="float",dest='right')
parser.add_option('--top',
                  help=("Top Latitude"), action="store", type="float", dest='top')
parser.add_option('--bottom',
                  help=("Bottom Latitude"), action="store", type="float", dest='bottom')
parser.add_option('--tiles',
                  help=("The number of rows/column in a divided map (n x n). 3 produces 3x3 tiles."), type="int", dest='tiles')
(options, args) = parser.parse_args()

if options.name:
    name = options.name
if options.left:
    Lb = options.left
    print "Left longitude: %.2f" % Lb
else:
    print "Left longitude required!"
    # exit()
if options.right:
    Le = options.right
    print "Right longitude: ",Le
else:
    print "Right longitude required!"
    # exit()
if options.top:
    le = options.top
    print "Top latitude: ",le
else:
    print "Top latitude required!"
    # exit()
if options.bottom:
    lb = options.bottom
    print "Bottom latitude: ",lb
else:
    print "Bottom latitude required!"
    # exit()
if options.tiles:
    num = options.tiles
    print "Tiles number n x n: ",num
else:
    print "Tiles number required!!"
    # exit()

_lb=lb
_Lb=Lb

for i in range(0,num):
    _le = _lb + (le-lb) / float(num)
    _Lb=Lb    
    for j in range(0,num):
        _Le = _Lb + (Le-Lb) / float(num)
        # http://api.openstreetmap.org/api/0.6/map?bbox=<SW-longitude,SW-latitude,NE-longitude,NE-latitude>
        call1 = "wget http://api.openstreetmap.org/api/0.6/map?bbox=" + str(_Lb) + "," + str(_lb) +"," + str(_Le) + "," + str(_le) +" -O "  + name + str(i) + "x" + str(j) + ".osm.xml"
        call2 = "wget  \"http://tile.openstreetmap.org/cgi-bin/export?bbox=" + str(_Lb) + "," + str(_lb) +"," + str(_Le) + "," + str(_le) +"&scale=110000&format=png\" -O " + name + str(i) + "x" + str(j) + ".png"
        print call1
        os.system(call1)
        # os.system(call2)
        _Lb=_Le    
    _lb=_le
