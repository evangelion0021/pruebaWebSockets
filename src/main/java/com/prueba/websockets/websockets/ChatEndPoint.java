/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prueba.websockets.websockets;

import com.prueba.websockets.model.Message;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Set;
import javax.websocket.EncodeException;
import javax.websocket.server.PathParam;
/**
 *
 * @author PC
 */
@ServerEndpoint(value = "/chat/{username}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class ChatEndPoint {
    
    private Session sesion;
    private static Set<ChatEndPoint> chatEndPoints= new CopyOnWriteArraySet<>();
    private static HashMap<String, String> users=new HashMap<>();
    
    @OnOpen
    public void onOpen(Session sesion,
            @PathParam("username") String username)throws IOException, EncodeException{
        this.sesion=sesion;
        chatEndPoints.add(this);
        users.put(sesion.getId(), username);
        Message mensaje=new Message();
        mensaje.setFrom(username);
        mensaje.setContent("Connected!");
        broadcast(mensaje);
        
        
    }
    
    @OnMessage
    public void onMessage(Session sesion, Message mensaje)throws IOException,EncodeException{
        mensaje.setFrom(users.get(sesion.getId()));
        broadcast(mensaje);
    }
    
    @OnClose
    public void onClose(Session sesion)throws IOException,EncodeException{
        chatEndPoints.remove(this);
        Message mensaje=new Message();
        mensaje.setFrom(users.get(sesion.getId()));
        mensaje.setContent("Disconnected!");
        broadcast(mensaje);
        
    }
    
    @OnError
    public void onError(Session sesion, Throwable throwable){
        
    }
    
    private static void broadcast(Message mensaje)throws IOException, EncodeException{
        chatEndPoints.forEach(endPoint -> {
            synchronized(endPoint){
                try{
                    endPoint.sesion.getBasicRemote().sendObject(mensaje);
                }catch(IOException | EncodeException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
