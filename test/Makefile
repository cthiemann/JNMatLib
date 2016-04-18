

default:		all
all:			JNMatLibExample.class


JNMatLibExample.class:	JNMatLibExample.java Makefile
			javac -classpath "$(PWD)/../lib/jna-4.2.2.jar:$(PWD)/../dist/jnmatlib.jar" JNMatLibExample.java

test:			all
			export LD_LIBRARY_PATH="$(PWD)/../lib-r2014a:$(LD_LIBRARY_PATH)" && \
			java -classpath "$(PWD)/../lib/jna-4.2.2.jar:$(PWD)/../dist/jnmatlib.jar:$(PWD)" JNMatLibExample
