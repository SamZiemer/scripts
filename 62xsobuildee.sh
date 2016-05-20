#!/bin/bash

start=$(date +"%s")

cd ~/repos/liferay-plugins-ee-6.2.x/portlets/calendar-portlet/
ant all
cd ../chat-portlet/
ant all
cd ../contacts-portlet/
ant all
cd ../../hooks/deploy-listener-hook/
ant all
cd ../../portlets/events-display-portlet/
ant all
cd ../microblogs-portlet/
ant all
cd ../notifications-portlet/
ant all
cd ../../hooks/portal-compat-hook/
ant all
cd ../../portlets/private-messaging-portlet/
ant all
cd ../../hooks/sharepoint-hook/
ant all
cd ../so-activities-hook/
ant all
cd ../so-hook/
ant all
cd ../../portlets/so-announcements-portlet/
ant all
cd ../so-configurations-portlet/
ant all
cd ../so-portlet/
ant all
cd ../../themes/so-theme/
ant all
cd ../../portlets/tasks-portlet/
ant all
cd ../wysiwyg-portlet/
ant all

end=$(date +"%s")

runtime=$(($end-$start))

echo -e "\n\nRuntime took $(($runtime/60)) minutes and $(($runtime%60)) seconds."