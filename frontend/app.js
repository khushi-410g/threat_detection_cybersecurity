const API = "http://127.0.0.1:5000";

let threatCounts = {
    normal: 0,
    port_scan: 0,
    ddos: 0,
    brute_force: 0
};

let chartTimeLabels = [];
let chartTimeValues = [];

const pieCtx = document.getElementById('pieChart').getContext('2d');
const timeCtx = document.getElementById('timeChart').getContext('2d');

// Pie chart
const pieChart = new Chart(pieCtx, {
    type: "pie",
    data: {
        labels: ["Normal", "Port Scan", "DDoS", "Brute Force"],
        datasets: [{
            data: [1,1,1,1],
            backgroundColor: ["#2ecc71","#f1c40f","#e74c3c","#3498db"]
        }]
    }
});

// Time-series
const timeChart = new Chart(timeCtx, {
    type: "line",
    data: {
        labels: [],
        datasets: [{
            label: "Threat Confidence",
            data: [],
            borderColor: "#2980b9",
            tension: 0.3
        }]
    }
});

async function fetchThreat() {
    try {
        const res = await fetch(`${API}/detect`);
        const data = await res.json();

        addToTable(data);
        updateCharts(data);

    } catch (err) {
        console.error("Backend not reachable");
    }
}

function addToTable(data) {
    const table = document.querySelector("#threatTable tbody");

    const row = `
        <tr>
            <td>${new Date().toLocaleTimeString()}</td>
            <td>${data.ip}</td>
            <td>${data.threat}</td>
            <td>${(data.confidence * 100).toFixed(1)}%</td>
        </tr>
    `;

    table.insertAdjacentHTML("afterbegin", row);
}

function updateCharts(data) {
    threatCounts[data.threat]++;

    pieChart.data.datasets[0].data = [
        threatCounts.normal,
        threatCounts.port_scan,
        threatCounts.ddos,
        threatCounts.brute_force
    ];
    pieChart.update();

    chartTimeLabels.push(new Date().toLocaleTimeString());
    chartTimeValues.push(data.confidence);

    if(chartTimeLabels.length > 20) {
        chartTimeLabels.shift();
        chartTimeValues.shift();
    }

    timeChart.data.labels = chartTimeLabels;
    timeChart.data.datasets[0].data = chartTimeValues;
    timeChart.update();
}

// Retrain button
document.getElementById("trainBtn").onclick = async () => {
    alert("Training Model...");

    await fetch(`${API}/train`, { method: "POST" });

    alert("Model retrained successfully!");
};

// Fetch every 2 seconds
setInterval(fetchThreat, 2000);
