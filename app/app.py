from flask import Flask, jsonify
import pandas as pd
from joblib import load
import hashlib
from flask_cors import CORS   # your local file, NOT pip



app = Flask(__name__)
CORS(app)

model = load("model/threat_model.pkl")
encoder = load("model/protocol_encoder.pkl")

def ip_hash(ip):
    return int(hashlib.md5(ip.encode()).hexdigest(), 16) % 10**8

from flask import Flask, jsonify
from faker import Faker
import random
from datetime import datetime

app = Flask(__name__)
fake = Faker()

@app.route('/detect', methods=['GET'])
def detect():
    threats = ["normal", "port_scan", "ddos", "brute_force"]

    response = {
        "ip": fake.ipv4_public(),
        "threat": random.choice(threats),
        "confidence": round(random.uniform(0.70, 0.99), 3),
        "time": datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    }

    return jsonify(response)


if __name__ == "__main__":
    app.run(port=5000, debug=True)




if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)

