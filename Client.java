/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package d9l2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 *
 * @author Mostafa
 *
 */
public class Client extends Application {

    Button Save = new Button("Save");
    BorderPane BD = new BorderPane();
    TextArea SA = new TextArea();
    TextField TA = new TextField();
    Button BT = new Button("Send");
    Button OT = new Button("Open");
    Label label = new Label("Enter Your Message");
    Socket mySocket;
    DataInputStream DIS;
    PrintStream PS;
    String Reply;

    @Override
    public void init() {
        SA.setText("Chat Messages : " + "\n");
        SA.setEditable(false);
        TA.setPromptText("Enter Your Message");
        BT.setDefaultButton(true);
    }

    FileChooser fileChooser = new FileChooser();

    @Override
    public void start(Stage primaryStage) {

        fileChooser.setTitle("Save File");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text",".txt"));
        Save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileWriter writer = null;
                File f = fileChooser.showSaveDialog(primaryStage);
                if (f != null) {
                    try {
                        writer = new FileWriter(f);
                        try (BufferedWriter buffer = new BufferedWriter(writer)) {
                            buffer.write(SA.getText());
                            System.out.println(SA.getText());
                        } catch (IOException ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            try {
                                writer.close();
                            } catch (IOException ex) {
                                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.out.println("No File found");
                }
            }
        });

        OT.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                FileChooser fc = new FileChooser();

                FileChooser.ExtensionFilter exf = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                fc.getExtensionFilters().add(exf);
                StringBuilder sb = new StringBuilder();
                File f = fc.showOpenDialog(primaryStage);
                if (f != null) {
                    BufferedReader br = null;
                    try {
                        br = new BufferedReader(new FileReader(f));
                        String text;
                        try {
                            while ((text = br.readLine()) != null) {
                                sb.append(text);
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try {
                            br.close();
                        } catch (IOException ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        SA.setText(sb.toString());
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        try {
                            br.close();
                        } catch (IOException ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });
        Socket mySocket = null;
        try {
            mySocket = new Socket("127.0.0.1", 5005);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            DIS = new DataInputStream(mySocket.getInputStream());
        } catch (IOException ex){
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            PS = new PrintStream(mySocket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        HBox hbox = new HBox(label, TA, BT, Save, OT);
        BT.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                PS.println(TA.getText());
                TA.setText("");
            }
        });
        Thread th = new Thread(new Runnable() {
            @Override

            public void run() {

                while (true) {
                    try {

                        Reply = DIS.readLine();
                        SA.appendText("\n" + Reply);
                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        th.setDaemon(true);

        th.start();

        BD.setCenter(SA);

        BD.setBottom(hbox);

        Scene scene = new Scene(BD, 500, 500);

        primaryStage.setTitle("Chat Client");

        primaryStage.setScene(scene);

        primaryStage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        //new Client();
    }
}
