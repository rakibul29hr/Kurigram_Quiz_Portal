document.addEventListener("DOMContentLoaded", function () {
    const timerElement = document.getElementById("timer");
    const timerBox = document.getElementById("timerBox");
    const quizForm = document.getElementById("quizForm");

    if (!timerElement || !quizForm) return;

    let totalSeconds = (typeof TIME_LIMIT_MINUTES !== 'undefined' ? TIME_LIMIT_MINUTES : 15) * 60;

    function updateTimer() {
        const minutes = Math.floor(totalSeconds / 60);
        const seconds = totalSeconds % 60;

        timerElement.textContent =
            `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;

        // Highlight timer when under 2 minutes
        if (totalSeconds <= 120 && timerBox) {
            timerBox.classList.add("urgent");
        }

        if (totalSeconds <= 0) {
            clearInterval(timerInterval);
            alert("Time is up! Your quiz will now be automatically submitted.");
            quizForm.submit();
        }
        totalSeconds--;
    }

    updateTimer();
    const timerInterval = setInterval(updateTimer, 1000);
});