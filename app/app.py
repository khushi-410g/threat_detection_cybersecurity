from flask import Flask, jsonify
import pandas as pd
from joblib import load
import hashlib

app = Flask(__name__)

model = load("model/threat_model.pkl")
encoder = load("model/protocol_encoder.pkl")

def ip_hash(ip):
    return int(hashlib.md5(ip.encode()).hexdigest(), 16) % 10**8

@app.route("/detect", methods=["GET"])
def detect():
    results = []

    # Balanced distribution: normal 60%, scan 20%, ddos 10%, brute 10%
    LABELS = ["normal", "port_scan", "ddos", "brute_force"]
    WEIGHTS = [0.6, 0.2, 0.1, 0.1]

    for _ in range(10):  # generate 10 new logs each refresh
        src_ip = fake.ipv4()
        dst_port = random.choice([22, 80, 443, 8080, 53])
        packet_size = random.randint(60, 1500)
        protocol = random.choice(["TCP", "UDP"])
        threat = random.choices(LABELS, weights=WEIGHTS)[0]

        # Encode protocol
        proto_enc = encoder.transform([protocol])[0]

        X = [[
            ip_hash(src_ip),
            dst_port,
            packet_size,
            proto_enc
        ]]

        pred = model.predict(X)[0]
        conf = model.predict_proba(X).max()

        results.append({
            "ip": src_ip,
            "threat": pred,
            "confidence": float(conf),
            "time": datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        })

    return jsonify(results)



if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)

