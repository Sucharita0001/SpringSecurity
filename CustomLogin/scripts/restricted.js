document.getElementById("restrictedRequestHeading").textContent =
  localStorage.getItem("restrictedPageHeader");
localStorage.removeItem("restrictedPageHeader");
