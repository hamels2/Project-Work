package model;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProcessParserTest{
    

    @Test
    public void test_GetProcessName(){
        ProcessParser parser = new ProcessParser(0);
        assertTrue(parser.getProcessName().equals("System"));

        parser = new ProcessParser(4);
        assertTrue(parser.getProcessName().equals("System"));

    }

    @Test
    public void test_GetProcessMemory(){
        ProcessParser parser = new ProcessParser(0);
        assertTrue(parser.getProcessMemory().length()>0);

        parser = new ProcessParser(4);
        assertTrue(parser.getProcessMemory().length()>0);

    }


    @Test
    public void test_GetProcessCPU(){
        ProcessParser parser = new ProcessParser(0);
        assertTrue(parser.getProcessCPU().length()>0);

        parser = new ProcessParser(4);
        assertTrue(parser.getProcessCPU().length()>0);
    }

}

