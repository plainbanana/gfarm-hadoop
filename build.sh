#!/usr/bin/env bash

# export GFARM_HOME=
export CPLUS_INCLUDE_PATH=${GFARM_HOME}/include
echo $JAVA_HOME
echo $HADOOP_HOME
echo $GFARM_HOME

# Include jar files
export CLASSPATH=$(hadoop classpath)

echo "########"
echo $CLASSPATH
echo "########"

make 

echo "" 
read -n1 -p "install? (y/N): " yn
if [[ $yn = [Y] ]]; then
  echo "" 
  echo "install" 
  cp hadoop-gfarm.jar ${HADOOP_HOME}/lib/
  cp hadoop-gfarm.jar ${HADOOP_HOME}/share/hadoop/tools/lib/
  cp hadoop-gfarm.jar ${HADOOP_HOME}/share/hadoop/common/lib/
  cp libGfarmFSNative.so ${HADOOP_HOME}/lib/native/
  echo "install done" 
else
  echo "" 
  echo "abort" 
fi
