/**
 * admin/home.html
 */

document.addEventListener('DOMContentLoaded', function() {
	
	// 현재 날짜를 yyyy-mm-dd 형식으로 반환하는 함수
    function getCurrentDateString() {
        const today = new Date();
        const year = today.getFullYear();
        const month = String(today.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 1을 더함
        const day = String(today.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    }

	function fetchUserData(startDate, endDate) {
	    const url = `/api/user-statistics/getUserData?startDate=${startDate}&endDate=${endDate}`;
        fetch(url)
	        .then(response => {
	            if (!response.ok) {
	                throw new Error('Network response was not ok');
	            }
	            return response.json();
	        })
	        .then(data => {
	            console.log('가져온 사용자 데이터:', data);
	            drawTables(data);
	            drawCharts(data);
	        })
	        .catch(error => {
	            console.error('Error fetching user data:', error);
	        });
	}
	
	// 날짜 선택 이벤트 리스너
    document.getElementById("resultButton").addEventListener("click", function() {
        const startDate = document.getElementById("startDate").value;
        const endDate = document.getElementById("endDate").value;
        
        if (startDate && endDate) {
            fetchUserData(startDate, endDate);
        } else {
            alert("시작 날짜와 종료 날짜를 모두 선택하세요."); 
        }
    });
    
    function formatTimeSpent(seconds) {
        if (seconds < 60) {
            return `${seconds} 초`; // 60 초 미만
        } else if (seconds < 3600) {
            const minutes = Math.floor(seconds / 60);
            return `${minutes} 분`; // 3600 초 미만
        } else if (seconds < 216000) {
            const hours = Math.floor(seconds / 3600);
            const minutes = Math.floor((seconds % 3600) / 60);
            return `${hours} 시간 ${minutes} 분`; // 216000 초 미만
        } else {
            const hours = Math.floor(seconds / 3600);
            const days = Math.floor(hours / 24);
            return `${days} 일 ${hours % 24} 시간`; // 216000 초 이상 (일 단위 추가)
        }
    }


	function drawTables(data) {
    let totalScrollA = 0, totalScrollB = 0;
    let totalLikeA = 0, totalLikeB = 0;
    let totalUnlikeA = 0, totalUnlikeB = 0;
    let totalTimeSpentA = 0, totalTimeSpentB = 0;
    let totalVisitorCountA = 0, totalVisitorCountB = 0;
    

    // 데이터를 순회하며 그룹 A와 그룹 B의 통계를 집계
    data.forEach(stat => {
        if (stat.userGroup === 'A') {
            totalScrollA += stat.scrollCount;
            totalLikeA += stat.likeCount;
            totalUnlikeA += stat.unlikeCount;
            totalTimeSpentA += stat.timeSpent; // 초 단위
            totalVisitorCountA += stat.visitorCount;

    

        } else if (stat.userGroup === 'B') {
            totalScrollB += stat.scrollCount;
            totalLikeB += stat.likeCount;
            totalUnlikeB += stat.unlikeCount;
            totalTimeSpentB += stat.timeSpent; // 초 단위
            totalVisitorCountB += stat.visitorCount;

        }
    });

    // A 그룹 통계 값 삽입
    document.getElementById("scrollCountA").innerText = totalScrollA + "회";
    document.getElementById("likeCountA").innerText = totalLikeA + "회";
    document.getElementById("unlikeCountA").innerText = totalUnlikeA + "회";
    document.getElementById("timeSpentA").innerText = formatTimeSpent(totalTimeSpentA); // 시간 형식으로 변환하여 표시
    document.getElementById("visitorCountA").innerText = totalVisitorCountA + "명"; // A 그룹 방문자 수 표시

    // A 그룹 전환율 계산
    const conversionRateA = totalVisitorCountA > 0 ? (totalLikeA / totalVisitorCountA) * 100 : 0;
    document.getElementById("conversionRateA").innerText = conversionRateA.toFixed(2) + "%";

    // B 그룹 통계 값 삽입
    document.getElementById("scrollCountB").innerText = totalScrollB + "회";
    document.getElementById("likeCountB").innerText = totalLikeB + "회";
    document.getElementById("unlikeCountB").innerText = totalUnlikeB + "회";
    document.getElementById("timeSpentB").innerText = formatTimeSpent(totalTimeSpentB); // 시간 형식으로 변환하여 표시
    document.getElementById("visitorCountB").innerText = totalVisitorCountB + "명";

    // B 그룹 전환율 계산
    const conversionRateB = totalVisitorCountB > 0 ? (totalLikeB / totalVisitorCountB) * 100 : 0;
    document.getElementById("conversionRateB").innerText = conversionRateB.toFixed(2) + "%";

}


fetch('/api/user-visits/return-rate/odd')
    .then(response => response.json())
    .then(rate => {
        console.log(`홀수 user_id의 재방문율: ${rate.toFixed(2)}%`);
        document.getElementById('oddReturnRate').innerText = `${rate.toFixed(2)}%`; // A그룹의 재방문율
    })
    .catch(error => console.error('Error fetching odd user return rate:', error));

fetch('/api/user-visits/return-rate/even')
    .then(response => response.json())
    .then(rate => {
        console.log(`짝수 user_id의 재방문율: ${rate.toFixed(2)}%`);
        document.getElementById('evenReturnRate').innerText = `${rate.toFixed(2)}%`; // B그룹의 재방문율
    })
    .catch(error => console.error('Error fetching even user return rate:', error));

    
// 차트 섹션
let chartInstance = null; // 차트 인스턴스를 전역 변수로 선언

function drawCharts(data) {
    const ctx = document.getElementById('chartAB').getContext('2d');

    // 기존 차트가 존재하는지 확인하고 파괴(destroy)
    if (chartInstance) {
        chartInstance.destroy();
        chartInstance = null; // 파괴 후 차트 인스턴스 초기화
    }

    const totalScroll = data.reduce((sum, stat) => sum + stat.scrollCount, 0);
    const totalLikeClick = data.reduce((sum, stat) => sum + stat.likeCount, 0);
    const totalUnlikeClick = data.reduce((sum, stat) => sum + stat.unlikeCount, 0);
    const totalTimeSpent = data.reduce((sum, stat) => sum + stat.timeSpent, 0);
    const totalVisitors = data.reduce((sum, stat) => sum + stat.visitorCount, 0);

    const calculateData = (userGroup) => [
        ((data.reduce((sum, stat) => sum + (stat.userGroup === userGroup ? stat.timeSpent : 0), 0) / Math.max(totalTimeSpent, 1)) * 100) || 0,
        ((data.reduce((sum, stat) => sum + (stat.userGroup === userGroup ? stat.scrollCount : 0), 0) / Math.max(totalScroll, 1)) * 100) || 0,
        ((data.reduce((sum, stat) => sum + (stat.userGroup === userGroup ? stat.unlikeCount : 0), 0) / Math.max(totalUnlikeClick, 1)) * 100) || 0,
        ((data.reduce((sum, stat) => sum + (stat.userGroup === userGroup ? stat.likeCount : 0), 0) / Math.max(totalLikeClick, 1)) * 100) || 0,
        ((data.reduce((sum, stat) => sum + (stat.userGroup === userGroup ? stat.visitorCount : 0), 0) / Math.max(totalVisitors, 1)) * 100) || 0
    ].map(Number).map(value => value.toFixed(1));

    const chartData = {
        labels: ['총 이용 시간', '스크롤 수', '좋아요 해제 수', '좋아요 클릭 수', '방문자 수'],
        datasets: [
            {
                label: 'A 그룹',
                data: calculateData('A'),
                backgroundColor: 'rgba(111, 66, 193)',
                borderColor: 'white',
                borderWidth: 1,
                datalabels: {
                    font: {
                        weight: 'bold',
                        size: window.innerWidth < 600 ? 10 : 16
                    },
                    color: 'black',
                    anchor: 'center',
                    align: 'center',
                    formatter: (value) => `${value}%`,
                    display: true
                }
            },
            {
                label: 'B 그룹',
                data: calculateData('B'),
                backgroundColor: 'rgba(255, 193, 7)',
                borderColor: 'white',
                borderWidth: 1,
                datalabels: {
                    font: {
                        weight: 'bold',
                        size: window.innerWidth < 600 ? 10 : 16
                    },
                    color: 'black',
                    anchor: 'center',
                    align: 'center',
                    formatter: (value) => `${value}%`,
                    display: true
                }
            }
        ]
    };

    // 새로운 차트를 생성하고 chartInstance에 저장
    chartInstance = new Chart(ctx, {
        type: 'bar',
        data: chartData,
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                title: {
                    display: true,
                    text: 'A 그룹 vs B 그룹: 사용자 행동 비율 비교',
                    color: 'white',
                    font: {
                        size: window.innerWidth < 600 ? 17 : 20,
                    },
                    padding: {
                        top: 10,
                        bottom: window.innerWidth < 600 ? 20 : 50
                    }
                },
                legend: {
                    position: window.innerWidth < 600 ? 'top' : 'right',
                    labels: {
                        color: 'white',
                        font: {
                            size: window.innerWidth < 600 ? 12 : 16
                        }
                    }
                },
                tooltip: {
                    padding: 10,
                    backgroundColor: 'rgba(0, 0, 0, 0.8)',
                    bodyFont: {
                        size: window.innerWidth < 600 ? 12 : 16,
                        family: 'Arial',
                        weight: 'bold'
                    },
                    callbacks: {
                        label: function (tooltipItem) {
                            return tooltipItem.dataset.label + ': ' + tooltipItem.raw + '%';
                        }
                    }
                },
                datalabels: {
                    display: true,
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    stacked: true,
                    title: {
                        display: true,
                        text: '비율 (%)',
                        color: 'white',
                        font: {
                            size: window.innerWidth < 600 ? 10 : 16
                        }
                    },
                    ticks: {
                        color: 'white',
                        font: {
                            size: window.innerWidth < 600 ? 10 : 16
                        }
                    }
                },
                x: {
                    stacked: true,
                    ticks: {
                        color: 'white',
                        font: {
                            size: window.innerWidth < 600 ? 10 : 16
                        }
                    }
                }
            }
        },
        plugins: [ChartDataLabels]
    });
}


        
   		

    window.onload = function() {
        // 현재 날짜를 시작 날짜와 종료 날짜 입력 필드에 설정
        const currentDate = getCurrentDateString();
        document.getElementById("startDate").value = currentDate;
        document.getElementById("endDate").value = currentDate;

        // 기본적으로 당일 데이터를 가져옴
        fetchUserData(currentDate, currentDate);
       
    };


});