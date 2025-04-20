/*
 * home.js 
 */

document.addEventListener('DOMContentLoaded', function() {
	let startRow = 0;
	let pageSize = 21;
	let isLoading = false;
	let scrollCount = 0;  // 스크롤 횟수를 추적
	const startTime = Date.now();  // 페이지 로드 시점 시간 기록
	let loadedMovieIds = new Set(); // 중복 방지를 위한 Set

	// 페이지에서 userId를 전달받아 설정
	const userId = parseInt(document.getElementById('userId').value);
	const apiUrl = userId % 2 === 0 ? '/api/recMovies/home_B' : '/api/recMovies/home_A';

	let userGroup = (userId % 2 === 0) ? 'B' : 'A';

	// 배경색 설정
	if (userGroup === 'A') {
		document.body.style.backgroundColor = '#141414'; // 기본 배경색
	} else {
		document.body.style.background = 'linear-gradient(to bottom, #141414, #8A2BE2)';
	}

	function loadMovies() {
		if (isLoading) return;
		isLoading = true;

		axios.get(`${apiUrl}?startRow=${startRow}&pageSize=${pageSize}`)
			.then(response => {
				const movies = response.data;
				const movieListDiv = document.querySelector('.movie-list');

				if (!movieListDiv) {
					console.error('Error: .movie-list element not found.');
					return;
				}

				if (Array.isArray(movies)) {
					movies.forEach(movie => {
						// 중복된 영화 ID 체크
						if (!loadedMovieIds.has(movie.movieId)) {
							loadedMovieIds.add(movie.movieId); // 새로운 영화 ID 추가
							
							const movieCard = document.createElement('div');
							movieCard.className = 'movie-card';

							const img = document.createElement('img');
							img.src = movie.movieImg;
							img.alt = 'Movie Image';
							img.className = 'poster-image';

							img.setAttribute('data-movie-id', movie.movieId);
							img.setAttribute('data-bs-toggle', 'modal');
							img.setAttribute('data-bs-target', '#modalMovieDetails');

							movieCard.appendChild(img);
							movieListDiv.appendChild(movieCard);
							}
						});
					startRow += pageSize;
					bindPosterImageClickEvent();
				} else {
					console.error('Error: 응답 데이터의 content가 배열이 아닙니다.');
				}

				isLoading = false;
			})
			.catch(error => {
				console.error('Error fetching movies:', error);
				isLoading = false;
			});
	}

	// 초기 로드
	loadMovies();

	// 스크롤 이벤트 리스너
	window.addEventListener('scroll', () => {
		if (window.innerHeight + window.scrollY >= document.body.offsetHeight) {
			loadMovies();
			scrollCount++;  // 스크롤 횟수 증가
		}
	});

	// 사용자가 페이지를 떠날 때 time_spent를 기록
	window.addEventListener('beforeunload', function() {
		const endTime = Date.now();
		const timeSpent = Math.floor((endTime - startTime) / 1000);  // 페이지에 머문 시간
		const statTime = new Date().toISOString(); // UTC 기준

		saveUserData(userGroup, timeSpent, scrollCount, statTime);
	});

	function saveUserData(userGroup, timeSpent, scrollCount, statTime) {
		const userData = {
			userGroup: userGroup,
			timeSpent: timeSpent,
			scrollCount: scrollCount,
			statTime: statTime
		};

		// 서버에 POST 요청
		axios.post('/api/user-statistics/saveUserData', userData)
			.then(response => {
				console.log('User data saved successfully:', response.data);
			})
			.catch(error => {
				console.error('Error saving user data:', error);
			});
	}

});