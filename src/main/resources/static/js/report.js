document.addEventListener("DOMContentLoaded", function () {
  // Initialize all detail buttons
  document.querySelectorAll(".details-button").forEach((button) => {
    button.addEventListener("click", function () {
      const content = this.nextElementSibling;
      const icon = this.querySelector(".chevron-icon");

      content.classList.toggle("show");
      icon.style.transform = content.classList.contains("show")
        ? "rotate(180deg)"
        : "rotate(0)";
    });
  });

  // Add transition for icons
  document.querySelectorAll(".chevron-icon").forEach((icon) => {
    icon.style.transition = "transform 0.3s ease";
  });
});
