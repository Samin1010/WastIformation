package app;

import java.util.ArrayList;
import java.util.List;

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

public class PageST2A implements Handler {

        // URL of this page relative to http://localhost:7001/
        public static final String URL = "/page2A.html";

        @Override
        public void handle(Context context) throws Exception {
                // Create a simple HTML webpage in a String
                String html = "<html>\r\n";
                SaminJDBCConnection jdbc = new SaminJDBCConnection();

                List<String> LGAnameList = context.formParams("LGAname");
                ArrayList<String> LGAname = new ArrayList<>(LGAnameList);
                String resource = context.formParam("resource");
                List<String> subTypeList = context.formParams("subType");
                ArrayList<String> subType = new ArrayList<>(subTypeList);
                String sortBy = context.formParam("sortBy");
                String sort = context.formParam("sort");

                ArrayList<String> LGAnames = jdbc.getLGAnames();

                html += "    <head>\r\n" + //
                                "        <title>LGAs</title>\r\n" + //
                                "        <link rel=\"stylesheet\" type=\"text/css\" href=\"common.css\">\r\n" + //
                                "<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/gh/habibmhamadi/multi-select-tag@3.1.0/dist/css/multi-select-tag.css\">"
                                +
                                "    </head>\r\n" + //
                                "    <body>\r\n" + //
                                "    <main>" +
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
                html += "<div class='phone-menu'>" +
                                "<a href=\"/\">Home</a>\r\n" + //
                                "<p><a href=\"mission.html\">Mission</a>\r\n<p>" +
                                "<p><a href=\"page2A.html\">LGA 2019-2020</a>\r\n<p>" +
                                "<p><a href=\"page2B.html\">Regional Group</a>\r\n<p>" +
                                "<p><a href=\"page3A.html\">Similar LGAs</a>\r\n<p>" +
                                "<p><a href=\"page3B.html\">Change in Region</a>\r\n<p>" +
                                "</div>";
                html += "<div class='content'>";
                html += "        <div class=\"form1A\">\r\n" +
                                "          <form action=\"/page2A.html\" method=\"post\">";
                if ((LGAname == null || resource == null || sort == null || sortBy == null || subType == null)) {
                        html += """
                                        <p class='error-msg'>Please select an option from all below criteria</p>
                                        """;
                }
                html += "          <div class='form-group'>" +
                                "<label for='LGAname'>Select LGAs : </label> " +
                                "<div class='weird-box'><select id=\"LGA1A\" name=\"LGAname\" multiple>\r\n";
                for (String i : LGAnames) {
                        html += "<option";
                        if (LGAname != null && LGAname.contains(i))
                                html += " selected";
                        html += ">" + i + "</option>\r\n";
                }

                html += "</select></div>\r\n" +
                                "<select id=\"resource\" name=\"resource\" onchange=\"updateSubTypeOptions()\">" +
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

                html += "</select>\r\n";
                html += "</div><div class='form-group'>";
                html += "<label for='subType'>Select Resource Sub Types : </label>";
                html += "<select id=\"sub1A\" name=\"subType\" multiple>";
                if (resource == null)
                        html += "<option value='' selected>Select a Resource type</option>";
                if (resource != null && resource.equals("LGARecyclingStatistics")) {
                        html += " <option value='Kerbside'>Kerbside Recycling</option>\r\n" +
                                        "<option value='CDS'>CDS Recycling</option>\r\n" +
                                        " <option value='DropOff'>Drop Off Recycling</option>\r\n" +
                                        " <option value='CleanUp'>Cleanup Recycling</option>\r\n";
                }
                if (resource != null && resource.equals("LGAOrganicStatistics")) {
                        html += " <option value='Kerbside'>Kerbside Organics Bin</option>\r\n" +
                                        " <option value='KerbsideFOGO'>Kerbside FOGO Organics</option>\r\n" +
                                        " <option value='DropOff'>Drop Off Organics</option>\r\n" +
                                        " <option value='CleanUp'>Cleanup Organics</option>\r\n" +
                                        " <option value='OtherCouncil'>Other Council Garden Organics</option>\r\n";
                }
                if (resource != null && resource.equals("LGAWasteStatistics")) {
                        html += " <option value='Kerbside'>Kerbside Waste Bin</option>\r\n" +
                                        " <option value='DropOff'>Drop Off</option>\r\n" +
                                        " <option value='CleanUp'>Cleanup</option>\r\n";
                }
                html += "</select>\r\n" +
                                "</div>" +
                                "<div class='form-group'>";

                html += "<div>" +
                                "<label for='sort1A'>Sort Results<label>\r\n" +
                                "<select id=\"sort1A\" name=\"sort\">" +
                                "  <option value='LGA'";
                if (sort != null && sort.equals("LGA")) {
                        html += " selected";
                }
                html += ">LGA Name</option>\r\n" +
                                "  <option value='Population'";
                if (sort != null && sort.equals("Population")) {
                        html += " selected";
                }
                html += ">Population</option>\r\n" +
                                "  <option value='Houses'";
                if (sort != null && sort.equals("Houses")) {
                        html += " selected";
                }
                html += ">Houses Surveyed</option>\r\n" +
                                "  <option value='TotalCollected'";
                if (sort != null && sort.equals("TotalCollected")) {
                        html += " selected";
                }
                html += ">Collected</option>\r\n" +
                                "  <option value='TotalRecycled'";
                if (sort != null && sort.equals("TotalRecycled")) {
                        html += " selected";
                }
                html += ">Recycled</option>\r\n" +
                                "  <option value='AvgRecycled'";
                if (sort != null && sort.equals("AvgRecycled")) {
                        html += " selected";
                }
                html += ">Percentage Recycled</option>\r\n" +
                                "  <option value='AvgWastePerHousehold'";
                if (sort != null && sort.equals("AvgWastePerHousehold")) {
                        html += " selected";
                }
                html += ">Waste Per Household</option>\r\n" +
                                "</select>" +
                                "</div>";

                html += "<div>" +
                                "<select id=\"sortBy1A\" name=\"sortBy\">" +
                                "  <option value='ASC'";
                if (sortBy != null && sort.equals("ASC")) {
                        html += " selected";
                }
                html += ">Ascending</option>\r\n" +
                                "  <option value='DESC'";
                if (sortBy != null && sort.equals("DESC")) {
                        html += " selected";
                }
                html += ">Descending</option>\r\n" +
                                "</select>" +
                                "</div>" +
                                "</div>" +
                                "<div class='form-group'>";

                html += "<button type='submit' class='btn btn-primary'>Get Results</button> </div>" +
                                "</form></div>";
                html += "<h1>Results</h1>";
                ArrayList<result2A> resultArray = new ArrayList<>();
                if (LGAname != null && resource != null && sort != null && sortBy != null && subType != null) {
                        html += "<p class='show-result'>Showing results for selected LGA's with resource type ";
                        if (resource.equals("LGARecyclingStatistics")) {
                                html += "Recyclable";
                        } else if (resource.equals("LGAOrganicStatistics")) {
                                html += "Organic";
                        }
                        if (resource.equals("LGAWasteStatistics")) {
                                html += "Waste";
                        }
                        html += " sorted by  " + sort + " in " + (sortBy.equals("ASC") ? "ascending" : "descending")
                                        + " </p><br></br>";
                        html += "                <div class='hor-center'>\r\n" + //
                                        "                <div class='table-div whole-table'>\r\n" + //
                                        "                    <table>\r\n" + //
                                        "                        <tr class='tr-header'>\r\n" + //
                                        "                            <th>\r\n" + //
                                        "                                LGA Name\r\n" + //
                                        "                            </th>\r\n" + //
                                        "                            <th>\r\n" + //
                                        "                                Population\r\n" + //
                                        "                            </th>\r\n" + //
                                        "                            <th>\r\n" + //
                                        "                                Houses Surveyed\r\n" + //
                                        "                            </th>\r\n" + //
                                        "                            <th>\r\n" + //
                                        "                                Total Waste Collected\r\n" + //
                                        "                            </th>\r\n" + //
                                        "                            <th>\r\n" + //
                                        "                                Total Waste Recycled\r\n" + //
                                        "                            </th>\r\n" + //
                                        "                            <th>\r\n" + //
                                        "                                Avg. Percentage recycled\r\n" + //
                                        "                            </th>\r\n" + //
                                        "                            <th>Avg. Waste Per Household</th>\r\n" +
                                        "                        </tr>\r\n";
                        for (result2A result : jdbc.get2A(LGAname, resource, subType, sort, sortBy)) {
                                resultArray.add(result);
                        }
                }
                if ((LGAname == null || resource == null || sort == null || sortBy == null || subType == null)) {
                        html += """
                                        <div class='hor-center'>
                                                <p class='no-data-text'>No data to show :(</p>
                                        </div>
                                        """;
                }

                for (result2A result : resultArray) {
                        html += "<tr class='tr-data'>" +
                                        "<td>" + result.LGAname + "</td>" +
                                        "<td>" + result.population + "</td>" +
                                        "<td>" + result.house + "</td>" +
                                        "<td>" + result.waste + "</td>" +
                                        "<td>" + result.recycled + "</td>" +
                                        "<td>" + result.percRecycled + "</td>" +
                                        "<td>" + result.avgWaste + "</td>" +
                                        "</tr>";
                }

                html += "</table></div></div>\r\n";
                for (result2A result : resultArray) {
                        html += "<div class='whole-card'>" +
                                        "<div class='hor-center'>" +
                                        "<div class='phone-card'>" +
                                        "<p class='lg-text-2'> LGA Name : " + result.LGAname + "</p>" +
                                        "<p> Population : " + result.population + "</p>" +
                                        "<p> House Surveyed : " + result.house + "</p>" +
                                        "<p> Total Waste Collected : " + result.waste + "</p>" +
                                        "<p> Total Waste Recycled : " + result.recycled + "</p>" +
                                        "<p> Average Recycled Percentage : " + result.percRecycled + " % </p>"
                                        +
                                        "<p> Average Waste Per Household : " + result.avgWaste + "</p>"
                                        +
                                        "</div>" +
                                        "</div>" +
                                        "</div></div>";
                }

                html += "        </main>\r\n" +
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
                                "        </script>\r\n"; //
                html += """
                                <script src="https://cdn.jsdelivr.net/gh/habibmhamadi/multi-select-tag@3.1.0/dist/js/multi-select-tag.js"></script>
                                <script>
                                    new MultiSelectTag('LGA1A', {
                                    rounded: true,    // default true
                                    shadow: true,      // default false
                                    placeholder: 'Search',  // default Search...
                                    tagColor: {
                                        textColor: '#327b2c',
                                        borderColor: '#92e681',
                                        bgColor: '#f5faf3',
                                    },
                                    onChange: function(values) {
                                        console.log(values)
                                    }
                                })
                                </script>
                                <script>
                                    function updateSubTypeOptions() {
                                        const resource = document.getElementById('resource').value;
                                        const subTypeSelect = document.getElementById('sub1A');
                                        subTypeSelect.innerHTML = ''; // Clear existing options

                                        const subTypeOptions = {
                                            "LGARecyclingStatistics": [
                                                { value: "Kerbside", text: "Kerbside Recycling" },
                                                { value: "CDS", text: "CDS Recycling" },
                                                { value: "DropOff", text: "Drop Off Recycling" },
                                                { value: "CleanUp", text: "Cleanup Recycling" }
                                            ],
                                            "LGAOrganicStatistics": [
                                                { value: "Kerbside", text: "Kerbside Organics Bin" },
                                                { value: "KerbsideFOGO", text: "Kerbside FOGO Organics" },
                                                { value: "DropOff", text: "Drop Off Organics" },
                                                { value: "CleanUp", text: "Cleanup Organics" },
                                                { value: "OtherCouncil", text: "Other Council Garden Organics" }
                                            ],
                                            "LGAWasteStatistics": [
                                                { value: "Kerbside", text: "Kerbside Waste Bin" },
                                                { value: "DropOff", text: "Drop Off" },
                                                { value: "CleanUp", text: "Cleanup" }
                                            ]
                                        };

                                        // Add new options based on selected resource
                                        if (subTypeOptions[resource]) {
                                            subTypeOptions[resource].forEach(option => {
                                                const opt = document.createElement('option');
                                                opt.value = option.value;
                                                opt.text = option.text;
                                                subTypeSelect.appendChild(opt);
                                            });
                                        }
                                    }
                                </script>

                                """;
                html += "    <footer>\r\n" + //
                                "        <a href=\"mission.html\">Contact | FAQ | Other Relevant Information</a>\r\n" + //
                                "    </footer>\r\n" + //
                                "    </body>\r\n" + //
                                "</html>";

                // DO NOT MODIFY THIS
                // Makes Javalin render the webpage
                System.out.println("We did it boys. Time to go home.");
                context.html(html);
        }

}
