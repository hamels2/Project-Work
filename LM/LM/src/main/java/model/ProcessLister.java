package model;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


public class ProcessLister{
    private ArrayList<ProcessInfo> processes;

    public ProcessLister(){
        updateProcesses();
    }

    public void updateProcesses(){
        this.processes = new ArrayList<ProcessInfo>();
		try {
            Process process = Runtime.getRuntime().exec("tasklist.exe /v");
            Scanner scanner = new Scanner(new InputStreamReader(process.getInputStream()));
            int i =0;
        while (scanner.hasNext()) {
            String s = scanner.nextLine();
            System.out.println(s);
            if(i>2){
                String [] vals = s.split("\\s{2,100}");
                int pid = Integer.parseInt(vals[1].split(" ")[0]);
                processes.add(new ProcessInfo(pid));
   
            }
            i++;
        }
        scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }



    public ArrayList<ProcessInfo> getAllProcessess(){
        return processes;
    }


}