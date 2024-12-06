function handleEdit(button, field) {
  const infoItem = button.closest(".info-item");
  const textSpan = infoItem.querySelector(".info-text");
  const inputField = infoItem.querySelector(".info-input");

  if (button.textContent === "수정") {
    textSpan.style.display = "none";
    inputField.style.display = "inline-block";
    inputField.value = textSpan.textContent.trim();
    button.textContent = "취소";

    const saveButton = document.createElement("button");
    saveButton.textContent = "저장";
    saveButton.className = "save-button";
    saveButton.onclick = () => handleSave(field, inputField.value, infoItem);
    infoItem.appendChild(saveButton);
  } else {
    textSpan.style.display = "inline-block";
    inputField.style.display = "none";
    button.textContent = "수정";

    const saveButton = infoItem.querySelector(".save-button");
    if (saveButton) {
      saveButton.remove();
    }
  }
}

function handleSave(field, value, infoItem) {
  const memberId = document.querySelector('meta[name="memberId"]')?.content;

  if (!memberId) {
    alert("로그인이 필요합니다.");
    window.location.href = "/";
    return;
  }

  const data = {
    [field]: value,
    memberId: memberId,
  };

  fetch("/member/update", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  })
    .then((response) => response.json())
    .then((data) => {
      if (data.success) {
        const textSpan = infoItem.querySelector(".info-text");
        const inputField = infoItem.querySelector(".info-input");
        const editButton = infoItem.querySelector(".edit-button");
        const saveButton = infoItem.querySelector(".save-button");

        textSpan.textContent = value;
        textSpan.style.display = "inline-block";
        inputField.style.display = "none";
        editButton.textContent = "수정";
        saveButton.remove();

        alert("저장되었습니다.");
      } else {
        alert("저장에 실패했습니다.");
      }
    })
    .catch((error) => {
      console.error("Error:", error);
      alert("저장 중 오류가 발생했습니다.");
    });
}
