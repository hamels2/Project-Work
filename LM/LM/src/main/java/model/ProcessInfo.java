package model;

public class ProcessInfo{
    private int pid;
    private String cpu;
    private String memory;
    private ProcessParser parser;

    public ProcessInfo(int pid){
        this.pid = pid;
        parser = new ProcessParser(pid);
        cpu="";
        memory="";

    }

    public String getCPU(){
        return cpu;
    }
    public String getMemory(){
        return memory;
    }
    public int getPID(){
        return this.pid;
    }

    public void updateCPU(){
        this.cpu = parser.getProcessCPU();
    }
    public void updateMemory(){
        this.memory = parser.getProcessMemory();
    }
}