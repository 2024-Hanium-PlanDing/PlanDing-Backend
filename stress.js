import http from 'k6/http';
import { check, sleep } from 'k6';
import stomp from 'k6/x/stomp';

const BASE_URL = 'http://localhost:8080';

export let options = {
    vus: 500,
    duration: '120s',
    thresholds: {
        http_req_failed: ['rate<0.01'],
        http_req_duration: ['p(95)<300'],
    },
};

// 유틸 함수: 랜덤 배열 선택
function getRandomElement(array) {
    return array[Math.floor(Math.random() * array.length)];
}

// API 호출에 사용될 헤더 세팅
function getHeaders(accessToken) {
    return {
        headers: {
            Authorization: `Bearer ${accessToken}`,
            'Content-Type': 'application/json',
        },
    };
}

// 로그인 후 액세스 토큰 추출 API
function loginAPI() {
    const userId = __VU;
    const email = `user${userId}@example.com`;
    const nickname = `user${userId}`;

    const payload = JSON.stringify({
        profileNickname: nickname,
        accountEmail: email,
        profileImage: 'image_url',
        socialId: 1,
    });

    const params = {
        headers: { 'Content-Type': 'application/json' },
    };

    const res = http.post(`${BASE_URL}/api/v1/login/android/kakao`, payload, params);
    const success = check(res, { 'login succeeded': (r) => r.status === 200 });
    if (!success) {
        console.error(`Login failed for user: ${email}`);
    }

    const jsonResponse = res.json();
    return {
        accessToken: jsonResponse.data.accessToken,
        userCode: jsonResponse.data.userCode, // userCode 추출
    };
}

// 로비 관련 API 호출
function favoriteAPI(accessToken) {
    const params = getHeaders(accessToken);
    const res = http.get(`${BASE_URL}/api/v1/favorite`, params);
    const success = check(res, { 'favorite succeeded': (r) => r.status === 200 });
    if (!success) {
        console.error('Favorite API failed');
    }
}

function groupPagingAPI(accessToken) {
    const params = getHeaders(accessToken);
    const res = http.get(`${BASE_URL}/api/v1/group/paging`, params);
    const success = check(res, { 'group succeeded': (r) => r.status === 200 });
    if (!success) {
        console.error('Group paging API failed');
    }
}

function todayScheduleAPI(accessToken) {
    const params = getHeaders(accessToken);
    const res = http.get(`${BASE_URL}/api/v1/common/schedule/today`, params);
    const success = check(res, { 'todaySchedule succeeded': (r) => r.status === 200 });
    if (!success) {
        console.error('Today schedule API failed');
    }
}

function invitationAPI(accessToken) {
    const params = getHeaders(accessToken);
    const res = http.get(`${BASE_URL}/api/v1/invitation`, params);
    const success = check(res, { 'invite succeeded': (r) => r.status === 200 });
    if (!success) {
        console.error('Invitation API failed');
    }
}

function notificationSchedulesAPI(accessToken) {
    const params = getHeaders(accessToken);
    const res = http.get(`${BASE_URL}/api/v1/notification/schedules`, params);
    const success = check(res, { 'schedule succeeded': (r) => r.status === 200 });
    if (!success) {
        console.error('Notification schedules API failed');
    }
}

// 개인 스케줄 관련 API
function fetchPersonalScheduleAPI(accessToken, startDate, endDate) {
    const params = getHeaders(accessToken);
    const res = http.get(`${BASE_URL}/api/v1/schedule/week/${startDate}/${endDate}`, params);
    const success = check(res, { 'schedule fetched': (r) => r.status === 200 });
    if (!success) {
        console.error('Failed to fetch personal schedule');
    }
}

function createPersonalScheduleAPI(accessToken, schedulePayload) {
    const params = getHeaders(accessToken);
    const res = http.post(`${BASE_URL}/api/v1/schedule`, schedulePayload, params);
    const success = check(res, { 'schedule created': (r) => r.status === 200 });
    if (!success) {
        console.error('Failed to create personal schedule');
    }
}

// 그룹 스케줄 관련 API
function fetchGroupPagingAPI(accessToken) {
    const params = getHeaders(accessToken);
    const res = http.get(`${BASE_URL}/api/v1/group/paging`, params);
    const success = check(res, { 'group succeeded': (r) => r.status === 200 });
    if (!success) {
        console.error('Failed to fetch group information');
    }
    return res.json().data[0].code;
}

function createGroupScheduleStomp(client, groupCode, title, content, scheduleDate, startTime, endTime) {
    client.send(
        `/pub/schedule/create/${groupCode}`,
        'application/json',
        JSON.stringify({
            title: title,
            content: content,
            scheduleDate: scheduleDate,
            startTime: startTime,
            endTime: endTime,
        })
    );
}

