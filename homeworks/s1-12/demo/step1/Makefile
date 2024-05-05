.RECIPEPREFIX = >

sources=$(wildcard src/*.scala)
libdir=lib
target=classes
targetjar=HelloCats.jar
CLASSPATH=lib/cats-core_3-2.9.0.jar:lib/cats-kernel_3-2.9.0.jar

all: clean compile run

clean: 
> rm -rf ${target}

cleanjar: 
> rm -f ${targetjar}

compile: 
> mkdir ${target}
> scalac -cp ${CLASSPATH} -d ${target} ${sources}

compilejar: cleanjar
> scalac -cp ${CLASSPATH} -d ${targetjar} ${sources}

run: ${target}
> scala -cp ${CLASSPATH}:${target} HelloCats

runjar: ${targetjar} ${libdir}
> scala -cp ${CLASSPATH} -jar ${targetjar}