const validationRules = {
  memberId: {
    pattern: /^[a-z0-9]{5,15}$/,
    message: "아이디는 5~15자의 소문자와 숫자로만 구성되어야 합니다.",
  },
  pwd: {
    pattern:
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*(),.?":{}|<>])[A-Za-z\d!@#$%^&*(),.?":{}|<>]{10,20}$/,
    message: "비밀번호 형식이 올바르지 않습니다.",
  },
  pwdConfirm: {
    pattern:
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*(),.?":{}|<>])[A-Za-z\d!@#$%^&*(),.?":{}|<>]{10,20}$/,
    message: "비밀번호가 일치하지 않습니다.",
  },
  name: {
    pattern: /^[가-힣]{2,5}$/,
    message: "이름은 2~5자의 한글만 입력 가능합니다.",
  },
  phone1: {
    pattern: /^\d{3}$/,
    message: "3자리 숫자를 입력해주세요.",
  },
  phone2: {
    pattern: /^\d{4}$/,
    message: "4자리 숫자를 입력해주세요.",
  },
  phone3: {
    pattern: /^\d{4}$/,
    message: "4자리 숫자를 입력해주세요.",
  },
  email: {
    pattern: /^(?=.{1,254}$)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
    message: "이메일 형식이 올바르지 않습니다.",
  },
};

// 숫자만 입력되도록 처리
function onlyNumbers(input) {
  input.value = input.value.replace(/[^0-9]/g, "");
}

