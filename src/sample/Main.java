package sample;

import com.chin.fofaquerydemo.ConnMySql;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main extends Application {
    String fofaquery;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Group root = new Group();
        Scene scene = new Scene(root,330,150);
        primaryStage.setScene(scene);
        //设置框架标题
        primaryStage.setTitle("FofaQuery Inquery Tools");
        //设置窗口图标
        primaryStage.getIcons().add(new Image("/static/2.png"));

        //创建一个GridPane容器
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10,10,10,10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        scene.setRoot(gridPane);

        //首先创建一个Label控件以标记对应的文本字段是用于名称输入
        Label label1 = new Label("FofaQuery:");
        GridPane.setConstraints(label1,0,0);
        gridPane.getChildren().add(label1);


        //创建一个TextArea对象用于接收返回值
        TextArea output = new TextArea("Dear Portland!");
        output.setPromptText("Dear Portland!");
        //设置TextArea所在的行和列，以及显示框所占的行和列
        GridPane.setConstraints(output,0,3,3,3);
        gridPane.getChildren().add(output);


        //创建一个TextField对象
        final TextField textField = new TextField();
        //定义显示在文本字段中的字符串（无法用getText方法获取提示文本）
        textField.setPromptText("Enter Fofaquery.");
        //根据字符串长度自动调整大小
        //output.prefColumnCountProperty().bind(output.textProperty().length());
        //设置文本字段大小（通过设置一次可以显示的最大字符数）
        textField.setPrefColumnCount(10);
        //从文本字段获取值
        textField.getText();

        GridPane.setConstraints(textField,1,0);
        gridPane.getChildren().add(textField);


        //预定义文本创建文本域
        final TextField textField1 = new TextField("hi,I'm CJ.");
        textField1.setPromptText("hi,I'm CJ.");
        //textField1.setPrefColumnCount(15);
        GridPane.setConstraints(textField1,1,1);
        gridPane.getChildren().add(textField1);


        Button submit = new Button("Submit");
        GridPane.setConstraints(submit,2,0);
        gridPane.getChildren().add(submit);

        Button clear = new Button("Clear");
        GridPane.setConstraints(clear,2,1);
        gridPane.getChildren().add(clear);


        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (textField.getText() != null && !textField.getText().isEmpty()){
                    //利用正则将fofaquery的app=""去掉
                    Pattern p = Pattern.compile("app=\"(.*)?\"");
                    Matcher m = p.matcher(textField.getText());
                    if (m.matches()){
                        //去掉app=""
                        m.group().replace("\"","");
                        textField.setText(m.group().replace("app=\"","").replace("\"",""));
//                        System.out.println(textField.getText());
                    }


                    ConnMySql connMySql = new ConnMySql();
                    fofaquery = connMySql.ConnMySqlQuery(textField.getText());
                    System.out.println(fofaquery);
                    if (!fofaquery.equals("null\n" +
                            "旧规则：app=\"null\"\n" +
                            "新规则：app=\"null\"")){
                        output.setText(fofaquery);
                    }else {
                        output.setText("not find!");
                    }
                }
            }
        });


        clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                textField.clear();
                textField1.clear();
                output.clear();
            }
        });

        primaryStage.show();
    }

    public static String execCmd(String... cmd) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        //得到字节流
        InputStream inputStream = runtime.exec(cmd).getInputStream();
        //将字节流转化成字符流，并指定字符集
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"GBK");
        //将字符流以缓存的形式一行一行的输出
        BufferedReader bf = new BufferedReader(inputStreamReader);
        StringBuilder result = new StringBuilder();
        String newLine = "";

        while ((newLine = bf.readLine()) != null) {
//            System.out.println("enter");
            result.append(newLine).append("\n");
        }
        System.out.println(result);
        inputStream.close();

        return result.toString();
    }

    public static void main(String[] args) throws IOException {

//        String[] cmdStart = {"cmd","/c",".\\nircmd.exe elevate net start mysql57"};
        String[] cmdStart = {"cmd","/c",".\\nircmd.exe","elevate","net","start","mysql57"};
        String[] cmdStart2 = {"net","start","mysql57"};
        String[] cmdStop2 = {"net","stop","mysql57"};
        String cmd3 = "net start mysql57";
        String cmd34 = "cmd /c net start mysql57";

//        Process p = new ProcessBuilder(cmdStart).start();
//        InputStream stream = p.getInputStream();
//
//        InputStreamReader inputStreamReader = new InputStreamReader(stream,"GBK");
//        System.out.println("将字节流转化成字符流，并指定字符集");
//        //将字符流以缓存的形式一行一行的输出
//        BufferedReader bf = new BufferedReader(inputStreamReader);
//        System.out.println("将字符流以缓存的形式一行一行的输出");
//        String result = "";
//        String newLine = "";
//
//        while ((newLine = bf.readLine()) != null) {
//            System.out.println("进入。。。");
//            result += newLine + "\n";
//        }
//        System.out.println(result);

        //启动MySQL服务
//        String[] cmd = {"cmd","/C","runAs","/user:administrator","net start"};
        execCmd(cmdStart2);
//        String cmdStart = ".\\nircmd.exe elevate net start mysql57";
        launch(args);
        execCmd(cmdStop2);
    }
}
