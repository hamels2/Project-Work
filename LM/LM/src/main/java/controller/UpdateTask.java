package controller;
import javafx.concurrent.Task;
public class UpdateTask extends Task<Void>{
    ProcessController p;

    public UpdateTask(ProcessController p){
        super();
        this.p = p;
    }
    @Override
    protected Void call() throws Exception {
        while(p.isMonitored()){
            p.setCPU();
            p.setMemory();
            System.out.println(p.getPID()+" "+p.getCPU()+" "+p.getMemory());
            Thread.sleep(3000);
        }
        p.clear();
        return null;
    }

}