package model;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

public class ProcessListerTest{

    @Test
    public void test_updateProcess(){
        ProcessLister lister = new ProcessLister();
        ArrayList<ProcessInfo> old = lister.getAllProcessess();
        assertTrue(old.size()>1);

        //check that first process is system idle process (pid=0)
        assertTrue(old.get(0).getPID()==0);
        // check that second process is system (pid =4)
        assertTrue(old.get(1).getPID()==4);

        
        lister.updateProcesses();
        assertTrue(old != lister.getAllProcessess());

        //check that first process is system idle process (pid=0)
        assertTrue(old.get(0).getPID()==0);
        // check that second process is system (pid =4)
        assertTrue(old.get(1).getPID()==4);

    }
}