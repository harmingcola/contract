package org.seekay.contract.server

class Session {

    private static final ThreadLocal<Map<String, Object>> CONTEXT = new ThreadLocal<HashMap<String, Object>>()

    public Object getObjectByKey(String key) {
        Map<String, Object> map = CONTEXT.get()
        if(map == null) {
            return null
        }
        return map.get(key)
    }

    public void setKeyValue(String key, Object value) {
        Map<String, Object> map = CONTEXT.get()
        if(map == null) {
            map = new HashMap<String, Object>()
        }
        map.put(key, value)
        CONTEXT.set(map)
    }

}
