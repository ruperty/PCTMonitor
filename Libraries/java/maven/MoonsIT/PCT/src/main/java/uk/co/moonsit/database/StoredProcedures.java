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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Rupert Young
 */
public class StoredProcedures {

    public static void removeScores(String id) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:default:connection");
        PreparedStatement pstmt ;

        String query = "DELETE FROM PARAMETERS  WHERE id=?";

        pstmt = con.prepareStatement(query);
        pstmt.setString(1, id);
        pstmt.executeQuery();

        query = "DELETE FROM Scores  WHERE id=?";

        pstmt = con.prepareStatement(query);
        pstmt.setString(1, id);
        pstmt.executeQuery();

    }
}
