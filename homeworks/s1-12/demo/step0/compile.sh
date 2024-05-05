#!/usr/bin/bash

echo ""
scalac version
echo ""


mkdir out

scalac -release 21 -cp lib/cats-core_3-2.9.0.jar:lib/cats-kernel_3-2.9.0.jar -d out src/*
#scalac -release 21 -cp lib/cats-kernel_3-2.9.0.jar:lib/cats-core_3-2.9.0.jar -d out.jar src/*
