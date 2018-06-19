/*
 * Copyright 2018 Alex Thomson
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

package io.github.lxgaming.bungeepacket;

import io.netty.channel.Channel;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.netty.ChannelWrapper;

import java.lang.reflect.Field;
import java.util.Optional;

public final class BungeePacket {
    
    public static Optional<Channel> getChannel(PendingConnection pendingConnection) {
        return getChannelWrapper(pendingConnection.getClass(), pendingConnection).map(ChannelWrapper::getHandle);
    }
    
    public static Optional<Channel> getChannel(ProxiedPlayer proxiedPlayer) {
        return getChannelWrapper(proxiedPlayer.getClass(), proxiedPlayer).map(ChannelWrapper::getHandle);
    }
    
    public static boolean isRegistered(Channel channel, String name) {
        return channel.pipeline().names().contains(name);
    }
    
    public static <T> Optional<ChannelWrapper> getChannelWrapper(Class<? extends T> classOfT, T instance) {
        try {
            for (Field field : classOfT.getDeclaredFields()) {
                if (!ChannelWrapper.class.isAssignableFrom(field.getType())) {
                    continue;
                }
                
                field.setAccessible(true);
                Object channelWrapper = field.get(instance);
                if (channelWrapper != null) {
                    return Optional.of((ChannelWrapper) channelWrapper);
                }
                
                return Optional.empty();
            }
            
            throw new NoSuchFieldException();
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}