package com.example.web.etc.db.progVector.select;

import org.springframework.stereotype.Component;

import com.example.web.etc.sta.StrVector;

@Component
public class ProgStrVector extends StrVector {

    public ProgStrVector(ProgSelectSearvice vec) {
        super(vec);
        
    }
}
