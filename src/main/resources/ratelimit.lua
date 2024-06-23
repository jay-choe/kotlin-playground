local key = KEYS[1]
local limit = tonumber(ARGV[1])
local ttl = tonumber(ARGV[2])

local current = tonumber(redis.call('get', key) or '0')

if current >= limit then
    return redis.error_reply("Value exceeds limit")
else
    redis.call('INCR', key)
    redis.call('EXPIRE', key, ttl)
    return current + 1
end
