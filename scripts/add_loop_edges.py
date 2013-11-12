
# travers through xml nodes (input file) looking for each loop and adding an attribute "edge". WRtites new xml file (output file) 
# edges assigned to loops are hardcoded in script

import os
from xml.dom.minidom import parse, parseString
from xml.dom.minidom import Document
from xml.dom.minidom import getDOMImplementation
import argparse

#loops & controls
_edges = ["136969008","-93626510#1","-23866522#3","109439388","-35894685#0","88714961","-103452743#10","-125419151#0","-150058933#3","26732375","5017210#5","85192877#1","124176818#2","24430095","23899045#5","-88593721#1","-75627896#1","-80015627#2","30143811#2","71121158","116567652#2","-26585595#1","-160041888#3","-8599689","-56760938#0","28961985#1","-34425561#1","-125333159#0"]
_loopsId = ["1430",  "20",          "520",        "1473",     "1203",       "1232",    "460",          "479",         "446",         "1410",     "345",      "1406",     "304",        "1420",    "405","401",      "415",        "403",        "1431",      "1429",    "400",        "404",        "432",         "433",     "445",        "412",       "420",        "407"]

_input = "loop.xml"
_output = "loop.xml"

if __name__ == '__main__':

    parser = argparse.ArgumentParser()
    parser.add_argument('--input','-i',help='Xml input file with loops. File named "loop.xml" by default. ')
    parser.add_argument('--output','-o',help='File in which output data will be written. File named "loop.xml" by default. ')
    args = parser.parse_args()
    if args.input != None:
        _input=args.input
    if args.output != None:
        _output=args.output

    dom = parse(_input)
    loops = dom.getElementsByTagName("loop")
    for i in range(0,len(loops)):
        loop = loops[i];
        loopId = loop.getAttribute('id')
        edgeIndex = _loopsId.index(loopId)
        edge = _edges[edgeIndex]
        loop.setAttribute('edge',edge)
        print "loop: " + loopId + " edge: " + edge

    f = open(_output,'w')
    dom.writexml(f,encoding='utf-8')



