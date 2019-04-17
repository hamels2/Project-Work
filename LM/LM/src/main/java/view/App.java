package view;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;

import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;

import javafx.util.Callback;

import model.ProcessLister;
import model.ProcessInfo;
import controller.ProcessController;
import controller.UpdateTask;
public final class App extends Application {

    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Process Viewer");
        TableView<ProcessController> table = new TableView<ProcessController>();
        table.setEditable(true);
        final ObservableList<ProcessController> data = FXCollections.observableArrayList();
        populateTable(data);

        TableColumn<ProcessController, Number> pidCol = new TableColumn<ProcessController, Number>("PID");
        TableColumn<ProcessController, String> cpuCol = new TableColumn<ProcessController, String>("CPU time");
        TableColumn<ProcessController, String> memCol = new TableColumn<ProcessController, String>("Memory (K)");

        addButtonToTable(table);

        //link each column to processcontroller for observable list
        pidCol.setCellValueFactory(cellData -> cellData.getValue().getPidProperty());
        cpuCol.setCellValueFactory(cellData -> cellData.getValue().getCpuProperty());
        memCol.setCellValueFactory(cellData -> cellData.getValue().getMemoryProperty());
        
        table.getColumns().addAll(pidCol, cpuCol, memCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setItems(data);
        
        StackPane layout = new StackPane();
        layout.getChildren().add(table);
        Scene scene = new Scene(layout, 350, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void populateTable(ObservableList<ProcessController> data) {
        ProcessLister lister = new ProcessLister();
        ArrayList<ProcessInfo> vals = lister.getAllProcessess();
        for (ProcessInfo p : vals) {
            data.add(new ProcessController(p));
        }
    }

    private void addButtonToTable(TableView<ProcessController> table) { //put buttons in each column
        TableColumn<ProcessController, Void> colBtn = new TableColumn("Monitor");

        //create cellfactory
        Callback<TableColumn<ProcessController, Void>, TableCell<ProcessController, Void>> cellFactory = new Callback<TableColumn<ProcessController, Void>, TableCell<ProcessController, Void>>() {
            @Override
            public TableCell<ProcessController, Void> call(final TableColumn<ProcessController, Void> param) {
                final TableCell<ProcessController, Void> cell = new TableCell<ProcessController, Void>() {

                    //define button
                    private final Button btn = new Button("Monitor");
                    //when clicked run task to update values
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            ProcessController data = getTableView().getItems().get(getIndex());
                            UpdateTask task = new UpdateTask(data);
                            data.monitor();
                            if (data.isMonitored()) {
                                try {
                                    Thread backgroundThread = new Thread(task);
                                    backgroundThread.setDaemon(true);
                                    backgroundThread.start();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            //end task on second click
                            else{
                                task.cancel();
                                data.clear();
                            }

                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        table.getColumns().add(colBtn);

    }
}
