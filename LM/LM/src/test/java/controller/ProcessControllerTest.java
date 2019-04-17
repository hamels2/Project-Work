package controller;

import org.junit.Test;

import model.ProcessInfo;

import static org.junit.Assert.*;

public class ProcessControllerTest{
    ProcessInfo info = new ProcessInfo(0);
    ProcessController pc = new ProcessController(info);

    @Test 
    public void test_getCPU(){
        assertTrue(pc.getCPU().length()==0);
    }

    @Test
    public void test_getMemory(){
        assertTrue(pc.getMemory().length()==0);
    }

    @Test
    public void test_setCPU(){
        String old = pc.getCPU();
        pc.setCPU();
        assertTrue(old!=pc.getCPU());
        assertTrue(pc.getCPU().length()>0);
    }

    @Test
    public void test_setMemory(){
        String old = pc.getMemory();
        pc.setMemory();
        assertTrue(old!=pc.getMemory());
        assertTrue(pc.getMemory().length()>0);
    }

    @Test
    public void test_monitor(){
        assertFalse(pc.isMonitored());
        pc.monitor();
        assertTrue(pc.isMonitored());
        pc.monitor();
        assertFalse(pc.isMonitored());
    }

    @Test
    public void test_clear(){
        pc.clear();
        assertTrue(pc.getCPU().equals(""));
        assertTrue(pc.getMemory().equals(""));

    }

}