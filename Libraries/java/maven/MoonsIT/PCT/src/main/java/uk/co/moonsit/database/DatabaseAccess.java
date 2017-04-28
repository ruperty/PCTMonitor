/* 
  *  This software is the property of Moon's Information Technology Ltd.
  * 
  *  All rights reserved.
  * 
  *  The software is only to be used for development and research purposes.
  *  Commercial use is only permitted under license or agreement.
  * 
  *  Copyright (C)  Moon's Information Technology Ltd.
  *  
  *  Author: rupert@moonsit.co.uk
  * 
  * 
 */
package uk.co.moonsit.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.derby.drda.NetworkServerControl;

public final class DatabaseAccess {

    private static final Logger LOG = Logger.getLogger(DatabaseAccess.class.getName());
    private Connection conn;
    private String driver;
    private String url;
    private String uname;
    private String pword;
    private Statement stmt;
    private String errorMsg;
    boolean startedServer = false;

    public DatabaseAccess() throws IOException, ClassNotFoundException, Exception {
        PropertyResourceBundle prb;
        //try {
        //NetworkServerControl server = new NetworkServerControl();
        //server.start(null);
        //server.shutdown();

        String frb = "../properties/db.properties";
        File propFile = new File(frb);
        if (propFile.exists()) {
            LOG.info("+++ Reading from properties file");
            PropertyResourceBundle rb = new PropertyResourceBundle(new FileInputStream(frb));
            driver = rb.getString("driver");
            url = rb.getString("url");
            uname = rb.getString("user");
            pword = rb.getString("password");

        } else {
            prb = (PropertyResourceBundle) ResourceBundle.getBundle("utils.db.ReportProperties");
            driver = prb.getString("driver");
            url = prb.getString("url");
            uname = prb.getString("user");
            pword = prb.getString("password");
        }
        /* } catch (MissingResourceException e) {
        System.err.println(e.toString());
        }*/

        //while (!connect()){}
        if (!connect()) {
            LOG.info("Database connection failed, trying to start network server");
            NetworkServerControl server = new NetworkServerControl();
            server.start(null);
            LOG.info("Database server started");
            startedServer = true;
            /*Properties props = server.getCurrentProperties();
            for (Object key : props.keySet()) {
                LOG.info(key + " " + props.get(key));
            }*/
            if (!connect()) {
                throw new Exception("Database connection failed");
            }
        }
    }

    public DatabaseAccess(String driver, String url, String uname, String pword) throws IOException, ClassNotFoundException, Exception {
        this.driver = driver;
        this.url = url;
        this.uname = uname;
        this.pword = pword;

        //while (!connect()){}
        if (!connect()) {
            throw new Exception("Database connection failed");
        }
    }

    public void executeQuery(String sql) {// throws java.sql.SQLException {

        try {
            stmt = conn.createStatement();
            stmt.executeQuery(sql);
        } catch (java.sql.SQLException e) {
            errorMsg = e.toString();
            LOG.severe("error " + e.toString());
        } finally {
            closeStatement();
        }

    }

    public ResultSet getResultSet(String sql) {// throws java.sql.SQLException {
        ResultSet rs = null;

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            //System.out.println("Select succeeded");
        } catch (java.sql.SQLException e) {
            errorMsg = e.toString();
            LOG.severe("error " + e.toString());
        }

        return rs;
    }

    public void closeStatement() {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (java.sql.SQLException e) {
            errorMsg = e.toString();
            System.out.println("error " + errorMsg);
        }
    }

    public int executeUpdate(String sql) throws java.sql.SQLException {
        int rtn;

//	try{
        stmt = conn.createStatement();
        rtn = stmt.executeUpdate(sql);

        /*	}catch( java.sql.SQLException e){
        errorMsg = e.toString();
        System.out.print("error " + errorMsg);
        rtn = e.getErrorCode();
        System.out.println("error " + rtn);       
        }
         */
        return rtn;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void close() throws Exception {
        try {
            if (conn != null) {
                conn.close();
                if (stmt != null) {
                    stmt.close();
                }
            }
        } catch (java.sql.SQLException e) {
            System.out.println("error " + e.toString());
        }
        if (startedServer) {
            NetworkServerControl server = new NetworkServerControl();
            server.shutdown();
            LOG.info("Database server shutdown");
        }
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String d) {
        driver = d;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String u) {
        url = u;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String u) {
        uname = u;
    }

    public String getPword() {
        return pword;
    }

    public void setPword(String p) {
        pword = p;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection c) {
        conn = c;
    }

    public boolean connect() throws ClassNotFoundException {
        boolean rtn = true;

        if (conn == null) {
            try {
                Class.forName(driver);
                conn = DriverManager.getConnection(url, uname, pword);
                LOG.info("+++ Database Connection succeeded");
            } catch (java.sql.SQLException e) {
                LOG.log(Level.INFO, "+++ Database Connection failed {0}", e.toString());
                rtn = false;
            } catch (java.lang.ClassNotFoundException e) {
                LOG.log(Level.INFO, "Connection failed {0}", e.toString());
                throw e;
            }
        }
        //createPS();
        return rtn;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            DatabaseAccess db = new DatabaseAccess();

            //String query = "SELECT id INTO OUTFILE 'c:/tmp/exportdata/1.csv' FIELDS TERMINATED BY ',' LINES TERMINATED BY '\\n' FROM QUANTUM.PARAMETERS p";            
            //db.executeQuery(query);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*
    public DbConnect(String d, String u, String n, String p) {
        driver = d;
        url = u;
        uname = n;
        pword = p;
        connect();
    }
     */
}