function createGroupPlannerStomp(client, groupCode, title, content, scheduleDate, userCode, scheduleId) {
    client.send(
        `/pub/planner/create/${groupCode}`,
        'application/json',
        JSON.stringify({
            title: title,
            content: content,
            deadline: scheduleDate,
            status: 'TODO',
            managerCode: userCode,
            userCodes: [],
            scheduleId: scheduleId,
        })
    );
}

function fetchGroupPlannerAPI(accessToken, groupCode, startDate, endDate) {
    const params = getHeaders(accessToken);
    const res = http.get(`${BASE_URL}/api/v1/group-rooms/planner/week/${groupCode}/${startDate}/${endDate}`, params);
    const success = check(res, { 'planner fetched': (r) => r.status === 200 });
    if (!success) {
        console.error('Failed to fetch group planner');
    }
}

function handleStompMessage(subscription) {
    const msg = subscription.read();
    if (!msg) {
        console.error('No message received.');
        return null;
    }

    console.log('Raw Message:', msg);

    try {
        const msgString = String.fromCharCode.apply(null, new Uint8Array(msg.body));
        console.log('Decoded Message String:', msgString);

        const msgJson = JSON.parse(msgString);
        console.log('Decoded Message JSON:', msgJson);

        if (msgJson && msgJson.data && msgJson.data.scheduleCommonResponse && msgJson.data.scheduleCommonResponse.id) {
            console.log('Schedule ID:', msgJson.data.scheduleCommonResponse.id);
            return msgJson.data.scheduleCommonResponse.id;
        } else {
            console.error('Expected property not found in message:', msgJson);
        }
    } catch (error) {
        console.error('Error decoding or parsing message:', error);
    }

    return null;
}

// STOMP 메시지 처리 핸들러 (로그 추가)
function connectStompAndHandleMessages(accessToken, userCode, groupCode) {
    const stompOptions = {
        addr: 'localhost:8080',
        protocol: 'ws',
        path: '/api/v1/ws',
        headers: {
            Authorization: `Bearer ${accessToken}`,
            groupCode: groupCode,
        },
        timeout: '5s',
        error: function (err) {
            console.error('STOMP Error:', err);
        },
    };

    const client = stomp.connect(stompOptions);
    const subscription = client.subscribe(`/sub/schedule/${groupCode}`);

    try {
        // 그룹 스케줄 생성 메시지 전송
        ['2024-09-20', '2024-09-27', '2024-09-28'].forEach((date, index) => {
            createGroupScheduleStomp(
                client,
                groupCode,
                `Test Event ${index + 1}`,
                'Test Content',
                date,
                Math.min(1 + index, 9),
                Math.min(10 + index, 23)
            );
        });

        // 메시지 수신 및 스케줄 ID 추출
        const scheduleId = handleStompMessage(subscription);
        if (!scheduleId) {
            console.error('Failed to handle STOMP message');
            return;
        }

        console.log('Schedule ID received:', scheduleId);

        // 그룹 플래너 생성 메시지 전송
        ['2024-10-20T00:00:00', '2024-10-07T00:00:00', '2024-09-28T00:00:00'].forEach((date, index) => {
            createGroupPlannerStomp(
                client,
                groupCode,
                `Test Event ${index + 1}`,
                'Test Content',
                date,
                userCode, // 로그인한 유저의 userCode 전달
                scheduleId // 플래너 생성 시 scheduleId 사용
            );
        });
    } catch (error) {
        console.error('Error during STOMP processing:', error);
    } 
}

// 테스트 시나리오 실행
export default function () {
    const { accessToken, userCode } = loginAPI(); // userCode도 함께 추출

    // 로비 관련 API 호출
    favoriteAPI(accessToken);
    groupPagingAPI(accessToken);
    todayScheduleAPI(accessToken);
    invitationAPI(accessToken);
    notificationSchedulesAPI(accessToken);

    sleep(1);

    // 개인 스케줄 관련 API 호출
    const startDate = getRandomElement(['2024-08-01', '2024-08-02', '2024-08-03']);
    const endDate = getRandomElement(['2024-09-01', '2024-09-02', '2024-09-03']);
    fetchPersonalScheduleAPI(accessToken, startDate, endDate);

    const schedulePayload = JSON.stringify({
        title: 'New Schedule',
        content: 'Team meeting',
        scheduleDate: '2024-09-28',
        startTime: '10',
        endTime: '12',
    });
    createPersonalScheduleAPI(accessToken, schedulePayload);

    sleep(3);

    // 그룹 스케줄 관련 API 호출
    const groupCode = fetchGroupPagingAPI(accessToken);
    connectStompAndHandleMessages(accessToken, userCode, groupCode);

    fetchGroupPlannerAPI(accessToken, groupCode, startDate, endDate);
}
