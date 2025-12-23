#!/usr/bin/env bash
QueryDepth=5
VerMaxNum=100
EdgeMaxNum=200
EdgeLabelNum=20
VerLabelNum=20
QueryNum=100
RepeatTimes=5
for ((i=1; i<=${RepeatTimes}; i++))\
do
	bash /opt/janusgraph-0.5.3/bin/gremlin-server.sh restart
	bash /opt/hugegraph-0.11.2/bin/stop-hugegraph.sh
	bash /opt/hugegraph-0.11.2/bin/init-store.sh
	bash /opt/hugegraph-0.11.2/bin/start-hugegraph.sh
	echo -n "waiting."
	for ((t=1; t<=5; t++))\
	do
		sleep 1
		echo -n "."
	done
	dir_str="log-${i}"
	if [ -d "$dir_str" ]; then
		rm -rf "$dir_str" 
	fi
	mkdir "$dir_str"
	java -Dperiod="${i}" -Xmx1048m -jar Grand-1.0-SNAPSHOT.jar ${QueryDepth} ${VerMaxNum} ${EdgeMaxNum} ${EdgeLabelNum} ${VerLabelNum} ${QueryNum}
	usage="${i} times: ${QueryDepth} ${VerMaxNum} ${EdgeMaxNum} ${EdgeLabelNum} ${VerLabelNum} ${QueryNum} \n \n"
	result="${dir_str}/result-${i}.log"
	if [ -s "$result" ] ; then 
		echo "Exist difference!"
	else
        	#rm -r "$dir_str"
		echo "No difference"
	fi
	echo -e $usage >> testlog.txt
	echo -e $usage
	QueryDepth=$[2+$RANDOM%8]
	VerMaxNum=$[100+$RANDOM%900]
	EdgeMaxNum=$[200+$RANDOM%1800]
	EdgeLabelNum=$[20+$RANDOM%30]
	QueryNum=$[100+$RANDOM%20]
done
