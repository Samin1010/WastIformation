package app;

import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Example Index HTML class using Javalin
 * <p>
 * Generate a static HTML page using Javalin
 * by writing the raw HTML into a Java String object
 *
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 * @author Halil Ali, 2024. email: halil.ali@rmit.edu.au
 */

public class PageMission implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/mission.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        JDBCConnection jdbc = new JDBCConnection();

        // Add some Head information
        html = html + "<head>" +
                "<title>Our Mission</title>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";
        html = html + "</head>";

        // Add the body
        html = html + "<body>";
        html += "<main>";

        // Add the topnav
        // This uses a Java v15+ Text Block
        html = html +
                "        <div class=\"topnav\">\r\n" + //
                "            <a href=\"/\">\r\n" + //
                "            <img class='adapt-logo' src=\"logo.png\" alt=\"app logo\" id=\"logo\" width=\"260px\" height=\"30px\">\r\n"
                + //
                "            </a>\r\n" + //
                "            <div  class=\"menu\">\r\n" + //
                "            <a href=\"/\">Home</a>\r\n" + //
                "            <a href=\"mission.html\">Mission</a>\r\n" + //
                "            <a href=\"page2A.html\">LGA 2019-2020</a>\r\n" + //
                "            <a href=\"page2B.html\">Regional Group</a>\r\n" + //
                "            <a href=\"page3A.html\">Similar LGAs</a>\r\n" + //
                "            <a href=\"page3B.html\">Change in Region</a>\r\n" + //
                "            </div>\r\n" + //
                "        </div>\r\n";
        html += "<div class='phone-menu'>" +
                "<a href=\"/\">Home</a>\r\n" + //
                "<p><a href=\"mission.html\">Mission</a>\r\n<p>" +
                "<p><a href=\"page2A.html\">LGA 2019-2020</a>\r\n<p>" +
                "<p><a href=\"page2B.html\">Regional Group</a>\r\n<p>" +
                "<p><a href=\"page3A.html\">Similar LGAs</a>\r\n<p>" +
                "<p><a href=\"page3B.html\">Change in Region</a>\r\n<p>" +
                "</div>";

        // Add header content block
        // html = html + """
        // <div class='header'>
        // <h1>Our Mission</h1>
        // </div>
        // """;

        // Add Div for page Content
        html = html + "<div class='content'>";

        // Add HTML for the page content
        html = html
                + """
                        <h1>Mission Statement</h1>
                        <p>Our mission is to empower communities and decision-makers in New South Wales by providing transparent, accessible, and accurate data on waste management. By offering detailed insights into waste collection, recycling, disposal, and demographic information across local government areas and regional groups, we aim to support informed choices that drive sustainable practices and environmental responsibility.</p>
                        <h1>How It Works</h1>
                        <h2>LGA 2019-2020</h2>
                        <p>This page provides a detailed view of waste management data for selected Local Government Areas (LGAs) in New South Wales. Users can choose multiple LGAs, a waste resource type (recyclable, organics, or waste), and relevant subtypes, allowing for a focused analysis of specific waste categories. For each LGA selected, the page displays the population, number of surveyed households, total waste collected, total waste recycled, the recycling percentage, and the average waste per household. Data can be sorted by any of these values in ascending or descending order, ensuring easy access to insights for informed decision-making. The data can also be related with the Similar LGAs page if the user wants to view similar LGAs to a specific one he viewed in this page.</p>
                        <h2>Regional Group</h2>
                        <p>This page provides insights into waste management statistics for different regional groups in New South Wales, including the Sydney Metropolitan Area (SMA), Extended Regulated Area (ERA), Regional Regulated Area (RRA), and the Rest of NSW. You can select one regional group, choose a waste resource type (Recyclable, Organics, or Waste), and specify a threshold to filter results by a selected statistic (Collected, Recycled, or Disposed). For each year that meets your criteria and exceeds the threshold, the page displays details such as the total waste collected, recycled, and disposed, along with the percentage of waste disposed relative to the entire state. You can sort this data by any column in both ascending and descending order, allowing for easy comparison across annual periods and supporting informed analysis of regional waste trends.</p>
                        <h2>Similar LGAs</h2>
                        <p>This page enables you to find Local Government Areas (LGAs) with waste recycling patterns most similar to a selected LGA. Start by choosing an LGA, a waste resource type (Recyclable, Organics, or Waste), and a specific year, then set a cut-off value for the number of similar LGAs you'd like to compare. The system will identify the top matching LGAs based on the percentage of waste recycled for the chosen type and period. For each similar LGA, you'll see details including its name, recycling percentage for each subtype, and its similarity score relative to the selected LGA. The data can be sorted by any column in ascending or descending order, making it easy to compare recycling performance across regions. This tool helps identify and analyze recycling trends across LGAs with similar waste management profiles.</p>
                        <h2>Change in Region</h2>
                        <p>This page allows you to analyze waste collection and recycling trends over a chosen time range for specific regional groups in New South Wales, including the Sydney Metropolitan Area (SMA), Extended Regulated Area (ERA), Regional Regulated Area (RRA), and the Rest of NSW. Begin by selecting a start and end period, a regional group, and a waste resource type (Recyclable, Organics, or Waste). For each period, the page displays the total waste collected and recycled, along with the change between the two periods. You can view the change as either a percentage or an absolute difference, providing a clear comparison of waste trends over time to better understand shifts in regional waste management practices.</p>
                        <h1>Our Ideal Customers</h1>
                        <div class='hor-center'>
                            <div class='person-group adaptive-text'>
                            """;
        for (int name : jdbc.GetPersonaID()) {
            html += "<div class='person'>" +
                    "<div class='hor-center'><img class='persona-img' src=\"" + jdbc.GetPersonaImage(name)
                    + "\" alt=\"app logo\" id=\"logo\" width=\"270px\" height=\"300px\"></div>" +
                    "<h1>Name : </h1>";
            html += "<p>" + jdbc.GetPersonaName(name) + "</p>" +
                    "<h1>Attributes : </h1>";
            for (String i : jdbc.GetAttributes(name, "Attributes")) {
                html += "<p>" + i + "</p>";
            }
            html += "<h1>Background : </h1>";
            for (String i : jdbc.GetAttributes(name, "Background")) {
                html += "<p>" + i + "</p>";
            }
            html += "<h1>Needs : </h1>";
            for (String i : jdbc.GetAttributes(name, "Need")) {
                html += "<p> - " + i + "</p>";
            }
            html += "<h1>Goals : </h1>";
            for (String i : jdbc.GetAttributes(name, "Goal")) {
                html += "<p> - " + i + "</p>";
            }
            html += "<h1>Skills and Experience : </h1>";
            for (String i : jdbc.GetAttributes(name, "Skill")) {
                html += "<p> - " + i + "</p>";
            }
            html += "</div>";
        }
        html += """
                    </div>
                </div>
                <h1>Meet The Team</h1>
                <div class='hor-center'>
                """ + "<div class='person-group adaptive-text'>";
        for (Student student : jdbc.GetStudentDetails()) {
            html += "<div class='person'>" +
                    "<div class='hor-center'><img class='persona-img' src=\"" + student.imagePath
                    + "\" alt=\"app logo\" id=\"logo\" width=\"270px\" height=\"300px\"></div>" +
                    "<h1>Name : </h1>" +
                    "<p>" + student.studentName + "</p>" +
                    "<h1>Student Number : </h1>" +
                    "<p>" + student.studentID + "</p>" +
                    "</div>";
        }
        html += """
                </div>
                </div>
                """;

        // This example uses JDBC to lookup the countries
        // Close Content div
        html = html + "</div>";
        html += "</main>";

        html += "        <script>\r\n" + //
                "            // Get the current page URL\r\n" + //
                "            let currentPage = window.location.pathname.split(\"/\").pop();\r\n"
                + "if (currentPage === \"\") currentPage = \"/\";\r\n" + //
                "console.log(\"Current Page:\", currentPage);\r\n" + // check for script not working
                "            // Add 'active' class to the current page link\r\n" + //
                "            document.querySelectorAll('.menu a').forEach(link => {\r\n" + //
                "                if (link.getAttribute('href') === currentPage) {\r\n" + //
                "                    link.classList.add('active'); \r\n" + //
                "                }\r\n" + //
                "            });\r\n" + //
                "        </script>\r\n";

        // Footer
        html += "    <footer>\r\n" + //
                "        <a href=\"mission.html\">Contact | FAQ | Other Relevant Information</a>\r\n"
                + //
                "    </footer>\r\n";

        // Finish the HTML webpage
        html = html + "</body>";

        html += "</html>";

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

}