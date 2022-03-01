import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Session session = null;
        ChannelExec channel = null;
        Scanner intro = new Scanner(System.in);

        System.out.println("Introduce el usuario");
        String name = intro.nextLine();

        System.out.println("Introduce la contraseña");
        String password = intro.nextLine();

        System.out.println("Introduce la dirección ip");
        String sshIp = intro.nextLine();

        System.out.println("Introduce el puerto a conectar");
        int sshPort = intro.nextInt();

        boolean continueRuning = true;

        while(continueRuning){

            System.out.println("Introduce el fichero:");
            String nameFile = intro.next();

            try {
                session = new JSch().getSession(name,sshIp, sshPort);

                session.setPassword(password);
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();

                channel = (ChannelExec) session.openChannel("exec");

                channel.setCommand("cd /var/log; cat " + nameFile);

                ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
                channel.setOutputStream(responseStream);

                channel.connect();
                
                while(channel.isConnected()) {
                    Thread.sleep(100);
                }

                String responseString = new String(responseStream.toByteArray());
                System.out.println(responseString);


            } catch (JSchException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally{
                if(session != null) session.disconnect();
                if(channel != null) channel.disconnect();
            }

            System.out.println("¿Desea continuar?, escriba si para continuar no para terminar el programa");
            String confirmacion = intro.next();

            if(confirmacion.equals("no")){
                continueRuning = false;
            }
        }

    }
}
