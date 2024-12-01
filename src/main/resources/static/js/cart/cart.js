document.addEventListener("DOMContentLoaded", function () {
  const selectAllCheckbox = document.getElementById("select-all");
  const courseCountSpan = document.getElementById("course-count");
  const totalPriceSpan = document.getElementById("total-price");
  const deleteSelectedBtn = document.getElementById("delete-selected-btn");
  const checkoutBtn = document.getElementById("checkout-btn");
  let courseSelectCheckboxes = document.querySelectorAll(".course-select");
  const cartList = document.querySelector(".cart-list");

  let totalPrice = 0;

  // 전체 선택 체크박스 클릭 처리
  selectAllCheckbox.addEventListener("change", function () {
    const isChecked = selectAllCheckbox.checked;
    courseSelectCheckboxes.forEach((checkbox) => {
      checkbox.checked = isChecked;
    });
    updateCourseCount();
    updateTotalPrice();
  });

  // 개별 강좌의 체크박스 클릭 처리
  function updateCourseCheckboxes() {
    courseSelectCheckboxes = document.querySelectorAll(".course-select");
    courseSelectCheckboxes.forEach((checkbox) => {
      checkbox.addEventListener("change", function () {
        updateCourseCount();
        updateTotalPrice();
      });
    });
  }

  // 가격에서 숫자만 추출하는 함수 추가
  function extractPrice(priceText) {
    return priceText.replace(/[^0-9]/g, '');
  }

  //결제버튼
  // 결제 버튼 클릭 시 선택된 강좌들의 정보 담기
  function getSelectedCourses() {
    let courseIdxList = [];
    let priceList = [];

    // 체크된 강좌들만 가져오기
    document
      .querySelectorAll(".course-select:checked")
      .forEach(function (checkbox) {
        const courseItem = checkbox.closest(".cart-item");
        const courseIdx = courseItem.getAttribute("data-course-id");
        const priceText = courseItem.querySelector(".course-price").innerText;
        const price = extractPrice(priceText);  // 숫자만 추출

        courseIdxList.push(courseIdx);
        priceList.push(price);  // 숫자만 포함된 가격 추가
      });

    // hidden input에 선택된 강좌들의 idx와 가격을 설정
    document.getElementById("courseIdxList").value = courseIdxList.join(",");
    document.getElementById("priceList").value = priceList.join(",");

    console.log("결제 데이터:", { courseIdxList, priceList });
  }

  // 결제 버튼 클릭 시 선택된 강좌들을 서버로 전송
  document
    .getElementById("checkoutBtn")
    .addEventListener("click", function (e) {
      e.preventDefault();
      getSelectedCourses();
      document.getElementById("orderForm").submit(); // 폼 제출
    });

  // 선택된 강좌들 삭제
  function deleteSelectedCourses() {
    const selectedItems = Array.from(
      document.querySelectorAll(".course-select:checked")
    );
    const idxList = selectedItems.map((item) =>
        parseInt(item.closest(".cart-item").getAttribute("data-course-id"), 10)
    );
    console.log("선택된 강좌 ID들:", idxList);
    fetch("/cart/deleteList", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ idxList: idxList }),
    })
      .then((response) => response.json())
      .then((data) => {
        if (data.success) {
          alert("선택된 항목들이 삭제되었습니다!");
          selectedItems.forEach((item) => {
            item.closest(".cart-item").remove();
          });
          updateCourseCheckboxes(); // 삭제 후 강좌 체크박스 업데이트
          updateCourseCount();
          updateTotalPrice();
        } else {
          alert("삭제 실패!");
        }
      })
      .catch((error) => {
        console.error("Error:", error);
        alert("오류가 발생했습니다.");
      });
  }

  // 삭제 버튼 클릭 시 처리
  deleteSelectedBtn.addEventListener("click", deleteSelectedCourses);

  // 각 개별 삭제 버튼 클릭 시 처리
  document.querySelectorAll(".delete-btn").forEach((button) => {
    button.addEventListener("click", function () {
      const cartIdx = button.getAttribute("data-course-id");
      fetch("/cart/delete", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ idxList: [cartIdx] }),
      })
        .then((response) => response.json())
        .then((data) => {
          if (data.success) {
            alert("삭제 성공!");
            button.closest(".cart-item").remove();
            updateCourseCheckboxes(); // 삭제 후 강좌 체크박스 업데이트
            updateCourseCount();
            updateTotalPrice();
          } else {
            alert("삭제 실패!");
          }
        })
        .catch((error) => {
          console.error("Error:", error);
          alert("오류가 발생했습니다.");
        });
    });
  });

  // 선택된 강좌들의 가격을 합산
  function updateTotalPrice() {
    totalPrice = 0;
    document.querySelectorAll(".cart-item").forEach((item) => {
      const checkbox = item.querySelector(".course-select");
      const price = parseInt(
        item
          .querySelector(".course-price")
          .innerText.replace("₩", "")
          .replace(",", ""),
        10
      );
      if (checkbox.checked) {
        totalPrice += price;
      }
    });
    totalPriceSpan.innerText = `₩${totalPrice.toLocaleString()}`;
  }

  // 체크된 강좌 수 업데이트
  function updateCourseCount() {
    const selectedCount = Array.from(courseSelectCheckboxes).filter(
      (checkbox) => checkbox.checked
    ).length;
    const totalCount = courseSelectCheckboxes.length;
    courseCountSpan.innerText = `(${selectedCount}/${totalCount})`;
  }

  // 페이지 로딩 후 강좌 체크박스 이벤트 다시 연결
  updateCourseCheckboxes();
});
