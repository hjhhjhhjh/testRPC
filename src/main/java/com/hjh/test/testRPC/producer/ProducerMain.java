package com.hjh.test.testRPC.producer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

import com.hjh.test.testRPC.api.ProductService;

public class ProducerMain {
	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(9991);
			while(true){
				Socket socket = serverSocket.accept();
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				
				String className = ois.readUTF();
				String methodName = ois.readUTF();
				
				Class[] parameterTypes = (Class[])ois.readObject();
				Object[] args4Method = (Object[])ois.readObject();

				Class clazz = null;
				if(className.equals(ProductService.class.getName())){
					clazz = Producer1.class;
				}
				
				Method method = clazz.getMethod(methodName, parameterTypes);
				Object invoke = method.invoke(clazz.newInstance(),args4Method);
				
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject(invoke);
				oos.flush();
				
				ois.close();
				oos.close();
				socket.close();
						
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
