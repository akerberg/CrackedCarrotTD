package com.crackedcarrot.multiplayer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Toast;
import com.crackedcarrot.GameLoopGUI;


public class MultiplayerService extends Thread {
	
	private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    //private Handler mMultiplayerHandler;
    
    // Message types sent to the MultiplayerService Handler
    public static final int MESSAGE_SYNCH_LEVEL = 1;
    public static final int MESSAGE_PLAYER_SCORE = 2;
    public static final int MESSAGE_WRITE = 20;
    public static final int MESSAGE_DEVICE_NAME = 30;
    public static final int MESSAGE_TOAST = 40;
    
    // Message read types sent to the MultiplayerService Handler: MESSAGE_READ
    private final String SYNCH_LEVEL = "synchLevel";
    private final String PLAYER_SCORE = "Score";
    
    public MultiplayerHandler mpHandler;
    public GameLoopGUI gameLoopGui;

    public MultiplayerService(BluetoothSocket socket, GameLoopGUI glGui) {
        Log.d("MultiplayerService", "create ConnectedThread");
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        gameLoopGui = glGui;

        // Get the BluetoothSocket input and output streams
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {}

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
        mpHandler = new MultiplayerHandler(gameLoopGui);
        mpHandler.start();
    }
    
    

    public void run() {
        Log.d("MultiplayerService", "BEGIN MultiplayerService");
        byte[] buffer = new byte[1024];
        int bytes;

        // Keep listening to the InputStream while connected
        while (true) {
            try {
                // Read from the InputStream
                bytes = mmInStream.read(buffer);
                
                if(bytes > 0){
	                // construct a string from the valid bytes in the buffer
	                String readMessage = new String(buffer);
	                readMessage = readMessage.substring(0, bytes);
	                Log.d("XXXXX", "BIG: " + bytes);
	                if(readMessage.equals(SYNCH_LEVEL)){
	                	 // Send the obtained bytes to the UI Activity
	                    mpHandler.mMultiplayerHandler.obtainMessage(MESSAGE_SYNCH_LEVEL, 0, 
	                    		bytes, buffer).sendToTarget();
	                }
	                // The data consists of the opponents score
	                else {
	                	readMessage = readMessage.substring(0, 5);
	                	Log.d("YYYYY", readMessage);
	                	if(readMessage.equals(PLAYER_SCORE)){
	                		mpHandler.mMultiplayerHandler.obtainMessage(MESSAGE_PLAYER_SCORE, 0, 
	                				bytes, buffer).sendToTarget();
	                	}
	                	
	                }
                }
         	   //Log.d("MPSERVICE LOOP", "Send to handler");
            } catch (IOException e) {
            	Log.d("MPSERVICE LOOP", "Connection lost");
                connectionLost();
                break;
            } 
        }    
    }
    
    public synchronized void connected() {
        this.start();
    }
    
    /**
     * Write to the connected OutStream.
     * @param buffer  The bytes to write
     */
    public void write(byte[] buffer) {
       try {
    	   Log.d("MPSERVICE Write", "Write to OutputStream");
            mmOutStream.write(buffer);
        } catch (IOException e) {
            Log.e("MultiplayerService", "Exception during write", e);
        }
    }
    
    
    
    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed() {
    	/**
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(BluetoothChat.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(BluetoothChat.TOAST, "Unable to connect device");
        msg.setData(bundle);
        mHandler.sendMessage(msg); */
    }
    
    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
        // Send a failure message back to the Activity
        mpHandler.mMultiplayerHandler.obtainMessage(MESSAGE_TOAST).sendToTarget();
    }
    
}