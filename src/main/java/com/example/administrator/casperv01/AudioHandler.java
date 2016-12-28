package com.example.administrator.casperv01;
//서버의 Deliver 역활 버퍼로부터 데이터를 읽어와 AudioTrack으로 전달하는 역활
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Created by Administrator on 2016-05-22.
 */
public class AudioHandler extends Thread{

    //AudioFormat
//    private static final int BLOCK_SIZE = 3528;
    private static final int AudioSampleRate = 44100;
    private static final int AudioChannel = AudioFormat.CHANNEL_OUT_STEREO;
    private static final int AudioBit = AudioFormat.ENCODING_PCM_16BIT;
    private static final int AudioMode = AudioTrack.MODE_STREAM;
    private boolean playFlag = true;

    private AudioBuffer[] audioBuffers;
    private AudioRecord audioRecord;

    private AudioTrack audioTrack;
    private byte [] tempBuffer;
    private int readIndex;

    public AudioHandler(AudioBuffer[] audioBuffers)
    {
        super();
        this.audioBuffers = audioBuffers;
        readIndex = 0;

        tempBuffer = new byte[AudioBuffer.AUDIO_BUFFER_SIZE];
    }

    public void initPlay(){
        createAudioTrack();
        audioTrack.play();
        audioTrack.pause();
    }

    public void setAudioRecord(AudioRecord audioRecord){
        this.audioRecord = audioRecord;
    }

//    public AudioRecord getAudioRecord(){
//        return this.audioRecord;
//    }

    public boolean setAudioFlag(boolean flag){
        playFlag = flag;
        return playFlag;
    }

    private void createAudioTrack()
    {
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, AudioSampleRate, AudioChannel, AudioBit, audioTrack.getMinBufferSize(AudioSampleRate, AudioChannel, AudioBit), AudioMode);
    }

    public void writePCM()
    {
        audioBuffers[readIndex].getbuffer(tempBuffer);
        readIndex = (readIndex + 1 ) % AudioBuffer.NUM_OF_BUFFER;
        audioTrack.write(tempBuffer, 0, AudioBuffer.AUDIO_BUFFER_SIZE);

        //recording
        if(audioRecord.getRecordFlag() == true){
            audioRecord.writeAudioData(tempBuffer);
        }

    }

    public void play() {
        audioTrack.play();
    }

    public void pause() {
        audioTrack.flush();
        audioTrack.pause();
    }

    public int getAudioState(){
        return audioTrack.getPlayState();
    }

    @Override
    public void run(){

        initPlay();

        while(playFlag){
            writePCM();
        }
    }
}