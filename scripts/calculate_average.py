# Reads all xml files in current directory
# From each file computes the average flow for every hour from particular dates
# Outputs one xml file with average traffic volumes for every induction loop

import os
from xml.dom.minidom import parse, parseString
from xml.dom.minidom import Document
from xml.dom.minidom import getDOMImplementation

rootName = "loops"
nodeName = "loop"
outputFile = "Luxembourg.loop.xml"
loops = []
directions = []

impl = getDOMImplementation()
newdoc = impl.createDocument(None, rootName, None)   
root = newdoc.documentElement

path = './'
listing = os.listdir(path)
for file in listing:
    if file.endswith('.xml'):
        print "reading " + file + "..."
        dom = parse(file)
        hours = []
        loopId = ''
        averageCars = []
        averageTrucks = []
        averageTotal = []
        sumCars = []
        sumTrucks = []
        sumTotal = []
        counter = 0
        loops = dom.getElementsByTagName("loop")
        for i in range(0,len(loops)):
            loop = loops[i];
            counter = counter + 1
            datefrom = loop.getAttribute('datefrom')
            dateto = loop.getAttribute('dateto')
            if loopId == '':
                loopId = loop.getAttribute('id')
                loops.append(loopId)
                direction = loop.getAttribute('direction')
                directions.append(direction)
                print "reading loop: " + loopId + " in direction " + direction
                flows = loop.getElementsByTagName("flow")
                for flow in flows:
                    hour = flow.getAttribute('hour')
                    hours.append(hour)
                    sumCars.append(0)
                    sumTrucks.append(0)
                    sumTotal.append(0)
                    averageCars.append(0)
                    averageTrucks.append(0)
                    averageTotal.append(0)
            flows = loop.getElementsByTagName("flow")
            i = 0            
            for j in range(0,len(flows)):
                flow = flows[j]
                cars = int(flow.getAttribute('cars'))
                trucks = int(flow.getAttribute('trucks'))
                sumCars[j] = sumCars[j] + cars
                sumTrucks[j] = sumTrucks[j] + trucks
                sumTotal[j] = sumTotal[j] + cars + trucks
        #calculate average for every hour dor the loop
        for i in range(0,len(hours)):
            hour = hours[i]
            averageCars[i] = sumCars[i] / counter
            averageTrucks[i] = sumTrucks[i] / counter
            averageTotal[i] = sumTotal[i] / counter
            print "cars[" + hour + "]: " + str(averageCars[i]) + ", trucks[" + hour + "]: " + str(averageTrucks[i]) + ", total[" + hour + "]: " + str(averageTotal[i])
        #write xml
        xml_loop = newdoc.createElement(nodeName)
        xml_loop.setAttribute('id',loopId)
        xml_loop.setAttribute('direction',direction)
        for i in range(0,len(hours)):
            xml_flow = newdoc.createElement('flow')
            xml_flow.setAttribute('hour', str(hours[i]))
            xml_flow.setAttribute('cars', str(averageCars[i]))            
            xml_flow.setAttribute('trucks', str(averageTrucks[i]))
            xml_flow.setAttribute('total', str(averageTotal[i]))
            xml_loop.appendChild(xml_flow)
        root.appendChild(xml_loop)    

f = open(outputFile,'w')
newdoc.writexml(f, indent="", addindent="  ", newl="\n",encoding='utf-8')




