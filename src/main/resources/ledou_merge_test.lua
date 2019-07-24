
--[[
-- 准备数据
--  hset 1011168406 2019 100
-- ]]

-- KEYS[1] 1011168406
-- ARGV[1] 2018
-- ARGV[2] 2019

-- 测试如果 2018年是nil 2019年有值，

local table = redis.call('hmget', KEYS[1], ARGV[1], ARGV[2])



local lastYearNum = tonumber(table[1]);

local thisYearNum = tonumber(table[2]);

-- return lastYearNum  -- 能接受到返回值， 是null

-- return lastYearNum .. thisYearNum -- 报错 attempt to concatenate local 'lastYearNum' (a nil value)

--return type(table[1]) .. type(table[2]) --booleanstring