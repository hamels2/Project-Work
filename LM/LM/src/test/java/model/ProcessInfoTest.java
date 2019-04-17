package model;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProcessInfoTest{

    ProcessInfo p = new ProcessInfo(0);

    @Test
    public void test_getCPU(){
        assertTrue(p.getCPU().length()==0);
    }

    @Test
    public void test_getMemory(){
        assertTrue(p.getMemory().length()==0);
    }

    @Test
    public void test_getPid(){
        assertTrue(p.getPID()==0);
    }

    @Test
    public void test_updateCPU(){
        String oldcpu = p.getCPU();
        p.updateCPU();
        assertTrue(oldcpu!=p.getCPU());
    }

    @Test
    public void test_updateMemory(){
        String oldmem = p.getMemory();
        p.updateMemory();
        assertTrue(oldmem!=p.getMemory());
    }
}