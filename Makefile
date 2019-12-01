# Java
JAVA_SOURCES = \
	src/main/java/org/apache/hadoop/fs/gfarmfs/Gfarm.java \
	src/main/java/org/apache/hadoop/fs/gfarmfs/GfarmFileSystem.java \
	src/main/java/org/apache/hadoop/fs/gfarmfs/GfarmFSOutputStream.java \
	src/main/java/org/apache/hadoop/fs/gfarmfs/GfarmFSInputStream.java \
	src/main/java/org/apache/hadoop/fs/gfarmfs/GfarmFSNative.java \
	src/main/java/org/apache/hadoop/fs/gfarmfs/GfarmFSNativeOutputChannel.java \
	src/main/java/org/apache/hadoop/fs/gfarmfs/GfarmFSNativeInputChannel.java
JNI_CLASSES = \
	org.apache.hadoop.fs.gfarmfs.GfarmFSNative \
	org.apache.hadoop.fs.gfarmfs.GfarmFSNativeOutputChannel \
	org.apache.hadoop.fs.gfarmfs.GfarmFSNativeInputChannel

# GCC
CXXFLAGS = -Wall -O2 -g -D_REENTRANT -I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux
LDFLAGS  = -lgfarm

all: hadoop-gfarm.jar

hadoop-gfarm.jar: libGfarmFSNative.so
	jar cmf manifest.cf hadoop-gfarm.jar src/main/java/org/apache/hadoop/fs/gfarmfs/*.class libGfarmFSNative.so

libGfarmFSNative.so: org_apache_hadoop_fs_gfarmfs_GfarmFSNative.h GfarmFSNative.cpp
	g++ ${CXXFLAGS} -shared -fPIC src/main/cpp/GfarmFSNative.cpp -o libGfarmFSNative.so ${LDFLAGS} -L ${GFARM_HOME}/lib

org_apache_hadoop_fs_gfarmfs_GfarmFSNative.h: ${JAVA_SOURCES}
	javac -g -Xlint:deprecation -classpath ${CLASSPATH} ${JAVA_SOURCES}
	javah -classpath ${CLASSPATH} -jni ${JNI_CLASSES}

clean:
	rm -f *.class
	rm -f src/main/java/org/apache/hadoop/fs/gfarmfs/*.class
	rm -f org_apache_hadoop_fs_gfarmfs_GfarmFSNative.h
	rm -f libGfarmFSNative.so
	rm -f hadoop-gfarm.jar
	rm -f org_*.h

test:
	javac -g -Xlint:deprecation -classpath ${CLASSPATH} Test.java
	LD_LIBRARY_PATH=. java -classpath ${CLASSPATH}:hadoop-gfarm.jar Test

wc:
	wc src/main/java/org/apache/hadoop/fs/gfarmfs/*.java *.cpp | sort
