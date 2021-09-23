package com.example.algoStudy2.controller;

import com.example.algoStudy2.dto.ChatMessage;
import com.example.algoStudy2.dto.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

    @Component
// 스프링 빈이라고 표시하는 역할을 함 => 이 클래스를 어플리케이션 컨텍스트에 빈으로 등록
    public class WebSocketEventListener {//event listener를 이용하여 소켓연결 ,소켓연결끊기 이벤트 수신
        private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

        @Autowired
        private SimpMessageSendingOperations messagingTemplate;

        @EventListener
        public void handleWebSocketConnectListener(SessionConnectedEvent event){
            logger.info("새로운 웹 소켓연결을 받음!");
        }

        @EventListener
        public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

            String username = (String) headerAccessor.getSessionAttributes().get("username");
            if(username != null){
                logger.info("연결이 해제된 유저 : "+ username);

                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setType(MessageType.LEAVE);
                chatMessage.setSender(username);
                //사용자 퇴장 이벤트를 broadcast함
                messagingTemplate.convertAndSend("/topic/public",chatMessage);
            }
        }
    }


