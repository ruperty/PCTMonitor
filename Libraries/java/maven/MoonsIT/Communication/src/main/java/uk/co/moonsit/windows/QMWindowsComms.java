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
package uk.co.moonsit.windows;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ReStart
 */
public class QMWindowsComms extends WindowsComms {

    private static final Logger LOG = Logger.getLogger(QMWindowsComms.class.getName());

    private final float PLAYX_FACTOR = 0.5f;
    private final float PLAYY_FACTOR = 0.5f;
    private final float REPLAYX_FACTOR = 0.05f;
    private final float REPLAYY_FACTOR = 0.05f;
    private int level = 0;
    //private float startx_factor;
    //private float starty_factor;
    private int startx;
    private int starty;
    private int ypixeloffset = 0;

    private int xoffset = 0;

    private float xscale = 1;
    private float yscale = 1;
    private float qmstartx;
    private float qmstarty;
    private float yheaderfactor = 0.15f;
    private float interpolations;

    private final List<Integer[]> data;
    private final String directory;
    private float maxx, minx, maxy, miny;
    private long delay;

    public QMWindowsComms(int level) {
        this.level = level;
        data = new ArrayList<>();
        directory = ".." + File.separator + "data" + File.separator + "QuantumMoves" + File.separator;
    }

    public void loadData() throws IOException {
        List<Double[]> localdata = new ArrayList<>();
        String path = directory + "Level" + String.valueOf(level) + ".csv";
        LOG.log(Level.INFO, "File path to data :{0}", path);
        try (BufferedReader in = new BufferedReader(new FileReader(path))) {
            String line;
            in.readLine();
            while ((line = in.readLine()) != null) {
                String[] arr = line.split(",");
                double x = Double.parseDouble(arr[3]);
                double y = Double.parseDouble(arr[2]);
                Double[] d = new Double[2];
                d[0] = x;
                d[1] = y;
                localdata.add(d);
            }
        }
        interpolate(localdata);
    }

    private Integer[] scaleXY(Double[] dxy) {
        Integer[] xy = new Integer[2];
        xy[0] = scaleX(dxy[0]);
        xy[1] = scaleY(dxy[1]);
        LOG.log(Level.INFO, "Data: {0} {1} {2} {3}", new Object[]{dxy[0], dxy[1], xy[0], xy[1]});
        return xy;
    }

    private void interpolate(List<Double[]> loaddata) {
        int ctr = 0;
        Double[] olddxy = null;
        for (Double[] dxy : loaddata) {
            if (ctr > 0) {
                for (int i = 1; i <= interpolations; i++) {
                    Double[] tmpdxy = new Double[2];
                    tmpdxy[0] = olddxy[0] + ((dxy[0] - olddxy[0]) * i / (interpolations + 1));
                    tmpdxy[1] = olddxy[1] + ((dxy[1] - olddxy[1]) * i / (interpolations + 1));
                    data.add(scaleXY(tmpdxy));
                }
            }
            data.add(scaleXY(dxy));
            olddxy = dxy;
            ctr++;
        }
        LOG.info("Size: " + data.size());
    }

    private int scaleX(double qx) {
        return (int) (width * ((xscale * (qx - qmstartx) + qmstartx) - minx) / (maxx - minx)) - xoffset;
    }

    private int scaleY(double qy) {
        int headerOffset = (int) (yheaderfactor * height);
        int y = ypixeloffset + headerOffset + (int) ((height - headerOffset) * (maxy - (yscale * (qy - qmstarty) + qmstarty)) / (maxy - miny));
        if (y > height) {
            y = height - 5;
        }
        return y;
    }

    @Override
    public boolean findWindow(String name) {
        boolean rtn = super.findWindow(name); //To change body of generated methods, choose Tools | Templates.

        if (rtn) {
            configure();
        }
        return rtn;
    }

    private void configure() {
        yheaderfactor = 0.15f;

        switch (level) {
            case 5:
                interpolations = 3;
                delay = (long) (50 / (interpolations + 1));
                minx = -1f;
                maxx = 1f;
                maxy = 0f;
                miny = -600f;
                qmstartx = 0.5f;
                qmstarty = -400f;
                xscale = 1.08f;
                break;
            case 4:
                ypixeloffset = 55;
                interpolations = 3;
                delay = (long) (50 / (interpolations + 1));
                minx = -1f;
                maxx = 1f;
                maxy = 0f;
                miny = -600f;
                qmstartx = -0.5f;
                qmstarty = -100f;
                xscale = 1.075f;
                yscale=0.55f;
                break;
            case 3:
                interpolations = 3;
                delay = (long) (50 / (interpolations + 1));
                minx = -1f;
                maxx = 1f;
                maxy = 0f;
                miny = -600f;
                qmstartx = 0.35f;
                qmstarty = -400f;
                xscale = 1.08f;
                break;
            case 2:
                interpolations = 3;
                delay = -2 + (long) (50 / (interpolations + 1));
                minx = -0.75f;
                maxx = 0.75f;
                maxy = 0f;
                miny = -600f;
                qmstartx = 0.35f;
                qmstarty = -400f;
                xoffset = 0;
                xscale = 1.08f;
                break;
            default:
                interpolations = 3;
                delay = (long) (50 / (interpolations + 1));
                minx = -0.75f;
                maxx = 0.75f;
                maxy = 0f;
                miny = -600f;
                qmstartx = -0.25f;
                qmstarty = -400f;
                break;
        }

    }

    public long getDelay() {
        return delay;
    }

    public void pressPlay() {

    }

    public void moveToReplay() {
        int x = (int) (width * REPLAYX_FACTOR);
        int y = (int) (height * REPLAYY_FACTOR);
        moveMouse(x, y);
        //LOG.log(Level.INFO, "Moved to {0} {1}", new Object[]{x, y});
    }

    public void moveToPlay() {
        int x = (int) (width * PLAYX_FACTOR);
        int y = (int) (height * PLAYY_FACTOR);
        moveMouse(x, y);
        LOG.log(Level.INFO, "Moved to {0} {1}", new Object[]{x, y});
    }

    public void moveToQMPoint(float qx, float qy) {
        int x = scaleX(qx);
        int y = scaleY(qy);
        LOG.log(Level.INFO, "Move to {0} {1}", new Object[]{x, y});
        moveMouse(x, y);
    }

    public void moveToStart() {
        startx = scaleX(qmstartx);
        starty = scaleY(qmstarty);
        //LOG.log(Level.INFO, "Move to {0} {1}", new Object[]{startx, starty});
        moveMouse(startx, starty);
    }

    public void moveToData() throws InterruptedException {
        for (Integer[] xy : data) {
            pause(delay);
            moveMouse(xy[0], xy[1]);
            //pause(100);
            //LOG.log(Level.INFO, "Moved to {0} {1}", new Object[]{xy[0], xy[1]});

        }
    }
}
