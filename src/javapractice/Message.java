package javapractice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;

public class Message implements Serializable {

    private int m_mode;//0代表普通消息,1代表客户端向服务器上传，2代表服务器从客户端下载
    private String m_content;
    private String m_filename;
    private byte[] m_filedata;
    private String m_username;
    private Object[] m_args;
    private String m_function;
    private Object m_return;
    public static final int NORMAL=0;
    public static final int UPLOAD=1;
    public static final int DOWNLOAD=2;
    public static final int CLOSE=3;
    public static final int REFLECTION_CALL=4;
    public static final int REFLECTION_RETURN=5;
    public Message(String content,String username){
        m_mode=NORMAL;
        m_content=content;
        m_username=username;
    }
    public Message(String content,String username,String filename,byte[] filedata,int mode){
        m_mode=mode;
        m_content=content;
        m_filedata=filedata;
        m_filename=filename;
        m_username=username;
    }
    public Message(String content,String username,String function,Object[] args){
        m_mode=REFLECTION_CALL;
        m_content=content;
        m_username=username;
        m_function=function;
        m_args=args;
    }
    public Message(String content,String username,Object returnObject){
        m_mode=REFLECTION_RETURN;
        m_content=content;
        m_username=username;
        m_return=returnObject;
    }

    public String getContent(){
        return m_content;
    }
    public int getMode(){
        return m_mode;
    }
    public String getFilename(){
        return m_filename;
    }
    public byte[] getFiledata(){
        return m_filedata;
    }
    public String getUsername(){
        return m_username;
    }
    public Object[] getArgs(){
        return m_args;
    }
    public String getFunction(){
        return m_function;
    }
    public Object getReturn(){
        return m_return;
    }
}
