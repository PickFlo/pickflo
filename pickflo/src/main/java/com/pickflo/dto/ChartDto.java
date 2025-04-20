package com.pickflo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class ChartDto {

	 private String userGroup; // A 그룹 또는 B 그룹
	 private String actionType; // 페이지 방문 또는 스크롤 횟수

}