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
package uk.co.moons.control.neural;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.functions.BaseControlFunction;
import uk.co.moons.math.RMath;
import uk.co.moonsit.utils.timing.DateAndTime;
import uk.co.moonsit.utils.timing.Time;

/**
 *
 * @author ReStart
 */
public class AutoCorrelationNeuralFunction extends NeuralFunction {

    private static final Logger LOG = Logger.getLogger(AutoCorrelationNeuralFunction.class.getName());
    public Integer period = 1;
    private List<Double> history = null;
    private double[] xcorr = null;
    private double[] transformed = null;
    private int[] crossings = null;
    private double[] wavelet = null;
    private final int WAVELET_SIZE = 9;
    private final int WAVELET_START = -2;
    private long mark;
    private double samplingRate;
    public double outlierlimit = 1;
    public double standarddeviationscale = 1;

    public AutoCorrelationNeuralFunction() throws Exception {
        super();
        history = new ArrayList<>();
        xcorr = new double[period];
        transformed = new double[period];
        crossings = new int[period];
        wavelet = createWavelet();
    }

    public AutoCorrelationNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
        history = new ArrayList<>();
        wavelet = createWavelet();

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Period")) {
                period = Integer.parseInt(param.getValue());
            }
            if (pname.equals("OutlierLimit")) {
                outlierlimit = Double.parseDouble(param.getValue());
            }
            if (pname.equals("StandardDeviationScale")) {
                standarddeviationscale = Double.parseDouble(param.getValue());
            }
        }
        xcorr = new double[period];
        transformed = new double[period];
        crossings = new int[period];
        mark = System.currentTimeMillis();

    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double newValue = controls.get(0).getValue();
        addValue(newValue);
        long now = System.currentTimeMillis();
        samplingRate = RMath.smooth(samplingRate, now - mark, 0.5);
        mark = now;
        if (history.size() == period) {
            autoCorr();
            transform();
            findpeaks();
            output = extractFrequency(samplingRate);
        }
        return output;
    }

    private double extractFrequency(double samplingRate) {
        double freq = 0;

        List<Double> separations = new ArrayList<>();
        int lastIndex = -1;
        for (int i = 0; i < period; i++) {
            if (crossings[i] == 1) {
                if (lastIndex >= 0) {
                    separations.add((double) (i - lastIndex));
                }
                lastIndex = i;
            }
        }

        if (separations.size() > 2) {
            double mean = RMath.mean(separations);
            double sd = RMath.standardDeviation(separations, mean);
            Double separation = removeOutliers(separations, mean, standarddeviationscale * sd);
            if(separation==null)
                return freq;
            freq = (1000 / samplingRate) / separation;

            String time = formatTime(getTime(), true);
            //System.out.println(time + " fr " + freq + " sr " + samplingRate + " sp "+separation+" sd " + sd);
        }

        return freq;
    }

    protected String formatTime(String dt, boolean decimal) {
        String time = dt;

        if (dt.length() > 12) {
            time = dt.substring(11, dt.length());
        }

        if (decimal) {
            Time t = new Time(time);
            time = String.format("%8.6f", t.getTime());
        }

        return time;
    }

    @Override
    public String getTime() {
        DateAndTime dat = new DateAndTime();
        return dat.HMSS();
    }

    private Double removeOutliers(List<Double> list, double mean, double sd) {

        //System.out.print("b4 ");
        //printList(list);

        try {
            List<Integer> removeList = new ArrayList<>();
            int i = 0;
            for (Double s : list) {
                if (Math.abs(s - mean) > outlierlimit * sd) {
                    removeList.add(i);
                }
                i++;
            }
            i = 0;
            for (int j : removeList) {
                list.remove(j - i);
                i++;
            }
            if(list.size()<=2)
                return null;
            if (removeList.size() > 0) {
                mean = RMath.mean(list);
            }
        } catch (Exception e) {
            LOG.severe("+++ Ex: " + e.toString());
        }

        //System.out.print("b5 ");
        //printList(list);

        return mean;
    }

    private void printList(List<Double> list) {
        for (Double s : list) {
            System.out.print(" " + s);
        }
        System.out.println();
    }

    private void findpeaks() {

        for (int i = 0; i < period; i++) {
            int previousIndex = i - 1;
            if (previousIndex < 0) {
                continue;
            }
            if (transformed[i] >= 0 && transformed[previousIndex] < 0) {
                crossings[i] = 1;
            } else {
                crossings[i] = 0;
            }
        }
    }

    private double[] createWavelet() {
        double[] wave = new double[WAVELET_SIZE];
        double inc = Math.abs(WAVELET_START) / ((wave.length - 1) / 2.0);

        for (int i = 0; i < wave.length; i++) {
            double x = i * inc + WAVELET_START;
            wave[i] = -x * Math.exp(-(x * x / 2));
        }

        return wave;
    }

    private double waveletTransform(int index) {
        double sum = 0;
        for (int i = 0; i < wavelet.length; i++) {
            int transformIndex = i + index - (WAVELET_SIZE - 1) / 2;
            if (transformIndex < 0 || transformIndex > history.size() - 1) {
                continue;
            }
            sum += wavelet[i] * history.get(transformIndex);
        }

        return sum;
    }

    private void transform() {

        for (int i = 0; i < period; i++) {
            transformed[i] = waveletTransform(i);
        }
    }

    private void addValue(double value) {
        history.add(value);
        if (history.size() > period) {
            history.remove(0);
        }
    }

    private void autoCorr() {
        double sum = 0;
        for (int i = 0; i < period; i++) {
            sum = 0;
            for (int j = 0; j < period; j++) {
                sum += (history.get(i) * history.get(j));
            }
            xcorr[i] = sum;
        }
    }

    /*
    private void addToTransformed(int posindex, Double d) {
    if (posindex + 1 > transformed.size()) {
    transformed.add(d);
    } else {
    transformed.add(posindex, d);
    transformed.remove(posindex + 1);
    }
    }
    
    private void addToXcorr(int posindex, Double d) {
    if (posindex + 1 > xcorr.size()) {
    xcorr.add(d);
    } else {
    xcorr.add(posindex, d);
    xcorr.remove(posindex + 1);
    }
    }
     */
}
