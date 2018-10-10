package com.hjh.test.testRPC.consumer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

import com.hjh.test.testRPC.api.ProductService;
import com.hjh.test.testRPC.common.Person;

public class ConsumerMain{
	public static void main(String[] args) {
		ProductService productService =(ProductService)rpc(ProductService.class);
		Person person = productService.queryById("123");


		System.out.println(person.getName());
		
	}
	
	@SuppressWarnings("rawtypes")
	public static Object rpc(final Class clazz){
		return Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
			
			@SuppressWarnings({ "resource", "rawtypes" })
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				System.out.println("1");
				Socket socket = new Socket("127.0.0.1", 9991);
				String className = clazz.getName();
				String methodName = method.getName();
				Class[] parameterTypes = method.getParameterTypes();
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeUTF(className);
				oos.writeUTF(methodName);
				oos.writeObject(parameterTypes);
				oos.writeObject(args);
				oos.flush();
				
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				Object o = ois.readObject();
				
				ois.close();
				oos.close();
				
				return o;
			}
		});
	}
}
