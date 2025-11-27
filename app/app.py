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
    df = pd.read_csv("data/network_logs.csv")

    # Get last 10 logs (or however many you want)
    recent = df.tail(10)

    results = []

    for _, row in recent.iterrows():
        X = [[
            ip_hash(row["src_ip"]),
            row["dst_port"],
            row["packet_size"],
            encoder.transform([row["protocol"]])[0]
        ]]

        pred = model.predict(X)[0]
        conf = model.predict_proba(X).max()

        results.append({
            "ip": row["src_ip"],
            "threat": pred,
            "confidence": float(conf),
            "time": row["timestamp"]
        })

    return jsonify(results)


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)

