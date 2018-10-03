#!/bin/bash

JAVA_CMD="/opt/jdk"
EIIM_HOME="/Users/tony/workspace/git/EIIM/EII-ALLINONE"

CLASSPATH="$EIIM_HOME/conf/."
for jarFile in $EIIM_HOME/libs/*.jar; do
    CLASSPATH="$CLASSPATH:$jarFile"
done

echo "JAVA_CMD: $JAVA_CMD"
echo "EIIM_HOME: $EIIM_HOME"
echo "CLASSPATH: $CLASSPATH"

if [ -d $EIIM_HOME/logs ]; then
    mkdir -p $EIIM_HOME/logs
fi

cd $EIIM_HOME

$JAVA_CMD -Duser.timezone=GMT+8 -cp $CLASSPATH com.fs.eiim.EiimApplication >> $EIIM_HOME/logs/startup.log 2>&1 &
echo "Start the EIIM application server successfully."