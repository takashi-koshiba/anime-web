package com.example.web.etc.db.animeVector.select;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.web.etc.db.vector.VecDBSearvice;

@Transactional
@Service
public class AnimeSelectSearvice extends VecDBSearvice<AnimeVecDB> {

    private final AnimeSearchJDBC progJDBC;

    public AnimeSelectSearvice(AnimeSearchJDBC progJDBC) {
        super(progJDBC);
        this.progJDBC = progJDBC;
    }

}
