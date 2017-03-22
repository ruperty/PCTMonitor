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

import com.sun.jna.platform.win32.BaseTSD.ULONG_PTR;
import static com.sun.jna.platform.win32.User32.INSTANCE;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LONG;
import com.sun.jna.platform.win32.WinDef.WORD;
import com.sun.jna.platform.win32.WinUser.INPUT;
import static com.sun.jna.platform.win32.WinUser.SM_CXSCREEN;
import static com.sun.jna.platform.win32.WinUser.SM_CYSCREEN;
import com.sun.jna.platform.win32.WinUser.WINDOWINFO;
import com.sun.jna.win32.StdCallLibrary;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moonsit.sockets.QMClient;

/**
 *
 * @author ReStart
 */
public class WindowsComms {

    private static final Logger LOG = Logger.getLogger(WindowsComms.class.getName());
    private HWND hwnd;
    int[] screenDimensions;
    int[] windowCorners;
    int width, height;

    /*
    public static final long MOUSEEVENTF_MOVE = 0x0001L;
    public static final long MOUSEEVENTF_VIRTUALDESK = 0x4000L;
    public static final long MOUSEEVENTF_ABSOLUTE = 0x8000L;
     */
    public interface MUser32 extends StdCallLibrary {

        public static final long MOUSEEVENTF_MOVE = 0x0001L;
        public static final long MOUSEEVENTF_VIRTUALDESK = 0x4000L;
        public static final long MOUSEEVENTF_ABSOLUTE = 0x8000L;
        public static final long MOUSEEVENTF_LEFTDOWN = 0x0002L;
        public static final long MOUSEEVENTF_LEFTUP = 0x0004L;

        //User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);
        //DWORD SendInput(DWORD dWord, INPUT[] input, int cbSize);
    }

    private int[] getSystemMetrics() {
        int[] dims = new int[2];
        int screenwidth = INSTANCE.GetSystemMetrics(SM_CXSCREEN);
        int screenheight = INSTANCE.GetSystemMetrics(SM_CYSCREEN);
        LOG.log(Level.INFO, "w h {0} {1}", new Object[]{screenwidth, screenheight});
        dims[0] = screenwidth;
        dims[1] = screenheight;
        return dims;
    }

    private int[] getWindowInfo() {
        int[] corner = new int[4];
        WINDOWINFO pwi = new WINDOWINFO();
        pwi.cbSize = pwi.size();
        INSTANCE.GetWindowInfo(hwnd, pwi);
        corner[0] = pwi.rcClient.left;
        corner[1] = pwi.rcClient.top;
        corner[2] = pwi.rcClient.right;
        corner[3] = pwi.rcClient.bottom;

        return corner;
    }

