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
package uk.co.moons.config;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import pct.moons.co.uk.schema.layers.Layers;
import pct.moons.co.uk.schema.layers.Layers.Layer;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions;
import pct.moons.co.uk.schema.layers.Link;
import pct.moons.co.uk.schema.layers.Parameters;

/**
 *
 * @author Rupert Young Moon's Information Technology
 *
 */
public class XmlToLatex {

    private final List<String> breakFunctions;
    private final float width = 0.8f;
    private int rows = 1;
    private final int rowsLimit = 38;
    private final int type = 2;

    public XmlToLatex() {
        //String[] list = new String[]{""};
        String[] list = new String[]{"MotorBInput", "MotorCOutput", "RotationControlInput", "FullRotationWeightsCOutput",
            "", "LocationRotationWeightBSign", "PursuitControlOutput", "SequenceControlOutput", "MotorBChange", "SensorSumChangeReverseWeight", "SequenceSwitchError", "SCWeight", ""};
        breakFunctions = new ArrayList<>(Arrays.asList(list));
    }

    public void write(String file, Layers layers) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append("%");
        if (layers.getOrderedControllers() != null) {
            List<String> ordered = layers.getOrderedControllers().getController();
            for (String cont : ordered) {
                sb.append(cont).append(",");
            }
            sb.append("\n");
        }

        addMainHeader(sb);

        int i = 0;
        for (Layer layer : layers.getLayer()) {
            sb.append("\\textbf{Level}      & \\multicolumn{4}{c|}{").append(i++).append("}\n");
            sb.append("\\\\ \\hline \\hline\n");
            sb.append("\n");
            processLayer(layer, sb);
        }
        addMainFooter(sb);

        try (FileOutputStream fos = new FileOutputStream(file.replaceAll(".xml", ".tex"))) {
            fos.write(sb.toString().getBytes());
        }

        try (FileOutputStream fos = new FileOutputStream(file.replaceAll(".xml", ".ltx"))) {
            String prefix = getDocHeader();
            sb.append("\\end{document}");
            String out = prefix + sb.toString();
            fos.write(out.getBytes());
        }

