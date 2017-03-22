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

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
//import utils.db.*;

/**
 *
 * @author  YoungR
 */
public class ParameterService {

    private Connection conn;
    private PreparedStatement psParam;

    public ParameterService() throws IOException, ClassNotFoundException, Exception {

        DatabaseAccess db = new DatabaseAccess();
        conn = db.getConn();

        try {
            psParam = conn.prepareStatement("select  PARAMETER_VALUE  from PARAMSERVICE where PARAMETER = ? ");
        } catch (SQLException e) {
            System.err.println(e.toString());
        }

    }

    public String getParameterValue(String key) {
        String result = null;

        try {
            psParam.setString(1, key);
            ResultSet rs = psParam.executeQuery();

            if (!rs.next()) {
                System.err.println("Error: Value not found for " + key);
                return null;
            }
            result = rs.getString("PARAMETER_VALUE");
        } catch (SQLException e) {
            System.err.println(e.toString());
            return null;
        }
        return result;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ParameterService ps = null;
        try {

            ps = new ParameterService();

        } catch (IOException ex) {
            Logger.getLogger(ParameterService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ParameterService.class.getName()).log(Level.SEVERE, null, ex);
        }catch (Exception ex) {
            Logger.getLogger(ParameterService.class.getName()).log(Level.SEVERE, null, ex);
        }
        String str = ps.getParameterValue("col1:thing");

        System.out.println(str);

    }
}
