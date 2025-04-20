/**
 * signup.html에 포함
 */
document.addEventListener('DOMContentLoaded', () => {

	let isEmailChecked = false;
	let isPasswordChecked = false;
	let isNicknameChecked = false;
	let isBirthChecked = false;
	let isGenderChecked = false;

	const inputEmail = document.querySelector('input#email');
	inputEmail.addEventListener('change', checkEmail);

	const inputPassword = document.querySelector('input#password');
	inputPassword.addEventListener('change', checkPassword);

	const inputConfirmPassword = document.querySelector('input#confirmPassword');
	inputConfirmPassword.addEventListener('change', checkPassword);

	const inputNickname = document.querySelector('input#nickname');
	inputNickname.addEventListener('change', checkNickname);

	const inputBirth = document.querySelector('input#birthdate');
	inputBirth.addEventListener('change', checkBirth);

	const radioBtn = document.querySelector('div.radio-group');
	radioBtn.addEventListener('change', checkRadioBtn);

	const form = document.getElementById('myForm');
	form.addEventListener('keydown', function(event) {
		if (event.key === 'Enter') {
			event.preventDefault();
		}
	});
	

	let successMessage = '회원가입이 완료되었습니다.';
	let errorMessage = '회원가입이 실패했습니다.';

	document.getElementById("btnSignUp").addEventListener("click", function() {
		if (successMessage) {
			alert(successMessage);
		} else if (errorMessage) {
			alert(errorMessage);
		}
	});

	
	function changeButtonState() {
		const btnSignUp = document.querySelector('button#btnSignUp');

		if (isEmailChecked && isPasswordChecked && isNicknameChecked
			&& isBirthChecked && isGenderChecked ) {
			btnSignUp.classList.remove('disabled');
		} else {
			btnSignUp.classList.add('disabled');
		}
	}

	function checkEmail(event) {
		const email = event.target.value;
		const icon = inputEmail.nextElementSibling;
		const checkEmailResult = document.querySelector('div#checkEmailResult');
		const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

		// 이메일 정규 표현식은 전체 문자열의 길이를 제한할 수 없음
		if (email.length < 6 || email.length > 50) {
			isEmailChecked = false;
			checkEmailResult.style.color = 'red';
			checkEmailResult.innerHTML = '길이는 6자에서 50자 사이여야 합니다.';
			icon.classList.add("d-none");
			changeButtonState();
			return;
		}

		if (!emailRegex.test(email)) {
			isEmailChecked = false;
			checkEmailResult.style.color = 'red';
			checkEmailResult.innerHTML = '이메일 형식을 확인하세요.';
			icon.classList.add("d-none");
			changeButtonState();
			return;
		}

		const uri = `/api/check-email?email=${encodeURIComponent(email)}`;
		console.log('@@@@@@uri=', uri);
		axios
			.get(uri)
			.then((response) => {
				console.log('@@@@@@response=', response);
				if (response.data === 'Y') {
					console.log('YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY')
					isEmailChecked = true;
					checkEmailResult.style.color = 'green';
					checkEmailResult.innerHTML = '사용가능한 이메일입니다.';
					icon.classList.add("d-none");
				} else {
					console.log('NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNn')
					isEmailChecked = false;
					checkEmailResult.style.color = 'red';
					checkEmailResult.innerHTML = '사용할 수 없는 이메일입니다.';
					icon.classList.add("d-none");
				}

				changeButtonState();
			})
			.catch((error) => {
				console.log(error);
			})
	}

	function checkPassword() {
		const password = inputPassword.value;
		const confirmPassword = inputConfirmPassword.value;
		const checkPasswordResult = document.querySelector('div#checkPasswordResult');
		const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*]).{8,}$/;

		if (!passwordRegex.test(password)) {
			isPasswordChecked = false;
			checkPasswordResult.style.color = 'red';
			checkPasswordResult.innerHTML = '최소 8자 이상이며, 소문자, 대문자, 숫자, 특수문자가 각각 하나 이상 포함되어야 합니다.';
			changeButtonState();

			return;
		} else {
			checkPasswordResult.style.color = 'red';
			checkPasswordResult.innerHTML = '비밀번호를 다시 입력하세요.';
		}

		if (password === confirmPassword) {
			isPasswordChecked = true;
			checkPasswordResult.style.color = 'green';
			checkPasswordResult.innerHTML = '비밀번호가 일치합니다.';
			icon.classList.add("d-none");

		} else {
			isPasswordChecked = false;
			checkPasswordResult.style.color = 'red';
			checkPasswordResult.innerHTML = '비밀번호가 일치하지 않습니다.';
			icon.classList.add("d-none");
		}

		changeButtonState();
	}


	function checkNickname(event) {
		const nickname = event.target.value;
		const icon = inputNickname.nextElementSibling;
		const checkNicknameResult = document.querySelector('div#checkNicknameResult');
		const nicknameRegex = /^[a-zA-Z0-9가-힣._]{1,15}$/;

		if (!nicknameRegex.test(nickname)) {
			isNicknameChecked = false;
			checkNicknameResult.style.color = 'red';
			checkNicknameResult.innerHTML = '영문자, 숫자, 한글, 밑줄 및 점을 포함할 수 있으며, 길이가 1자에서 15자 사이여야 합니다.';
			icon.classList.add("d-none");
			changeButtonState();
			return;
		}

		if (nicknameRegex.test(nickname)) {
			isNicknameChecked = true;
			checkNicknameResult.style.color = 'green';
			checkNicknameResult.innerHTML = '사용가능한 닉네임 입니다.';
		}

	}

	function checkBirth() {
	    // `inputBirth`를 사용하여 생년월일 값이 있는지 확인하고 `isBirthChecked`를 설정
	    if (inputBirth.value) {
	        isBirthChecked = true;
	    } else {
	        isBirthChecked = false;
	    }
	    changeButtonState(); // 버튼 상태 업데이트
	}

	function checkRadioBtn() {
	    // `radioBtn` 내의 라디오 버튼들이 선택되었는지 확인
	    const genderInputs = document.querySelectorAll('input[name="gender"]');
	    let isChecked = false;
	    
	    for (let i = 0; i < genderInputs.length; i++) {
	        if (genderInputs[i].checked) {
	            isChecked = true;
	            break;
	        }
	    }

	    isGenderChecked = isChecked; // isGenderChecked를 업데이트
	    changeButtonState(); // 버튼 상태 업데이트
	}

})