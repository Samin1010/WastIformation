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

public class PageST3A implements Handler {

        // URL of this page relative to http://localhost:7001/
        public static final String URL = "/page3A.html";

        @Override
        public void handle(Context context) throws Exception {
                // Create a simple HTML webpage in a String
                String html = "<html>\r\n";

                SaminJDBCConnection jdbc = new SaminJDBCConnection();

                ArrayList<String> LGAnames = jdbc.getLGAnames();

                String LGAname = context.formParam("LGAname");
                String resource = context.formParam("resource");
                String period = context.formParam("period");
                String cutOffString = context.formParam("cutOff");
                String sort = context.formParam("sort");
                String sortBy = context.formParam("sortBy");

                html += "    <head>\r\n" + //
                                "        <title>Similar LGAs</title>\r\n" + //
                                "        <link rel=\"stylesheet\" type=\"text/css\" href=\"common.css\">\r\n" + //
                                "<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.18/dist/css/bootstrap-select.min.css\">\r\n"
                                + //
                                "<script src=\"https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.18/dist/js/bootstrap-select.min.js\"></script>\r\n"
                                + //
                                "" +
                                """
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
                                                        }
                                                        // colors: ['#7305ff']
                                                        };

                                                        var chart = new google.visualization.ColumnChart(document.getElementById('columnchart_material'));

                                                        chart.draw(data, options);
                                                }
                                                </script>
                                                """
                                +

                                "    </head>\r\n" + //
                                "    <body>\r\n" + //
                                "   <main>" +
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
                html += "<div class='content'>";
                html += "<div class='phone-menu'>" +
                                "<a href=\"/\">Home</a>\r\n" + //
                                "<p><a href=\"mission.html\">Mission</a>\r\n<p>" +
                                "<p><a href=\"page2A.html\">LGA 2019-2020</a>\r\n<p>" +
                                "<p><a href=\"page2B.html\">Regional Group</a>\r\n<p>" +
                                "<p><a href=\"page3A.html\">Similar LGAs</a>\r\n<p>" +
                                "<p><a href=\"page3B.html\">Change in Region</a>\r\n<p>" +
                                "</div>" +
                                "        <div class=\"form1A\">\r\n";
                if ((LGAname == null || resource == null || sort == null || sortBy == null || period == null
                                || cutOffString == null)) {
                        html += """
                                        <p class='error-msg'>Please select an option from all below criteria</p>
                                        """;
                }
                html += "          <form action=\"/page3A.html\" method=\"post\">" +
                                "<div class='form-group'>" +
                                "              <select id=\"LGA1A\" class='form-control' name=\"LGAname\">\r\n"
                                +
                                "                <option value=\"\" disabled selected hidden>Select a LGA</option>\r\n";
                for (String i : LGAnames) {
                        html += "<option";
                        if (LGAname != null && i.equals(LGAname))
                                html += " selected";
                        html += ">" + i + "</option>\r\n";
                }

                html += "</select>\r\n" +
                                "<select id=\"resource1A\" name=\"resource\" onchange=\"updateSubTypeOptions()\">" +
                                "  <option value=\"\" disabled selected hidden>Select Resource Type</option>\r\n" +
                                "  <option value='LGARecyclingStatistics'";
                if (resource != null && resource.equals("LGARecyclingStatistics"))
                        html += " selected";
                html += ">Recyclable</option>\r\n" +
                                "  <option value='LGAOrganicStatistics'";
                if (resource != null && resource.equals("LGAOrganicStatistics"))
                        html += " selected";
                html += ">Organic</option>\r\n" +
                                "  <option value='LGAWasteStatistics'";
                if (resource != null && resource.equals("LGAWasteStatistics"))
                        html += " selected";
                html += ">Waste</option>\r\n" +
                                "</select>";
                html += "</div>";

                html += "<div class='form-group'>";
                html += "<select id=\"period2A\" name=\"period\">" +
                                "  <option value=\"\" disabled selected hidden>Select Annual Period</option>\r\n" +
                                "  <option value='2018-2019'";
                if (period != null && period.equals("2018-2019"))
                        html += " selected";
                html += ">2018-2019</option>\r\n" +
                                "  <option value='2019-2020'";
                if (period != null && period.equals("2019-2020"))
                        html += " selected";
                html += ">2019-2020</option>\r\n" +
                                "</select>";

                html += "  <label for='cutOff'>Cut Off Value : </label>\r\n" +
                                "   <input type=\"number\" class='form-control' id='cutoff2A' value='10' name='cutOff'>\r\n";

                int cutOff = 0;
                if (cutOffString != null)
                        cutOff = Integer.parseInt(cutOffString);
                html += "</div>";
                html += "<div class='form-group'>";
                html += "<label for='sort'>Sort Result</label>\r\n" +
                                "<select id=\"sort2A\" name=\"sort\">" +
                                "  <option value='Similarity'>Similarity</option>\r\n" +
                                "  <option value='LGA'>LGA Name</option>\r\n";
                if (resource != null && resource.equals("LGARecyclingStatistics")) {
                        html += "  <option value='Kerbside'>Kerbside Recycling</option>\r\n" +
                                        "  <option value='CDS'>CDS Recycling</option>\r\n" +
                                        "  <option value='DropOff'>Drop Off Recycling</option>\r\n" +
                                        "  <option value='CleanUp'>Cleanup Recycling</option>\r\n";
                }
                if (resource != null && resource.equals("LGAOrganicStatistics")) {
                        html += "  <option value='Kerbside'>Kerbside Organics Bin</option>\r\n" +
                                        "  <option value='KerbsideFOGO'>Kerbside FOGO Organics</option>\r\n" +
                                        "  <option value='DropOff'>Drop Off Organics</option>\r\n" +
                                        "  <option value='CleanUp'>Cleanup Organics</option>\r\n" +
                                        "  <option value='OtherCouncil'>Other Council Garden Organics</option>\r\n";
                }
                if (resource != null && resource.equals("LGAWasteStatistics")) {
                        html += "  <option value='Kerbside'>Kerbside Waste Bin</option>\r\n" +
                                        "  <option value='DropOff'>Drop Off</option>\r\n" +
                                        "  <option value='CleanUp'>Cleanup</option>\r\n";
                }
                html += "</select>";

                html += "<select id=\"sortBy2A\" name=\"sortBy\">" +
                                "  <option value='DESC'";
                if (sortBy != null && sortBy.equals("DESC"))
                        html += " selected";
                html += ">Descending</option>\r\n" +
                                "  <option value='ASC'";
                if (sortBy != null && sortBy.equals("ASC"))
                        html += " selected";
                html += ">Ascending</option>\r\n" +
                                "</select>";
                html += "</div>";
                html += "<div class='form-group'>";

                html += "<button type='submit' class='btn btn-primary'>Get Results</button>";
                html += "</div>";
                html += "</form></div>\r\n";
                html += "<h1>Results</h1>";
                if ((LGAname == null || resource == null || sort == null || sortBy == null || period == null
                                || cutOffString == null)) {
                        html += """
                                        <div class='hor-center'>
                                                <p class='no-data-text'>No data to show :(</p>
                                        </div>
                                        """;
                }
                ArrayList<result3Arecyclable> resultArrayRecylable = new ArrayList<result3Arecyclable>();
                ArrayList<result3Aorganic> resultArrayOrganic = new ArrayList<result3Aorganic>();
                ArrayList<result3Awaste> resultArrayWaste = new ArrayList<result3Awaste>();

                if (LGAname != null && resource != null && sort != null && sortBy != null && period != null) {
                        html += "<p class='show-result'>Showing results for " + LGAname + ", ";
                        if(resource.equals("LGARecyclingStatistics")){
                                html += "Recyclable";
                        }else if(resource.equals("LGAOrganicStatistics")){
                                html += "Organic";
                        }if(resource.equals("LGAWasteStatistics")){
                                html += "Waste";
                        }
                        html += " during "
                                        + period + " upto " + cutOff + " values" + " sorted by " + sort + " in "
                                        + (sortBy.equals("ASC") ? "ascending" : "descending") + " order</p>";
                        html += """
                                        <script id="chart-data" type="application/json">
                                        [
                                                ["LGA", "Similarity"] """;

                        if (resource.equals("LGARecyclingStatistics")) {
                                for (result3Arecyclable result : jdbc.getResult3Arecyclable(LGAname, resource, period,
                                                cutOff, sort, sortBy)) {
                                        resultArrayRecylable.add(result);
                                        html += ",[\"" + result.LGAName + "\", " + result.similarity + "]";
                                }
                        } else if (resource.equals("LGAOrganicStatistics")) {
                                for (result3Aorganic result : jdbc.getResult3Aorganic(LGAname, resource, period, cutOff,
                                                sort, sortBy)) {
                                        resultArrayOrganic.add(result);
                                        html += ",[\"" + result.LGAName + "\", " + result.similarity + "]";
                                }
                        } else if (resource.equals("LGAWasteStatistics")) {
                                for (result3Awaste result : jdbc.getResult3Awaste(LGAname, resource, period, cutOff,
                                                sort, sortBy)) {
                                        resultArrayWaste.add(result);
                                        html += ",[\"" + result.LGAName + "\", " + result.similarity + "]";
                                }
                        }
                        html += """
                                        ]
                                        </script>
                                        """;
                }
                if (LGAname != null && resource != null && sort != null && sortBy != null && period != null) {
                        html += "<div class='hor-center'><h2 class='text-center'>LGA Similar to " + LGAname
                                        + " </h2></div>";
                        html += "<div class='hor-center'><div id=\"columnchart_material\" style=\"width: 100%; height: 500px;\"></div></div>";
                        html += "<br></br>";
                        if (resource.equals("LGARecyclingStatistics")) {
                                for (result3Arecyclable result : resultArrayRecylable) {
                                        html += "<div class='hor-center width-100'>" +
                                                        "<div class='whole-card'>" +
                                                        "<div class='hor-center'>" +
                                                        "<div class='phone-card'>" +
                                                        "<p class='lg-text-2'> LGA Name : " + result.LGAName + "</p>" +
                                                        "<p> Similarity : " + result.similarity + "</p>" +
                                                        "<p> Kerbside : " + result.kerbside + "</p>" +
                                                        "<p> Drop Off : " + result.dropOff + "</p>" +
                                                        "<p> CDS : " + result.cds + "</p>" +
                                                        "<p> Clean Up : " + result.cleanUp + "</p>" +
                                                        "</div>" +
                                                        "</div>" +
                                                        "</div>" +
                                                        "</div>";
                                }
                        } else if (resource.equals("LGAOrganicStatistics")) {
                                for (result3Aorganic result : resultArrayOrganic) {
                                        html += "<div class='hor-center width-100'>" +
                                                        "<div class='whole-card'>" +
                                                        "<div class='hor-center'>" +
                                                        "<div class='phone-card'>" +
                                                        "<p class='lg-text-2'> LGA Name : " + result.LGAName + "</p>" +
                                                        "<p> Similarity : " + result.similarity + "</p>" +
                                                        "<p> Kerbside : " + result.kerbside + "</p>" +
                                                        "<p> KervisedFOGO : " + result.kerbsideFOGO + "</p>" +
                                                        "<p> Drop Off : " + result.dropOff + "</p>" +
                                                        "<p> Clean Up : " + result.cleanUp + "</p>" +
                                                        "<p> Other : " + result.other + "</p>" +
                                                        "</div>" +
                                                        "</div>" +
                                                        "</div>" +
                                                        "</div>";
                                }
                        } else if (resource.equals("LGAWasteStatistics")) {
                                for (result3Awaste result : resultArrayWaste) {
                                        html += "<div class='hor-center width-100 '>" +
                                                        "<div class='whole-card'>" +
                                                        "<div class='hor-center'>" +
                                                        "<div class='phone-card'>" +
                                                        "<p class='lg-text-2'> LGA Name : " + result.LGAName + "</p>" +
                                                        "<p> Similarity : " + result.similarity + "</p>" +
                                                        "<p> Kerbside : " + result.kerbside + "</p>" +
                                                        "<p> Drop Off : " + result.dropOff + "</p>" +
                                                        "<p> Clean Up : " + result.cleanUp + "</p>" +
                                                        "</div>" +
                                                        "</div>" +
                                                        "</div>" +
                                                        "</div>";
                                }
                        }
                }
                if (LGAname != null && resource != null && sort != null && sortBy != null && period != null) {
                        if (resource.equals("LGARecyclingStatistics")) {
                                html += "                <div class='hor-center'>\r\n" + //
                                                "               <div class='table-div whole-table'>\r\n" + //
                                                "                    <table>\r\n" + //
                                                "                        <tr class='tr-header'>\r\n" + //
                                                "                            <th>\r\n" + //
                                                "                                LGA Name\r\n" + //
                                                "                            </th>\r\n" + //
                                                "                            <th>\r\n" + //
                                                "                                Similarity\r\n" + //
                                                "                            </th>\r\n" + //
                                                "                            <th>\r\n" + //
                                                "                                Percentage Kerbside Recycled\r\n" + //
                                                "                            </th>\r\n" + //
                                                "                            <th>\r\n" + //
                                                "                                Percentage Drop Off Recycled\r\n" + //
                                                "                            </th>\r\n" + //
                                                "                            <th>Percentage CDS Recycled</th>\r\n" +
                                                "                            <th>\r\n" + //
                                                "                                Percentage Clean Up Recycled\r\n" + //
                                                "                            </th>\r\n" + //
                                                "                        </tr>\r\n";
                                for (result3Arecyclable result : resultArrayRecylable) {
                                        html += "<tr class='tr-data'>" +
                                                        "<td>" + result.LGAName + "</td>" +
                                                        "<td>" + result.similarity + "</td>" +
                                                        "<td>" + result.kerbside + "</td>" +
                                                        "<td>" + result.dropOff + "</td>" +
                                                        "<td>" + result.cds + "</td>" +
                                                        "<td>" + result.cleanUp + "</td>" +
                                                        "</tr>";
                                }
                        }

                        if (resource.equals("LGAOrganicStatistics")) {
                                html += "                <div class='hor-center'>\r\n" + //
                                                "                <div class='table-div whole-table'>\r\n" + //
                                                "                    <table>\r\n" + //
                                                "                        <tr class='tr-header'>\r\n" + //
                                                "                            <th>\r\n" + //
                                                "                                LGA Name\r\n" + //
                                                "                            </th>\r\n" + //
                                                "                            <th>\r\n" + //
                                                "                                Similarity\r\n" + //
                                                "                            </th>\r\n" + //
                                                "                            <th>\r\n" + //
                                                "                                Percentage Kerbside Recycled\r\n" + //
                                                "                            </th>\r\n" + //
                                                "                            <th>\r\n" + //
                                                "                                Percentage Kerbside FOGO Recycled\r\n"
                                                + //
                                                "                            </th>\r\n" + //
                                                "                            <th>Percentage Drop Off Recycled</th>" +
                                                "                             <th>Percentage Clean Up Recycled\r\n" + //
                                                "                            </th>\r\n" + //
                                                "                            <th>Percentage Other Council Garden Organics Recycled</th>"
                                                +
                                                "                        </tr>\r\n";
                                for (result3Aorganic result : resultArrayOrganic) {
                                        html += "<tr class='tr-data'>" +
                                                        "<td>" + result.LGAName + "</td>" +
                                                        "<td>" + result.similarity + "</td>" +
                                                        "<td>" + result.kerbside + "</td>" +
                                                        "<td>" + result.kerbsideFOGO + "</td>" +
                                                        "<td>" + result.dropOff + "</td>" +
                                                        "<td>" + result.cleanUp + "</td>" +
                                                        "<td>" + result.other + "</td>" +
                                                        "</tr>";
                                }
                        }

                        if (resource.equals("LGAWasteStatistics")) {
                                html += "                <div class='hor-center'>\r\n" + //
                                                "                <div class='table-div whole-table'>\r\n" + //
                                                "                    <table>\r\n" + //
                                                "                        <tr class='tr-header'>\r\n" + //
                                                "                            <th>\r\n" + //
                                                "                                LGA Name\r\n" + //
                                                "                            </th>\r\n" + //
                                                "                            <th>\r\n" + //
                                                "                                Similarity\r\n" + //
                                                "                            </th>\r\n" + //
                                                "                            <th>\r\n" + //
                                                "                                Percentage Kerbside Recycled\r\n" + //
                                                "                            </th>\r\n" + //
                                                "                            <th>\r\n" + //
                                                "                                Percentage Drop Off Recycled\r\n" + //
                                                "                            </th>\r\n" + //
                                                "                            <th>\r\n" + //
                                                "                                Percentage Clean Up Recycled\r\n" + //
                                                "                            </th>\r\n" + //
                                                "                        </tr>\r\n";
                                for (result3Awaste result : resultArrayWaste) {
                                        html += "<tr class='tr-data'>" +

                                                        "<td>" + result.LGAName + "</td>" +
                                                        "<td>" + result.similarity + "</td>" +
                                                        "<td>" + result.kerbside + "</td>" +
                                                        " <td>" + result.dropOff + "</td>" +
                                                        " <td>" + result.cleanUp + "</td>" +
                                                        "</tr>";
                                }
                        }
                }

                html += "</table></div></div></div>\r\n";

                html += "   </main>" +
                                "        <script>\r\n" + //
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
                                "</script>" +
                                "  <script>\n" + //
                                "    // Include jQuery\n" + //
                                "    !(function(e, t) {\n" + //
                                "      \"use strict\";\n" + //
                                "      \"function\" == typeof define && define.amd\n" + //
                                "        ? define([\"jquery\"], t)\n" + //
                                "        : \"undefined\" != typeof exports\n" + //
                                "        ? (module.exports = t(require(\"jquery\")))\n" + //
                                "        : t(e.jQuery || e.Zepto);\n" + //
                                "    })(this, function(e) {\n" + //
                                "      \"use strict\";\n" + //
                                "      var t = (function() {\n" + //
                                "        function t(t) {\n" + //
                                "          (this.options = e.extend({}, i.DEFAULTS, t)),\n" + //
                                "            (this.$element = e(t.el)),\n" + //
                                "            (this.$element.wrap('<div class=\"dropdown selectpicker\"></div>')),\n" + //
                                "            (this.dropdown = this.$element.parent());\n" + //
                                "        }\n" + //
                                "        // Additional Bootstrap Select JavaScript here.\n" + //
                                "      });\n" + //
                                "      t.VERSION = \"1.13.18\";\n" + //
                                "      t.DEFAULTS = {\n" + //
                                "        dropupAuto: !0,\n" + //
                                "        header: !1,\n" + //
                                "      };\n" + //
                                "      (e.fn.selectpicker = function(i, n) {\n" + //
                                "        return this.each(function() {\n" + //
                                "          var a = e(this),\n" + //
                                "            r = a.data(\"bs.select\");\n" + //
                                "          r || a.data(\"bs.select\", (r = new t(this)));\n" + //
                                "        });\n" + //
                                "      }),\n" + //
                                "        (e.fn.selectpicker.Constructor = t);\n" + //
                                "    });\n" + //
                                "    $(document).ready(function() {\n" + //
                                "      $('#LGAname').selectpicker({\n" + //
                                "        liveSearch: true,\n" + //
                                "        title: 'Select an LGA'\n" + //
                                "      });\n" + //
                                "    });\n" + //
                                "        </script>\r\n" + //
                                "  <script>\n" + //
                                "        function updateSubTypeOptions() {\n" + //
                                "            const resource = document.getElementById('resource1A').value;\n" + //
                                "            const subTypeSelect = document.getElementById('sort2A');\n" + //
                                "            subTypeSelect.innerHTML = ''; // Clear existing options\n" + //
                                "\n" + //
                                "            const subTypeOptions = {\n" + //
                                "                \"LGARecyclingStatistics\": [\n" + //
                                "                    { value: \"Kerbside\", text: \"Kerbside Recycling\" },\n" + //
                                "                    { value: \"CDS\", text: \"CDS Recycling\" },\n" + //
                                "                    { value: \"DropOff\", text: \"Drop Off Recycling\" },\n" + //
                                "                    { value: \"CleanUp\", text: \"Cleanup Recycling\" },\n" + //
                                "                    { value: \"Similarity\", text: \"Similarity\" },\n" + //
                                "                    { value: \"LGA\", text: \"LGA Name\" }\n" + //
                                "                ],\n" + //
                                "                \"LGAOrganicStatistics\": [\n" + //
                                "                    { value: \"Kerbside\", text: \"Kerbside Organics Bin\" },\n" + //
                                "                    { value: \"KerbsideFOGO\", text: \"Kerbside FOGO Organics\" },\n" + //
                                "                    { value: \"DropOff\", text: \"Drop Off Organics\" },\n" + //
                                "                    { value: \"CleanUp\", text: \"Cleanup Organics\" },\n" + //
                                "                    { value: \"OtherCouncil\", text: \"Other Council Garden Organics\" },\n"
                                +
                                "                    { value: \"Similarity\", text: \"Similarity\" },\n" + //
                                "                    { value: \"LGA\", text: \"LGA Name\" }\n" + //
                                "                ],\n" + //
                                "                \"LGAWasteStatistics\": [\n" + //
                                "                    { value: \"Kerbside\", text: \"Kerbside Waste Bin\" },\n" + //
                                "                    { value: \"DropOff\", text: \"Drop Off\" },\n" + //
                                "                    { value: \"CleanUp\", text: \"Cleanup\" },\n" + //
                                "                    { value: \"Similarity\", text: \"Similarity\" },\n" + //
                                "                    { value: \"LGA\", text: \"LGA Name\" }\n" + //
                                "                ]\n" + //
                                "            };\n" + //
                                "\n" + //
                                "            // Add new options based on selected resource\n" + //
                                "            if (subTypeOptions[resource]) {\n" + //
                                "                subTypeOptions[resource].forEach(option => {\n" + //
                                "                    const opt = document.createElement('option');\n" + //
                                "                    opt.value = option.value;\n" + //
                                "                    opt.text = option.text;\n" + //
                                "                    subTypeSelect.appendChild(opt);\n" + //
                                "                });\n" + //
                                "            }\n" + //
                                "        }\n" + //
                                "    </script>" +
                                "    <footer>\r\n" + //
                                "        <a href=\"mission.html\">Contact | FAQ | Other Relevant Information</a>\r\n" + //
                                "    </footer>\r\n" + //
                                "    </body>\r\n" + //
                                "</html>";

                // DO NOT MODIFY THIS
                // Makes Javalin render the webpage
                context.html(html);
        }

}
