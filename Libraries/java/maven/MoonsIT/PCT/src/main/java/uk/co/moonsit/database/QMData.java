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
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moonsit.learning.rate.BaseLearningRate;
import uk.co.moonsit.learning.reorganisation.BaseReorganisation;
//import utils.db.*;

/**
 *
 * @author YoungR
 */
public class QMData {

    private final Connection conn;
    private PreparedStatement psParam;

    public QMData() throws IOException, ClassNotFoundException, Exception {

        DatabaseAccess db = new DatabaseAccess();
        conn = db.getConn();

    }

    public String getModel(String id) throws SQLException {
        String model = null;
        psParam = conn.prepareStatement("SELECT	 Model FROM QUANTUM.SCORES where ID = ? ");

        psParam.setString(1, id);
        ResultSet rs = psParam.executeQuery();
        if (rs.next()) {
            model = rs.getString("model");
        }

        return model;
    }

    public File getParamtersFile(String dir, String model, String id) {
        return new File(dir + File.separator + model + "-" + id + ".pars");
    }

    public String getParameters(String id) throws SQLException {
        StringBuilder sb = new StringBuilder();
        psParam = conn.prepareStatement("SELECT * FROM QUANTUM.PARAMETERS where ID = ? ");

        psParam.setString(1, id);
        ResultSet rs = psParam.executeQuery();

        while (rs.next()) {
            String functionname = rs.getString("functionname");
            String parameter = rs.getString("parameter");

            Double value = rs.getDouble("value");
            String svalue;
            if (parameter.contains("LearningType")) {
                svalue = BaseReorganisation.getLearningType(value.intValue());
            } else if (parameter.contains("LearningRateType")) {
                svalue = BaseLearningRate.getLearningRateType(value.intValue());
            } else {
                svalue = String.valueOf(value);
            }
            sb.append(functionname).append("_").append(parameter).append("=").append(svalue).append("\n");
        }

        return sb.toString();
    }

    public void save(File file, String s) throws IOException {
        try (FileOutputStream fout = new FileOutputStream(file)) {
            fout.write(s.getBytes());
        }
    }

    public void saveParameters(String dir, String id) throws IOException, SQLException {
        String parameters = getParameters(id);
        System.out.println(parameters);

        String model = getModel(id);

        File file = getParamtersFile(dir, model, id);

        save(file, parameters);
        System.out.println(file);
    }

    public void close() throws SQLException {
        if (psParam != null) {
            psParam.close();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String dir = "C:\\Versioning\\PCTMonitor\\Controllers\\Models\\QuantumMoves\\files\\parameters";
        int type = 0;
        QMData ps = null;
        try {
            ps = new QMData();

            switch (type) {

                case 0:
                    String id = "20170422-18-35-16.827";
                    ps.saveParameters(dir, id);
                    break;

            }
        } catch (IOException ex) {
            Logger.getLogger(QMData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(QMData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(QMData.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