// 비밀번호 유효성 검사 함수 추가
function validatePassword(password) {
  const conditions = {
    hasUpperLower: /(?=.*[a-z])(?=.*[A-Z])/.test(password),
    hasNumber: /(?=.*\d)/.test(password),
    hasSpecial: /(?=.*[!@#$%^&*(),.?":{}|<>])/.test(password),
    hasLength: /^.{10,20}$/.test(password),
  };

  document.querySelector(".pwVal1").style.color =
    conditions.hasUpperLower && conditions.hasNumber && conditions.hasSpecial
      ? "#00C471"
      : "red";
  document.querySelector(".pwVal2").style.color = conditions.hasLength
    ? "#00C471"
    : "red";
  document.querySelector(".mark1").innerHTML =
    conditions.hasUpperLower && conditions.hasNumber && conditions.hasSpecial
      ? "&#10004;"
      : "&#10006;";
  document.querySelector(".mark2").innerHTML = conditions.hasLength
    ? "&#10004;"
    : "&#10006;";

  return Object.values(conditions).every((condition) => condition);
}

// 실시간 유효성 검사 함수
function validateField(input) {
  const fieldName = input.name;
  const value = input.value;
  
  // radio 버튼은 별도 처리
  if (input.type === 'radio') {
    return true; // radio 버튼은 항상 유효하다고 처리
  }

  const errorElement = document.getElementById(`${fieldName}-error`);
  
  // 에러 요소가 없는 경우 처리
  if (!errorElement) {
    return true;
  }

  // 필드가 비어있는 경우
  if (!value.trim()) {
    errorElement.textContent = "필수 입력";
    errorElement.style.display = "block";
    errorElement.style.color = "red";
    input.classList.add("invalid-input");
    return false;
  }

  // 비밀번호 확인 필드 특별 처리
  if (fieldName === "pwdConfirm") {
    const pwdInput = document.querySelector('input[name="pwd"]');
    if (value !== pwdInput.value) {
      errorElement.textContent = "비밀번호가 일치하지 않습니다.";
      errorElement.style.display = "block";
      errorElement.style.color = "red";
      input.classList.add("invalid-input");
      return false;
    }
  }

  // 전화번호 필드 처리
  if (fieldName.startsWith("phone")) {
    const phoneErrorElement = document.getElementById("phone-error");
    if (!validationRules[fieldName].pattern.test(value)) {
      phoneErrorElement.textContent = validationRules[fieldName].message;
      phoneErrorElement.style.display = "block";
      phoneErrorElement.style.color = "red";
      input.classList.add("invalid-input");
      return false;
    }
    phoneErrorElement.style.display = "none";
    input.classList.remove("invalid-input");
    return true;
  }

  // 비밀번호 필드 특별 처리
  if (fieldName === "pwd") {
    const isValid = validatePassword(value);
    if (!isValid) {
      errorElement.textContent = validationRules[fieldName].message;
      errorElement.style.display = "block";
      errorElement.style.color = "red";
      input.classList.add("invalid-input");
      return false;
    }
  }

  // 패턴 검사
  if (validationRules[fieldName]) {
    const isValid = validationRules[fieldName].pattern.test(value);
    if (!isValid) {
      errorElement.textContent = validationRules[fieldName].message;
      errorElement.style.display = "block";
      errorElement.style.color = "red";
      input.classList.add("invalid-input");
      return false;
    }
  }

  // 유효한 경우
  errorElement.style.display = "none";
  input.classList.remove("invalid-input");
  return true;
}

// 모든 입력 필드에 대해 이벤트 리스너 등록
document.querySelectorAll("#signupForm input").forEach((input) => {
  input.addEventListener("input", (e) => {
    validateField(e.target);
  });
});

// 비밀번호 입력 필드에 이벤트 리스너 추가
document.querySelector('input[name="pwd"]').addEventListener("input", (e) => {
  validatePassword(e.target.value);
  validateField(e.target);

  // 비밀번호 확인 필드 재검사
  const pwdConfirmInput = document.querySelector('input[name="pwdConfirm"]');
  if (pwdConfirmInput.value) {
    validateField(pwdConfirmInput);
  }
});

// 아이디 중복 확인 상태를 저장할 변수
let isIdAvailable = false;
const memberIdInput = document.querySelector('input[name="memberId"]');
const checkDuplicateButton = document.getElementById("checkDuplicateId");

// 아이디 중복 체크 함수
async function checkDuplicateId(memberId) {
  try {
    const response = await fetch(
      "/sign/check-duplicate-id?memberId=" + memberId
    );
    if (!response.ok) {
      throw new Error("서버 응답 오류");
    }

    const { available } = await response.json();
    const errorElement = document.getElementById("memberId-error");

    if (available) {
      errorElement.textContent = "사용 가능한 아이디입니다.";
      errorElement.style.color = "green";
      errorElement.style.display = "block";
      isIdAvailable = true;
    } else {
      errorElement.textContent = "이미 사용 중인 아이디입니다.";
      errorElement.style.color = "red";
      errorElement.style.display = "block";
      isIdAvailable = false;
    }
  } catch (error) {
    console.error("Error:", error);
    alert("중복 확인 중 오류가 발생했습니다.");
  }
}

// 중복확인 버튼 클릭 이벤트
checkDuplicateButton.addEventListener("click", () => {
  const memberId = memberIdInput.value;

  // 아이디 유효성 검사
  if (!validateField(memberIdInput)) {
    return;
  }

  checkDuplicateId(memberId);
});

// 아이디 입력 필드 값이 변경될 때마다 중복확인 상태 초기화
memberIdInput.addEventListener("input", () => {
  isIdAvailable = false;
  const errorElement = document.getElementById("memberId-error");
  errorElement.style.display = "none";
});

// 전화번호 입력 필드들에 이벤트 리스너 추가
document.querySelectorAll(".phone-input-group input").forEach((input) => {
  input.addEventListener("input", (e) => {
    onlyNumbers(e.target);
    validateField(e.target);

    // 자동 포커스 이동
    if (e.target.value.length === e.target.maxLength) {
      const nextInput = e.target.nextElementSibling?.nextElementSibling;
      if (nextInput && nextInput.tagName === "INPUT") {
        nextInput.focus();
      }
    }
  });
});

document.getElementById("signupForm").addEventListener("submit", async (e) => {
  e.preventDefault();

  // 아이디 중복확인 여부 검사
  if (!isIdAvailable) {
    alert("아이디 중복확인을 해주세요.");
    memberIdInput.focus();
    return;
  }

  let isValid = true;
  let firstInvalidInput = null;

  // radio 버튼을 제외한 input 요소들만 검사
  document.querySelectorAll("#signupForm input:not([type='radio'])").forEach((input) => {
    if (!validateField(input)) {
      isValid = false;
      if (!firstInvalidInput) {
        firstInvalidInput = input;
      }
    }
  });

  if (!isValid) {
    alert("모든 필드를 올바르게 입력해주세요.");
    firstInvalidInput.focus();
    return;
  }

  const formData = new FormData(e.target);

  // 전화번호 조합
  const phone1 = formData.get("phone1");
  const phone2 = formData.get("phone2");
  const phone3 = formData.get("phone3");

  // 개별 phone 필드 삭제
  formData.delete("phone1");
  formData.delete("phone2");
  formData.delete("phone3");

  // 조합된 전화번호 추가
  formData.append("phone", phone1 + "-" + phone2 + "-" + phone3);

  formData.delete("pwdConfirm");
  const jsonData = Object.fromEntries(formData.entries());

  try {
    const response = await fetch("/sign/signUp", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(jsonData),
    });

    if (response.ok) {
      alert("회원가입에 성공하였습니다.");
      window.location.href = "/?showLogin=true";
    } else {
      const errorData = await response.json();
      Object.keys(errorData).forEach((fieldName) => {
        const errorElement = document.getElementById(fieldName + "-error");
        if (errorElement) {
          errorElement.textContent = errorData[fieldName];
          errorElement.style.display = "block";
        }
      });
    }
  } catch (error) {
    console.error("Error:", error);
    alert("서버 오류가 발생했습니다.");
  }
});
