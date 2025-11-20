package com.example.web.etc.db.vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Vec {
    private List<Map<String, Object>> values = new ArrayList<>();

    // コンストラクタ（最初の1件を入れたいとき用）
    public Vec(int index, double value) {
        addValue(index, value);
    }

    // 引数なしコンストラクタ（空のベクトルを作るとき）
    public Vec() {
    }

    // 値を追加する
    public void addValue(int index, double value) {
        Map<String, Object> map = new HashMap<>();
        map.put("index", index);
        map.put("value", value);
        values.add(map);
    }

    public List<Map<String, Object>> getValues() {
        return values;
    }

    public int size() {
        return values.size();
    }
}