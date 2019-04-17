package model;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ProcessParser {

    private int pid;

    public ProcessParser(int pid){
        this.pid = pid;
    }

    private String getProcessLine(){
        String line;
        try{
            Process process = Runtime.getRuntime().exec("tasklist /v /fi \"PID eq "+pid+"\"");
            Scanner scanner = new Scanner(new InputStreamReader(process.getInputStream()));
            scanner.nextLine();
            scanner.nextLine();
            scanner.nextLine();
            line = scanner.nextLine();
            scanner.close();
        }
        catch(IOException e){
            line = "error getting process";
        }

        return line;
    }

    public String getProcessMemory(){
        String s = getProcessLine();
        if(s.contains("INFO")){
            return "killed";
        }
        String [] vals = s.split("\\s{2,100}");
        String mem = vals[3].split(" ")[0];
        return mem;  
    }

    public String getProcessCPU() {
        String s = getProcessLine();
        if(s.contains("INFO")){
            return "killed";
        }
        String [] vals = s.split("\\s{2,100}");
        String cpu = vals[5].split(" ")[0];
        return cpu;
    }
    public String getProcessName(){
        String s = getProcessLine();
        String [] vals = s.split("\\s{2,100}");
        String name = vals[0].split(" ")[0];
        return name;
    }
}