/**
 * picker.html에 포함.
 */
document.addEventListener('DOMContentLoaded', () => {
    const layout = document.querySelector('div#layout');
    const goToHome = document.querySelector('div#home');

    const rating = 6.5; // 영화 평점
    let rn1 = 1;
    let rn2 = rn1 + 1;
    let isLoading = false; // 로딩 상태 추적
    
    let moviesBuffer = []; // 남은 영화를 저장할 배열
    const displayCount = 21; // 처음에 표시할 영화 수

	// 영화 데이터를 서버에서 가져오는 함수
	async function loadMovies() {
		if (isLoading) return; // 이미 로딩 중이면 중단
		isLoading = true; // 로딩 상태로 설정

		try {
			const response = await axios.get(`/api/picker/listByGenre`, {
				params: { rating, rn1, rn2 }
			});

			console.log("서버 응답:", response.data); // 응답 데이터 확인

			const fetchedMovies = response.data; // 서버에서 받아온 영화 데이터

			if (fetchedMovies.length > 0) {
				if (moviesBuffer.length === 0) {
					// 처음 로드 시
					showMovies(fetchedMovies.slice(0, displayCount)); // 처음 21개 영화 표시
					moviesBuffer = fetchedMovies.slice(displayCount); // 22번째 영화부터 끝까지 나머지 영화를 저장
				} else {
					// 추가 로드 시
					moviesBuffer = moviesBuffer.concat(fetchedMovies); // 기존 버퍼와 새로 불러온 영화 결합
					showMovies(moviesBuffer.slice(0, displayCount)); // 처음 21개 영화 표시
					moviesBuffer = moviesBuffer.slice(displayCount); // 22번째 영화부터 끝까지 나머지 영화를 저장
				}
				rn1 += 2;
			} else {
				console.log("더 이상 로드할 영화가 없습니다.");
			}

		} catch (error) {
			console.error("Error loading movies:", error);
		} finally {
			isLoading = false; // 로딩 상태 해제
		}
	}

	// 무한 스크롤 감지 함수
	window.addEventListener("scroll", () => {
		if ((window.innerHeight + window.scrollY) >= (document.documentElement.scrollHeight - 100)) {
			console.log("페이지 하단에 도달, 영화 데이터를 로드합니다.");

			// moviesBuffer가 21개 이상이면 서버에서 새 데이터를 가져오지 않고 로컬에서 처리
			if (moviesBuffer.length >= displayCount) {
				showMovies(moviesBuffer.slice(0, displayCount)); // 21개의 영화를 보여줌
				moviesBuffer = moviesBuffer.slice(displayCount); // 나머지 영화를 남김
			} else {
				loadMovies(); // 서버에서 데이터를 가져옴
			}
		}
	});

    // 초기 데이터 로드
    loadMovies();

    function showMovies(data) {
        let htmlStr = '';
        data.forEach(movie => {
			const { movieId: id, movieTitle: title, movieImg: img } = movie;

            htmlStr += `
                <div class="movie" movie-id="${id}">
                    <div class="poster"> 
                        <img class="poster-image" src="${img}" alt="${title}" />
                        <div class="overlay">
                            <i class="bi bi-check-circle"></i>
                        </div>
                    </div>
                </div>
            `;
        });

        layout.insertAdjacentHTML('beforeend', htmlStr);
    }

    // 이벤트 위임을 사용하여 영화 선택 처리
    layout.addEventListener('click', (event) => {
        const target = event.target;

        // 클릭한 요소가 .overlay가 포함된 요소일 경우 처리
        if (target.closest('.overlay')) {
			const movieContainer = target.closest('.movie');
            movieContainer.classList.toggle('selected');
            goToHomeVisibility();
        }
    });

    // 선택한 영화가 3개 이상일 경우 홈으로 가기 텍스트 표시
    function goToHomeVisibility() {
        const selectedMovies = document.querySelectorAll('.selected');
        goToHome.classList.toggle('d-none',selectedMovies.length < 3); 
    }

    // 홈으로 가기 텍스트 클릭 이벤트
    goToHome.addEventListener('click', () => {
        const selectedMoviesId = Array.from(document.querySelectorAll('.selected')).map(movie => movie.getAttribute('movie-id'));

        const userId = document.getElementById('userId').value;

        const data = selectedMoviesId.map(movieId => ({ userId, movieId }));

        const uri = `/api/usermovie/saveAll`;
        axios
            .post(uri, data, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then((response) => {
                console.log(response.data);
                window.location.href = '/';
            })
            .catch((error) => {
                console.log(error);
            });
    });
});