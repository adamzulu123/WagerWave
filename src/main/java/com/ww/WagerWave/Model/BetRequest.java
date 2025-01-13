package com.ww.WagerWave.Model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class BetRequest {
    private String type;  // SINGLE lub COMBO
    private List<BetData> bets;
    private CouponData couponData;

    @Data
    public static class BetData {
        private Integer eventID;
        private String betTeam;
        private String betType;  // TEAM_1, TEAM_2, DRAW
        private Double odds;
        private Double stake;
        private Double potentialWin;
        private String betEndTime;
    }

    @Data
    public static class CouponData {
        private Double odds;
        private Double stake;
        private Double potentialWin;
        private String endTime;
    }
}