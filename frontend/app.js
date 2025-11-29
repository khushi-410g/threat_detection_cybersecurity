const API_URL = "http://127.0.0.1:5000/detect";

let pieChart, lineChart;
let timeData = [];
let confidenceData = [];

async function fetchThreat() {
    try {
        const res = await fetch(API_URL);
        const data = await res.json();

        updateLatest(data.latest);
        updateLog(data.logs);
        updatePie(data.counts);
        updateLine(data.latest);

    } catch (e) {
        console.log("API error:", e);
    }
}

function updateLatest(threat) {
    document.getElementById("latestThreat").innerHTML =
        `${threat.time} — ${threat.ip} — ${threat.type} (conf: ${threat.confidence})`;
}

function updateLog(logs) {
    const tbody = document.querySelector("#logTable tbody");
    tbody.innerHTML = "";

    logs.slice(-15).reverse().forEach(log => {
        const row = `
            <tr>
                <td>${log.time}</td>
                <td>${log.ip}</td>
                <td>${log.type}</td>
                <td>${log.confidence}</td>
            </tr>
        `;
        tbody.innerHTML += row;
    });
}

function updatePie(counts) {
    const data = [
        counts.normal,
        counts.port_scan,
        counts.ddos,
        counts.brute_force
    ];

    if (!pieChart) {
        pieChart = new Chart(document.getElementById("pieChart"), {
            type: "pie",
            data: {
                labels: ["Normal", "Port Scan", "DDoS", "Brute Force"],
                datasets: [{
                    data: data,
                    backgroundColor: ["#2ecc71", "#f1c40f", "#e74c3c", "#3498db"]
                }]
            }
        });
    } else {
        pieChart.data.datasets[0].data = data;
        pieChart.update();
    }
}

function updateLine(threat) {
    timeData.push(threat.time);
    confidenceData.push(threat.confidence);

    if (timeData.length > 20) {
        timeData.shift();
        confidenceData.shift();
    }

    if (!lineChart) {
        lineChart = new Chart(document.getElementById("lineChart"), {
            type: "line",
            data: {
                labels: timeData,
                datasets: [{
                    label: "Threat Confidence",
                    data: confidenceData,
                    borderColor: "#e74c3c"
                }]
            }
        });
    } else {
        lineChart.update();
    }
}

// Fetch every 2 seconds
setInterval(fetchThreat, 2000);
