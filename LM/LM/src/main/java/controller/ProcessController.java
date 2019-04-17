package controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import model.ProcessInfo;


public class ProcessController{
    private final SimpleIntegerProperty pid = new SimpleIntegerProperty();
    private final SimpleStringProperty cpu = new SimpleStringProperty();
    private final SimpleStringProperty memory = new SimpleStringProperty();
    private final ProcessInfo info;
    private boolean monitor;

    public ProcessController(ProcessInfo p){
        info = p;
        pid.set(p.getPID());
        cpu.set(p.getCPU());
        memory.set(p.getMemory());
        monitor = false;
    }

    public SimpleIntegerProperty getPidProperty(){
        return pid;
    }

    public SimpleStringProperty getCpuProperty(){
        return cpu;
    }
    public SimpleStringProperty getMemoryProperty(){
        return memory;
    }

    public int getPID(){
        return pid.get();
    }

    public String getCPU(){
        return cpu.get();
    }

    public ProcessInfo getProcessInfo(){
        return info;
    }

    public String getMemory(){
        return memory.get();
    }

    public void setCPU(){
        info.updateCPU();
        String update = info.getCPU();
        cpu.set(update);
    }

    public void setMemory(){
        info.updateMemory();
        String update = info.getMemory();
        memory.set(update);

    }

    public void monitor(){
        monitor = !monitor;
    }

    public void clear(){
        cpu.set("");
        memory.set("");
    }
    public boolean isMonitored(){
        return monitor;
    }
    
}