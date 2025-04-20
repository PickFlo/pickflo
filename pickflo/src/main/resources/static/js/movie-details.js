/*
 * movie-deatils.js
 * fragments.html에 추가
 */

let currentMovieId = null;
let currentUserId = null;
let likeCount = 0; // 초기화
let unlikeCount = 0; // 초기화

function bindPosterImageClickEvent() {
	const posterImages = document.querySelectorAll('.poster-image');

	posterImages.forEach(image => {
		image.addEventListener('click', clickPosterImage);
	});

	function clickPosterImage() {
		const movieId = this.getAttribute('data-movie-id');
		currentMovieId = movieId;
		currentUserId = document.getElementById('userId').value;

		axios.get(`/api/movie/details`, { params: { movieId: movieId } })
			.then(response => {
				const movie = response.data;

				document.getElementById('title').textContent = movie.movieTitle;
				document.getElementById('synopsis').textContent = movie.movieOverview;
				document.getElementById('rating').textContent = movie.movieRating;
				document.getElementById('imgPoster').src = movie.movieImg;

				const releaseDate = new Date(movie.movieReleaseDate);
				const releaseYear = releaseDate.getFullYear();
				document.getElementById('releaseDate').textContent = releaseYear;

				const runtimeHours = Math.floor(movie.movieRuntime / 60);
				const runtimeMinutes = movie.movieRuntime % 60;
				const formattedRuntime = `${runtimeHours}시간 ${runtimeMinutes}분`;
				document.getElementById('runtime').textContent = formattedRuntime;

				const genreContainer = document.querySelector('.genre-badge-container');
				genreContainer.innerHTML = '';
				movie.genres.forEach(genre => {
					const span = document.createElement('span');
					span.className = 'badge';
					span.textContent = genre;
					genreContainer.appendChild(span);
				});

				const countryContainer = document.getElementById('country');
				countryContainer.innerHTML = movie.countries.join(' <span>∙</span> ');

				const actorContainer = document.querySelector('.actor-container');
				const directorContainer = document.querySelector('.director-container');

				const actors = [];
				const directors = [];

				movie.people.forEach(person => {
					if (person.job === 'Acting') {
						actors.push(person.personName);
					} else if (person.job === 'Director') {
						directors.push(person.personName);
					}
				});

				actorContainer.textContent = actors.join(', ') || '출연진 정보 없음';
				directorContainer.textContent = directors.join(', ') || '감독 정보 없음';
			})
			.catch(error => {
				console.error('Error fetching movie details:', error);
			});

		// 찜 상태 확인
		axios.get(`/api/movie/like-status`, { params: { userId: currentUserId, movieId: movieId } })
			.then(response => {
				const isFavorite = response.data;

				const iconHeart = document.getElementById('iconHeart');

				// 기존 핸들러 제거
				iconHeart.removeEventListener('click', handleFavoriteClick);

				if (isFavorite) {
					// 찜 상태일 때
					iconHeart.classList.remove('fa-regular', 'fa-heart');
					iconHeart.classList.add('fa-solid', 'fa-heart');
					iconHeart.style.color = 'red'; // 찜 상태일 때 색상 변경
				} else {
					// 찜 상태가 아닐 때
					iconHeart.classList.remove('fa-solid', 'fa-heart');
					iconHeart.classList.add('fa-regular', 'fa-heart');
					iconHeart.style.color = 'white'; // 찜 상태가 아닐 때 색상 변경
				}

				// 새 핸들러 등록
				iconHeart.addEventListener('click', handleFavoriteClick);
			})
			.catch(error => {
				console.error('Error checking favorite status:', error);
			});
	}
}

