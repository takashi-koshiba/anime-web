package com.example.web.etc.db.progVector.select;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.web.etc.db.vector.VecDBSearvice;

@Transactional
@Service
public class ProgSelectSearvice extends VecDBSearvice {

    private final ProgSearchJDBC progJDBC;

    // ✅ コンストラクタインジェクション
    public ProgSelectSearvice(ProgSearchJDBC progJDBC) {
        super(progJDBC); // 親クラスの jdbc にも渡す
        this.progJDBC = progJDBC;
    }

    // ここで ProgSearvice 独自のSQLやロジックを追加可能
}
