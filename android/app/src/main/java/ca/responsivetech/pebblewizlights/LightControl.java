package ca.responsivetech.pebblewizlights;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

class LightControl implements Runnable {

    private int ledRed = 0;
    private int ledBlue = 0;
    private int ledGreen = 0;
    private int ledDimming = 0;

    private int port;
    private String ip;
    private DatagramSocket socket = null;
    private JSONObject jsonData = null;

    public LightControl(String ip, int port) {
        this.port = port;
        this.ip = ip;


    }

    @Override
    public void run() {
        try {
            JSONObject params = new JSONObject();
            params.put("r", new Integer(ledRed));
            params.put("g", new Integer(ledGreen));
            params.put("b", new Integer(ledBlue));
            params.put("dimming", new Integer(ledDimming));

            jsonData = new JSONObject();
            jsonData.put("method", "setPilot");
            jsonData.put("params", params);

            Log.d("TestingJSON", jsonData.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();

                try {
                    socket = new DatagramSocket();

                    InetAddress host = InetAddress.getByName(ip);
                    byte[] data = jsonData.toString().getBytes();
                    DatagramPacket packet = new DatagramPacket(data, data.length, host, port);
                    try {
                        socket.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } catch (SocketException | UnknownHostException e) {
                    e.printStackTrace();
                } finally {
                    if (socket != null) {
                        socket.close();
                    }
                }
            }
        };

        thread.start();
    }

    public int getLedBlue() {
        return ledBlue;
    }

    public void setLedBlue(int ledBlue) {
        this.ledBlue = ledBlue;
    }

    public int getLedRed() {
        return ledRed;
    }

    public void setLedRed(int ledRed) {
        this.ledRed = ledRed;
    }

    public int getLedGreen() {
        return ledGreen;
    }

    public void setLedGreen(int ledGreen) {
        this.ledGreen = ledGreen;
    }

    public int getLedDimming() {
        return ledDimming;
    }

    public void setLedDimming(int ledDimming) {
        this.ledDimming = ledDimming;
    }
}
