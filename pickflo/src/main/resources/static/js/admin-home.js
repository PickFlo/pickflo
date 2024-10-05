/**
 * chart.html
 */
document.addEventListener("DOMContentLoaded", function() {
	fetchUserData();

	document.getElementById('periodStatsButton').onclick = function() {
		alert('@@@@@@@@@@@@@@22');
	    const startDate = document.getElementById('startDate').value;
	    const endDate = document.getElementById('endDate').value;
	   
	    periodStatistics(startDate, endDate);
	};

	function periodStatistics(startDate, endDate) {
		if (startDate && endDate) {
			// 서버에서 통계 데이터를 가져옴
			axios.get(`/api/chart/getDateData`, {
				params: {
					startDate: startDate,
					endDate: endDate,
				}
			})
				.then(response => {
					const data = response.data;
					// 데이터를 차트에 반영
					updateChartsWithData(data);
				})
				.catch(error => {
					console.error('통계 데이터를 가져오는 중 오류 발생:', error);
				});
		} else {
			alert("시작일, 종료일, 사용자 그룹을 모두 선택해 주세요.");
		}
	}

	function updateChartsWithData(data) {
		// 통합된 차트로 그리기
		drawChart(data.userStatistics);
	}
});

function updateChartsWithData(data) {
	drawChart(data.userStatistics);
}


function fetchUserData() {
	fetch('/pickflo/api/chart/getUserData') // 데이터 API를 호출합니다.
		.then(response => {
			if (!response.ok) {
				throw new Error('Network response was not ok');
			}
			return response.json();
		})
		.then(data => {
			// 데이터를 가져와서 차트를 그립니다.
			drawChart(data);
		})
		.catch(error => {
			console.error('Error fetching user data:', error);
		});
}


function drawChart(userStatistics) {
	// 각 그룹의 페이지 방문 및 스크롤 횟수 계산
	const groups = userStatistics.reduce((acc, stat) => {
		if (!acc[stat.userGroup]) {
			acc[stat.userGroup] = { stayTime: 0, scrollCount: 0, likeCount: 0, unlikeCount: 0 };
		}
		acc[stat.userGroup].stayTime += stat.stayTime;
		acc[stat.userGroup].scrollCount += stat.scrollCount;
		acc[stat.userGroup].likeCount += stat.likeCount;
		acc[stat.userGroup].unlikeCount += stat.unlikeCount;
		return acc;
	}, {});

	// 차트 데이터를 준비합니다.
	const labels = Object.keys(groups);
	const datasets = createDatasets(labels, groups);
	function createDatasets(labels, groups) {
		return [
			createDataset('페이지 체류시간', labels.map(group => groups[group].stayTime)),
			createDataset('스크롤 횟수', labels.map(group => groups[group].scrollCount)),
			createDataset('좋아요 이벤트 수', labels.map(group => groups[group].likeCount)),
			createDataset('싫어요 이벤트 수', labels.map(group => groups[group].unlikeCount))
		];
	}

	// 차트 생성
	createCharts(labels, datasets);
}

// 차트 색상 정의
const CHART_COLORS = {
	background: 'rgba(255, 115, 102, 0.2)',
	border: 'rgba(0, 0, 0, 0)',
	groupColors: ['rgb(60, 179, 113)', 'rgb(255, 165, 0)']
};

function createDataset(label, data) {
	return {
		label: label,
		data: data,
		backgroundColor: CHART_COLORS.groupColors,
		borderColor: CHART_COLORS.border,
		borderWidth: 1
	};
}

function createCharts(labels, datasets) {
	const chartIds = ['userStatsChart', 'scrollCountChart', 'likeEventChart', 'unlikeEventChart'];
	const chartTitles = ['페이지 체류 시간', '스크롤 횟수', '좋아요 이벤트 수', '싫어요 이벤트 수']; // 각 차트의 제목
	const orderedLabels = ['A그룹', 'B그룹']; // A그룹 -> B그룹 순으로 레이블 정렬

	// 그룹 데이터 순서를 'A그룹', 'B그룹' 순으로 맞추기 위한 데이터 처리
	const orderedDatasets = datasets.map(dataset => {
		// A그룹, B그룹 순서대로 데이터 배열
		const orderedData = [
			dataset.data[labels.indexOf('A')], // A그룹 데이터
			dataset.data[labels.indexOf('B')]  // B그룹 데이터
		];

		return {
			...dataset,  // 기존 dataset 객체의 모든 속성을 복사
			data: orderedData // 데이터를 A -> B 순서로 설정
		};
	});

	orderedDatasets.forEach((dataset, index) => {
		const ctx = document.getElementById(chartIds[index]).getContext('2d');
		new Chart(ctx, {
			type: 'pie', // 차트 유형
			data: {
				labels: orderedLabels, // A그룹이 먼저 나오도록 레이블 정렬
				datasets: [dataset]
			},
			options: {
				responsive: true,
				maintainAspectRatio: false, // 비율 유지하지 않음
				plugins: {
					title: {
						display: true,
						text: chartTitles[index],
						color: 'white',
						font: {
							size: 25,
							weight: 'bold'
						}
					},
					// 범례 설정 (A그룹, B그룹)
					legend: { // legend 추가
						labels: {
							color: 'white',
							font: {
								size: 13,
							}
						}
					},
					// Datalabels plugin 설정
					datalabels: {
						formatter: (value, context) => {
							const total = context.chart.data.datasets[0].data.reduce((acc, val) => acc + val, 0);
							const percentage = ((value / total) * 100).toFixed(2) + '%';
							return percentage; // 퍼센트 포맷으로 리턴
						},
						color: 'black',
						font: {
							size: 20,
							weight: 'bold',
						},
						anchor: 'centet',
						align: 'center'
					}
				}
			},
			plugins: [ChartDataLabels] // Datalabels 플러그인 추가
		});
	});
}
