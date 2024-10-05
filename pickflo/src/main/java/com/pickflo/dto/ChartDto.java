package com.pickflo.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartDto {


	//private Long userId;
    private String userGroup;    
    private String actionType;    
    private LocalDate statsDate;
    private Long eventCount;
    /*private long totalScrolls;    
    private long totalLikes;     
    private long totalUnlikes; */
    //private long eventCount; // 주별 이벤트 횟수
   // private int week; 
    

}
