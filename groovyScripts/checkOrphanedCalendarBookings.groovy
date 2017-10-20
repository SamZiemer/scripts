import com.liferay.calendar.model.Calendar
import com.liferay.calendar.model.CalendarBooking
import com.liferay.calendar.service.CalendarBookingLocalServiceUtil
import com.liferay.calendar.service.CalendarLocalServiceUtil
import com.liferay.portal.kernel.dao.orm.QueryUtil

List<CalendarBooking> bookingList =
		CalendarBookingLocalServiceUtil.getCalendarBookings(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS)

HashMap<Long, List> calIdToBookingMap = new HashMap<Long, List>()
List calendarIds = new ArrayList<Long>()

for (CalendarBooking booking: bookingList) {
	long calendarId = booking.getCalendarId()

	if (!calendarIds.contains(calendarId)) {
		calendarIds.add(calendarId)
	}

	bookingIds = calIdToBookingMap.get(calendarId)

	if (bookingIds == null) {
		bookingIds = new ArrayList<Long>()
	}

	bookingIds.add(booking.getCalendarBookingId())
	calIdToBookingMap.put(booking.getCalendarId(), bookingIds)
}


for (Long id: calendarIds) {

	Calendar cal = CalendarLocalServiceUtil.fetchCalendar(id.longValue())

	if (cal == null) {
		out.println("Calendar with Id: " + id + " no longer exists. The " +
			"following calendar booking Ids are orphaned:")

		List bookings = calIdToBookingMap.get(id.longValue())

		out.println(bookings)
		out.println()
	}
}