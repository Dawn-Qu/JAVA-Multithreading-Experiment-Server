package javapractice;
import java.io.*;
import java.net.*;

public class SocketServer extends ServerSocket {
    private static final int SERVER_PORT =12345;
    public static final String SERVER_FILE_PATH="/home/ubuntu/ServerFile/";

    public SocketServer()throws IOException {
        super(SERVER_PORT);

        try {
            while (true) {
                Socket socket = accept();
                new CreateServerThread(socket);//当有请求时，启一个线程处理
            }
        }catch (IOException e) {
        }finally {
            close();
        }
    }

    //线程类
    class CreateServerThread extends Thread {
        private Socket client;
        private InputStream is;
        private OutputStream os;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;


        public CreateServerThread(Socket s)throws IOException {
            client = s;
            is=s.getInputStream();
            os=s.getOutputStream();
            ois=new ObjectInputStream(is);
            oos=new ObjectOutputStream(os);
            start();
        }

        public void run() {
            try {
                while (true) {
                    Message message;
                    message = (Message) ois.readObject();
                    System.out.println(message.getUsername() + message.getContent());
                    switch (message.getMode()) {
                        case Message.NORMAL:
                            break;
                        case Message.UPLOAD: {
                            File file = new File(SERVER_FILE_PATH + message.getFilename());
                            if (!file.exists()) {
                                boolean isCreatedSuccess = file.createNewFile();
                                if (!isCreatedSuccess) {
                                    throw new IOException("File creating fail.");
                                }
                                FileOutputStream fos = new FileOutputStream(file);
                                fos.write(message.getFiledata());
                                fos.close();
                            }
                            break;
                        }
                        case Message.DOWNLOAD: {
                            File file = new File(SERVER_FILE_PATH + message.getFilename());
                            FileInputStream fis = new FileInputStream(file);
                            byte[] bytes = new byte[(int) file.length()];
                            fis.read(bytes);
                            Message messageReturn = new Message(">>>FILE_TO_CLIENT", message.getUsername(), message.getFilename(), bytes, Message.DOWNLOAD);
                            oos.writeObject(messageReturn);
                            oos.flush();
                            break;
                        }
                        case Message.CLOSE:{
                            os.close();
                            is.close();
                            client.close();
                            return;
                        }
                    }
                }
            }
            catch(ClassNotFoundException e){
                e.printStackTrace();
            }
//            catch(SocketException e){
//                return;
//            }
            catch(IOException e){
                e.printStackTrace();
            }
//            catch (InterruptedException e){
//                e.printStackTrace();
//            }
        }
    }

    public static void main(String[] args)throws IOException {
        new SocketServer();
    }
}
