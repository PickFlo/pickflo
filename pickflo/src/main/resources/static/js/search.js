/*
 * search.html 
 */

document.addEventListener('DOMContentLoaded', function() {
	const startTime = Date.now();
	const userId = parseInt(document.getElementById('userId').value);
	let userGroup = (userId % 2 === 0) ? 'B' : 'A';
	
    // 뒤로가기버튼 동적으로 페이지이동
    const backButton = document.getElementById('back-btn');

    // 백 버튼 클릭 시 처리
    backButton.addEventListener('click', function(event) {
        event.preventDefault(); // 기본 앵커 동작 방지

        // 히스토리 엔트리가 있는지 확인
        if (window.history.length > 1) {
            window.history.back(); // 이전 페이지로 이동
        } else {
            window.location.href = '/'; // 루트 페이지로 이동 (히스토리가 없을 때)
        }
    });

    bindPosterImageClickEvent();

    const clearInput = document.getElementById('clear-input');
    const searchInput = document.getElementById('search-input');
    const genreButtons = document.querySelectorAll('.searching-category[data-type="genre"] .btn');
    const countryButtons = document.querySelectorAll('.searching-category[data-type="country"] .btn');
    const movieList = document.querySelector('.movie-list');
    const loading = document.getElementById('loading');

    let selectedGenre = null;
    let selectedCountry = null;
    let currentPage = 1;
    let isLoading = false;

    // searchInput에 키워드 입력하면 clear-input 버튼이 나타남 
    const toggleClearButton = () => {
        clearInput.style.display = searchInput.value.trim() !== '' ? 'block' : 'none';
    };

    // 입력 필드에서 입력이 발생할 때마다 X 아이콘 표시/숨기기
    searchInput.addEventListener('input', toggleClearButton);

    // X 아이콘 클릭 시 입력값 지우기
    clearInput.addEventListener('click', () => {
        searchInput.value = '';
        toggleClearButton(); 
        searchInput.focus();
        updateMovieList([]); // 검색 결과를 비웁니다.
    });

    // 페이지 로드 시 X 아이콘 상태 업데이트
    toggleClearButton();	

    // 장르 버튼 클릭 이벤트 
    genreButtons.forEach(button => {
        button.addEventListener('click', function() {
            handleSelection(this, 'genre');		
        });
    });

    // 국가 버튼 클릭 이벤트
    countryButtons.forEach(button => {
        button.addEventListener('click', function() {
            handleSelection(this, 'country');
        });
    });

    // 버튼의 선택 상태를 관리하는 함수
    function handleSelection(button, type) {
        if (type === 'genre') {
            if (selectedGenre === button) {
                selectedGenre.classList.remove('selected');
                selectedGenre = null;
            } else {
                if (selectedGenre) {
                    selectedGenre.classList.remove('selected');
                }
                button.classList.add('selected');
                selectedGenre = button;
            }
        } else if (type === 'country') {
            if (selectedCountry === button) {
                selectedCountry.classList.remove('selected');
                selectedCountry = null;
            } else {
                if (selectedCountry) {
                    selectedCountry.classList.remove('selected');
                }
                button.classList.add('selected');
                selectedCountry = button;
            }
        }
        resetAndFilterMovies();
    }

    function resetAndFilterMovies() {
        movieList.innerHTML = ''; // 기존 목록 지우기
        currentPage = 1; // 페이지 번호 초기화
        filterMovies(); // 필터링
    }

	function filterMovies() {
	    if (isLoading) return; // 데이터 로딩 중이면 요청 무시

	    isLoading = true;
	    document.body.classList.add('loading'); // 로딩 중일 때 스크롤 잠시 비활성화
	    loading.style.display = 'block'; // 로딩 표시

	    let queryParams = '';
	    
	    if (selectedGenre) {
	        const genreCode = selectedGenre.getAttribute('data-value');
	        queryParams += `genreCode=${encodeURIComponent(genreCode)}`;
	    }
	    
	    if (selectedCountry) {
	        const countryCode = selectedCountry.getAttribute('data-value');
	        if (queryParams.length > 0) queryParams += '&';
	        queryParams += `countryCode=${encodeURIComponent(countryCode)}`;
	    }
	    
	    if (queryParams.length > 0) {
	        fetch(`/api/search/movies?${queryParams}&page=${currentPage}&limit=21`) 
	            .then(response => {
	                if (!response.ok) {
	                    throw new Error(`HTTP error! Status: ${response.status}`);
	                }
	                return response.json();
	            })
	            .then(movies => {
	                if (movies.length > 0) {
	                    appendMovieList(movies);
	                    currentPage++; // 페이지 번호 증가
	                } else {
	                    console.log("No more movies available.");
	                }
	                isLoading = false;
	                document.body.classList.remove('loading'); // 로딩이 끝나면 스크롤 다시 활성화
	                loading.style.display = 'none'; // 로딩 숨기기
	            })
	            .catch(error => {
	                console.error('Error fetching movies:', error.message);
	                isLoading = false;
	                document.body.classList.remove('loading'); // 에러 발생 시에도 스크롤 다시 활성화
	                loading.style.display = 'none'; // 로딩 숨기기
	            });
	    } else {
	        isLoading = false;
	        document.body.classList.remove('loading'); // 로딩이 끝나면 스크롤 다시 활성화
	        loading.style.display = 'none'; // 로딩 숨기기
	    }
	}

    function appendMovieList(movies) {
        movies.forEach(movie => {
            if (movie.movieImg && movie.movieImg.trim() !== '') {
                const movieCard = document.createElement('div');
                movieCard.classList.add('movie-card');

                movieCard.innerHTML = `
                    <img src="${movie.movieImg}" class="poster-image" 
                        data-movie-id="${movie.movieId}" data-bs-toggle="modal" data-bs-target="#modalMovieDetails">
                `;

                movieList.appendChild(movieCard);
            }
        });

        bindPosterImageClickEvent();
    }

    function onScroll() {
        const scrollableHeight = document.documentElement.scrollHeight;
        const scrollPosition = window.innerHeight + window.scrollY;

        console.log(`Scroll Position: ${scrollPosition}, Scrollable Height: ${scrollableHeight}, Is Loading: ${isLoading}`);

        if (scrollPosition >= scrollableHeight - 100 && !isLoading) {
            filterMovies(); // 스크롤 시 새로운 영화 로드
        }
    }

    window.addEventListener('scroll', onScroll);

    // 초기 영화 목록 로드
    filterMovies();
    
    window.addEventListener('beforeunload', function() {
	    const endTime = Date.now();
	    const timeSpent = Math.floor((endTime - startTime) / 1000); // (초) 단위
	    
	    saveUserData(userGroup, timeSpent); 
	});
	
	function saveUserData(userGroup, timeSpent) {
	    const userData = {
	        userGroup: userGroup,
	        timeSpent: timeSpent,
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
