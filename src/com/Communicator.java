package com;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Communicator {
    public static void send(int port, String ip) throws Exception {

        String localAdr = "Failed to get IP of machine.";

        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            localAdr = socket.getLocalAddress().getHostAddress();
        }

        DatagramSocket udpSocket = new DatagramSocket();

        InetAddress mcIPAddress = InetAddress.getByName(ip);

        byte[] msg = localAdr.getBytes();
        DatagramPacket packet = new DatagramPacket(msg, msg.length);
        packet.setAddress(mcIPAddress);
        packet.setPort(port);
        udpSocket.send(packet);

        System.out.println("Sent a  multicast message.");
        System.out.println("Done");
        udpSocket.close();
    }

    public static String receive(int port, String ip) throws Exception {
        MulticastSocket mcSocket;
        InetAddress mcIPAddress;
        mcIPAddress = InetAddress.getByName(ip);
        mcSocket = new MulticastSocket(port);
        System.out.println("Multicast Receiver running at:"
                + mcSocket.getLocalSocketAddress());
        mcSocket.joinGroup(mcIPAddress);

        DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);

        System.out.println("Waiting for a  multicast message...");
        mcSocket.receive(packet);
        String msg = new String(packet.getData(), packet.getOffset(),
                packet.getLength());
        System.out.println("[Multicast  Receiver] Received:" + msg);

        mcSocket.leaveGroup(mcIPAddress);
        mcSocket.close();
        return msg;
    }
}