        System.out.println(sb.toString());
    }

    private String getDocHeader(){
        String rtn = null;
        switch (type) {
            case 1:
                rtn = "\\documentclass{article}\n" + "\\usepackage{" + getTabName() + "}\n" + "\\begin{document}\n";
                break;
            case 2:
                rtn = "\\documentclass{article}\n \\begin{document}\n";
                break;
        }
        return rtn;
    }
    private void addMainFooter(StringBuilder sb) {
        switch (type) {
            case 1:
                sb.append("\\end{").append(getTabName()).append("}\n");
                break;
            case 2:
                sb.append("\\end{").append(getTabName()).append("}\n");
                sb.append("\\end{table}\n");
                break;
        }
    }

    private void addMainHeader(StringBuilder sb) {
        switch (type) {
            case 1:
                sb.append("%distance between rows\n%\\def\\arraystretch{0.7}\n\\tablecaption{Control System Specification}\n" + "\\label{control-table}\n");
                sb.append("\\small\n");
                sb.append(beginTab(width));
                break;
            case 2:
                
                sb.append("\\begin{table}[]\n");
                sb.append("\\small\n");
                sb.append("\\caption{Control System Specification}\n");
                sb.append("\\label{control-table}\n");
                sb.append(beginTab(width));
                
                break;
        }
    }

    private String beginTab(float f) {
        return "\\begin{" + getTabName() + "}{|l|l|l|l|l|l|}\\hline\n";
        //return "\\begin{"+getTabName()+"*}{" + f + "\\textwidth}{|l|l|l|l|l|l|}\\hline\n";
    }

    private void processLayer(Layer layer, StringBuilder sb) throws Exception {
        //int cnt = 0;
        for (pct.moons.co.uk.schema.layers.Layers.Layer.Controller cont : layer.getController()) {
            processController(cont, sb);
            //hmControllers.put(cont.getName(), control);
            //layers[layerLevel].add(control);
        }
    }

    private void processController(pct.moons.co.uk.schema.layers.Layers.Layer.Controller controller, StringBuilder sb) throws Exception {

        Functions functions = controller.getFunctions();
        sb.append("\\textbf{Controller} & \\multicolumn{4}{c|}{").append(controller.getName()).append("}\\\\ \\hline\n\n");
        rows++;
        //pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.ErrorFunctions.Transfers 

        if (functions.getInputFunctions() != null) {
            sb.append("\\multicolumn{5}{|c|}{InputFunctions} \\\\ \\hline\n");
            rows++;
            processControlFunctionCollection(functions.getInputFunctions().getInput(), convertTransfers(functions.getInputFunctions()), sb, "Input");
        }

        if (functions.getReferenceFunctions() != null) {
            sb.append("\\multicolumn{5}{|c|}{ReferenceFunctions} \\\\ \\hline\n");
            rows++;
            processControlFunctionCollection(functions.getReferenceFunctions().getReference(), convertTransfers(functions.getReferenceFunctions()), sb, "Reference");
        }

        if (functions.getErrorFunctions() != null) {
            sb.append("\\multicolumn{5}{|c|}{ErrorFunctions} \\\\ \\hline\n");
            rows++;
            processControlFunctionCollection(functions.getErrorFunctions().getError(), convertTransfers(functions.getErrorFunctions()), sb, "Error");
        }

        if (functions.getOutputFunctions() != null) {
            sb.append("\\multicolumn{5}{|c|}{OutputFunctions} \\\\ \\hline\n");
            rows++;
            processControlFunctionCollection(functions.getOutputFunctions().getOutput(), convertTransfers(functions.getOutputFunctions()), sb, "Output");
        }

    }

    private void processControlFunctionCollection(pct.moons.co.uk.schema.layers.ControlFunction function, List<pct.moons.co.uk.schema.layers.ControlFunction> transfers, StringBuilder sb, String type) throws Exception {
        if (function != null) {
            processFunctionShortFormat(function, sb, type);
            processTransfers(transfers, sb);
        }
    }

    private void processFunctionShortFormat(pct.moons.co.uk.schema.layers.ControlFunction function, StringBuilder sb, String type) throws Exception {

        sb.append("\\multicolumn{1}{|c||}{").append(type).append("}                 & \\textbf{Name}        & ");
        sb.append(function.getName());
        pct.moons.co.uk.schema.layers.NeuralFunction nf = function.getNeuralFunction();
        int num = getSize(nf);
        sb.append("& \\textbf{Type} & ").append(nf.getType().length() == 0 ? "PassThrough" : nf.getType()).append("\\\\ \\cline{2-5}\n");
        if (num == 0) {
            sb.append("\\multicolumn{1}{|c||}{} & \\textbf{Description} & \\multicolumn{3}{l|}{").append(function.getDescription()).append("}\\\\ \\hline \n");
        } else {
            sb.append("\\multicolumn{1}{|c||}{} & \\textbf{Description} & \\multicolumn{3}{l|}{").append(function.getDescription()).append("}\\\\ \\cline{2-5} \n");
        }
        String parameters = getParameterList(nf.getParameters());
        if (parameters.length() > 0) {
            if (nf.getLinks().isEmpty()) {
                sb.append("\\multicolumn{1}{|c||}{} & \\textbf{Parameters} & \\multicolumn{3}{l|}{").append(parameters).append("}\\\\ \\hline \n");
            } else {
                sb.append("\\multicolumn{1}{|c||}{} & \\textbf{Parameters} & \\multicolumn{3}{l|}{").append(parameters).append("}\\\\ \\cline{2-5} \n");
            }
        }

        if (nf.getLinks().size() > 0) {
            sb.append(getLinks(nf.getLinks()));
        }

        if (breakFunctions.contains(function.getName())) {
            sb.append(breakFunction());
        }

        rows = rows + 4;
        if (rows > rowsLimit) {
            //sb.append(breakFunction());
            rows = 0;
        }

    }

    private String getParameterList(List<Parameters> parameters) {
        StringBuilder sb = new StringBuilder();
        int ctr = 0;
        for (Parameters parameter : parameters) {
            sb.append(parameter.getName()).append(":").append(parameter.getValue());
            if (ctr++ < parameters.size() - 1) {
                sb.append(";");
            }
        }

        return sb.toString();
    }

    private String getLinks(List<Link> links) {
        StringBuilder sb = new StringBuilder();
        sb.append("\\multicolumn{1}{|c||}{} & \\textbf{Links} & \\multicolumn{3}{l|}{");

        int ctr = 1;
        for (Link link : links) {
            sb.append(link.getName());
            if (ctr++ < links.size()) {
                if (ctr % 3 == 0) {
                    sb.append("}\\\\ \\cline{3-5}\n");
                    sb.append("\\multicolumn{1}{|c||}{} & \\textbf{} & \\multicolumn{3}{l|}{");
                } else {
                    sb.append(",");
                }
            } else {
                sb.append("}\\\\ \\hline");
            }

        }
        sb.append("\n");
        return sb.toString();
    }

    private String getLinksList(List<Link> links) {
        StringBuilder sb = new StringBuilder();
        int ctr = 0;
        for (Link link : links) {
            sb.append(link.getName());
            if (ctr++ < links.size() - 1) {
                sb.append(",");
            }
            if (ctr == 2) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    private void processFunction(pct.moons.co.uk.schema.layers.ControlFunction function, StringBuilder sb, String type) throws Exception {

        sb.append("\\multicolumn{1}{|c||}{}                 & \\textbf{Name}        & ");
        sb.append(function.getName());
        pct.moons.co.uk.schema.layers.NeuralFunction nf = function.getNeuralFunction();
        sb.append("& \\textbf{Type} & ").append(nf.getType()).append("\\\\ \\cline{2-5}\n");
        sb.append("\\multicolumn{1}{|c||}{}                 & \\textbf{Description} & \\multicolumn{3}{l|}{").append(function.getDescription()).append("}\\\\ \\cline{2-5} \n");
        sb.append("\\multicolumn{1}{|c||}{}                 & \\multicolumn{2}{c|}{\\textbf{Parameters}}                   & \\multicolumn{2}{c|}{\\textbf{Links}}               \\\\ \\cline{2-5} \n");
        sb.append("\\multicolumn{1}{|c||}{\\textbf{").append(type).append("}}  & \\textbf{Name}        & \\textbf{Value} & \\multicolumn{2}{c|}{}                             \\\\ \\cline{2-5} \n");
        int num = getSize(nf);
        rows = rows + num + 2;

        String[][] array = new String[num][3];
        processParameters(nf.getParameters(), array);
        processLinks(nf.getLinks(), array);
        processParametersAndLinks(array, sb);
        sb.append("\n");

        if (breakFunctions.contains(function.getName())) {
            sb.append(breakFunction());
        }
        if (rows > rowsLimit) {
            //sb.append(breakFunction());
            rows = 0;
        }

    }

    private String getTabName() {
        String rtn = null;

        switch (type) {
            case 1:
                rtn = "supertabular";
                break;
            case 2:
                rtn = "tabular";
                break;
        }
        //return "supertabular";
        return rtn;
    }

    private String getTabEnd() {
        return "\\end{" + getTabName() + "}\n\n\n\\pagebreak\n";
    }

    private String breakFunction() {
        String rtn=null;
        switch(type){
            case 1:
                rtn = getTabEnd() + beginTab(width);
                break;
            case 2:
                rtn = "\\end{" + getTabName() + "}\n" +"\\end{table}\n\\pagebreak\n\n\n\\begin{table}[]\n\\small\n"+ beginTab(width);
                
                break;
        }
            
        return rtn;
    }

    private void processTransfers(List<pct.moons.co.uk.schema.layers.ControlFunction> transfers, StringBuilder sb) throws Exception {
        if (transfers == null) {
            return;
        }
        for (pct.moons.co.uk.schema.layers.ControlFunction function : transfers) {
            processFunctionShortFormat(function, sb, "Transfer");
        }
    }

    private int getSize(pct.moons.co.uk.schema.layers.NeuralFunction inf) {

        return inf.getParameters().size() > inf.getLinks().size() ? inf.getParameters().size() : inf.getLinks().size();
    }

    //private int getIntType(String type) throws Exception {
    //  return NeuralTypes.getNeuralTypes(type);
    //}
    private void processParameters(List<Parameters> parameters, String[][] array) {
        int i = 0;
        for (Parameters parameter : parameters) {
            array[i][0] = parameter.getName();
            array[i][1] = parameter.getValue();
            i++;
        }
    }

    private void processLinks(List<Link> links, String[][] array) throws Exception {
        for (Link link : links) {
            array[0][2] = link.getName();
        }
    }

    private void processParametersAndLinks(String[][] array, StringBuilder sb) throws Exception {

        int i = 0;
        for (String[] sarr : array) {
            i++;
            sb.append("\\multicolumn{1}{|c||}{}                 & ");
            sb.append(sarr[0] == null ? "" : sarr[0]).append("& \\multicolumn{1}{c|}{");
            sb.append(sarr[1] == null ? "" : sarr[1]).append("} & \\multicolumn{2}{l|}{");
            if (i == array.length) {
                sb.append(sarr[2] == null ? "" : sarr[2]).append("}                              \\\\ \\hline\n");
            } else {
                sb.append(sarr[2] == null ? "" : sarr[2]).append("}                             \\\\ \\cline{2-5}\n");
            }
        }
    }

    private List<pct.moons.co.uk.schema.layers.ControlFunction> convertTransfers(pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.InputFunctions function) {
        List<pct.moons.co.uk.schema.layers.ControlFunction> list = null;
        pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = function.getTransfers();
        if (transfers != null) {
            list = new ArrayList<>();
            for (pct.moons.co.uk.schema.layers.ControlFunction transfer : transfers.getTransfer()) {
                list.add(transfer);
            }
        }
        return list;
    }

    private List<pct.moons.co.uk.schema.layers.ControlFunction> convertTransfers(pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.ReferenceFunctions function) {
        List<pct.moons.co.uk.schema.layers.ControlFunction> list = null;
        pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers transfers = function.getTransfers();
        if (transfers != null) {
            list = new ArrayList<>();
            for (pct.moons.co.uk.schema.layers.ControlFunction transfer : transfers.getTransfer()) {
                list.add(transfer);
            }
        }
        return list;
    }

    private List<pct.moons.co.uk.schema.layers.ControlFunction> convertTransfers(pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.ErrorFunctions function) {
        List<pct.moons.co.uk.schema.layers.ControlFunction> list = null;
        pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.ErrorFunctions.Transfers transfers = function.getTransfers();
        if (transfers != null) {
            list = new ArrayList<>();
            for (pct.moons.co.uk.schema.layers.ControlFunction transfer : transfers.getTransfer()) {
                list.add(transfer);
            }
        }
        return list;
    }

    private List<pct.moons.co.uk.schema.layers.ControlFunction> convertTransfers(pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.OutputFunctions function) {
        List<pct.moons.co.uk.schema.layers.ControlFunction> list = null;
        pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = function.getTransfers();
        if (transfers != null) {
            list = new ArrayList<>();
            for (pct.moons.co.uk.schema.layers.ControlFunction transfer : transfers.getTransfer()) {
                list.add(transfer);
            }
        }
        return list;
    }
}
