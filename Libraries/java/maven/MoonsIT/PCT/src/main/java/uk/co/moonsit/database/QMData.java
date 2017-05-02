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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moonsit.learning.rate.BaseLearningRate;
import uk.co.moonsit.learning.reorganisation.BaseReorganisation;
import uk.co.moonsit.utils.timing.DateAndTime;
//import utils.db.*;

/**
 *
 * @author YoungR
 */
public class QMData {

    private final Connection conn;
    private PreparedStatement psParam;
    private DatabaseAccess db;

    public QMData() throws IOException, ClassNotFoundException, Exception {

        db = new DatabaseAccess();
        conn = db.getConn();

    }

    public QMData(String driver, String url, String uname, String pword) throws IOException, ClassNotFoundException, Exception {

        db = new DatabaseAccess(driver, url, uname, pword);
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

    public String getScores(int level, String model, int top) throws SQLException {
        StringBuilder sb = new StringBuilder();
        psParam = conn.prepareStatement("SELECT ID, Level, Score, TimeScore, Fidelity, SimulatedTime, ConstraintKey, Model "
                + "FROM QUANTUM.SCORES "
                + "where level = ?   and model = ? "
                + "order by level, score desc "
                + "FETCH FIRST ? ROWS ONLY");

        psParam.setInt(1, level);
        psParam.setString(2, model);
        psParam.setInt(3, top);
        ResultSet rs = psParam.executeQuery();
        while (rs.next()) {

            String id = rs.getString("id");
            String score = rs.getString("score");
            String timescore = rs.getString("timescore");
            String fidelity = rs.getString("fidelity");
            float simulatedTime = rs.getFloat("SimulatedTime");

            sb.append(String.format("%21s %6s %6s %6s %5.3f \n", id, score, timescore, fidelity, simulatedTime));

        }

        return sb.toString();

    }

    public String getMaxScore() throws SQLException {
        StringBuilder sb = new StringBuilder();
        psParam = conn.prepareStatement("SELECT ID, Level, Score, TimeScore, Fidelity, SimulatedTime, ConstraintKey, Model "
                + "FROM QUANTUM.SCORES "
                + "order by level, score desc ");

        ResultSet rs = psParam.executeQuery();
        if (rs.next()) {
            String id = rs.getString("id");
            String score = rs.getString("score");
            String timescore = rs.getString("timescore");
            String fidelity = rs.getString("fidelity");
            float simulatedTime = rs.getFloat("SimulatedTime");

            sb.append(String.format("%21s %6s %6s %6s %5.3f \n", id, score, timescore, fidelity, simulatedTime));
        }

        return sb.toString();
    }

    public String getModelMaxScore(String model) throws SQLException {
        StringBuilder sb = new StringBuilder();
        psParam = conn.prepareStatement("SELECT ID, Level, Score, TimeScore, Fidelity, SimulatedTime, ConstraintKey, Model "
                + "FROM QUANTUM.SCORES "
                + " where model = ? "
                + "order by level, score desc ");

        psParam.setString(1, model);
        ResultSet rs = psParam.executeQuery();
        if (rs.next()) {
            String id = rs.getString("id");
            String score = rs.getString("score");
            String timescore = rs.getString("timescore");
            String fidelity = rs.getString("fidelity");
            float simulatedTime = rs.getFloat("SimulatedTime");

            sb.append(String.format("%21s %6s %6s %6s %5.3f \n", id, score, timescore, fidelity, simulatedTime));
        }

        return sb.toString();
    }

    public String getTodaysScores(int top) throws SQLException {
        StringBuilder sb = new StringBuilder();

        DateAndTime dt = new DateAndTime();
        String date = dt.YMD();
        psParam = conn.prepareStatement("SELECT ID, Level, Score, TimeScore, Fidelity, SimulatedTime, ConstraintKey, Model "
                + "FROM QUANTUM.SCORES "
                + "where id > ? "
                + "order by level, score desc "
                + "FETCH FIRST ? ROWS ONLY");

        psParam.setString(1, date);
        psParam.setInt(2, top);
        ResultSet rs = psParam.executeQuery();
        while (rs.next()) {

            String id = rs.getString("id");
            String score = rs.getString("score");
            String timescore = rs.getString("timescore");
            String fidelity = rs.getString("fidelity");
            float simulatedTime = rs.getFloat("SimulatedTime");

            sb.append(String.format("%21s %6s %6s %6s %5.3f \n", id, score, timescore, fidelity, simulatedTime));

        }

        return sb.toString();

    }

    public File getParamtersFile(String dir, String model, String id) {
        return new File(dir + File.separator + model + "-" + id + ".pars");
    }

    public File getParamtersLatestFile(String dir, String model) {
        return new File(dir + File.separator + model + "-Latest.pars");
    }

    public String getParameters(String id) throws SQLException {
        List<String> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        psParam = conn.prepareStatement("SELECT * FROM QUANTUM.PARAMETERS where ID = ? ");

        psParam.setString(1, id);
        ResultSet rs = psParam.executeQuery();

        String[] rateParameters = new String[2];
        rateParameters[0] = null;
        rateParameters[1] = null;

        while (rs.next()) {
            String functionname = rs.getString("functionname");
            String parameter = rs.getString("parameter");
            Double value = rs.getDouble("value");
            if (parameter.equals("Value")) {
                continue;
            }
            String svalue;
            if (parameter.contains("LearningType")) {
                svalue = BaseReorganisation.getLearningType(value.intValue());
            } else if (parameter.contains("LearningRateType")) {
                svalue = BaseLearningRate.getLearningRateType(value.intValue());
            } else {
                svalue = String.valueOf(value);
            }

            int index = BaseLearningRate.getLearningRateParameterIndex(parameter);
            if (index >= 0) {
                rateParameters[index] = svalue;
                if (rateParameters[0] != null && rateParameters[1] != null) {
                    parameter = "RateParameters";
                    svalue = rateParameters[0] + "^" + rateParameters[1];
                    rateParameters[0] = null;
                    rateParameters[1] = null;
                }
            }

            if (parameter.equals("RateParameters") || index < 0) {
                list.add(functionname + "_" + parameter + "=" + svalue + "\n");
            }
        }

        Collections.sort(list);

        for (String s : list) {
            sb.append(s);
        }

        return sb.toString();
    }

    public void save(File file, String s) throws IOException {
        try (FileOutputStream fout = new FileOutputStream(file)) {
            fout.write(s.getBytes());
        }
    }

    public void saveParameters(String dir, String id) throws IOException, SQLException, Exception {
        String parameters = getParameters(id);
        //System.out.println(parameters);

        String model = getModel(id);
        if (model == null) {
            throw new Exception("No model found for ID " + id);
        }

        File file = getParamtersFile(dir, model, id);
        save(file, parameters);
        System.out.println(file);

        file = getParamtersLatestFile(dir, model);
        save(file, parameters);
        System.out.println(file);

    }

    public String getLearningType(String s) {

        for (int i = 1; i < 4; i++) {
            String type = BaseReorganisation.getLearningType(i);
            if (s.contains(type)) {
                return type;
            }
        }

        return null;
    }

    public String getLearningRateType(String s) {

        for (int i = 1; i < 5; i++) {
            String type = BaseLearningRate.getLearningRateType(i);
            if (s.contains(type)) {
                return type;
            }
        }

        return null;
    }

    public void saveParametersMulti(String dir, String id, String model) throws IOException, SQLException, Exception {
        String parameters = getParameters(id);
        //System.out.println(parameters);

        String learningType = getLearningType(parameters);
        String learningRateType = getLearningRateType(parameters);
        String rateParameters = BaseLearningRate.getRateParameters(learningRateType);

        for (int i = 1; i < 4; i++) {
            String ltype = BaseReorganisation.getLearningType(i);
            parameters = parameters.replaceAll(learningType, ltype);
            for (int j = 0; j < 4; j++) {
                String lrtype = BaseLearningRate.getLearningRateType(j);
                String rpars = BaseLearningRate.getRateParameters(lrtype);
                parameters = parameters.replaceAll(learningRateType, lrtype);
                parameters = parameters.replaceAll(rateParameters, rpars);

                File file = getParamtersFile(dir, model, ltype + "-" + lrtype);
                save(file, parameters);
                System.out.println(file);

            }

        }

    }

    public void close() throws SQLException, Exception {
        if (psParam != null) {
            psParam.close();
        }
        if (db != null) {
            db.close();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String dir = "C:\\Versioning\\PCTMonitor\\Controllers\\Models\\QuantumMoves\\files\\parameters";

        String driver = "org.apache.derby.jdbc.ClientDriver";
        String user = "quantum";
        String password = "moves";
        String url = "jdbc:derby://localhost/QuantumMoves";
        String id = null;

        int type = 0;

        String model = null;
        Integer level = null;
        Integer top = null;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-id")) {
                id = args[++i];
            }
            if (args[i].equals("-model")) {
                model = args[++i];
            }
            if (args[i].equals("-level")) {
                level = Integer.parseInt(args[++i]);
            }
            if (args[i].equals("-top")) {
                top = Integer.parseInt(args[++i]);
            }
            if (args[i].equals("-today")) {
                type = 2;
            }
            if (args[i].equals("-max")) {
                type = 3;
            }
        }

        if (model != null && level != null) {
            type = 1;
        }

        if (model != null && type == 3) {
            type = 4;
        }

        if (id != null) {
            type = 0;
        }

        System.out.println("Running: QMData " + sb.toString());
        System.out.println("Type " + type);

        QMData ps = null;
        try {
            ps = new QMData();
            //ps = new QMData(driver, url, user, password);

            switch (type) {

                case 0:
                    //String id = "20170422-18-35-16.827";
                    //String id = "20170427-14-51-05.382";
                    //String id = "20170427-19-38-56.295";
                    //String id = "20170424-01-00-17.025"; // embedded                    
                    //String id = "20170428-16-13-17.756";
                    //String id = "20170429-20-56-34.827";
                    ps.saveParameters(dir, id);
                    break;
                case 1: {
                    String scores = ps.getScores(level, model, top);
                    System.out.println(scores);
                    break;
                }

                case 2: {
                    String scores = ps.getTodaysScores(top);
                    System.out.println(scores);
                    break;
                }

                case 3: {
                    String scores = ps.getMaxScore();
                    System.out.println(scores);
                    String id1 = scores.split(" ")[0];
                    ps.saveParameters(dir, id1);

                    break;
                }

                case 4: {
                    String scores = ps.getModelMaxScore(model);
                    System.out.println(scores);
                    String id1 = scores.split(" ")[0];

                    ps.saveParametersMulti(dir, id1, model);

                    break;
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(QMData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(QMData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(QMData.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(QMData.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(QMData.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }
}
