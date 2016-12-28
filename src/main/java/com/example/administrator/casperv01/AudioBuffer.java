package com.example.administrator.casperv01;

/**
 * Created by yms on 2016-12-19.
 */
public class AudioBuffer {

    private byte buffer[];
    private boolean AudioFlag = true;
    //네트워크 데이터 크기
    public static final int AUDIO_BUFFER_SIZE = UdpNetwork.BUFFER_SIZE;

    public static final int NUM_OF_BUFFER = 5;

    public AudioBuffer(){
        buffer = new byte[AUDIO_BUFFER_SIZE];
    }


    synchronized public void getbuffer(byte [] tempBuffer){
        if(!AudioFlag){
            try{
                wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        System.arraycopy(buffer, 0, tempBuffer, 0, AudioBuffer.AUDIO_BUFFER_SIZE);
        AudioFlag = false;
        notify();
    }

    synchronized public void setBuffer(byte [] buffer){
        System.arraycopy(buffer, 0, this.buffer, 0, AudioBuffer.AUDIO_BUFFER_SIZE);
        setFlag();
        notify();

        try{
            Thread.sleep(1);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void setFlag(){
        AudioFlag = true;
    }


}