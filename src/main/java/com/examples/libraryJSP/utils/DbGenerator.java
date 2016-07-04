package com.examples.libraryJSP.utils;

//import org.springframework.core.io.ClassPathResource;
//import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import com.examples.libraryJSP.model.DataManager;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Created by Аяз on 03.07.2016.
 */
public class DbGenerator {
    // TODO: generate DB and data from resource sql
    //private static DataManager dataManager;
    private static final String[] SQL_SCRIPTS = {"/mysql-scripts/library_create.sql"
            , "/mysql-scripts/library_populate.sql"};
    private static final String[][] AVATARS =
            {{"1", "/images/users/descartes.jpg"}
                    , {"2", "/images/users/sestritza_alenushka.jpg"}
                    , {"3", "/images/users/sterh.jpg"}
                    , {"4", "/images/users/valen.jpg"}
                    , {"5", "/images/users/homer_simpsonwoohooo.gif"}
                    , {"6", "/images/users/mr_zlo.jpg"}
            };
    private static final String[][] COVERS =
            {{"978-5-9221-0530-9", "/images/books/1.landau_kv_meh.jpg"}
                    , {"978-5-496-00740-5", "/images/books/2.lafore_algoritmy.jpg"}
                    , {"978-5-8459-1918-2", "/images/books/3.shildt_java8.jpg"}
                    , {"978-5-91657-583-5", "/images/books/4.kollinz_good_to_great.jpg"}
                    , {"978-5-9614-5347-8", "/images/books/5.minczberg_strateg_safari.jpg"}
                    , {"978-5-91671-509-5", "/images/books/6.ibuka_posle3.jpg"}
                    , {"978-5-9614-5515-1", "/images/books/7.kovi_7navykov.jpg"}
                    , {"000-0-0000-0000-1", "/images/books/8.feinman_lekcii.jpg"}
                    , {"978-5-86793-857-4", "/images/books/9.nlo.jpg"}
                    , {"978-5-17-080115-2", "/images/books/10.oruell_1984.jpg"}
                    , {"978-5-17-089655-4", "/images/books/11.gibel_imperii.jpg"}
            };

    public static void main(String[] args) {

        DataManager dataManager = new DataManager();

        try {  // load the database JDBC driver
            Class.forName(dataManager.getDbDriver());
        } catch (ClassNotFoundException e) {
            System.out.println(e.toString());
        }

        Connection connection = dataManager.getConnection();
        if (connection != null) {

            try (InputStream is0 = DbGenerator.class.getResourceAsStream(SQL_SCRIPTS[0])) {
                importSQL(connection, is0);
                try (InputStream is1 = DbGenerator.class.getResourceAsStream(SQL_SCRIPTS[1])) {
                    importSQL(connection, is1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (String avatar[] : AVATARS) {
                try (InputStream avatarSource = DbGenerator.class.getResourceAsStream(avatar[1])) {
                    dataManager.setAvatarByUserId(Integer.parseInt(avatar[0] == null ? "" : avatar[0]), avatarSource);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for (String cover[] : COVERS) {
                try (InputStream coverSource = DbGenerator.class.getResourceAsStream(cover[1])) {
                    dataManager.setCoverByBookId(cover[0], coverSource);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            dataManager.putConnection(connection);
        }


    }

    public static void importSQL(Connection conn, InputStream in) throws SQLException {
        Scanner s = new Scanner(in);
        s.useDelimiter("(;(\r)?\n)|(--\n)");
        Statement st = null;
        try {
            st = conn.createStatement();
            while (s.hasNext()) {
                String line = s.next();
                if (line.startsWith("/*!") && line.endsWith("*/")) {
                    int i = line.indexOf(' ');
                    line = line.substring(i + 1, line.length() - " */".length());
                }

                if (line.trim().length() > 0) {
                    st.execute(line);
                }
            }
        } finally {
            if (st != null) st.close();
        }
    }


//        DataSource dataSource = // getYourMySQLDriverBackedDataSource();
//
//        ResourceDatabasePopulator rdp = new ResourceDatabasePopulator();
//        rdp.addScript(new ClassPathResource(
//                "mysql-scripts/library_create.sql"));
//        rdp.addScript(new ClassPathResource(
//                "mysql-scripts/library_populate.sql"));
//
//        try {
//            Connection connection = dataSource.getConnection();
//            rdp.populate(connection); // this starts the script execution, in the order as added
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }


//    public static void init() {
//        dataManager = new DataManager();
//        // dataManager.setDbURL(config.getInitParameter("dbURL")); // use DbURL from dataManager
//        dataManager.setDbUserName(config.getInitParameter("dbUserName"));
//        dataManager.setDbPassword(config.getInitParameter("dbPassword"));
//
//        ServletContext context = config.getServletContext();
//        context.setAttribute("base", config.getInitParameter("viewBase"));
//        context.setAttribute("imageURL", config.getInitParameter("imageURL"));
//        context.setAttribute("dataManager", dataManager);
//        context.setAttribute("httpMethod", httpMethod);
//
//        try {  // load the database JDBC driver
//            Class.forName(config.getInitParameter("jdbcDriver"));
//        } catch (ClassNotFoundException e) {
//            System.out.println(e.toString());
//        }
//    }
}
