create database IF NOT EXISTS roommate;
use roommate;
-- 1. 기본 옵션들 삽입
INSERT INTO options (id, category, name)
VALUES
    -- 취침시간
    (1, 'BED_TIME', 'NO_PREF'),
    (101, 'BED_TIME', 'AT_22'),
    (102, 'BED_TIME', 'AT_23'),
    (103, 'BED_TIME', 'AT_00'),
    (104, 'BED_TIME', 'AT_01'),
    (105, 'BED_TIME', 'AT_02'),
    (106, 'BED_TIME', 'AT_03'),

    -- 기상시간
    (2, 'WAKEUP_TIME', 'NO_PREF'),
    (201, 'WAKEUP_TIME', 'AT_06'),
    (202, 'WAKEUP_TIME', 'AT_07'),
    (203, 'WAKEUP_TIME', 'AT_08'),
    (204, 'WAKEUP_TIME', 'AT_09'),
    (205, 'WAKEUP_TIME', 'AT_10'),
    (206, 'WAKEUP_TIME', 'AT_11'),

    -- 난방 설정
    (3, 'HEATING', 'NO_PREF'),
    (301, 'HEATING', 'BELOW_20'),
    (302, 'HEATING', 'FROM_21_TO_23'),
    (303, 'HEATING', 'FROM_24_TO_26'),
    (304, 'HEATING', 'ABOVE_27'),

    -- 냉방 설정
    (4, 'COOLING', 'NO_PREF'),
    (401, 'COOLING', 'BELOW_20'),
    (402, 'COOLING', 'FROM_21_TO_23'),
    (403, 'COOLING', 'FROM_24_TO_26'),
    (404, 'COOLING', 'ABOVE_27'),

    -- 잠버릇
    (5, 'SLEEP_HABIT', 'NO_PREF'),
    (501, 'SLEEP_HABIT', 'YES'),
    (502, 'SLEEP_HABIT', 'NO'),

    -- 흡연 여부
    (6, 'SMOKING', 'NO_PREF'),
    (601, 'SMOKING', 'NON_SMOKER'),
    (602, 'SMOKING', 'SMOKER'),

    -- 소음 민감도
    (7, 'NOISE', 'NO_PREF'),
    (701, 'NOISE', 'EARPHONES'),
    (702, 'NOISE', 'FLEXIBLE'),
    (703, 'NOISE', 'SPEAKERS'),

    -- 실내 통화
    (8, 'INDOOR_CALL', 'NO_PREF'),
    (801, 'INDOOR_CALL', 'BAN'),
    (802, 'INDOOR_CALL', 'SIMPLE'),
    (803, 'INDOOR_CALL', 'FREE'),

    -- 실내 취식
    (9, 'EATING', 'NO_PREF'),
    (901, 'EATING', 'BAN'),
    (902, 'EATING', 'SNACK'),
    (903, 'EATING', 'FOOD'),

    -- 음주 빈도
    (10, 'DRINKING', 'NO_PREF'),
    (1001, 'DRINKING', 'NON_DRINKER'),
    (1002, 'DRINKING', 'OCCASIONAL'),
    (1003, 'DRINKING', 'FREQUENT'),

    -- 향 민감도
    (11, 'SCENT', 'NO_PREF'),
    (1101, 'SCENT', 'SENSITIVE'),
    (1102, 'SCENT', 'NORMAL'),
    (1103, 'SCENT', 'NOT_SENSITIVE'),

    -- 청소 방식
    (12, 'CLEANING', 'NO_PREF'),
    (1201, 'CLEANING', 'INDIVIDUAL'),
    (1202, 'CLEANING', 'ROTATION'),
    (1203, 'CLEANING', 'TOGETHER'),

    -- 룸메 관계
    (13, 'RELATIONSHIP', 'NO_PREF'),
    (1301, 'RELATIONSHIP', 'INVISIBLE'),
    (1302, 'RELATIONSHIP', 'NORMAL'),
    (1303, 'RELATIONSHIP', 'CLOSE');

-- 2. 연령(AGE) 옵션 삽입 (현재 연도 2025 기준, 18세부터 9단계)
INSERT INTO option (id, category, name)
VALUES (2007, 'AGE', '2007'),
       (2006, 'AGE', '2006'),
       (2005, 'AGE', '2005'),
       (2004, 'AGE', '2004'),
       (2003, 'AGE', '2003'),
       (2002, 'AGE', '2002'),
       (2001, 'AGE', '2001'),
       (2000, 'AGE', '2000'),
       (1999, 'AGE', '1999'),
       (0, 'AGE', 'NO_PREF');


--회원 2만명 생성
SET SESSION cte_max_recursion_depth = 20000;
INSERT INTO member (nickname, dorm, gender, year)
WITH RECURSIVE numbers AS (SELECT 1 AS n
                           UNION ALL
                           SELECT n + 1
                           FROM numbers
                           WHERE n <= 20000)
SELECT CONCAT('user', LPAD(n, 6, '0')),
       CASE MOD(n, 11)
           WHEN 0 THEN 'JINLI'
           WHEN 1 THEN 'JEONGUI'
           WHEN 2 THEN 'GAECHUCK'
           WHEN 3 THEN 'GYEYANG'
           WHEN 4 THEN 'SINMIN'
           WHEN 5 THEN 'JISUN'
           WHEN 6 THEN 'MYEONGDEOK'
           WHEN 7 THEN 'INUI'
           WHEN 8 THEN 'YEJI'
           WHEN 9 THEN 'YANGHYEON'
           WHEN 10 THEN 'YANGHYEON'
        END AS dorm,

       CASE MOD(n, 11)
           WHEN 0 THEN 'MALE'
           WHEN 1 THEN 'MALE'
           WHEN 2 THEN 'FEMALE'
           WHEN 3 THEN 'FEMALE'
           WHEN 4 THEN 'MALE'
           WHEN 5 THEN 'FEMALE'
           WHEN 6 THEN 'MALE'
           WHEN 7 THEN 'MALE'
           WHEN 8 THEN 'FEMALE'
           WHEN 9 THEN 'MALE'
           WHEN 10 THEN 'MALE'
        END AS gender,

    CASE MOD(n, 9)
        WHEN 0 THEN '2007'
        WHEN 1 THEN '2006'
        WHEN 2 THEN '2005'
        WHEN 3 THEN '2004'
        WHEN 4 THEN '2003'
        WHEN 5 THEN '2002'
        WHEN 6 THEN '2001'
        WHEN 7 THEN '2000'
        WHEN 8 THEN '1999'
    END AS year
FROM numbers;