// 하트 아이콘 클릭 시 호출되는 함수
function handleFavoriteClick() {
	const iconHeart = document.getElementById('iconHeart');
	let userGroup = (currentUserId % 2 === 0) ? 'B' : 'A';

	console.log("Movie ID:", currentMovieId);
	console.log("User ID:", currentUserId);

	if (iconHeart.classList.contains('fa-regular')) {
		axios.get('/api/movie/like', {
			params: {
				movieId: currentMovieId,
				userId: currentUserId
			},
		})
			.then(response => {
				iconHeart.classList.remove('fa-regular', 'fa-heart');
				iconHeart.classList.add('fa-solid', 'fa-heart');
				iconHeart.style.color = 'red';
				console.log("추가 성공");

				// 현재 페이지가 /일 때만 saveUserData 호출
				if (window.location.pathname === '/') {
					saveUserData(userGroup, 1, 0);
				}

				if (window.location.pathname === '/movie/like') {
					updateMovieList();
				}
			})
			.catch(error => console.error('Error:', error));

	} else {
		axios.get('/api/movie/unlike', {
			params: {
				movieId: currentMovieId,
				userId: currentUserId
			},
		})
			.then(response => {
				if (response.data === 'no') {
					alert('찜한 콘텐츠는 3개 이상이어야 합니다.');
				} else {
					iconHeart.classList.remove('fa-solid', 'fa-heart');
					iconHeart.classList.add('fa-regular', 'fa-heart');
					iconHeart.style.color = '#ffffff';
					console.log("해제 성공");

					// 현재 페이지가 /일 때만 saveUserData 호출
					if (window.location.pathname === '/') {
						saveUserData(userGroup, 0, 1);
					}

					if (window.location.pathname === '/movie/like') {
						removeMovieFromLikePage(currentMovieId);
					}
				}
			})
			.catch(error => console.error('Error:', error));
	}
}

function saveUserData(userGroup, likeCount, unlikeCount) {
	const userData = {
		userGroup: userGroup,
		likeCount: likeCount,
		unlikeCount: unlikeCount,
		statTime: new Date().toISOString()
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


function updateMovieList() {
	// 서버에서 찜한 영화 목록 다시 불러오기
	const userId = document.getElementById('userId').value;

	axios.get('/api/movie/updated-movie-list', {
		params: {
			userId: userId
		}
	})
		.then(response => {
			const movies = response.data;
			const movieListContainer = document.getElementById('movieListContainer');

			// 기존 영화 목록 비우기
			movieListContainer.innerHTML = '';

			// 새로 받은 영화 목록으로 다시 그리기
			movies.forEach(movie => {
				const movieCard = document.createElement('div');
				movieCard.classList.add('movie-card');

				const movieImg = document.createElement('img');
				movieImg.src = movie.movieImg;
				movieImg.classList.add('poster-image');
				movieImg.setAttribute('data-movie-id', movie.movieId);
				movieImg.setAttribute('data-bs-toggle', 'modal');
				movieImg.setAttribute('data-bs-target', '#modalMovieDetails');

				movieCard.appendChild(movieImg);
				movieListContainer.appendChild(movieCard);
			});

			// 바인딩 새로 하기
			bindPosterImageClickEvent();
		})
		.catch(error => console.error('Error fetching updated movie list:', error));
}

function removeMovieFromLikePage() {
	// 영화 목록을 다시 불러와서 그리기
	updateMovieList();

}

// 스와이프 감지를 위한 변수
let touchStartY = 0;
let touchEndY = 0;

function handleTouchStart(event) {
	touchStartY = event.changedTouches[0].screenY;
}

function handleTouchMove(event) {
	touchEndY = event.changedTouches[0].screenY;
}

function handleTouchEnd() {
	const swipeDistance = touchEndY - touchStartY;

	// 스와이프가 아래로 100px 이상이면 모달 닫기
	if (swipeDistance > 100) {
		const modal = document.querySelector('#modalMovieDetails');
		const modalInstance = bootstrap.Modal.getInstance(modal);
		modalInstance.hide();
	}
}

document.querySelector('#modalMovieDetails').addEventListener('touchstart', handleTouchStart);
document.querySelector('#modalMovieDetails').addEventListener('touchmove', handleTouchMove);
document.querySelector('#modalMovieDetails').addEventListener('touchend', handleTouchEnd);


