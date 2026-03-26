import http from 'k6/http';
import {check} from 'k6';
import {Rate, Trend} from 'k6/metrics';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const HASH_KEY = __ENV.HASH_KEY || '98q1iSNUWLubrwD/wSFFFixTy4E=';
const VUS = Number(__ENV.VUS) || 10;

export let options = {
    vus: VUS,
    duration: __ENV.DURATION || '30s',
};

const reissueLatency = new Trend('reissue_latency_ms');
const reissueErrorRate = new Rate('reissue_error_rate');

export function setup() {
    // 1. serverToken 발급
    const tokenRes = http.post(
        `${BASE_URL}/auth/token`,
        JSON.stringify({identifier: HASH_KEY}),
        {headers: {'Content-Type': 'application/json'}}
    );
    const serverToken = tokenRes.json('accessToken');

    // 2. VU마다 다른 username으로 로그인 → refreshToken 발급
    const refreshTokens = [];
    for (let i = 1; i <= VUS; i++) {
        const loginRes = http.post(
            `${BASE_URL}/auth/login`,
            JSON.stringify({username: `k6test@${i}`}),
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${serverToken}`,
                },
            }
        );
        refreshTokens.push(loginRes.json('refreshToken'));
    }

    return {refreshTokens};
}

export default function (data) {
    let refreshToken = data.refreshTokens[__VU - 1];

    const res = http.post(`${BASE_URL}/auth/reissue`, null, {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${refreshToken}`,
        },
    });

    const success = check(res, {
        'status 200': (r) => r.status === 200,
        'has refreshToken': (r) => r.json('refreshToken') !== undefined,
    });

    reissueErrorRate.add(!success);
    reissueLatency.add(res.timings.duration);

    // 다음 iteration에 새 refreshToken 사용
    if (success) {
        data.refreshTokens[__VU - 1] = res.json('refreshToken');
    }
}