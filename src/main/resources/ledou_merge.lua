
-- KEYS[1] uid
-- KEYS[2] lid

-- ARGV[1] lastYear
-- ARGV[2] thisYear

local LY = ARGV[1];
local TY = ARGV[2];

-- 先获取到 绑定在 uid lid 上近两年的乐豆数量
local uidNum = redis.call('hmget', KEYS[1], ARGV[1], ARGV[2])
local lidNum = redis.call('hmget', KEYS[2], ARGV[1], ARGV[2])

-- return tostring(type(uidNum)); -- table
-- return tostring(next(uidNum)) -- 1
-- return tostring(uidNum[1]) -- false
-- return tostring(uidNum) -- 0x25b93d0

-- 近两年 没有绑定在 uid上的乐豆 则无需合并 返回去年 存在 lid中的乐豆数量
if(
( uidNum[1] == false and uidNum[2] == false )
        or ( (tonumber(uidNum[1]) == 0) and (tonumber(uidNum[2]) == 0) )
        or ( (tonumber(uidNum[1]) == 0) and uidNum[2] == false )
        or ( (tonumber(uidNum[2]) == 0) and uidNum[1] == false )
)
then
    local lyl -- lastYearLenovoId 绑定的乐豆数量
    if(lidNum[1] and tonumber(lidNum[1]) ~= 0)
    then
        lyl = tonumber(lidNum[1])
    else
        lyl = 0
    end

    local tyl
    if(lidNum[2] and tonumber(lidNum[2]) ~= 0)
    then
        tyl = tonumber(lidNum[2])
    else
        tyl = 0
    end

    redis.call('del', KEYS[1]);

    return "{" ..
            "\"status\":\"0\","..
            "\"redis\":{"..
            "\"lyn\":"..lyl.. -- lyn lastYearNow 合并后redis中去年的乐豆数量 无需合并所以返回原 lid中的乐豆数量
            "\"tyn\":"..tyl.. -- tyn thisYearNow 合并后redis中今年的乐豆数量
            "}"..
            "}"; -- uid上没有乐豆数据， 无需合并，直接将数据库中的 uid绑定的乐豆数据删除，判断一下数据库中的lid数据与redis中的数据是否一致，如果不一致，以redis中的数据为准
end

-- 合并去年的乐豆数据
local lyMerge = 0
local lyu = 0
local lyl = 0
if( (uidNum[1] and tonumber(uidNum[1]) ~= 0) ) -- 去年 uid上有绑定的乐豆
then
    lyu = tonumber(uidNum[1]);
    if( (lidNum[1] and tonumber(lidNum[1]) ~= 0) ) -- 去年 lid上的乐豆数据不为空，更新 为 uid 和 lid 合并后的值
    then
        lyl = tonumber(lidNum[1]);
        lyMerge = lyl + lyu;
    else -- 不管去年 lid上的乐豆数据是否为空 或是 0 ，则更新为 uid的乐豆值
        lyMerge = lyu;
    end
    redis.call('hset', KEYS[2], LY, tostring(lyMerge));
else
    if( (lidNum[1] and tonumber(lidNum[1]) ~= 0) )
    then
        lyl = tonumber(lidNum[1]);
        lyMerge = lyl
    end
end

-- 合并今年的乐豆数据
local tyMerge = 0
local tyu = 0
local tyl = 0
if( (uidNum[2] and tonumber(uidNum[2]) ~= 0) )-- 今年 uid上有绑定的乐豆
then
    tyu = tonumber(uidNum[2]);
    if( (lidNum[2] and tonumber(lidNum[2]) ~= 0) ) -- 今年 lid上的乐豆数据不为空，更新 为 uid 和 lid 合并后的值
    then
        tyl = tonumber(lidNum[2]);
        tyMerge = tyl + tyu;
    else -- 不管去年 lid上的乐豆数据是否为空 或是 0 ，则更新为 uid的乐豆值
        tyMerge = tyu;
    end
    redis.call('hset', KEYS[2], TY, tostring(tyMerge));
else
    if( (lidNum[2] and tonumber(lidNum[2]) ~= 0) )
    then
        tyl = tonumber(lidNum[2]);
        tyMerge = tyl
    end
end

redis.call('del', KEYS[1]);

return "{"..
        "\"status\":\"1\","..
        "\"lidRedis\":{"..
        "\"lyn\":"..lyMerge..
        "\"tyn\":"..tyMerge..
        "},"..
        "\"redisLog\":{"..
        "\"lyu\":"..lyu..
        "\"tyu\":"..tyu..
        "\"bfmg\":".. (lyl + tyl) ..
        "\"afmg\":".. (lyMerge + tyMerge) ..
        "}"..
        "}" -- 按照返回的数据更新数据库（将 uid乐豆删除，更新 lid乐豆数据）

