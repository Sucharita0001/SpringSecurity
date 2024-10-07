const registerFormEvent = document.getElementById("registerForm");
if (registerFormEvent) {
  registerFormEvent.addEventListener("submit", async function (event) {
    event.preventDefault();
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const age = document.getElementById("age").value;
    const role = document.getElementById("role").value;
    const customer = {
      email: email,
      password: password,
      age: age,
      role: role,
    };
    try {
      const response = await fetch("http://localhost:10/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(customer),
      });
      if (response.ok) {
        alert("Customer registered successfully!");
        document.getElementById("registerForm").reset();
      } else {
        alert("Failed to register customer.");
      }
    } catch (error) {
      console.error("Error:", error);
      alert("Error registering customer.");
    }
    console.log(customer);
    window.location.href = "index.html";
  });
}

const loginFormEvent = document.getElementById("loginForm");
if (loginFormEvent) {
  loginFormEvent.addEventListener("submit", async function (event) {
    event.preventDefault();
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const credentials = `${email}:${password}`;
    console.log(credentials)
    const encodedCredentials = encodeToBase64(credentials);
    console.log(encodedCredentials)
    try {
      const response = await fetch("http://localhost:10/restricted", {
        method: "GET",
        headers: {
          Authorization: "Basic " + encodedCredentials,
        },
      });
      if (response.ok) {
        alert("Customer logged in successfully!!");
        sessionStorage.setItem("authCredentials", encodedCredentials);
        window.location.href = "index.html";
      } else if (response.status === 401) {
        document.getElementById("loginStatus").textContent =
          "Invalid credentials. Try again.";
      } else {
        document.getElementById("loginStatus").textContent =
          "Login failed. Please try again.";
      }
    } catch (error) {
      console.error("Error:", error);
      document.getElementById("loginStatus").textContent =
        "Error connecting to the server.";
    }
  });
}

const openPageEvent = document.getElementById("openPage");
if (openPageEvent) {
  openPageEvent.addEventListener("click", async function (event) {
    event.preventDefault();
    try {
      const response = await fetch("http://localhost:10/open", {
        method: "GET",
      });
      if (response.ok) {
        const data = await response.text();
        localStorage.setItem("openPageHeader", data);
        window.location.href = "open.html";
      } else {
        document.getElementById("loginStatus").textContent =
          "Login failed. Please try again.";
      }
    } catch (error) {
      console.error("Error:", error);
      document.getElementById("loginStatus").textContent =
        "Error connecting to the server.";
    }
  });
}

const restrictedPageEvent = document.getElementById("restricedPage");
if (restrictedPageEvent) {
  restrictedPageEvent.addEventListener("click", async function (event) {
    event.preventDefault();
    fetchRestrictedData();
  });
}

async function fetchRestrictedData() {
  const encodedCredentials = sessionStorage.getItem("authCredentials");
  if (!encodedCredentials) {
    alert("You are not logged in. Redirecting to login page.");
    window.location.href = "login.html";
    return;
  }
  try {
    const response = await fetch("http://localhost:10/restricted", {
      method: "GET",
      headers: {
        Authorization: "Basic " + encodedCredentials,
      },
    });
    if (response.ok) {
        const data = await response.text();
        localStorage.setItem("restrictedPageHeader", data);
        window.location.href = "restricted.html";
      } else {
        document.getElementById("loginStatus").textContent =
          "Login failed. Please try again.";
      }
  } catch (error) {
    console.error("Error:", error);
    document.getElementById("responseHeading").textContent =
      "Error connecting to the server.";
  }
}

function encodeToBase64(input) {
  // Create a Uint8Array from the UTF-8 encoded string
  const utf8Array = new TextEncoder().encode(input);
  // Convert the Uint8Array to a Base64 string
  const base64String = btoa(String.fromCharCode(...utf8Array));
  return base64String;
}
