import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.fileTypes.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.intellij.openapi.vfs.VirtualFile;
import java.io.*;
/**
 * Created by nightost on 2017/12/5.
 */
public class fdDevAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        runScript(e);
    }
    @Override
    public void update(AnActionEvent e) {
        VirtualFile file = DataKeys.VIRTUAL_FILE.getData(e.getDataContext());
        FileType fileType = file.getFileType();
    }

    public void runScript(AnActionEvent event){
        VirtualFile file = DataKeys.VIRTUAL_FILE.getData(event.getDataContext());

        String folderName = "";

        String path = file.getPath();

        folderName = file.getName();

        String startString = "#!/bin/sh" + "\n";

        String changePath = "cd " + path + "\n";

        String dev = "npm run dev -- path=" + folderName + "\n";
        System.out.print(changePath);
        System.out.print(dev);
        createShFile(path , startString + changePath + dev);


        /*try {
            Runtime.getRuntime().exec("/usr/bin/open -a Terminal /path/to/the/executable");
        }catch (Exception e){
            System.out.print(e);
            String outputPath = executeCommand(changePath);
            String outputDev = executeCommand(dev);
        }*/
    }

    public void createShFile(String path , String content){
        try{
            String filePath = path + "/dev.sh";
            Writer output = new BufferedWriter(new FileWriter(filePath));
            output.write(content);
            output.close();
            Runtime.getRuntime().exec("chmod u+x " + filePath);
//            execShFile(filePath);
            executeCommand(filePath);
        }catch (IOException e){
            System.out.print(e + "-----create shell file error");
        }
    }

    public void execShFile(String path){
        ProcessBuilder pb = new ProcessBuilder(path);
        String s = null;
        int runningStatus = 0;
        try{
            Process p = pb.start();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((s = stdInput.readLine()) != null) {
                System.out.print(s);
            }
            while ((s = stdError.readLine()) != null) {
                System.out.print(s);
            }
            try {
                runningStatus = p.waitFor();
            } catch (InterruptedException e) {
            }
        }catch (Exception e){
            System.out.print(e + "-----execute shell file error");
        }
    }

    private void executeCommand(String command) {

        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec("/bin/sh " + command);

        } catch (Exception e) {
            System.out.print(e);
        }
    }
}
