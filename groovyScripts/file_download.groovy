import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.HtmlUtil;

out.print("<a id=\"downloadReport\" download=\"repository_report_");
out.print(DateUtil.getCurrentDate("yyyyMMddhhmmss", Locale.US));
out.print(".html\">download</a>");
out.println();

out.println("<div id=\"debugContent\">");
out.println("<p>awesome</p>");
out.println("</div>");

out.println("<script>");
out.println("{");

out.print("var blob = new Blob([");
out.print("document.getElementById('debugContent').innerHTML");
out.print("], {type: 'text/html'});");
out.println();

out.println("document.getElementById('downloadReport').href = window.URL.createObjectURL(blob);");

out.println("}");
out.println("</script>");