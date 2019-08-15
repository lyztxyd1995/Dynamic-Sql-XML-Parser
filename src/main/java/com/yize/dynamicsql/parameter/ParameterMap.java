package com.yize.dynamicsql.parameter;
import java.util.HashMap;
import java.util.Map;

public class ParameterMap {

    private Map<String, String> map;

    private ParameterList parameterList;

    public ParameterMap(){
        this.map = new HashMap<>();
        this.parameterList = new ParameterList();
    }

    public void put(int index, String value){
        this.parameterList.put(index, value);
    }

    public void put(String key, String value) {
        this.map.put(key, value);
    }

    public String get(String key) {
        return map.get(key);
    }

    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public String poll() {
        return parameterList.poll();
    }

    public String peek() {
        return parameterList.peek();
    }
}
