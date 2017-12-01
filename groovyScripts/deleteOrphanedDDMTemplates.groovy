package groovyScripts

import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplate;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.service.DDMTemplateLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.service.persistence.DDMTemplateActionableDynamicQuery;

public class DeleteOrphanedDDMTemplates {

	public void doDeleteOrphanedDDMTemplates() {
		long ddmStructureClassNameId = PortalUtil.getClassNameId(
			DDMStructure.class.getName());

		ActionableDynamicQuery actionableDynamicQuery =
			new DDMTemplateActionableDynamicQuery() {

				@Override
				protected void addCriteria(DynamicQuery dynamicQuery) {
					DynamicQuery ddmStructureDynamicQuery =
						DDMStructureLocalServiceUtil.dynamicQuery();

					Projection structureIdProjection =
						ProjectionFactoryUtil.property("structureId");

					ddmStructureDynamicQuery.setProjection(
						structureIdProjection);

					Property classNameIdProperty = PropertyFactoryUtil.forName(
						"classNameId");
					Property classPKProperty = PropertyFactoryUtil.forName(
						"classPK");

					dynamicQuery.add(
						classNameIdProperty.eq(ddmStructureClassNameId));
					dynamicQuery.add(
						classPKProperty.notIn(ddmStructureDynamicQuery));
				}

				@Override
				protected void performAction(Object object) {
					DDMTemplate ddmTemplate = (DDMTemplate)object;

					if (_log.isInfoEnabled()) {
						_log.info(
							"Deleting DDM template " +
								ddmTemplate.getTemplateId() + " because the " +
									"DDM structure it is associated with " +
										"does not exist");
					}

					try {
						DDMTemplateLocalServiceUtil.deleteTemplate(ddmTemplate);

						if (_log.isInfoEnabled()) {
							_log.info("Successfully deleted DDM template " +
								ddmTemplate.getTemplateId());
						}
					}
					catch (Exception e) {
						_log.error(
							"Failed to delete DDM template " +
								ddmTemplate.getTemplateId(),
							e);

						_undeletedOrphanedDDMTemplates++;
					}
				}
			};

		actionableDynamicQuery.performActions();

		if (_log.isInfoEnabled()) {
			if (_undeletedOrphanedDDMTemplates == 0) {
				_log.info("Script completed successfully");
				_log.info("All orphaned DDM templates have been deleted");
				_log.info("Please proceed with the upgrade");
			}
			else {
				_log.info(
					"Script finished running, but was unable to delete all " +
						"orphaned DDM templates");
				_log.info(
					_undeletedOrphanedDDMTemplates + " orphaned DDM " +
						"templates could not be deleted");
				_log.info(
					"See the logs above for more details about which DDM " +
						"templates could not be deleted and why");
				_log.info(
					"Please be sure to remove these DDM templates before " +
						"proceeding with the upgrade");
			}
		}
	}

	private long _undeletedOrphanedDDMTemplates = 0;

	private static Log _log = LogFactoryUtil.getLog(
		DeleteOrphanedDDMTemplates.class);

}

(new DeleteOrphanedDDMTemplates()).doDeleteOrphanedDDMTemplates();
