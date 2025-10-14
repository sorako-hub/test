-- ====== 依存関係の順にテーブル削除（存在すれば削除） ======
DROP TABLE IF EXISTS creature_appearance_times;
DROP TABLE IF EXISTS creature_appearance_periods;
DROP TABLE IF EXISTS creature_areas;
DROP TABLE IF EXISTS appearance_times;
DROP TABLE IF EXISTS appearance_periods;
DROP TABLE IF EXISTS areas;
DROP TABLE IF EXISTS creatures;
--DROP TABLE IF EXISTS authentications;
--DROP TYPE IF EXISTS role;

-- ====== 出現場所 ======
CREATE TABLE areas (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- ====== 出現期間 ======
CREATE TABLE appearance_periods (
    id SERIAL PRIMARY KEY,
    hemisphere VARCHAR(20) NOT NULL,
    start_month INT NOT NULL,
    end_month INT NOT NULL,
    CONSTRAINT uq_appearance_period UNIQUE (hemisphere, start_month, end_month)
);

-- ====== 生き物 ======
CREATE TABLE creatures (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    price INT NOT NULL,
    type VARCHAR(50) NOT NULL  -- 魚・虫・海の幸を区別
);

-- ====== 生き物 × 出現場所 ======
CREATE TABLE creature_areas (
    id SERIAL PRIMARY KEY,
    creature_id INT NOT NULL,
    area_id INT NOT NULL,
    CONSTRAINT uq_creature_area UNIQUE (creature_id, area_id),
    FOREIGN KEY (creature_id) REFERENCES creatures(id),
    FOREIGN KEY (area_id) REFERENCES areas(id)
);

-- ====== 生き物 × 出現期間 ======
CREATE TABLE creature_appearance_periods (
    id SERIAL PRIMARY KEY,
    creature_id INT NOT NULL,
    appearance_period_id INT NOT NULL,
    CONSTRAINT uq_creature_period UNIQUE (creature_id, appearance_period_id),
    FOREIGN KEY (creature_id) REFERENCES creatures(id),
    FOREIGN KEY (appearance_period_id) REFERENCES appearance_periods(id)
);

-- ====== 出現時間 ======
CREATE TABLE appearance_times (
    id SERIAL PRIMARY KEY,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL
);

-- ====== 生き物 × 出現時間 ======
CREATE TABLE creature_appearance_times (
    id SERIAL PRIMARY KEY,
    creature_id INT NOT NULL,
    appearance_time_id INT NOT NULL,
    CONSTRAINT uq_creature_time UNIQUE (creature_id, appearance_time_id),
    FOREIGN KEY (creature_id) REFERENCES creatures(id),
    FOREIGN KEY (appearance_time_id) REFERENCES appearance_times(id)
);

-- 権限用のENUM型
--CREATE TYPE role AS ENUM ('ADMIN', 'USER');

-- 認証情報を格納するテーブル
--CREATE TABLE authentications (
	-- ユーザー名：主キー
	--username VARCHAR(50) PRIMARY KEY,
	-- パスワード
	--password VARCHAR(255) NOT NULL,
	-- 権限
	--authority role NOT NULL,
	-- 表示名
	--displayname VARCHAR(50) NOT NULL);












































