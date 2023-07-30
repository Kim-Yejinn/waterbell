package com.ssafy.fcc.controller;

import com.ssafy.fcc.domain.alarm.Step;
import com.ssafy.fcc.domain.member.ApartMember;
import com.ssafy.fcc.handler.MyWebSocketHandler;
import com.ssafy.fcc.service.ApartManagerService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Api("알림")
public class NotificationController {

    private final MyWebSocketHandler myWebSocketHandler;
    private final ApartManagerService apartManagerService;
    private final Logger logger = LoggerFactory.getLogger(VerificationController.class);

    //테스트용
    @PostMapping("/send-notification")
    public ResponseEntity<String> sendNotification(@RequestParam String message, @RequestParam String userId) throws IOException {
        myWebSocketHandler.sendNotificationToSpecificUser(userId, message);
        return new ResponseEntity<>("Notification sent successfully", HttpStatus.OK);
    }

    //차수판 가동시 알림
    @PostMapping("/notification/activation")
    public ResponseEntity<Map<String, String>> sendActivationNotification(/**@RequestHeader("X-AUTH-TOKEN") String token /이건 2안 **/) throws IOException {
        Map<String, String> resultMap = new HashMap<>();
        HttpStatus status = null;
        // 스프링 시큐리티를 통해 관리자 member_id를 획득(가능한지 몰루?)
//        int member_id = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        int member_id = 2; // 테스트용
        try {
            // 관리자 건물의 입주민들 웹 알림
            apartManagerService.sendFloodWebNotification(member_id, Step.ACTIVATION);
            // 문자 알림
            apartManagerService.sendSmsWebNotification(member_id, Step.ACTIVATION);
            resultMap.put("message", "전송완료");
            status = HttpStatus.OK;
        } catch (Exception e){
            logger.error("알림 전송 실패 : {}", e);
            resultMap.put("message", "전송 실패");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, status);
    }
    //차수판 해제시 알림
    @PostMapping("/notification/deactivation")
    public ResponseEntity<Map<String, String>> sendDeactivationNotification(/**@RequestHeader("X-AUTH-TOKEN") String token /이건 2안 **/) throws IOException {
        Map<String, String> resultMap = new HashMap<>();
        HttpStatus status = null;
        // 스프링 시큐리티를 통해 관리자 member_id를 획득(가능한지 몰루?)
//        int member_id = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        int member_id = 2; // 테스트용
        try {
            // 관리자 건물의 입주민들 웹 알림
            apartManagerService.sendFloodWebNotification(member_id, Step.DEACTIVATION);
            // 문자 알림
            apartManagerService.sendSmsWebNotification(member_id, Step.DEACTIVATION);
            resultMap.put("message", "전송완료");
            status = HttpStatus.OK;
        } catch (Exception e){
            logger.error("알림 전송 실패 : {}", e);
            resultMap.put("message", "전송 실패");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, status);
    }
}

