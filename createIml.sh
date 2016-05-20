IML_FILE=$PWD/liferay-portal.iml

echo '<?xml version="1.0" encoding="UTF-8"?>' > $IML_FILE
echo '<module type="JAVA_MODULE" version="4">' >> $IML_FILE
echo '<component name="NewModuleRootManager" inherit-compiler-output="true">' >> $IML_FILE
echo '<exclude-output />' >> $IML_FILE
echo '<content url="file://$MODULE_DIR$">' >> $IML_FILE

echo '<excludeFolder url="file://$MODULE_DIR$/.ivy" />' >> $IML_FILE

echo '<sourceFolder url="file://$MODULE_DIR$/portal-web/docroot" isTestSource="false" />' >> $IML_FILE
echo '<excludeFolder url="file://$MODULE_DIR$/portal-web/test" />' >> $IML_FILE

for module_folder in `find . -maxdepth 1 -type d | rev | cut -d'/' -f -1 | grep -v "\." | rev | sort`; do
	for src_folder in `find $module_folder -name 'src' | sort`; do
		IS_TEMPLATE=`echo $src_folder | grep "_tmpl"`
		if [ "" == "$IS_TEMPLATE" ]; then
			parent_folder=`dirname $src_folder`

			if [ -d $parent_folder/src/main/java ]; then
				echo '<sourceFolder url="file://$MODULE_DIR$/____/src/main/java" isTestSource="false" />' | sed "s#____#$parent_folder#g" >> $IML_FILE
			else
				echo '<sourceFolder url="file://$MODULE_DIR$/____/src" isTestSource="false" />' | sed "s#____#$parent_folder#g" >> $IML_FILE
			fi

			if [ -d $parent_folder/src/main/resources ]; then
				echo '<sourceFolder url="file://$MODULE_DIR$/____/src/main/resources" isTestSource="false" />' | sed "s#____#$parent_folder#g" >> $IML_FILE
			fi

			if [ -d $parent_folder/src/META-INF ]; then
				echo '<sourceFolder url="file://$MODULE_DIR$/____/src/META-INF" isTestSource="false" />' | sed "s#____#$parent_folder#g" >> $IML_FILE
			fi

			echo '<excludeFolder url="file://$MODULE_DIR$/____/classes" />' | sed "s#____#$parent_folder#g" >> $IML_FILE

			if [ -d $parent_folder/test ]; then
				if [ -d $parent_folder/test/unit ]; then
					echo '<sourceFolder url="file://$MODULE_DIR$/____/test/unit" isTestSource="true" />' | sed "s#____#$parent_folder#g" >> $IML_FILE
				fi

				if [ -d $parent_folder/test/integration ]; then
					echo '<sourceFolder url="file://$MODULE_DIR$/____/test/integration" isTestSource="true" />' | sed "s#____#$parent_folder#g" >> $IML_FILE
				fi
			fi

			if [ -d $parent_folder/src/test/java ]; then
				echo '<sourceFolder url="file://$MODULE_DIR$/____/src/test/java" isTestSource="true" />' | sed "s#____#$parent_folder#g" >> $IML_FILE
			fi

			if [ -d $parent_folder/src/testIntegration/java ]; then
				echo '<sourceFolder url="file://$MODULE_DIR$/____/src/testIntegration/java" isTestSource="true" />' | sed "s#____#$parent_folder#g" >> $IML_FILE
			fi

			if [ -d $parent_folder/test-classes ]; then
				echo '<excludeFolder url="file://$MODULE_DIR$/____/test-classes" />' | sed "s#____#$parent_folder#g" >> $IML_FILE
			fi
		fi
	done
done

echo '</content>' >> $IML_FILE
echo '<orderEntry type="inheritedJdk" />' >> $IML_FILE
echo '<orderEntry type="sourceFolder" forTests="false" />' >> $IML_FILE
echo '<orderEntry type="library" exported="" name="global" level="project" />' >> $IML_FILE
echo '<orderEntry type="library" exported="" name="ivy" level="project" />' >> $IML_FILE
echo '<orderEntry type="library" exported="" name="development" level="project" />' >> $IML_FILE
echo '<orderEntry type="library" exported="" name="portal" level="project" />' >> $IML_FILE
echo '</component>' >> $IML_FILE
echo '</module>' >> $IML_FILE