# Downloads traffic volumes data from Ponts et Chausees
# Writes xml file for each induction loop
# In every file there is a list of traffic volumes for particular dates 

import mechanize
import argparse
from mechanize import Browser
import re
from datetime import date
from datetime import timedelta
from BeautifulSoup import BeautifulSoup
from xml.dom.minidom import Document
from xml.dom.minidom import getDOMImplementation
import xml.dom.minidom

if __name__ == '__main__':

    #loops:
    # loops = [1430, 1420 , 1410, 304, 520, 20, 304, 1444, 1407, 1446, 520, 1232, 1475,10,30,20, 10, 818]
    #controls:
    loops = [394, 403, 407, 445, 400, 432, 412, 420, 415, 1406, 1408, 400, 1413, 413,390, 423, 390]

    output = "PEC-traffic-data.xml"
    _postId=0 
    verbose=False
    _type="Journalier"
    _vehic="U"
    _dateFrom="01.03.2013"
    _dateTo="01.07.2013"
    _direc="1"
    _numberOfDays = 10
    _weekday = 1 #day of the week as an integer, where Monday is 0 and Sunday is 6 

    parser = argparse.ArgumentParser()
    parser.add_argument('--output','-o',help='File basename in which output data will be written. File named "LoopId_PEC-traffic-data.xml" by default. ')
    parser.add_argument('--weekday', default=_weekday,help='Day of the week as an integer, where Monday is 0 and Sunday is 6.')
    parser.add_argument('--days', default=_numberOfDays,help='Number of the latest days. E.g. 10 means that data from 10 latest tuesdays (if dayweek=1) will be stored in the output.')
    parser.add_argument('--direction', default=_direc,help='Direction of the traffic, 1 (one direction), 2 (the other direction), or 3 (both directions).')
    parser.add_argument('--loops', required=False,type=int, nargs='+', help='List of loops to be fetched, given by their id (separated by spaces)')
    parser.add_argument('-v', '--verbose', action='store_true', dest='verbose', default=False,help='Extra debuging information.') 

    # read parameters
    args = parser.parse_args()
    if args.weekday != None:
        _weekday=int(args.weekday)
    if args.days != None:
        _numberOfDays=int(args.days)
    if args.direction != None:
        _direc=args.direction
    if args.output != None:
        output = args.output
    if args.loops != None:
        loops = args.loops

    uri="http://www.pch.public.lu/"
    urn="trafic/comptage/comptage/index.jsp"
    br = Browser()
    br.open(uri+urn)
    base="http://www.pch.public.lu/trafic/comptage/comptage/"

    for loop in loops:
        impl = getDOMImplementation()
        newdoc = impl.createDocument(None, "loops", None)   
        root = newdoc.documentElement

        _postId=str(loop)
        print "Loop no " + _postId + ":"
        
        # open link
        link = base + "poste_detail.jsp?POSTE_ID="+ _postId
        br = Browser()
        r = br.open(link)

        #read the latest available date from the html "Donnees disponibles du 01.07.1990 au 29.03.2011."
        html = r.read()

        latestDate = _dateTo
        m = re.search("(?<=disponibles du .{10} au ).{10}", html, re.DOTALL)
        if m != None:
            latestDate = m.group(0)
        day = int(latestDate[0:2])   
        month = int(latestDate[3:5])
        year = int(latestDate[-4:])
        lastDate = date(year,month,day)
        weekday = lastDate.weekday()
        delta = 0
        if weekday > _weekday:
            delta = weekday-_weekday
        elif weekday < _weekday:
            delta = 7 - _weekday + weekday
        newDate = lastDate - timedelta(days=delta)
       
        # write data for foregoing weekdays
        i = 0
        while i < _numberOfDays:
            print "request for date: "+str(newDate)   
            
            # form
            br.select_form("selection")
            #!!! trick - add artificially direction 2 and 3
            xitem = mechanize.Item(br.form.find_control(name="direc"), {'type':'radio', 'name':'direc', 'value':'2'})
            xitem = mechanize.Item(br.form.find_control(name="direc"), {'type':'radio', 'name':'direc', 'value':'3'})

            # set form variables
            br["Type"]=["Journalier"]
            br["vehic"]=[_vehic]
            br["direc"] = [_direc]
            newDateStr = str(newDate.day) + "." + str(newDate.month) + "." + str(newDate.year)
            br["dateDu"]=newDateStr
            br["dateAu"]=newDateStr

            if args.verbose:
                print br.form 

            cars=[]
            trucks=[]
            total=[]

            #read response
            response = br.submit()
            html = response.read()
            m = re.search('(?=<table).*<\/table>', html, re.DOTALL)
            html = m.group(0)      

            soup = BeautifulSoup(html)
            direction = soup.find(text=re.compile("Direction " + _direc + " - "))
            if direction == None:
                direction = "no data"
                print "No data available for loop " + _postId + "!"
            else:
                i = i + 1
                direction = direction.rstrip(' \n').lstrip(' \n')     
                direction = re.sub("Direction\s"+_direc+"\s-\s","",direction)
                if args.verbose:
                    print "direction: "+direction
                tags =  soup.find(attrs={'class' : "tablepch"}).next.findNextSiblings('tr')
                assert (tags[2].find('th').contents[0] == "Utilitaires")
                for t in tags[2].findAll('td'):
                    trucks.append(str(t.find('font').contents[0])) 
                assert (tags[3].find('th').contents[0] == "Voitures")
                for t in tags[3].findAll('td'):
                    cars.append(str(t.find('font').contents[0])) 
                assert (tags[5].find('th').contents[0] == "Total")
                for t in tags[5].findAll('td'):
                    total.append(str(t.find('font').contents[0])) 
                assert (tags[8].find('th').contents[0] == "Utilitaires")
                for t in tags[8].findAll('td'):
                    trucks.append(str(t.find('font').contents[0])) 
                assert (tags[9].find('th').contents[0] == "Voitures")
                for t in tags[9].findAll('td'):
                    cars.append(str(t.find('font').contents[0])) 
                assert (tags[11].find('th').contents[0] == "Total")
                for t in tags[11].findAll('td'):
                    total.append(str(t.find('font').contents[0])) 
                if args.verbose:
                    print "trucks :" +    ",".join(trucks)
                    print "cars :" +    ",".join(cars)
                    print "total :" +    ",".join(total)
                    print "------------------------------------------------"

                #write xml
                xml_loop = newdoc.createElement('loop')
                xml_loop.setAttribute('id',_postId)
                xml_loop.setAttribute('direction',direction)
                xml_loop.setAttribute('datefrom',newDateStr)
                xml_loop.setAttribute('dateto',newDateStr)
                for h in range(1,25):
                    xml_flow = newdoc.createElement('flow')
                    xml_flow.setAttribute('hour', str(h))
                    xml_flow.setAttribute('cars', str(cars[h-1]))            
                    xml_flow.setAttribute('trucks', str(trucks[h-1]))      
                    xml_loop.appendChild(xml_flow)
                root.appendChild(xml_loop)
                
            newDate = newDate - timedelta(days=7)  
            br = Browser()
            r = br.open(link) 
        
        f = open(str(_postId)+"_dir"+str(_direc)+"_"+output,'w')
        newdoc.writexml(f, indent="", addindent="  ", newl="\n",encoding='utf-8')
        print "For loop: ",_postId," wrote ",i," days "
        

