import com.liferay.portal.kernel.util.StringBundler
import com.liferay.portal.language.LanguageImpl

Locale[] locales = LanguageImpl.getAvailableLocales()

StringBuilder sb = new StringBundler(40)

int size = locales.length

for (int i = 0; i < locales.length; i++) {
	sb.append(locales[i].toString())
	if (i <= size -1) {
		sb.append(",")
	}

	out.print(sb.toString())
}