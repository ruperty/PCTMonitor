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
package uk.co.moons.control.neural.nodes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import uk.co.moons.control.neural.NeuralFunction;
import uk.co.moons.control.functions.BaseControlFunction;
import java.util.List;
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moonsit.database.DatabaseAccess;
import uk.co.moonsit.utils.MoonsString;

public class RecordQMNode extends NeuralFunction {

    private static final Logger LOG = Logger.getLogger(RecordQMNode.class.getName());

    public Double threshold = null;
    public String key = null;

    private DatabaseAccess db;
    private int dataIndex;
    private int resetIndex;
    private String targetx;
    private String targety;
    private List<String> parametersSql;
    private String constraintKey;
    private String level;
    private String oldLevel;
    private String id;
    private final boolean debug = false;

    public RecordQMNode() {
        super();
    }

    public RecordQMNode(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Threshold")) {
                threshold = Double.parseDouble(param.getValue());
            }
            if (pname.equals("Key")) {
                key = param.getValue();
            }
        }

        if (key == null) {
            throw new Exception("Key null for RecordQMNode");
        }
        if (threshold == null) {
            throw new Exception("Threshold null for RecordQMNode");
        }
    }

    @Override
    public void verifyConfiguration() throws Exception {

        List<BaseControlFunction> controls = links.getControlList();
        for (int i = 0; i < controls.size(); i++) {
            String lname = links.getType(i);
            if (lname.equalsIgnoreCase("keydata")) {
                dataIndex = i;
                continue;
            }
            if (lname.equalsIgnoreCase("reset")) {
                resetIndex = i;
            }
        }

    }

    @Override
    public void init() throws Exception {

        db = new DatabaseAccess();

        parametersSql = new ArrayList<>();

        List<BaseControlFunction> controls = links.getControlList();

        setValuesAtStartofGame(controls);
        oldLevel = level;
    }

    private void setValuesAtStartofGame(List<BaseControlFunction> controls) throws SQLException {
        id = getID(controls);
        getTargets(controls);
        constructParametersSql(controls);
        level = getLevel(controls);
        constraintKey = getConstraintKey(controls);
    }

    @Override
    public double compute() throws Exception {
        List<BaseControlFunction> controls = links.getControlList();

        double resetVal = controls.get(resetIndex).getValue();
        if (resetVal == 1) {
            if (isValidRecord(controls.get(dataIndex))) {
                recordData(controls);
                recordParameters(id);
            }
            setValuesAtStartofGame(controls);
        }
        return output;
    }

    private void constructParametersSql(List<BaseControlFunction> controls) {
        parametersSql.clear();
        for (int i = 0; i < controls.size(); i++) {
            String lname = controls.get(i).getName();
            String ltype = links.getType(i);
            if (ltype.toLowerCase().startsWith("parameters")) {
                //LOG.info("Parameters from " + lname + " recording");
                String data = controls.get(i).getNeural().getParametersString();
                String[] arr = data.split("_");
                for (String arr1 : arr) {

                    String[] vals = arr1.split(":");
                    StringBuilder sql = new StringBuilder();
                    sql.append("insert into parameters(id,functionname,parameter,value)values(");
                    sql.append("'").append("<ID>").append("','").append(lname).append("','")
                            .append(vals[0]).append("',").append(MoonsString.formatStringPlaces(vals[1], 6)).append(")");
                    //LOG.info(sql.toString());
                    parametersSql.add(sql.toString());
                }
            }
        }
    }

    private void recordParameters(String id) throws SQLException {
        for (String sql : parametersSql) {
            String actualSql = sql.replace("<ID>", id);
            //LOG.info(actualSql);
            db.executeUpdate(actualSql);
        }
    }

    /*
    private void recordParameters(List<BaseControlFunction> controls, String id) throws SQLException {

        for (int i = 0; i < controls.size(); i++) {
            String lname = controls.get(i).getName();
            String ltype = links.getType(i);
            if (ltype.toLowerCase().startsWith("parameters")) {
                String data = controls.get(i).getNeural().getParametersString();
                String[] arr = data.split(",");
                for (String arr1 : arr) {
                    String[] vals = arr1.split(":");
                    StringBuilder sql = new StringBuilder();
                    sql.append("insert into parameters(id,functionname,parameter,value)values(");
                    sql.append("'").append(id).append("','").append(lname).append("','").append(vals[0]).append("',").append(vals[1]).append(")");
                    LOG.info(sql.toString());
                    db.executeUpdate(sql.toString());
                }

            }
        }
    }*/
    private void getTargets(List<BaseControlFunction> controls) {
        for (int i = 0; i < controls.size(); i++) {
            String lname = links.getType(i);
            if (lname.toLowerCase().startsWith("data")) {
                String data = controls.get(i).getNeural().getDataString();

                if (data.startsWith("Target")) {
                    String[] arr = data.split("_");
                    String[] vals = arr[0].split(":");
                    targetx = vals[1];
                    vals = arr[1].split(":");
                    targety = vals[1];
                }
            }
        }
    }

    private String addDataToStrings(String data, StringBuilder names, StringBuilder values, StringBuilder update) {
        //String id = null;
        String model = null;
        String[] arr = data.split("_");
        for (String arr1 : arr) {
            String[] vals = arr1.split(":");
            String value;
            if (vals[0].equalsIgnoreCase("model")) {
                model = vals[1];
            }
            if (vals[0].equalsIgnoreCase("id")) {
                value = formatValue(vals[0].toLowerCase(), id);
            } else {
                value = formatValue(vals[0].toLowerCase(), vals[1]);
            }
            names.append(vals[0]).append(",");
            values.append(value);
            update.append(vals[0]).append("=").append(value);
        }
        return model;
    }

    private String getID(List<BaseControlFunction> controls) throws SQLException {
        String rid = null;

        for (int i = 0; i < controls.size(); i++) {
            String lname = links.getType(i);
            if (lname.toLowerCase().startsWith("keydata")) {
                String data = controls.get(i).getNeural().getDataString();
                String[] arr = data.split("_");
                for (String arr1 : arr) {
                    String[] vals = arr1.split(":");
                    if (vals[0].equalsIgnoreCase("id")) {
                        rid = vals[1];
                        break;
                    }
                }
            }
        }

        return rid;
    }

    private String getLevel(List<BaseControlFunction> controls) {
        String llevel = null;

        for (int i = 0; i < controls.size(); i++) {
            String lname = links.getType(i);
            if (lname.toLowerCase().startsWith("data")) {
                String data = controls.get(i).getNeural().getDataString();
                String dname;
                if (data.startsWith("Target")) {
                } else {
                    dname = controls.get(i).getName();
                    if (dname.equalsIgnoreCase("level")) {
                        llevel = data;
                    }
                }
            }
        }

        return llevel;
    }

    private void recordData(List<BaseControlFunction> controls) throws SQLException {
        StringBuilder insert = new StringBuilder();
        StringBuilder update = new StringBuilder();
        StringBuilder names = new StringBuilder();
        StringBuilder values = new StringBuilder();
        String model = null;

        insert.append("insert into scores (");
        update.append("update scores set ");

        for (int i = 0; i < controls.size(); i++) {
            String lname = links.getType(i);
            if (lname.toLowerCase().startsWith("keydata")) {
                String data = controls.get(i).getNeural().getDataString();
                model = addDataToStrings(data, names, values, update);
            }
            if (lname.toLowerCase().startsWith("data")) {
                String data = controls.get(i).getNeural().getDataString();
                String dname;
                if (data.startsWith("Target")) {
                    //addDataToStrings(data, names, values);
                    String[] arr = data.split("_");
                    String[] vals = arr[0].split(":");
                    names.append(vals[0]).append(",");
                    String value = formatValue(vals[0].toLowerCase(), targetx);
                    values.append(value);

                    update.append(vals[0]).append("=").append(value);
                    vals = arr[1].split(":");
                    names.append(vals[0]).append(",");
                    value = formatValue(vals[0].toLowerCase(), targety);
                    values.append(value);
                    update.append(vals[0]).append("=").append(value.substring(0, value.length() - 1));
                } else {
                    dname = controls.get(i).getName();
                    if (dname.equalsIgnoreCase("level")) {
                        names.append(dname).append(",");
                        values.append(formatValue(dname.toLowerCase(), level));
                    }
                }
            }
        }
        names.append("ConstraintKey");
        values.append("'").append(constraintKey).append("'");

        insert.append(names.toString());
        insert.append(") values ( ");
        insert.append(values.toString());

        update.append(" where ConstraintKey='").append(constraintKey).append("' and model='").append(model).append("'");

        insert.append(")");
        if (debug) {
            LOG.info(insert.toString());
        }
        try {
            db.executeUpdate(insert.toString());
        } catch (SQLIntegrityConstraintViolationException ex) {
            if (debug) {
                LOG.info(ex.toString());
            }
            if (debug) {
                LOG.info(update.toString());
            }
            String oldId = getOldID(model);
            removeOldParameters(oldId);
            db.executeUpdate(update.toString());
            //id = null;
        }

    }

    private void removeOldParameters(String id) throws SQLException {
        String sql = "delete from parameters where id='" + id + "'";
        db.executeUpdate(sql);
    }

    private String getOldID(String model) throws SQLException {
        String oldId = null;
        ResultSet rs = db.getResultSet("select id from scores where ConstraintKey='" + constraintKey + "' and Model='" + model + "'");
        if (rs.next()) {
            oldId = rs.getString("ID");
        }
        db.closeStatement();
        return oldId;
    }

    private String getConstraintKey(List<BaseControlFunction> controls) {
        StringBuilder constraint = new StringBuilder();
        String delimiter = "_";
        constraint.append(level).append(delimiter);
        constraint.append(targetx).append(delimiter);
        constraint.append(targety).append(delimiter);

        for (int i = 0; i < controls.size(); i++) {
            String lname = links.getType(i);
            if (lname.toLowerCase().startsWith("parameters")) {
                String data = controls.get(i).getNeural().getParametersString();
                String[] arr = data.split("_");
                for (String arr1 : arr) {
                    String[] vals = arr1.split(":");
                    constraint.append(MoonsString.formatStringPlaces(vals[1], 6)).append(delimiter);
                }
            }
        }
        return constraint.toString();
    }

    private String formatValue(String name, String value) {
        StringBuilder val = new StringBuilder();

        switch (name) {
            case "level":
            case "score":
            case "fidelity":
            case "fidelityscore":
            case "timescore":
            case "simulatedtime":
            case "targetx":
            case "targety":
                val.append(value).append(",");
                break;
            default:
                val.append("'").append(value).append("',");
        }

        return val.toString();
    }

    private boolean isValidRecord(BaseControlFunction link) {
        boolean rtn = false;

        String data = link.getNeural().getDataString();
        //LOG.info("Data: " + data);

        if (!level.equalsIgnoreCase(oldLevel)) {
            oldLevel = level;
            return false;
        }
        String[] arr = data.split("_");
        for (String arr1 : arr) {
            String[] vals = arr1.split(":");
            if (vals[0].equalsIgnoreCase(key)) {
                if (Double.parseDouble(vals[1]) > threshold) {
                    return true;
                }
                break;
            }
        }

        return rtn;
    }

    @Override
    public void close() throws Exception {
        if (db != null) {
            db.close();
        }
    }

}
