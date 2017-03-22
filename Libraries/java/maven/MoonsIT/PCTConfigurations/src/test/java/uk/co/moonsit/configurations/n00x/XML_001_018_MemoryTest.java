/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.moonsit.configurations.n00x;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rupert
 */
public class XML_001_018_MemoryTest {

    public XML_001_018_MemoryTest() {
    }

    /**
     * Test of main method, of class XML_001_018_Memory.
     * @throws java.lang.Exception
     */
    @Test
    @SuppressWarnings("CallToPrintStackTrace")
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
        try {
            XML_001_018_Memory.main(args);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

}
