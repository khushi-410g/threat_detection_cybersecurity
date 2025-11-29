const API_URL = "http://127.0.0.1:5000/detect";

let pieChart, lineChart;
let timeLabels = [];
let threatValues = [];

// ---------------- PIE CHART ----------------
function initPieChart() {
    const ctx = document.getElementById("pieChart").getContext("2d");

    pieChart = new Chart(ctx, {
        type: "pie",
        data: {
            labels: ["Normal", "Port Scan", "DDoS", "Brute Force"],
            datasets: [
                {
                    data: [25, 25, 25, 25],
                    backgroundColor: ["#2ecc71", "#f1c40f", "#e74c3c", "#3498db"],
                    borderColor: "#ffffff",
                    borderWidth: 2
                }
            ]
        }
    });
}

// ---------------- LINE CHART ----------------
function initLineChart() {
    const ctx = document.getElementById("lineChart").getContext("2d");

    lineChart = new Chart(ctx, {
        type: "line",
        data: {
            labels: timeLabels,
            datasets: [
                {
                    label: "Threat Confidence",
                    data: threatValues,
                    borderColor: "#e74c3c",
                    borderWidth: 2,
                    tension: 0.3
                }
            ]
        },
        options: {
            scales: {
                x: { title: { display: true, text: "Time" } },
                y: { title: { display: true, text: "Confidence" }, min: 0, max: 1 }
            }
        }
    });
}

// ---------------- UPDATE UI ----------------
function updateDashboard(data) {
    document.getElementById("latest-threat").innerHTML =
        `<b>${data.threat}</b> detected from <b>${data.ip}</b> 
         (conf: <b>${data.confidence}</b>) at ${data.time}`;

    const logTable = document.getElementById("log-table");
    const row = document.createElement("tr");
    row.innerHTML = `
        <td>${data.time}</td>
        <td>${data.ip}</td>
        <td>${data.threat}</td>
        <td>${data.confidence}</td>`;
    logTable.prepend(row);

    // update pie
    const mapping = { normal: 0, port_scan: 1, ddos: 2, brute_force: 3 };
    const idx = mapping[data.threat] ?? 0;
    pieChart.data.datasets[0].data[idx] += 1;
    pieChart.update();

    // update line
    timeLabels.push(data.time.slice(11));
    threatValues.push(data.confidence);

    if (timeLabels.length > 60) {
        timeLabels.shift();
        threatValues.shift();
    }

    lineChart.update();
}

// ---------------- FETCH API ----------------
async function fetchThreat() {
    try {
        const res = await fetch(API_URL);
        const data = await res.json();
        updateDashboard(data);
    } catch (err) {
        console.error("API error:", err);
    }
}

// ---------------- START ----------------
window.onload = () => {
    initPieChart();
    initLineChart();
    setInterval(fetchThreat, 2000);
};
