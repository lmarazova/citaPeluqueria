package com.example.citaPeluqueria.domain.enums;

import com.example.citaPeluqueria.util.Constants;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum HairService {
        CUT(Constants.CUT),
        COLOR(Constants.COLOR),
        HIGHLIGHTS(Constants.HIGHLIGHTS),
        WASH(Constants.WASH),
        BLOWDRY(Constants.BLOWDRY),
        STRAIGHTEN(Constants.STRAIGHTEN),
        CURL(Constants.CURL),
        MASK(Constants.MASK),
        HYDRATION(Constants.HYDRATION),
        CUSTOM(Constants.CUSTOM);

        private final String label;

        HairService(String label) {
                this.label = label;
        }



}