    @SuppressWarnings("SleepWhileInLoop")
    public boolean findWindow(String name) {
        boolean rtn = false;
        hwnd = INSTANCE.FindWindow(null, name);
        int cnt = 0;
        while (hwnd == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(QMClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            hwnd = INSTANCE.FindWindow(null, name);
            if (cnt++ == 10) {
                break;
            }
        }
        if (hwnd != null) {
            rtn = true;
        }
        screenDimensions = getSystemMetrics();
        windowCorners = getWindowInfo();
        width = windowCorners[2] - windowCorners[0];
        height = windowCorners[3] - windowCorners[1];
        LOG.log(Level.INFO, "WindowInfo {0} {1} {2} {3} {4} {5}", new Object[]{windowCorners[0], windowCorners[1], windowCorners[2], windowCorners[3], width, height});

        return rtn;
    }

    public void waitabit(long pause) throws InterruptedException {
        for (int i = 0; i < 1; i++) {
            Thread.sleep(pause);
            //hwnd = WindowsComms.findWindow(name);
            //LOG.log(Level.INFO, "Window {0}", hwnd.toString());
            //LOG.log(Level.INFO, "Paused {0}", pause);
        }

    }

    public void pause(long pause) throws InterruptedException {
        Thread.sleep(pause);
    }

    public boolean openWindow(String name) {
        return findWindow(name);
        //int len = User32.INSTANCE.GetWindowTextLength(hwnd);
        //char[] s = new char[len];
        //User32.INSTANCE.GetWindowText(hwnd, s, len);
        //LOG.info("Text: " + new String(s));
    }

    public String getWindowText() {
        int len = INSTANCE.GetWindowTextLength(hwnd);
        char[] s = new char[len + 1];

        INSTANCE.GetWindowText(hwnd, s, len + 1);
        String str = new String(s);

        return str;
    }

    public void setForegroundWindow() {
        INSTANCE.SetForegroundWindow(hwnd);
    }

    public void setWindowFocus() {
        //User32.INSTANCE.SetForegroundWindow(hwnd);
        INSTANCE.SetFocus(hwnd);//.SetForegroundWindow(hwnd);
    }

    public void pressReturn() {
        // Bring the window to the front
        //User32.INSTANCE.SetForegroundWindow(hwnd);
        //User32.INSTANCE.SetFocus(hwnd);
        INPUT input = new INPUT();
        input.type = new DWORD(INPUT.INPUT_KEYBOARD);
        input.input.setType("ki"); // Because setting INPUT_INPUT_KEYBOARD is not enough: https://groups.google.com/d/msg/jna-users/NDBGwC1VZbU/cjYCQ1CjBwAJ
        input.input.ki.time = new DWORD(0); //The timestamp
        //input.input.ki.dwFlags = new DWORD(KEYBDINPUT.KEYEVENTF_UNICODE); //I am handing you a unicode character
        input.input.ki.wScan = new WORD(0); //The unicode code in decimal (right?)
        input.input.ki.wVk = new WORD(0); //Virtual key code, i am setting this to 0 because of the unicode flag in dwFlags
        input.input.ki.dwExtraInfo = new ULONG_PTR(0); //I have no idea in hell of what this does :)
        // Press carriage return
        byte cr = '\r';
        input.input.ki.wVk = new WORD(cr);
        input.input.ki.dwFlags = new DWORD(0);  // keydown
        INSTANCE.SendInput(new DWORD(1), (INPUT[]) input.toArray(1), input.size());
        // Release carriage return
        //input.input.ki.wVk = new WORD(cr);
        //input.input.ki.dwFlags = new DWORD(2);  // keyup

    }

    public void moveWindow(String name, int x, int y, int width, int height) {
        hwnd = INSTANCE.FindWindow(null, name);
        INSTANCE.MoveWindow(hwnd, x, y, width, height, true);
    }

    public void moveMouse(int xr, int yr) {
        int x = windowCorners[0] + xr;
        int y = windowCorners[1] + yr;

        INPUT input = new INPUT();
        input.type = new DWORD(INPUT.INPUT_MOUSE);
        input.input.setType("mi");

        input.input.mi.dx = new LONG(x * 65536 / screenDimensions[0]);
        input.input.mi.dy = new LONG(y * 65536 / screenDimensions[1]);
        //LOG.info("Move " + x + " " + input.input.mi.dx + " " + y + " " + input.input.mi.dy);

        //input.input.mi.dx = new LONG(x);
        //input.input.mi.dy = new LONG(y);
        input.input.mi.mouseData = new DWORD(0);
        input.input.mi.dwFlags = new DWORD(MUser32.MOUSEEVENTF_MOVE | MUser32.MOUSEEVENTF_ABSOLUTE);//| MOUSEEVENTF_VIRTUALDESK );//);
        // input.input.mi.dwFlags = new DWORD(0x8000L);
        input.input.mi.time = new DWORD(0);

        INPUT[] inArray = {input};

        int cbSize = input.size(); // mouse input struct size
        DWORD nInputs = new DWORD(1); // number of inputs
        DWORD result = INSTANCE.SendInput(nInputs, inArray, cbSize);
        //LOG.log(Level.INFO, "result: {0}", result); // return 1 if the 1 event successfully inserted
        //LOG.log(Level.INFO, "Moved to {0} {1}", new Object[]{x, y});

    }

    public void click() {
        pressMouse();
        releaseMouse();
    }

    public void pressMouse() {
        INPUT input = new INPUT();
        input.type = new DWORD(INPUT.INPUT_MOUSE);
        input.input.setType("mi");

        input.input.mi.time = new DWORD(0);
        input.input.mi.mouseData = new DWORD(0);
        input.input.mi.dwFlags = new DWORD(MUser32.MOUSEEVENTF_LEFTDOWN);

        INPUT[] inArray = {input};
        DWORD result = INSTANCE.SendInput(new DWORD(1), inArray, input.size());
        //LOG.log(Level.INFO, "result: {0}", result); // return 1 if the 1 event successfully inserted

    }

    public void releaseMouse() {
        INPUT input = new INPUT();
        input.type = new DWORD(INPUT.INPUT_MOUSE);
        input.input.setType("mi");

        input.input.mi.time = new DWORD(0);
        input.input.mi.mouseData = new DWORD(0);
        input.input.mi.dwFlags = new DWORD(MUser32.MOUSEEVENTF_LEFTUP);

        INPUT[] inArray = {input};
        DWORD result = INSTANCE.SendInput(new DWORD(1), inArray, input.size());
        //LOG.log(Level.INFO, "result: {0}", result); // return 1 if the 1 event successfully inserted

    }
}
