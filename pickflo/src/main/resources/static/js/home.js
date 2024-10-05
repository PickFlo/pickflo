document.addEventListener('DOMContentLoaded', function() {
	let startRow = 0;
	let endRow = startRow + 21;
	let isLoading = false;
	let stayTime = 0; // 체류 시간 변수
	let stayStartTime; // 페이지 로드 시각

	// 페이지에서 userId를 전달받아 설정
	const userId = parseInt(document.getElementById('userId').value);
	const apiUrl = userId % 2 === 0 ? '/pickflo/api/recMovies/home_B' : '/pickflo/api/recMovies/home_A';

	let userGroup = (userId % 2 === 0) ? 'B' : 'A';

	if (userGroup === 'A') {
		document.body.style.backgroundColor = '#141414'; 
	} else {
		document.body.style.background = 'linear-gradient(to bottom, #141414, #8A2BE2)';
	}
	
	function loadMovies() {
		if (isLoading) return;
		isLoading = true;

		axios.get(`${apiUrl}?startRow=${startRow}&endRow=${endRow}`)
			.then(response => {
				const movies = response.data;
				const movieListDiv = document.querySelector('.movie-list');

				if (!movieListDiv) {
					console.error('Error: .movie-list element not found.');
					return;
				}

				if (Array.isArray(movies)) {
					movies.forEach(movie => {
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
					});
					startRow = endRow + 1;
					endRow = startRow + 20;
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
			saveUserData(userId, userGroup, 'scroll');
		}
	});
	
	function saveUserData(userId, userGroup, actionType) {
		const userData = {
			userId: userId,
			userGroup: userGroup,
			actionType: actionType,
			stayTime: stayTime // 체류 시간을 추가
		};

		// 서버에 POST 요청
		axios.post('/pickflo/api/chart/saveUserData', userData)
			.then(response => {
				console.log('User data saved successfully:', response.data);
			})
			.catch(error => {
				console.error('Error saving user data:', error);
			});
	}
	
	// 페이지 로드 시각 기록
	stayStartTime = Date.now();

	// 페이지를 떠날 때 stayTime을 저장
	window.addEventListener('beforeunload', () => {
		// 체류 시간 계산 (초 단위로)
		stayTime = Math.floor((Date.now() - stayStartTime) / 1000); // 밀리초를 초로 변환
		saveUserData(userId, userGroup, 'stayTime'); // stayTime 저장
	});

});
