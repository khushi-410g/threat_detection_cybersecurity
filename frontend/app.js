let pieChart;
let lineChart;
let threatCount = {
    normal: 0,
    ddos: 0,
    port_scan: 0,
    brute_force: 0
};

let timeLabels = [];
let timeValues = [];

/* Fetch latest threat every 2 sec */
setInterval(fetchThreat, 2000);

function fetchThreat() {
    fetch("http://127.0.0.1:5000/detect")
        .then(res => res.json())
        .then(data => {
            updateLatest(data);
            updateTable(data);
            updatePie(data);
            updateLine(data);
        })
        .catch(err => console.log("Backend not reachable", err));
}

/* Display latest threat */
function updateLatest(d) {
    document.getElementById("latest-alert").innerHTML =
        `<b>${d.threat}</b> detected from <b>${d.ip}</b> with confidence <b>${(d.confidence * 100).toFixed(1)}%</b>`;
}

/* Append to table */
function updateTable(d) {
    const row = `
        <tr>
            <td>${d.time}</td>
            <td>${d.ip}</td>
            <td>${d.threat}</td>
            <td>${(d.confidence * 100).toFixed(1)}%</td>
        </tr>
    `;
    document.querySelector("#log-table tbody").innerHTML = row +
        document.querySelector("#log-table tbody").innerHTML;
}

/* Pie chart update */
function updatePie(d) {
    threatCount[d.threat]++;

    const ctx = document.getElementById("pieChart");

    if (!pieChart) {
        pieChart = new Chart(ctx, {
            type: 'pie',
            data: {
                labels: ["normal", "ddos", "port_scan", "brute_force"],
                datasets: [{
                    data: [
                        threatCount.normal,
                        threatCount.ddos,
                        threatCount.port_scan,
                        threatCount.brute_force
                    ],
                    backgroundColor: ["#3498db", "#e74c3c", "#f1c40f", "#8e44ad"]
                }]
            }
        });
    } else {
        pieChart.data.datasets[0].data = [
            threatCount.normal,
            threatCount.ddos,
            threatCount.port_scan,
            threatCount.brute_force
        ];
        pieChart.update();
    }
}

/* Line chart update */
function updateLine(d) {
    const now = new Date().toLocaleTimeString();
    timeLabels.push(now);
    timeValues.push(Math.floor(Math.random() * 10)); // simulated

    const ctx = document.getElementById("lineChart");

    if (!lineChart) {
        lineChart = new Chart(ctx, {
            type: "line",
            data: {
                labels: timeLabels,
                datasets: [{
                    label: "Threat Activity",
                    data: timeValues,
                    borderColor: "#003366",
                    fill: false
                }]
            }
        });
    } else {
        lineChart.update();
    }
}
