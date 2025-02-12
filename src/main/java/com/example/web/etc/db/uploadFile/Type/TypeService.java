package com.example.web.etc.db.uploadFile.Type;



import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public class TypeService {
    private final TypeJDBC typeJDBC;

    // コンストラクタインジェクション
 
    public TypeService(TypeJDBC typeJDBC) {
        this.typeJDBC = typeJDBC;
    }

    public List<Type> selectAll(){
    	return typeJDBC.selectAll();
    }
    

}
