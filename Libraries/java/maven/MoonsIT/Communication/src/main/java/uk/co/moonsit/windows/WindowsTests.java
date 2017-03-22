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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ReStart
 */
public class WindowsTests {

    private static final Logger LOG = Logger.getLogger(WindowsTests.class.getName());

    @SuppressWarnings("SleepWhileInLoop")
    public static void main(String[] args) throws Exception {
        int level = 5;
        QMWindowsComms qmwc = new QMWindowsComms(level);
        Process process = null;
        boolean runProcess = false;
        String name = "Quantum Moves";
        int test = 6;
        int runs = 5;

        //String exe = "C:\\packages\\Perceptual learning-20170103T142628Z\\Perceptual learning\\Builds\\2017-01-03\\Server\\Quantum Moves.exe";
        if (runProcess) {
            String exe = "C:\\Program Files (x86)\\Science at Home\\Quantum Moves\\Quantum Moves.exe";
            ProcessBuilder pbuilder = new ProcessBuilder(exe);
            if (process == null) {
                process = pbuilder.start();
            }
        }
        //Thread.sleep(2000);
        boolean success = qmwc.findWindow(name);
        if (!success) {
            LOG.log(Level.INFO, "{0} is not running", name);
        } else {
            LOG.log(Level.INFO, "{0} is running", name);
            //LOG.log(Level.INFO, "Window {0}", hwnd.toString());
            qmwc.setWindowFocus();
            qmwc.setForegroundWindow();
            LOG.info("Focus set");
            if (runProcess) {
                qmwc.pressReturn();
            }
            String str = qmwc.getWindowText();
            LOG.log(Level.INFO, "Text: {0}", str);

            if (runProcess) {
                Thread.sleep(10000);
            }

            switch (test) {

                case 0:
                    qmwc.loadData();
                    break;
                case 1:
                    qmwc.moveToPlay();
                    qmwc.click();
                    break;
                case 2:
                    qmwc.moveToStart();
                    //qmwc.click();
                    break;
                case 3:
                    qmwc.moveToReplay();
                    qmwc.click();
                    break;
                case 4:
                    qmwc.loadData();
                    qmwc.moveToStart();
                    qmwc.moveToData();
                    break;
                case 5:
                    //qmwc.moveToQMPoint(-0.75f, 0.0f);
                    //qmwc.moveToQMPoint(0.75f, -600.0f);
                    /*qmwc.moveToQMPoint(-0.75f, -400.0f);
                    qmwc.moveToQMPoint(0.75f, -400.0f);
                    qmwc.moveToQMPoint(-0.25f, -400.0f);
                    qmwc.moveToQMPoint(-0.243f, -580.0f);
                    qmwc.moveToQMPoint(0.35f, -400.0f);
                    qmwc.moveToQMPoint(0.09f, -624.0f);*/
                    qmwc.moveToQMPoint(-0.5f, -110.0f);
                    qmwc.moveToQMPoint(0.35f, 0.0f);
                    LOG.info("Delay: " + qmwc.getDelay());
                    break;
                case 6:
                    qmwc.loadData();
                    for (int i = 0; i < runs; i++) {
                        qmwc.moveToStart();
                        long start = System.currentTimeMillis();
                        qmwc.pressMouse();
                        qmwc.moveToData();
                        qmwc.releaseMouse();
                        long time = System.currentTimeMillis() - start;
                        LOG.info("Iter " + i + " Delay: " + qmwc.getDelay() + " Time: " + time );
                        Thread.sleep(8000);
                        qmwc.moveToReplay();
                        qmwc.click();
                        Thread.sleep(1000);
                    }
                    break;
                case 7:
                    qmwc.loadData();
                    for (int i = 0; i < runs; i++) {
                        LOG.info("Iter " + i + " Delay: " + qmwc.getDelay());
                        qmwc.moveToStart();
                        long start = System.currentTimeMillis();
                        qmwc.pressMouse();
                        qmwc.moveToData();
                        //qmwc.releaseMouse();
                        long time = System.currentTimeMillis() - start;
                        LOG.info("Time: " + time);
                        Thread.sleep(8000);
                        //qmwc.moveToReplay();
                        //qmwc.click();
                        //Thread.sleep(1000);
                    }
                    break;
            }

            /*
            qmwc.pressMouse();
            qmwc.releaseMouse();
            for (int i = 0; i < 10; i++) {
                Thread.sleep(100);
            }
             */
 /*
            x = x - 100;
            y = y + 100;
            qmwc.moveMouse(x, y);
            LOG.info("Moved " + x + " " + y);
            qmwc.pressMouse();

            for (int i = 0; i < 80; i++) {
                x = x + 5;
                qmwc.moveMouse(x, y);
                Thread.sleep(20);
            }
            qmwc.releaseMouse();
             */
        }
        for (int i = 0; i < 10; i++) {
            Thread.sleep(100);
        }

        if (runProcess) {
            process.destroy();
        }
    }

}
