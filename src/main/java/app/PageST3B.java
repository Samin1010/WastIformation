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

public class PageST3B implements Handler {

        // URL of this page relative to http://localhost:7001/
        public static final String URL = "/page3B.html";

        @Override
        public void handle(Context context) throws Exception {

                JDBCConnection jdbc = new JDBCConnection();

                boolean isPercentageRecycled = false;
                boolean isPercentageCollected = true;


                String regional_group_drop = context.formParam("regional-group-drop");
                String resource_type_drop = context.formParam("resource-type-drop");
                String start_year_drop = context.formParam("start-year-drop");
                String end_year_drop = context.formParam("end-year-drop");
                String change_type_drop = context.formParam("change-type-drop");
                
                // Create a simple HTML webpage in a String
                String html = "<html>";

                // Add some Head information
                html = html + "<head>" +
                                "<title>Change in Region</title>";

                // Add some CSS (external file)
                html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";
                html += """
                                <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
                                <script type="text/javascript">
                                google.charts.load('current', {'packages':['corechart']});
                                google.charts.setOnLoadCallback(drawChart);

                                function drawChart() {
                                        var dataFromBody = JSON.parse(document.getElementById('chart-data').textContent);

                                        var data = google.visualization.arrayToDataTable(dataFromBody);

                                        var options = {
                                        animation: {
                                                startup : true,
                                                duration : 1200,
                                                easing: 'out'
                                        },
                                        colors: ['#86f381', '#2f9400']
                                        };

                                        var chart = new google.visualization.ColumnChart(document.getElementById('columnchart_material'));

                                        chart.draw(data, options);
                                }
                                </script>
                                """;
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
                                "            <div class=\"menu\">\r\n" + //
                                "            <a href=\"/\">Home</a>\r\n" + //
                                "            <a href=\"mission.html\">Mission</a>\r\n" + //
                                "            <a href=\"page2A.html\">LGA 2019-2020</a>\r\n" + //
                                "            <a href=\"page2B.html\">Regional Group</a>\r\n" + //
                                "            <a href=\"page3A.html\">Similar LGAs</a>\r\n" + //
                                "            <a href=\"page3B.html\">Change in Region</a>\r\n" + //
                                "            </div>\r\n" + //
                                "        </div>\r\n";
                                html += 
                                "<div class='phone-menu'>"+
                                "<a href=\"/\">Home</a>\r\n" + //
                                    "<p><a href=\"mission.html\">Mission</a>\r\n<p>" + 
                                    "<p><a href=\"page2A.html\">LGA 2019-2020</a>\r\n<p>" + 
                                    "<p><a href=\"page2B.html\">Regional Group</a>\r\n<p>" + 
                                    "<p><a href=\"page3A.html\">Similar LGAs</a>\r\n<p>" + 
                                    "<p><a href=\"page3B.html\">Change in Region</a>\r\n<p>" + 
                                "</div>";

                // Add Div for page Content
                html = html + "<div class='content'>";

                // Add HTML for the page content
                html = html + "<form  action='/page3B.html' method='post'>";
                if((regional_group_drop == null || resource_type_drop == null || start_year_drop == null || end_year_drop == null || change_type_drop == null)) {                      
                        html += """
                                <p class='error-msg'>Please select an option from all below criteria</p>
                                """;
                    }

                html = html + "<div class='form-group'>";
                html = html + "   <div>";
                html = html + "      <select id='regional-group-drop' name='regional-group-drop'>";
                html = html + " <option disabled selected>" + "Select Region Group" + "</option>";
                for (String name : jdbc.getRegionalGroups()) {
                html = html + " <option " + (((regional_group_drop != null) && (regional_group_drop.equals(name))) ? " selected " : "") + ">" + name + "</option>";
                }
                html = html + "</select>";
                html = html + "   </div>";

                html = html + "   <div>";
                html = html + "      <select id='resource-type-drop' name='resource-type-drop'>";
                html = html + " <option disabled selected>" + "Select Resource Type" + "</option>";
                html = html + " <option " + (((resource_type_drop != null) && (resource_type_drop.equals("Recycling"))) ? " selected " : "") +" value='Recycling'>" + "Recyclable" + "</option>";
                html = html + " <option " + (((resource_type_drop != null) && (resource_type_drop.equals("Organic"))) ? " selected " : "") + " value='Organic'>" + "Organic" + "</option>";
                html = html + " <option " + (((resource_type_drop != null) && (resource_type_drop.equals("Waste"))) ? " selected " : "") + " value='Waste'>" + "Waste" + "</option>";
                html = html + "</select>";
                html = html + "   </div>";

                html = html + "   <div>";
                html = html + "      <select id='start-year-drop' name='start-year-drop'>";
                html = html + " <option disabled selected>" + "Start Period" + "</option>";
                for (Integer year : jdbc.GetStartYears()) {
                        html = html + " <option " + (((start_year_drop != null) && (start_year_drop.equals(year + ""))) ? " selected " : "") + " >" + year + "</option>";
                }
                html = html + "</select>";
                html = html + "   </div>";

                html = html + "   <div>";
                html = html + "      <select id='end-year-drop' name='end-year-drop'>";
                html = html + " <option disabled selected>" + "End Period" + "</option>";
                for (Integer year : jdbc.GetEndYears()) {
                        html = html + " <option " + (((end_year_drop != null) && (end_year_drop.equals(year + ""))) ? " selected " : "") + " >" + year + "</option>";
                }
                html = html + "</select>";
                html = html + "   </div>";

                html = html + "   <div>";
                html = html + "      <select id='change-type-drop' name='change-type-drop'>";
                html = html + " <option  value='ABS' disabled selected>" + "View Change As" + "</option>";
                html = html + " <option " + (((change_type_drop != null) && (change_type_drop.equals("ABS"))) ? " selected " : "") + " value='ABS'>" + "Absolute Value" + "</option>";
                html = html + " <option " + (((change_type_drop != null) && (change_type_drop.equals("Percentage"))) ? " selected " : "") + " value='Percentage'>" + "Percentage" + "</option>";
                html = html + "</select>";
                html = html + "   </div>";

                html = html + "</div>";

                html = html + "<div class='form-group'>";
                html = html + "   <button type='submit' class='btn btn-primary'>Get Results</button>";
                html = html + "</div>";

                html += "</form>";

                html += "<h1>Results</h1>";
                
                if(regional_group_drop != null && resource_type_drop != null && start_year_drop != null && end_year_drop != null && change_type_drop != null) {     
                        ST3BResult DisplayResult = jdbc.GetST3BResults(regional_group_drop, resource_type_drop, start_year_drop, end_year_drop, change_type_drop);                 
                        html+= "<p class='show-result'>Showing results for " + regional_group_drop + ", " + resource_type_drop + ", " + " between " + start_year_drop + " and " + end_year_drop +" as " + change_type_drop + "</p>" ;
                        html += """
                                        <script id="chart-data" type="application/json">
                                        [
                                                ["Year", "Collected", "Recycled"], """ +
                                                "[\"" + start_year_drop +"\", " + DisplayResult.StartCollected +", " + DisplayResult.StartRecycled +"]," +
                                                "[\"" + end_year_drop +"\", " + DisplayResult.EndCollected+", " + DisplayResult.EndRecycled + "]" +
                                                """
                                        ]
                                        </script>
                                        """;
                        html += "<div class='hor-center'><h2 class='text-center'>Change in Waste Collected and Recycled Over Time</h2></div>";
                        html += "<div class='hor-center'><div id=\"columnchart_material\" style=\"width: 100%; height: 500px;\"></div></div>";
                        html += "<div class='hor-center'><h2 class='text-center'>Key Information</h2></div>";
                        html += "<br></br>";
                        html += """
                                <div class='hor-center card-holder'>
                                        <div class='data-card'>
                                        <h2 class='text-center'>Waste Collected</h2>""";
                        html += 
                                        "<p class='text-center'>Start period : " + DisplayResult.StartCollected + "</p>" +
                                        "<p class='text-center'>End period : " +DisplayResult.EndCollected + "</p>" + 
                                        "<p class='text-center'>Change between time period : </p>" +
                                        "<p class='very-big-text text-center'>"; 
                                        if(DisplayResult.StartCollected == 0){
                                                html +=  (DisplayResult.isPercent ? 0 + "%" : DisplayResult.EndCollected - DisplayResult.StartCollected); 
                                        }else{
                                                html +=  (DisplayResult.isPercent ? ((DisplayResult.EndCollected - DisplayResult.StartCollected) * 100 / DisplayResult.StartCollected) + "%" : (DisplayResult.EndCollected - DisplayResult.StartCollected));
                                        }
                                        
                                       html +=  "</p>" + """
                                        </div>
                                        <div class='data-card'>
                                        <h2 class='text-center'>Waste Recycled</h2>
                                        """ + 

                                        "<p class='text-center'>Start period : " + DisplayResult.StartRecycled + "</p>" +
                                        "<p class='text-center'>End period : " + DisplayResult.EndRecycled + "</p>" +
                                        "<p class='text-center'>Change between time period : </p>" +
                                        "<p class='very-big-text text-center'>"; 
                                        if(DisplayResult.StartRecycled == 0){
                                                html +=  (DisplayResult.isPercent ? 0 + "%" : (DisplayResult.EndRecycled - DisplayResult.StartRecycled));
                                        }else{
                                                html +=  (DisplayResult.isPercent ? ((DisplayResult.EndRecycled - DisplayResult.StartRecycled) * 100 / DisplayResult.StartRecycled) + "%" : (DisplayResult.EndRecycled - DisplayResult.StartRecycled));
                                        }
                                        
                                       html +=  "</p>" + """
                                        </div>
                                </div>
                                """;    
                                html += "<br></br>";
                                html += "<br></br>";                    
                }else{
                        html += """
                                <div class='hor-center'>
                                        <p class='no-data-text'>No data to show :(</p>
                                </div>
                                """;
                }

                if(regional_group_drop != null && resource_type_drop != null && start_year_drop != null && end_year_drop != null && change_type_drop != null) {
                        
                }


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
                html = html + "</body>" + "</html>";

                // DO NOT MODIFY THIS
                // Makes Javalin render the webpage
                context.html(html);
        }

}


