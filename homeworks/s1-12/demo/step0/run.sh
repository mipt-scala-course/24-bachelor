#!/usr/bin/bash

echo ""
scala -version
echo ""

scala -cp lib/cats-core_3-2.9.0.jar:lib/cats-kernel_3-2.9.0.jar:out HelloCats
#scala -cp lib/cats-core_3-2.9.0.jar:lib/cats-kernel_3-2.9.0.jar:out.jar HelloCats
#scala -cp lib/cats-core_3-2.9.0.jar:lib/cats-kernel_3-2.9.0.jar -jar out.jar