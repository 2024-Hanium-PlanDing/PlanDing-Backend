package com.tukorea.planding.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * 잘못된 URL Error
     */

    INVALID_URL("CLIENT-001", "잘못된 URL 입니다.", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST("CLIENT-002", "잘못된 형식 입니다.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("SERVER-001", "서버 문제 입니다.", HttpStatus.INTERNAL_SERVER_ERROR),


    /**
     * JWT Error
     */
    INVALID_AUTH_TOKEN("JWT-001", "토큰이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_AUTH_TOKEN("JWT-002", "만료된 토큰 입니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_REFRESH_TOKEN("JWT-002", "만료된 리프레시토큰 입니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_REFRESH_TOKEN_ERROR("JWT-003", "아직 액세스 토큰을 재발급할 수 없습니다.", HttpStatus.UNAUTHORIZED),

    /**
     * User Error
     */
    USER_NOT_FOUND("USER-001", "유저가 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    /**
     * Schedule Error
     */
    SCHEDULE_NOT_FOUND("SCHEDULE-001", "해당 스케줄은 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    UNAUTHORIZED_SCHEDULE("SCHEDULE-002", "본 사용자는 스케줄에 권한이 없습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_SCHEDULE_TIME("SCHEDULE-003", "스케줄 시간을 확인해 주세요.", HttpStatus.BAD_REQUEST),

    /**
     * Group Error
     */
    ACCESS_DENIED("GROUP-001", "본 사용자는 이 그룹에 접근할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    USER_NOT_INVITABLE("GROUP-002", "초대할 유저를 찾지 못하였습니다.", HttpStatus.NOT_FOUND),
    GROUP_ROOM_NOT_FOUND("GROUP-003", "그룹룸이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    UNAUTHORIZED_GROUP_ROOM_INVITATION("GROUP-004", "그룹룸에 초대할 권한이 없습니다.", HttpStatus.UNAUTHORIZED),
    USER_ALREADY_INVITED("GROUP-005", "이미 그룹룸에 초대되었습니다.", HttpStatus.UNAUTHORIZED),
    FAVORITE_ALREADY_ADD("GROUP-006", "이미 즐겨찾기에 등록하였습니다.", HttpStatus.UNAUTHORIZED),
    FAVORITE_ALREADY_DELETE("GROUP-007", "즐겨찾기에 등록된 그룹이 아닙니다.", HttpStatus.UNAUTHORIZED),
    USER_ALREADY_IN_GROUP("GROUP-008", "이미 그룹에 존재하는 유저입니다.", HttpStatus.CONFLICT),
    FILE_UPLOAD_ERROR("GROUP-009", "썸네일 업로드중 오류가 발생했습니다.", HttpStatus.BAD_REQUEST),

    /**
     * Invite Error
     */
    INVITATION_ALREADY_SENT("INVITATION-001", "이미 초대한 사용자입니다.", HttpStatus.CONFLICT),
    CANNOT_INVITE_YOURSELF("INVITATION-002", "자기 자신은 초대를 할 수 없습니다.", HttpStatus.CONFLICT),
    NOTEXIST_INVITE("INVITATION-004", "초대받은 적이 없습니다.", HttpStatus.NOT_FOUND),

    /**
     * Group-Schedule-ATTENDANCE Error
     */
    INVALID_ATTENDANCE_STATUS("ATTENDANCE-001", "올바르지 않은 상태값입니다.", HttpStatus.NOT_FOUND),

    /**
     * User Alert Setting
     */
    SETTING_NOT_FOUND("NOTIFICATION-001", "설정을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    /**
     * Alarm
     */
    ALARM_NOT_FOUND("ALARM-001", "알람값이 존재하지 않습니다.", HttpStatus.NOT_FOUND);

    private final String errorCode;
    private final String message;
    private final HttpStatus status;
}
