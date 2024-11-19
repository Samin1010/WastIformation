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

public class PageST2B implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/page2B.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        JDBCConnection jdbc = new JDBCConnection();

        // Add some Head information
        html = html + "<head>" +
                "<title>Regional Groups</title>";

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
                                    colors: ['#ff6405', '#2f9400', '#4119bd'],
                                    curveType: 'none',
                                    legend: { position: 'bottom' }
                                    };

                                    var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));

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
                "            <img class='adapt-logo' src=\"logo.png\" alt=\"app logo\" id=\"logo\" width=\"260px\" height=\"30px\">\r\n" + //
                "            </a>\r\n" + 
                "            <div class=\"menu\">\r\n" + //
                    "            <a href=\"/\">Home</a>\r\n" + //
                    "            <a href=\"mission.html\">Mission</a>\r\n" + //
                    "            <a href=\"page2A.html\">LGA 2019-2020</a>\r\n" + //
                    "            <a href=\"page2B.html\">Regional Group</a>\r\n" + //
                    "            <a href=\"page3A.html\">Similar LGAs</a>\r\n" + //
                    "            <a href=\"page3B.html\">Change in Region</a>\r\n" + //
                "            </div>\r\n" + 
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

        // Add header content block
        // html = html + """
        //             <div class='header'>
        //                 <h1>Subtask 2.B</h1>
        //             </div>
        //         """;

        // Add Div for page Content
        html = html + "<div class='content'>";

        // Add HTML for the page content
        html = html + "<div>";
        html = html + "<form action='/page2B.html' method='post'>";

        String regional_group_drop = context.formParam("regional-group-drop");
        String resource_type_drop = context.formParam("resource-type-drop");
        String waste_statistic_drop = context.formParam("waste-statistic-drop");
        String waste_statistic_textbox = context.formParam("waste-statistic-textbox");
        String sort_order_drop = context.formParam("sort-order-drop");
        String sort_attribute_drop = context.formParam("sort-attribute-drop");

        if((regional_group_drop == null || resource_type_drop == null || sort_order_drop == null || waste_statistic_drop == null || waste_statistic_textbox == null || sort_attribute_drop == null)) {                      
            html += """
                    <p class='error-msg'>Please select an option from all below criteria</p>
                    """;
        }

        html = html + "<div class='form-group'>";
        html = html + "   <div>";
        
        html = html + "      <select id='regional-group-drop' name='regional-group-drop'>";
        html = html + " <option disabled selected>" + "Select Region Group" + "</option>";
        for (String name : jdbc.getRegionalGroups()) {
            html = html + " <option " + (((regional_group_drop != null) && (regional_group_drop.equals(name))) ? " selected " : "") +" >" + name + "</option>";
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
        html = html + "</div>";
        
        html = html + "<div class='form-group'>";
        html += "<label for='sort'>Select Minimum Waste Statistic</label>\r\n" ;
        html = html + "      <select id='waste-statistic-drop' name='waste-statistic-drop'>";
        html = html + " <option disabled selected>" + "Select Waste Statistic" + "</option>";
        html = html + " <option " + (((waste_statistic_drop != null) && (waste_statistic_drop.equals("Collected"))) ? " selected " : "") + " value='Collected'>" + "Collected" + "</option>";
        html = html + " <option " + (((waste_statistic_drop != null) && (waste_statistic_drop.equals("Recycled"))) ? " selected " : "") + " value='Recycled'>" + "Recycled" + "</option>";
        html = html + " <option " + (((waste_statistic_drop != null) && (waste_statistic_drop.equals("Disposed"))) ? " selected " : "") + " value='Disposed'>" + "Disposed" + "</option>";
        html = html + " <option " + (((waste_statistic_drop != null) && (waste_statistic_drop.equals("Percentage"))) ? " selected " : "") + " value='Percentage'>" + "Waste Disposed Percentage" + "</option>";
        html = html + "</select>";
        
        html = html + "      <input type=\"number\" value='0' placeholder='Minimum Waste Statistic' id='waste-statistic-textbox' name='waste-statistic-textbox'>";
        html = html + "</div>";

        html = html + "<div class='form-group'>";
        
        html = html + "<label id='sort-attribute-drop'> Sort results </label>";
        html = html + "   <div>";
        html = html + "      <select id='sort-attribute-drop' name='sort-attribute-drop'>";
        html = html + " <option  value='Period' selected>" + "Annual Period" + "</option>";
        html = html + " <option " + (((sort_attribute_drop != null) && (sort_attribute_drop.equals("Collected"))) ? " selected " : "") + " value='Collected'>" + "Collected" + "</option>";
        html = html + " <option " + (((sort_attribute_drop != null) && (sort_attribute_drop.equals("Recycled"))) ? " selected " : "") +" value='Recycled'>" + "Recycled" + "</option>";
        html = html + " <option " + (((sort_attribute_drop != null) && (sort_attribute_drop.equals("Disposed"))) ? " selected " : "") + " value='Disposed'>" + "Disposed" + "</option>";
        html = html + " <option " + (((sort_attribute_drop != null) && (sort_attribute_drop.equals("Percentage"))) ? " selected " : "") +" value='Percentage'>" + "Waste Disposed Percentage" + "</option>";
        // for (String type : movieTypes) {

        // }
        html = html + "</select>";
        html = html + "   </div>";
        
        html = html + "   <div>";
        html = html + "      <select id='sort-order-drop' name='sort-order-drop'>";
        html = html + " <option " + (((sort_order_drop != null) && (sort_order_drop.equals("ASC"))) ? " selected " : "") + " value='ASC' selected>" + "Ascending" + "</option>";
        html = html + " <option " + (((sort_order_drop != null) && (sort_order_drop.equals("DESC"))) ? " selected " : "") + " value='DESC'>" + "Descending" + "</option>";
        // for (String type : movieTypes) {

        // }
        html = html + "</select>";
        html = html + "   </div>";
        html = html + "</div>";
        html = html + "<div class='form-group'>";
        html = html + "   <button type='submit' class='btn btn-primary'>Get Results</button>";
        html = html + "</div>";

        html = html + "</form>";
        html = html + "</div>";

        html += "<h1>Results</h1>";

        ArrayList<ST2BResult> resultsArray = new ArrayList<ST2BResult>();

        if(regional_group_drop != null && resource_type_drop != null && sort_order_drop != null && waste_statistic_drop != null&& waste_statistic_textbox != null && sort_attribute_drop != null) {                      
            html += 
                """
                        <script id="chart-data" type="application/json">
                                        [
                                                ["Year", "Collected", "Recycled", "Disposed"] """;
            for(ST2BResult result : jdbc.GetST2BResults(regional_group_drop, resource_type_drop , waste_statistic_drop, Integer.parseInt(waste_statistic_textbox), sort_attribute_drop, sort_order_drop)){
                resultsArray.add(result);
                html += ",[ \""+ result.AnnualPeriod + "\", " + result.Collected +" , " + result.Recycled +" , " + result.Disposed +" ]";
                
            }
            html += """
                                        ]
                                        </script>
                        """;
            }

            if((regional_group_drop == null || resource_type_drop == null || sort_order_drop == null || waste_statistic_drop == null || waste_statistic_textbox == null || sort_attribute_drop == null)) {                      
                html += """
                                <div class='hor-center'>
                                        <p class='no-data-text'>No data to show :(</p>
                                </div>
                                """;
            }
        
            if(regional_group_drop != null && resource_type_drop != null && sort_order_drop != null && waste_statistic_drop != null&& waste_statistic_textbox != null && sort_attribute_drop != null) {                      
                html+= "<p class='show-result'>Showing results for " + regional_group_drop + ", " + resource_type_drop + ", " + " with a minimum value of " + waste_statistic_textbox + " for " + waste_statistic_drop +" sorted by  " + sort_attribute_drop + " in " + (sort_order_drop.equals("ASC") ? "ascending" : "descending") +" order</p>" ;
                html += "<div class='hor-center'><h2 class='text-center'>Change in Waste Collected, Recycled and Disposed Over Time for " + regional_group_drop +" </h2></div>";
                html += "<div class='hor-center'><div id=\"curve_chart\" style=\"width: 100%; height: 500px\"></div></div>";
                html += "<br></br>";
                html += """
                <div class='hor-center '>
                <div class='table-div whole-table'>
                    <table class=''>
                        <tr class='tr-header'>
                            <th>
                                Annual Period
                            </th>
                            <th>
                                Collected
                            </th>
                            <th>
                                Recycled
                            </th>
                            <th>
                                Disposed
                            </th>
                            <th>
                                Waste Disposed Percentage
                            </th>
                        </tr>
                        """;                
                }

        for(ST2BResult result : resultsArray){
            html += """
                        <tr class='tr-data'>
                            <td>
                    """;
            html += result.AnnualPeriod +
                    "       </td>" +
                            "<td>" +
                            result.Collected +
                            "</td>" +
                            "<td>" +
                            result.Recycled +
                            "</td>" +
                            "<td>" +
                            result.Disposed +
                            "</td>" +
                            "<td>" +
                            result.WasteDisposedPercentage + "%" +
                            "</td>" +
                        "</tr>";
        }

        
        html += """
                    </table>
                </div>
                </div>
                """;

            for(ST2BResult result : resultsArray){
                html += "<div class='hor-center'>"+
                "<div class='whole-card'>"+
                "<div class='hor-center'>"+
                "<div class='phone-card'>"+
                "<p class='lg-text-2'> Annual Period : " + result.AnnualPeriod +"</p>" +
                "<p> Total Collected : " + result.Collected +"</p>" +
                "<p> Total Recycled : " + result.Recycled +"</p>" +
                "<p> Total Disposed : " + result.Disposed +"</p>" +
                "<p> Waste Disposed Percentage : " + result.WasteDisposedPercentage +" %</p>" +
                "</div>"+
                "</div>"+
                "</div>"+
                "</div>";
            }

        // for (String type : movieTypes) {
        // }

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
        html += "    <footer >\r\n" + //
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

