package com.atsu.tabletennisreservation.webSocket;

import com.atsu.tabletennisreservation.pojo.User;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.List;
import java.util.Map;

//webSocket建立连接的校验配置
public class MyEndpointConfigurator extends ServerEndpointConfig.Configurator{
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        //获取userId
        Map<String, List<String>> parameterMap = request.getParameterMap();
        List<String> list = parameterMap.get("userId");
        String userId = list.get(0);
        // 进行登录校验逻辑，如果校验不通过，则抛出异常
        if (!validate(request)) {
            throw new IllegalArgumentException("用户["+userId+"]未登录，无法建立长连接");
        }
        // 如果校验通过，则继续进行握手
        super.modifyHandshake(config, request, response);
    }

    /**
     * 校验请求是否合法（判断当前请求的长连接user_id是否是当前登录用户的）
     */
    private boolean validate(HandshakeRequest request) {
        //获取userId
        Map<String, List<String>> parameterMap = request.getParameterMap();
        List<String> list = parameterMap.get("userId");
        String userId = list.get(0);
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        User user = (User) httpSession.getAttribute("user");
        if (user.getGuid().equals(userId))
            return true;
        return false;
    }
}
