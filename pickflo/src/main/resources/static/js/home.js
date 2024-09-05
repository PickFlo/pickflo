/**
 * home.html
 */

document.addEventListener('DOMContentLoaded', function() {
    const movieListDiv = document.querySelector('.movie-list');
    const loading = document.getElementById('loading');
    
    let currentPage = 1;
    let isLoading = false;

    const fetchMovies = (page) => {
        return axios.get(`/pickflo/api/home/recMovies?page=${page}&limit=21`)
            .then(response => response.data)
            .catch(error => {
                console.error('Error fetching movies:', error);
                return []; 
            });
    };

    const appendMoviesToDOM = (movies) => {
        if (!movieListDiv) {
            console.error('Error: .movie-list element not found.');
            return;
        }

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

        bindPosterImageClickEvent();
    };

    // 추가 영화 로드 함수
    const loadMoreMovies = () => {
        if (isLoading) return;

        isLoading = true;
        loading.style.display = 'block';

        fetchMovies(currentPage).then(movies => {
            if (movies.length > 0) {
                appendMoviesToDOM(movies);
                currentPage++;
            }
            loading.style.display = 'none';
            isLoading = false;
        });
    };

    const handleScroll = () => {
        const scrollTop = window.scrollY || window.pageYOffset;
        const scrollHeight = document.documentElement.scrollHeight;
        const clientHeight = document.documentElement.clientHeight;

        if (scrollTop + clientHeight >= scrollHeight - 50) {
            loadMoreMovies();
        }
    };

    window.addEventListener('scroll', handleScroll);

    loadMoreMovies();
});



/*
document.addEventListener('DOMContentLoaded', function() {
    const movieListDiv = document.querySelector('.movie-list');
    const loading = document.getElementById('loading');

    let currentPage = 1;
    let isLoading = false;
    const moviesPerPage = 28; // 서버에서 28개 데이터를 받아옴
    const moviesToShowAtOnce = 21; // 한 번에 21개씩 보여줄 것
    let fetchedMovies = []; // 서버에서 받아온 전체 영화 데이터
    let displayedIndex = 0; // 현재까지 화면에 보여준 영화의 인덱스

    // 서버에서 영화 데이터를 가져오는 함수
    const fetchMovies = (page) => {
        return axios.get(`/pickflo/api/home/recMovies?page=${page}`)
            .then(response => response.data)
            .catch(error => {
                console.error('Error fetching movies:', error);
                return [];
            });
    };

    // 받아온 영화 데이터를 DOM에 추가하는 함수
    const appendMoviesToDOM = (movies) => {
        if (!movieListDiv) {
            console.error('Error: .movie-list element not found.');
            return;
        }

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

        bindPosterImageClickEvent();
    };

    // 추가 영화 로드 함수
    const loadMoreMovies = () => {
        if (isLoading) return;
        isLoading = true;
        loading.style.display = 'block';

        if (displayedIndex < fetchedMovies.length) {
            const moviesToDisplay = fetchedMovies.slice(displayedIndex, displayedIndex + moviesToShowAtOnce);
            appendMoviesToDOM(moviesToDisplay);
            displayedIndex += moviesToShowAtOnce;
            loading.style.display = 'none';
            isLoading = false;
        } else {
            fetchMovies(currentPage).then(movies => {
                if (movies.length > 0) {
                    fetchedMovies = movies;
                    currentPage++;
                    displayedIndex = 0; // 새 데이터를 받을 때마다 인덱스를 초기화
                    loadMoreMovies(); // 받은 데이터를 화면에 추가
                }
                loading.style.display = 'none';
                isLoading = false;
            });
        }
    };

    // 스크롤 이벤트 처리 함수
    const handleScroll = () => {
        const scrollTop = window.scrollY || window.pageYOffset;
        const scrollHeight = document.documentElement.scrollHeight;
        const clientHeight = document.documentElement.clientHeight;

        if (scrollTop + clientHeight >= scrollHeight - 50) {
            loadMoreMovies();
        }
    };

    // 스크롤 이벤트 리스너 추가
    window.addEventListener('scroll', handleScroll);

    // 페이지 로드 시 첫 영화 로드
    loadMoreMovies();
});
*/


/*
document.addEventListener('DOMContentLoaded', function() {
	axios.get('/pickflo/api/home/recMovies')
		.then(response => {
			const movies = response.data;
			const movieListDiv = document.querySelector('.movie-list');
			
			if (!movieListDiv) {
                console.error('Error: .movie-list element not found.');
                return;
            }
			
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
			
			bindPosterImageClickEvent();
		})
		.catch(error => {
			console.error('Error fetching movies:', error);
		});
}); 
*/


