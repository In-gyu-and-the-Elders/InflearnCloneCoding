document.addEventListener("DOMContentLoaded", () => {
  const loginButton = document.getElementById("loginButton");
  const loginModal = document.getElementById("loginModal");
  const closeModal = document.getElementById("closeModal");

  loginButton.addEventListener("click", () => {
    loginModal.classList.add("active");
    loginModal.classList.remove("hidden");
  });

  closeModal.addEventListener("click", () => {
    loginModal.classList.remove("active");
    loginModal.classList.add("hidden");
  });

  window.addEventListener("click", (e) => {
    if (e.target === loginModal) {
      loginModal.classList.remove("active");
    }
  });
});

document.addEventListener("DOMContentLoaded", () => {
  const toggleButton = document.getElementById("toggleTypeButton");
  const inputField = document.querySelector(".e-sign-in-input");

  const eyeIcon = `
    <svg width="16" height="16" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" id="toggleTypeButton">
      <path fill="#212529" d="M10.333 8c0 1.289-1.044 2.333-2.333 2.333C6.71 10.333 5.667 9.29 5.667 8 5.667 6.711 6.71 5.667 8 5.667c1.289 0 2.333 1.044 2.333 2.333z"></path>
      <path fill="#212529" fill-rule="evenodd" d="M8 2.333c-2.288 0-4.083 1.023-5.37 2.16C1.348 5.63.544 6.902.22 7.469.03 7.8.03 8.2.22 8.533c.323.566 1.127 1.838 2.41 2.973 1.287 1.138 3.082 2.16 5.37 2.16 2.288 0 4.083-1.022 5.37-2.16 1.283-1.135 2.087-2.407 2.41-2.973.19-.333.19-.733 0-1.065-.323-.567-1.127-1.839-2.41-2.974-1.287-1.138-3.082-2.16-5.37-2.16zm-6.912 5.63c.295-.516 1.035-1.685 2.205-2.72C4.461 4.21 6.03 3.333 8 3.333c1.97 0 3.54.877 4.707 1.91 1.17 1.035 1.91 2.204 2.205 2.72.008.015.01.028.01.037 0 .01-.002.022-.01.037-.295.516-1.035 1.685-2.205 2.72-1.168 1.033-2.737 1.91-4.707 1.91-1.97 0-3.54-.877-4.707-1.91-1.17-1.035-1.91-2.204-2.205-2.72-.008-.015-.01-.028-.01-.037 0-.01.002-.022.01-.037z" clip-rule="evenodd"></path>
    </svg>`;

  const crossedEyeIcon = `
    <svg width="16" height="16" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16">
      <path fill="#212529" d="M5.368 3.89C6.145 3.55 7.021 3.334 8 3.334c1.97 0 3.54.877 4.707 1.91 1.17 1.034 1.91 2.204 2.205 2.72.007.013.01.026.01.036 0 .01-.003.024-.012.04-.177.31-.518.862-1.02 1.478-.174.215-.142.53.073.704.214.174.529.142.703-.072.544-.67.915-1.269 1.113-1.614.188-.33.192-.733.001-1.068-.323-.567-1.127-1.838-2.41-2.974-1.287-1.137-3.082-2.16-5.37-2.16-1.13 0-2.143.25-3.035.642-.252.111-.367.407-.256.66.111.252.406.367.659.256z"></path>
      <path fill="#212529" fill-rule="evenodd" d="M12.777 11.991c-1.225.928-2.822 1.675-4.777 1.675-2.288 0-4.083-1.022-5.37-2.16C1.348 10.37.544 9.099.22 8.532c-.191-.334-.188-.736 0-1.067.307-.537 1.044-1.705 2.212-2.791l-1.554-1.1c-.226-.159-.279-.471-.12-.696.16-.226.472-.28.697-.12l13.667 9.667c.226.16.279.471.12.697-.16.225-.472.279-.697.12l-1.768-1.25zm-9.51-6.726C2.115 6.292 1.384 7.447 1.09 7.96c-.008.015-.011.029-.011.04 0 .01.002.022.01.036.295.516 1.035 1.685 2.205 2.72C4.461 11.79 6.03 12.667 8 12.667c1.556 0 2.86-.547 3.916-1.285L9.572 9.724c-.415.378-.966.609-1.572.609-1.289 0-2.333-1.045-2.333-2.333 0-.323.065-.63.183-.909L3.268 5.265z" clip-rule="evenodd"></path>
    </svg>
  `;

  toggleButton.addEventListener("click", () => {
    if (inputField.type === "password") {
      inputField.type = "text";
      toggleButton.innerHTML = crossedEyeIcon;
    } else {
      inputField.type = "password";
      toggleButton.innerHTML = eyeIcon;
    }
  });
});