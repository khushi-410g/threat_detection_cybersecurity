from flask import Flask, jsonify
import random, time

app = Flask(__name__)

# ---------------------------
# Manual CORS (no dependencies)
# ---------------------------
@app.after_request
def apply_cors(response):
    response.headers["Access-Control-Allow-Origin"] = "*"
    response.headers["Access-Control-Allow-Methods"] = "GET,POST,OPTIONS"
    response.headers["Access-Control-Allow-Headers"] = "Content-Type"
    return response

# ---------------------------
# Generate simulated threat data
# ---------------------------
threat_types = ["normal", "port_scan", "ddos", "brute_force"]

def generate_threat():
    return {
        "time": time.strftime("%Y-%m-%d %H:%M:%S"),
        "ip": ".".join(str(random.randint(1, 255)) for _ in range(4)),
        "type": random.choice(threat_types),
        "confidence": round(random.uniform(0.70, 0.99), 3)
    }

logs = []

@app.route("/detect")
def detect():
    global logs
    threat = generate_threat()
    logs.append(threat)

    # Keep only last 50
    logs = logs[-50:]

    return jsonify({
        "latest": threat,
        "logs": logs,
        "counts": {
            "normal": sum(1 for x in logs if x["type"] == "normal"),
            "port_scan": sum(1 for x in logs if x["type"] == "port_scan"),
            "ddos": sum(1 for x in logs if x["type"] == "ddos"),
            "brute_force": sum(1 for x in logs if x["type"] == "brute_force"),
        }
    })

if __name__ == "__main__":
    print("🚀 Flask backend running at http://127.0.0.1:5000")
    app.run(host="127.0.0.1", port=5000)
