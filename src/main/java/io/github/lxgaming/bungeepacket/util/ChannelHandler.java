/*
 * Copyright 2017 Alex Thomson
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.lxgaming.bungeepacket.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.connection.InitialHandler;

public class ChannelHandler {
	
	public void addChannel(ProxiedPlayer proxiedPlayer, String baseName, String name, ChannelDuplexHandler handler) throws ExceptionInInitializerError, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, NoSuchMethodException, NullPointerException, SecurityException {
		getChannel(getChannel(proxiedPlayer)).pipeline().addBefore(baseName, name, handler);
	}
	
	public void removeChannel(ProxiedPlayer proxiedPlayer, String name) throws ExceptionInInitializerError, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, NoSuchMethodException, NullPointerException, SecurityException {
		getChannel(getChannel(proxiedPlayer)).pipeline().remove(name);
	}
	
	public void addChannel(PendingConnection pendingConnection, String baseName, String name, ChannelDuplexHandler handler) throws ExceptionInInitializerError, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, NoSuchMethodException, NullPointerException, SecurityException {
		getChannel(getChannel(pendingConnection)).pipeline().addBefore(baseName, name, handler);
	}
	
	public void removeChannel(PendingConnection pendingConnection, String name) throws ExceptionInInitializerError, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, NoSuchMethodException, NullPointerException, SecurityException {
		getChannel(getChannel(pendingConnection)).pipeline().remove(name);
	}
	
	private Object getChannel(ProxiedPlayer proxiedPlayer) throws ExceptionInInitializerError, IllegalAccessException, IllegalArgumentException, NoSuchFieldException, NullPointerException, SecurityException {
		if (!(proxiedPlayer instanceof UserConnection)) {
			return null;
		}
		
		Field field = UserConnection.class.getDeclaredField("ch");
		field.setAccessible(true);
		return field.get((UserConnection) proxiedPlayer);
	}
	
	private Object getChannel(PendingConnection pendingConnection) throws ExceptionInInitializerError, IllegalAccessException, IllegalArgumentException, NoSuchFieldException, NullPointerException, SecurityException {
		if (!(pendingConnection instanceof InitialHandler)) {
			return null;
		}
		
		Field field = InitialHandler.class.getDeclaredField("ch");
		field.setAccessible(true);
		return field.get((InitialHandler) pendingConnection);
	}
	
	private Channel getChannel(Object object) throws ExceptionInInitializerError, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, NullPointerException, SecurityException {
		return (Channel) object.getClass().getDeclaredMethod("getHandle").invoke(object);
	}
}
