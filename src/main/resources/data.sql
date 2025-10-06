-- ==============================
-- 出現エリア（重複しないように一度だけ）
-- ==============================
INSERT INTO areas (name) VALUES 
('川'), ('池'), ('海'), ('桟橋'), ('崖の上');

-- ==============================
-- 出現時間（重複を避けるため一度だけ）
-- ==============================
INSERT INTO appearance_times (start_time, end_time) VALUES
('00:00', '23:59'),   -- 終日
('09:00', '16:00'),   -- 9時～16時
('16:00', '23:59'),   -- 16時～24時
('00:00', '09:00');   -- 0時～9時

-- ==============================
-- 生き物
-- ==============================
INSERT INTO creatures (name, price) VALUES 
('タナゴ', 900),
('オイカワ', 200),
('フナ', 160),
('ウグイ', 200),
('コイ', 300);

-- ==============================
-- タナゴ
-- ==============================
-- 出現エリア
INSERT INTO creature_areas (creature_id, area_id) VALUES (1, 1); -- 川

-- 出現期間（北半球：11月～3月）
INSERT INTO appearance_periods (hemisphere, start_month, end_month) VALUES 
('北半球', 11, 12),
('北半球', 1, 3),
('南半球', 5, 9);
INSERT INTO creature_appearance_periods (creature_id, appearance_period_id) VALUES 
(1, 1), (1, 2), (1, 3);

-- 出現時間（9:00～16:00）
INSERT INTO creature_appearance_times (creature_id, appearance_time_id) VALUES 
(1, 2);

-- ==============================
-- オイカワ
-- ==============================
INSERT INTO creature_areas (creature_id, area_id) VALUES (2, 1); -- 川
INSERT INTO appearance_periods (hemisphere, start_month, end_month) VALUES 
('北半球', 3, 11),
('南半球', 9, 5);
INSERT INTO creature_appearance_periods (creature_id, appearance_period_id) VALUES 
(2, 4), (2, 5);
INSERT INTO creature_appearance_times (creature_id, appearance_time_id) VALUES 
(2, 2);

-- ==============================
-- フナ
-- ==============================
INSERT INTO creature_areas (creature_id, area_id) VALUES (3, 1); -- 川
INSERT INTO appearance_periods (hemisphere, start_month, end_month) VALUES 
('北半球', 1, 12),
('南半球', 1, 12);
INSERT INTO creature_appearance_periods (creature_id, appearance_period_id) VALUES 
(3, 6), (3, 7);
INSERT INTO creature_appearance_times (creature_id, appearance_time_id) VALUES 
(3, 1);

-- ==============================
-- ウグイ
-- ==============================
INSERT INTO creature_areas (creature_id, area_id) VALUES (4, 1); -- 川
INSERT INTO appearance_periods (hemisphere, start_month, end_month) VALUES 
('北半球', 11, 5),
('南半球', 5, 11);
INSERT INTO creature_appearance_periods (creature_id, appearance_period_id) VALUES 
(4, 8), (4, 9);
INSERT INTO creature_appearance_times (creature_id, appearance_time_id) VALUES 
(4, 1);

-- ==============================
-- コイ
-- ==============================
INSERT INTO creature_areas (creature_id, area_id) VALUES (5, 1); -- 川
INSERT INTO appearance_periods (hemisphere, start_month, end_month) VALUES 
('北半球', 1, 12),
('南半球', 1, 12);
INSERT INTO creature_appearance_periods (creature_id, appearance_period_id) VALUES 
(5, 10), (5, 11);
INSERT INTO creature_appearance_times (creature_id, appearance_time_id) VALUES 
(5, 1);

-- 認証テーブルへのダミーデータの追加
--password:adminpass
--INSERT INTO authentications (username, password, authority, displayname) VALUES 
--('admin','$2a$10$XuXBZhq1OzdxLYO6LWWUtuGOIjeGDVP6WXtDFjz72CGr5SvhYi7Hm', 'ADMIN', '管理太郎');

-- password:userpass
--INSERT INTO authentications (username, password, authority, displayname) VALUES 
--('user','$2a$10$bv1x471dce2aQ2Bq2mHq9OOjtz7n1Db2Bh4u/JpJy/xUP50/ye7zC', 'USER', '一般花子');